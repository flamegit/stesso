package com.tmmt.innersect.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Information;
import com.tmmt.innersect.ui.adapter.AdvancedAdapter;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.PermissionUtils;
import com.tmmt.innersect.utils.Util;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.webkit.WebSettings.LOAD_DEFAULT;

public class InfoDetailActivity extends AppCompatActivity {
    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.title_view)
    TextView mTitleView;
//    @BindView(R.id.time_view)
//    TextView mTimeView;

    @BindView(R.id.bottom_view)
    View mBottomView;

    @BindView(R.id.image_view)
    ImageView mIconView;

    @BindView(R.id.count_view)
    TextView mCountView;

    boolean isRunning;
    //boolean isInfo;

    PopupWindow mPopWindow;

    @BindView(R.id.share_view)
    View mShareView;
    int mId;
    Information mInfo;
    public static void start(Context context, int id,String url){
        Intent intent = new Intent(context, InfoDetailActivity.class);
        intent.putExtra(Constants.INFO_ID, id);
        intent.putExtra(Constants.ICON_URL,url);
        context.startActivity(intent);
    }

    public static void start(Context context, int id){
        Intent intent = new Intent(context, InfoDetailActivity.class);
        intent.putExtra(Constants.INFO_ID, id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);
        AnalyticsUtil.reportEvent("home_news_click");
        ButterKnife.bind(this);
        mId=getIntent().getIntExtra(Constants.INFO_ID,0);
        //isInfo=getIntent().getBooleanExtra(Constants.IS_INFO,false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(LOAD_DEFAULT);
        //mWebView.setNestedScrollingEnabled(false);
        mWebView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();
//            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith("innersect")){
                    Util.openTarget(InfoDetailActivity.this, url," ");
                    return true;
                }
                if(url.startsWith("http")){
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                    return true;
                }
                //return true;
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getInfoDetail(mId).enqueue(new NetCallback<Information>() {
            @Override
            public void onSucceed(Information data) {
                if(isRunning){
                    fillView(data);
                }
            }
        });
    }

    @OnClick(R.id.share_view)
    void share(){
        PermissionUtils.checkPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                100, new PermissionUtils.PermissionGrant() {
                    @Override
                    public void onPermissionGranted(int requestCode) {
                        doShare();
                    }
                });
    }

    void doShare(){
        AnalyticsUtil.reportEvent("home_news_detail_share");
        UMWeb umWeb=new UMWeb("https://m.innersect.net/news/"+mId);
        if(mInfo!=null){
            umWeb.setTitle(mInfo.title);
        }else {
            umWeb.setTitle("加入INNERSECT，顶尖正价潮货抢先GO");
        }
        umWeb.setDescription("最值得期待潮流APP，汇集全球顶级潮流品牌");
        umWeb.setThumb(new UMImage(this,R.mipmap.app_icon));
        new ShareAction(this)
                .withText("VLONE亚洲首发，加入innersect顶尖正价潮货抢先GO！")
                .withMedia(umWeb)
                .setDisplayList(Util.getDisplayList(this))
                .setCallback(umShareListener)
                .open();
    }

    private static UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
            KLog.d("plat","platform"+platform);
            AnalyticsUtil.reportEvent("home_news_detail_share_channel","platform",platform.toString());
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if(t!=null){
                KLog.d("throw","throw:"+t.getMessage());
            }
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            AnalyticsUtil.reportEvent("home_news_detail_share_cancel");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode!=100){
            return;
        }
        PermissionUtils.requestPermissionsResult(this,requestCode,permissions,grantResults,new PermissionUtils.PermissionGrant(){
            @Override
            public void onPermissionGranted(int requestCode) {
                doShare();
            }
        });
    }

    private void showWindow(){
        View view= LayoutInflater.from(this).inflate(R.layout.popupwindow_related, null);
        View close=view.findViewById(R.id.close_view);
        close.setOnClickListener(v -> mPopWindow.dismiss());
        RecyclerView recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        AdvancedAdapter<Information.Product> adapter=new AdvancedAdapter<>(R.layout.viewholder_product,(holder, position, data) -> {
            Context context=holder.itemView.getContext();
            holder.<TextView>get(R.id.title_view).setText(data.name);
            holder.<TextView>get(R.id.brand_view).setText(data.brandName);
            Util.fillImage(context,data.thumbnail,holder.get(R.id.icon_view));
            holder.itemView.setOnClickListener(v -> {
                mPopWindow.dismiss();
                CommodityDetailActivity.start(context,data.productId);
            });

        });
        recyclerView.setAdapter(adapter);
        adapter.addItems(mInfo.productList);
        mPopWindow= new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setFocusable(true);// 取得焦点
        //mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        backgroundAlpha(0.4f);
        mPopWindow.setOnDismissListener(() -> backgroundAlpha(1));
        mPopWindow.setAnimationStyle(R.style.pop_window_anim);
        mPopWindow.showAtLocation(mBottomView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning=true;
    }

    @OnClick(R.id.back_view)
    void back(){
        onBackPressed();
    }

    private void fillView(Information info){
        if(info==null){
            return;
        }
        mInfo=info;

        mTitleView.setText(info.title);

        if(info.productCount>0){
            mBottomView.setVisibility(View.VISIBLE);
        }
        mCountView.setText(info.productCount+"件");
        Util.fillImage(this,info.productPic,mIconView);
        mBottomView.setOnClickListener(v -> showWindow());
        mWebView.loadData(getHtmlData(info.content), "text/html; charset=utf-8", "utf-8");

    }
    private String getHtmlData(String bodyHTML) {
        KLog.d(bodyHTML);
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";

       //return  "<html><head><meta name=\"viewport\" content=\"initial-scale=1.0,user-scalable=no\" /></head><body>"+ bodyHTML+"</body></html>";
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.destroy();
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }


}
