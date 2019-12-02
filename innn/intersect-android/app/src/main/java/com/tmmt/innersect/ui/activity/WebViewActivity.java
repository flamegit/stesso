package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.utils.Util;

import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.web_view)
    WebView mWebView;


    @Override
    protected int getLayoutId() {
        int type=getIntent().getIntExtra(Constants.TYPE,1);
        if(type==0){
            return R.layout.activity_web_view;
        }
        return R.layout.activity_agreement;
    }

    @Override
    protected String getTitleString() {
        String title=getIntent().getStringExtra(Constants.TITLE);
        if(title!=null){
            return title;
        }
        return " ";
    }

    public static void start(Context context, String url){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.TARGET_URL, url);
        context.startActivity(intent);
    }

    public static void start(Context context, String url,String title){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.TARGET_URL, url);
        intent.putExtra(Constants.TITLE, title);
        context.startActivity(intent);
    }

    public static void start(Context context, String url,String title,int type){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.TARGET_URL, url);
        intent.putExtra(Constants.TITLE, title);
        intent.putExtra(Constants.TYPE,type);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient());

        String targetUrl=getIntent().getStringExtra(Constants.TARGET_URL);
        if(targetUrl!=null){

            if(AccountInfo.getInstance().isLogin()){
                Map<String,String> header=new TreeMap<>();
                header.put("session-token",AccountInfo.getInstance().getToken());
                mWebView.loadUrl(targetUrl,header);
            }else {
                mWebView.loadUrl(targetUrl);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(
                    WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mWebView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();
//            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith("innersect")){
                    Util.openTarget(WebViewActivity.this, URLEncoder.encode(url),"test");
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

        });
    }
}
