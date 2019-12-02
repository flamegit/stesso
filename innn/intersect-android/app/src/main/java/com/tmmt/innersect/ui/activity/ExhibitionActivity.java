//package com.tmmt.innersect.ui.activity;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Typeface;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.ViewPager;
//import android.support.v4.widget.NestedScrollView;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.bumptech.glide.Glide;
//import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
//import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.AccountInfo;
//import com.tmmt.innersect.datasource.net.ApiManager;
//import com.tmmt.innersect.datasource.net.NetCallback;
//import com.tmmt.innersect.mvp.model.BannerInfo;
//import com.tmmt.innersect.mvp.model.ExhibitionModel;
//import com.tmmt.innersect.mvp.model.HttpBean;
//import com.tmmt.innersect.mvp.model.InformationModel;
//import com.tmmt.innersect.mvp.model.UpdateInfo;
//import com.tmmt.innersect.ui.adapter.ArtistAdapter;
//import com.tmmt.innersect.ui.adapter.BrandPagerAdapter;
//import com.tmmt.innersect.ui.adapter.ImagePagerAdapter;
//import com.tmmt.innersect.ui.adapter.InfoAdapter;
//import com.tmmt.innersect.ui.fragment.CommonDialogFragment;
//import com.tmmt.innersect.ui.fragment.RateDialogFragment;
//import com.tmmt.innersect.utils.AnalyticsUtil;
//import com.tmmt.innersect.utils.SystemUtil;
//import com.tmmt.innersect.utils.Util;
//import com.tmmt.innersect.widget.CirclePageIndicator;
//import com.tmmt.innersect.widget.RefreshHeader;
//import java.util.List;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
//import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class ExhibitionActivity extends AppCompatActivity {
//    private static final int REQUEST_READ_PHONE_STATE=1;
//    @BindView(R.id.scroll_view)
//    NestedScrollView mScrollView;
//    @BindView(R.id.pager_indicator)
//    CirclePageIndicator mIndicator;
//    @BindView(R.id.view_pager)
//    ViewPager mViewPager;
//    @BindView(R.id.brand_view)
//    ViewPager mBrandView;
//    @BindView(R.id.star_view)
//    ViewPager mStartView;
//    @BindView(R.id.artist_view)
//    ViewPager mArtistView;
//    @BindView(R.id.brand_indicator)
//    CirclePageIndicator mBrandIndicator;
//    @BindView(R.id.star_indicator)
//    CirclePageIndicator mStarIndicator;
//    @BindView(R.id.artist_indicator)
//    CirclePageIndicator mArtistIndicator;
//    @BindView(R.id.recycler_view)
//    RecyclerView mRecyclerView;
//    @BindView(R.id.video_player)
//    JCVideoPlayerStandard mVideoPlayer;
//    @BindView(R.id.refresh_layout)
//    TwinklingRefreshLayout mRefreshLayout;
//    @BindView(R.id.end_view)
//    TextView mEndView;
//    @BindView(R.id.brand_title)
//    TextView mBrandTitle;
//    @BindView(R.id.star_title)
//    TextView mStartTitle;
//    @BindView(R.id.artist_title)
//    TextView mArtistTitle;
//    @BindView(R.id.title_view)
//    TextView mTitleView;
//    @BindView(R.id.info_title)
//    TextView mInfoTitle;
//    @BindView(R.id.time_address)
//    TextView mTimeView;
//    @BindView(R.id.ticket_enter)
//    ImageView mEnterView;
//
//    InfoAdapter mAdapter;
//    ImagePagerAdapter mPagerAdapter;
//    BrandPagerAdapter mBrandAdapter;
//    ArtistAdapter mStarAdapter;
//    ArtistAdapter mArtistAdapter;
//    CountDownTimer mTimer;
//    private volatile boolean showAnimation;
//    int mOpenCount;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        checkPermission();
//        setContentView(R.layout.activity_exhibition);
//        ButterKnife.bind(this);
//        mOpenCount=AccountInfo.getOpenCount();
//        if(mOpenCount==2){
//            RateDialogFragment dialogFragment=new RateDialogFragment();
//            dialogFragment.show(getSupportFragmentManager(),"rate");
//        }
//        //Typeface font=Util.getTypeface(this,"siyuan.otf");
//        //mTitleView.setTypeface(font);
//        //mTimeView.setTypeface(font);
//        //mBrandTitle.setTypeface(font);
//        //mStartTitle.setTypeface(font);
//        //mArtistTitle.setTypeface(font);
//        //mInfoTitle.setTypeface(font);
//        mAdapter=new InfoAdapter();
//        mPagerAdapter=new ImagePagerAdapter();
//        mBrandAdapter=new BrandPagerAdapter(1);
//        //mStarAdapter=new BrandPagerAdapter(2);
//        //mArtistAdapter=new BrandPagerAdapter(2);
//        mStarAdapter=new ArtistAdapter(getSupportFragmentManager());
//        mArtistAdapter=new ArtistAdapter(getSupportFragmentManager());
//        mBrandView.setAdapter(mBrandAdapter);
//
//        mViewPager.setAdapter(mPagerAdapter);
//        mIndicator.setViewPager(mViewPager);
//        mStartView.setAdapter(mStarAdapter);
//        mArtistView.setAdapter(mArtistAdapter);
//        mBrandIndicator.setViewPager(mBrandView);
//        mStarIndicator.setViewPager(mStartView);
//        mArtistIndicator.setViewPager(mArtistView);
//        mTimer=new CountDownTimer(4*1000*1000,4*1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                if (millisUntilFinished < 3995000) {
//                    mViewPager.arrowScroll(View.FOCUS_RIGHT);
//                }
//            }
//            @Override
//            public void onFinish() {}
//        };
//
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setNestedScrollingEnabled(false);
//        loadContent(System.currentTimeMillis(),false);
//        loadBanner();
//        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getExhibitionInfo().enqueue(new NetCallback<ExhibitionModel>() {
//            @Override
//            public void onSucceed(ExhibitionModel data) {
//                if(!isDestroyed()){
//                    fillView(data);
//                }
//            }
//        });
//        mRefreshLayout.setHeaderView(new RefreshHeader(this));
//        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
//                super.onRefresh(refreshLayout);
//                loadContent(System.currentTimeMillis(),false);
//                AnalyticsUtil.reportEvent("home_pulldown_refresh");
//            }
//            @Override
//            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
//                super.onLoadMore(refreshLayout);
//                long time=mAdapter.getLastTimestamp();
//                loadContent(time,true);
//                AnalyticsUtil.reportEvent("home_news_pullup_load");
//            }
//        });
//        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                  if(scrollY>Util.getWindowWidth() && !showAnimation){
//                      mBrandAdapter.rotation();
//                      showAnimation=true;
//                  }
//            }
//        });
//        checkUpdate();
//    }
//    private void checkPermission(){
//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
//        } else {
//            //TODO
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_READ_PHONE_STATE:
//                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    //TODO
//                }else {
//                    Toast.makeText(this,"抱歉没有该权限，应用无法运行",Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                break;
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        showAnimation=false;
//        mBrandAdapter.reset();
//        if(mScrollView.getScrollY()>Util.getWindowWidth() && !showAnimation){
//            mBrandAdapter.rotation();
//            showAnimation=true;
//        }
//    }
//
//    @OnClick(R.id.ticket_enter)
//    void buyTicket(){
//        Util.startActivity(this,HomeActivity.class);
//        //Util.startActivity(this,TicketDetailActivity.class);
//    }
//
//    //TODO
//    @OnClick(R.id.user_view)
//    void openUser(){
//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        boolean islogin=AccountInfo.getInstance().isLogin();
//        if(islogin){
//            Util.startActivity(this,PersonInfoActivity.class);
//        }else {
//            Util.startActivity(this,LoginActivity.class);
//        }
//    }
//    private void checkUpdate(){
//        String version= SystemUtil.getPackageInfo().versionName;
//        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getAppInfo(version,"android").enqueue(new NetCallback<UpdateInfo>() {
//
//            @Override
//            public void onSucceed(UpdateInfo data) {
//                UpdateInfo info = data;
//                if (info.isForceUpdate != 0) {
//                    int layoutId = R.layout.dialog_vlone_update;
//                    if (info.isForceUpdate == 2) {
//                        layoutId = R.layout.dialog_force_update;
//                    }
//                    CommonDialogFragment updateDialog = CommonDialogFragment.newInstance(layoutId,info.title,info.changeContent);
//                    updateDialog.setCancelable(false);
//                    updateDialog.setActionListener(new CommonDialogFragment.ActionListener() {
//                        @Override
//                        public void doAction() {
//                            Uri uri = Uri.parse("http://innersect.net/share/#/");
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            startActivity(intent);
//                            AnalyticsUtil.reportEvent(AnalyticsUtil.UPDATE_FORCE_CONFIRM);
//                        }
//                        @Override
//                        public void cancel() {
//                            AnalyticsUtil.reportEvent(AnalyticsUtil.UPDATE_NORMAL_CANCEL);
//                        }
//                    });
//                    updateDialog.show(getSupportFragmentManager(), "update");
//                }
//            }
//        });
//    }
//
//    private void loadContent(long time, final boolean append){
//        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getInfoList(time,5).enqueue(new Callback<InformationModel>() {
//            @Override
//            public void onResponse(Call<InformationModel> call, Response<InformationModel> response) {
//                if(append){
//                    mRefreshLayout.finishLoadmore();
//                }else {
//                    mRefreshLayout.finishRefreshing();
//                }
//                if(response.isSuccessful()){
//                    if(response.body().code==200){
//                        InformationModel.Data data=response.body().data;
//                        if(data.isEnd==1){
//                            mEndView.setVisibility(View.VISIBLE);
//                            mRefreshLayout.setEnableLoadmore(false);
//                        }
//                        if(append){
//                            mAdapter.appendItems(response.body().data.retvalList);
//                        }else {
//                            mAdapter.addItems(response.body().data.retvalList);
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onFailure(Call<InformationModel> call, Throwable t) {
//                if(append){
//                    mRefreshLayout.finishLoadmore();
//                }else {
//                    mRefreshLayout.finishRefreshing();
//                }
//            }
//        });
//    }
//    private void loadBanner(){
//        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getBannerInfo().enqueue(new Callback<HttpBean<List<BannerInfo>>>() {
//            @Override
//            public void onResponse(Call<HttpBean<List<BannerInfo>>> call, Response<HttpBean<List<BannerInfo>>> response) {
//                if(response.isSuccessful()){
//                    if(response.body().code==200){
//                        mPagerAdapter.addItems(response.body().data);
//                        if(mPagerAdapter.getCount()>1000){
//                            mTimer.start();
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onFailure(Call<HttpBean<List<BannerInfo>>> call, Throwable t) {}
//        });
//    }
//
//    private void fillView(final ExhibitionModel model){
//        if(model==null){
//            return;
//        }
//        mTitleView.setText(model.title);
//        mTimeView.setText(model.time+"\n"+model.address);
//        mVideoPlayer.setUp(model.video, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, " ");
//
//        Glide.with(this).load(model.videoImg).into(mVideoPlayer.thumbImageView);
//
//        Glide.with(this).load(model.ticketImg).into(mEnterView);
//
//        mBrandAdapter.addItems(model.brands );
//        mStarAdapter.addItems(model.stars);
//        mArtistAdapter.addItems(model.artists);
//
//        if(model.brands==null ||model.brands.isEmpty()){
//            mBrandTitle.setVisibility(View.GONE);
//            mBrandView.setVisibility(View.GONE);
//            mBrandIndicator.setVisibility(View.GONE);
//        }
//        if(model.stars==null ||model.stars.isEmpty()){
//            mStartTitle.setVisibility(View.GONE);
//            mStartView.setVisibility(View.GONE);
//            mStarIndicator.setVisibility(View.GONE);
//        }
//        if(model.artists==null ||model.artists.isEmpty()){
//            mArtistTitle.setVisibility(View.GONE);
//            mArtistView.setVisibility(View.GONE);
//            mArtistIndicator.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (JCVideoPlayer.backPress()) {
//            return;
//        }
//        AccountInfo.saveOpenCount(++mOpenCount);
//        super.onBackPressed();
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        JCVideoPlayer.releaseAllVideos();
//        mBrandAdapter.stopRotation();
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mTimer.cancel();
//    }
//}
//
//
//
