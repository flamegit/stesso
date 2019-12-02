package com.tmmt.innersect.ui.activity;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;

import butterknife.BindView;

public class AgreementActivity extends BaseActivity {

    @BindView(R.id.web_view)
    WebView mWebView;
    boolean isPrivate;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_agreement;
    }

    @Override
    protected String getTitleString() {

        if(isPrivate){
            return getString(R.string.privacy_policy);
        }
        return getString(R.string.service_policy);
    }

    @Override
    protected void initView() {
        super.initView();
        isPrivate=getIntent().getBooleanExtra(Constants.PRIVATE,false);
        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();
//            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        String url=Constants.AGREEMENT_URL;

        if(isPrivate){
            url=Constants.PRIVATE_URL;
        }
        //url="http://47.92.31.5:8074/product/test.html";

        mWebView.loadUrl(url);
    }
}
