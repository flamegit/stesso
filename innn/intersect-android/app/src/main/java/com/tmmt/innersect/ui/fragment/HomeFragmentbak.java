//package com.tmmt.innersect.ui.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.http.SslError;
//import android.os.Build;
//import android.os.CountDownTimer;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.webkit.SslErrorHandler;
//
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.TextView;
//
//import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
//import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
//import com.tmmt.innersect.BuildConfig;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.datasource.net.ApiManager;
//import com.tmmt.innersect.datasource.net.NetCallback;
//import com.tmmt.innersect.mvp.model.BannerInfo;
//import com.tmmt.innersect.mvp.model.HttpBean;
//import com.tmmt.innersect.mvp.model.ShopCart;
//import com.tmmt.innersect.mvp.presenter.CommonPresenter;
//import com.tmmt.innersect.mvp.presenter.ShopCartPresenter;
//import com.tmmt.innersect.mvp.view.CommonView;
//import com.tmmt.innersect.mvp.view.ExtraView;
//import com.tmmt.innersect.mvp.view.ShopCartView;
//import com.tmmt.innersect.ui.activity.IShare;
//import com.tmmt.innersect.ui.activity.SearchActivity;
//import com.tmmt.innersect.ui.activity.ShopCartActivity;
//import com.tmmt.innersect.ui.adapter.ImagePagerAdapter;
//import com.tmmt.innersect.utils.Util;
//import com.tmmt.innersect.widget.CirclePageIndicator;
//import com.tmmt.innersect.widget.RefreshHeader;
//import java.util.List;
//import butterknife.BindView;
//import butterknife.OnClick;
//import retrofit2.Call;
//
///**
// * Created by flame on 2017/4/12.
// */
//
//public class HomeFragment extends BaseFragment implements CommonView<List<BannerInfo>>,ExtraView{
//    @BindView(R.id.pager_indicator)
//    CirclePageIndicator mIndicator;
//    @BindView(R.id.view_pager)
//    ViewPager mViewPager;
//    ImagePagerAdapter mPagerAdapter;
//    CountDownTimer mTimer;
//    @BindView(R.id.web_view)
//    WebView mWebView;
//    @BindView(R.id.pager_container)
//    View mPagerContainer;
//    @BindView(R.id.shop_cart_size)
//    TextView mCartSizeView;
//    IShare mIShare;
//    CommonPresenter mPresenter;
//    ShopCartPresenter mCartPresenter;
//    @BindView(R.id.refresh_layout)
//    TwinklingRefreshLayout mRefreshLayout;
//
//    @Override
//    int getLayout() {
//        return R.layout.fragment_home;
//    }
//
//    @Override
//    protected void initView(final View view) {
//        super.initView(view);
//        mPresenter=new CommonPresenter();
//        mPresenter.attachView(this);
//        mPresenter.loadBanner();
//        mCartPresenter=new ShopCartPresenter();
//        mCartPresenter.attachView(this);
//        mPagerAdapter = new ImagePagerAdapter();
//        mViewPager.setAdapter(mPagerAdapter);
//        mIndicator.setViewPager(mViewPager);
//        mTimer = new CountDownTimer(4 * 1000 * 1000, 4 * 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                long time = millisUntilFinished / 1000;
//                if (time <= 3996) {
//                    mViewPager.arrowScroll(View.FOCUS_RIGHT);
//                }
//            }
//            @Override
//            public void onFinish() {}
//        };
//
//        mWebView.getSettings().setJavaScriptEnabled(true);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mWebView.getSettings().setMixedContentMode(
//                    WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
//        }
//
//        mWebView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed();
//            }
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if(url.startsWith("innersect")){
//                    Util.openTarget(getContext(), url," ");
//                    return true;
//                }
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//        });
//        mWebView.loadUrl(BuildConfig.POPUPURL);
//        mRefreshLayout.setHeaderView(new RefreshHeader(getContext()));
//        mRefreshLayout.setEnableLoadmore(false);
//        //mRefreshLayout.sete
//        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                super.onRefresh(refreshLayout);
//                mWebView.reload();
//                ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getBannerInfo().enqueue(new NetCallback<List<BannerInfo>>() {
//                    @Override
//                    public void onSucceed(List<BannerInfo> data) {
//                        if(data==null || data.isEmpty()){
//                            mPagerContainer.setVisibility(View.GONE);
//                        }
//                        mPagerAdapter.addItems(data);
//                        mRefreshLayout.finishRefreshing();
//                    }
//                    @Override
//                    public void onFailure(Call<HttpBean<List<BannerInfo>>> call, Throwable t) {
//                        super.onFailure(call, t);
//                        mRefreshLayout.finishRefreshing();
//                    }
//                });
//            }
//        });
//        mCartPresenter.loadShopCart();
//    }
//
//    //TODO bug
//    @Override
//    public void success(List<BannerInfo> data) {
//        if(data==null || data.isEmpty()){
//            mPagerContainer.setVisibility(View.GONE);
//        }
//        mPagerAdapter.addItems(data);
//        if(mPagerAdapter.getCount()>1000){
//            mTimer.start();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        setCartSize();
//    }
//
//    @Override
//    public void failed() {}
//
//    @Override
//    public void onAttach(Context context) {
//        if (context instanceof IShare) {
//            mIShare = (IShare) context;
//        } else {
//            throw new RuntimeException(context.toString() + " must implement IShare");
//        }
//        super.onAttach(context);
//    }
//
//    @OnClick(R.id.scan_view)
//    void scan(){
//        if(mIShare!=null){
//            mIShare.scan();
//        }
//    }
//
//    @Override
//    public void onFailure(int code) {}
//
//    @Override
//    public void onSuccess(Object data) {
//        setCartSize();
//    }
//
//    private void setCartSize() {
//        ShopCart shopCart = ShopCart.getInstance();
//        int size = shopCart.getCommodityCount();
//        if(size>0){
//            mCartSizeView.setVisibility(View.VISIBLE);
//            mCartSizeView.setText(String.valueOf(size));
//        }else {
//            mCartSizeView.setVisibility(View.GONE);
//        }
//    }
//
//    @OnClick(R.id.shop_cart)
//    void open(){
//        Intent intent=new Intent(getContext(),ShopCartActivity.class);
//        Util.checkLogin(getContext(),intent);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mPresenter.onDestory();
//        mCartPresenter.onDestory();
//        mWebView.removeAllViews();
//        mWebView.destroy();
//        mTimer.cancel();
//    }
//}
