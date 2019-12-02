package com.tmmt.innersect.ui.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.bumptech.glide.Glide;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.socks.library.KLog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Location;
import com.tmmt.innersect.mvp.model.MoreInfo;
import com.tmmt.innersect.mvp.model.ReserveInfo;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.presenter.ExtraPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.mvp.view.ExtraView;
import com.tmmt.innersect.ui.adapter.CommonPagerAdapter;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.LocationUtil;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.utils.Util;
import com.tmmt.innersect.widget.RefreshHeader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;

public class ReservationActivity extends AppCompatActivity implements CommonView<ReserveInfo>,
        ExtraView<Boolean>, BDLocationListener {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.more_pager)
    ViewPager mMorePager;
    @BindView(R.id.location_view)
    TextView mLocationView;
    @BindView(R.id.rule_view)
    TextView mRuleView;
    @BindView(R.id.step1_title)
    TextView mStep1Title;
    @BindView(R.id.step2_title)
    TextView mStep2Title;
    @BindView(R.id.step3_title)
    TextView mStep3Title;
    @BindView(R.id.step2_subtitle)
    TextView mStep2SubTitle;
    @BindView(R.id.step1_action)
    TextView mStepAction;
    @BindView(R.id.step3_subtitle)
    TextView mStep3SubTitle;
    @BindView(R.id.title_view)
    TextView mTitleView;
    @BindView(R.id.reserve_view)
    TextView mReserveView;
    @BindView(R.id.location_container)
    LinearLayout mContainer;
    @BindView(R.id.share_layout)
    View mShareLayout;
    @BindView(R.id.share_title)
    TextView mShareTitle;
    @BindView(R.id.share_time)
    TextView mShareTime;
    @BindView(R.id.image_view)
    ImageView mImageView;
    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout mRefreshLayout;

    String mLocation;
    CommonPagerAdapter mAdapter;
    //MorePagerAdapter mMoreAdapter;
    CommonPagerAdapter<MoreInfo> mMoreAdapter;
    CommonPresenter mPresenter;
    ExtraPresenter mExtraPresenter;

    LocationUtil mLocationUtil;

    private ReserveInfo mInfo;

    private boolean retry;
    private long mId;

    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);
        mPresenter = new CommonPresenter();
        mPresenter.attachView(this);
        mExtraPresenter = new ExtraPresenter();
        mExtraPresenter.attachView(this);
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setHeaderView(new RefreshHeader(this));
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mPresenter.getActivityInfo(mId);
            }
        });
        retry = false;
        mId = getIntent().getLongExtra(Constants.INFO_ID, 0);
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean allow) {
                        if (allow) {
                            getLocation();
                        } else {
                            mLocationView.setText(getString(R.string.without_permission));
                            mLocationView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    retry = true;
                                    openSetting();
                                }
                            });

                        }

                    }
                });
        mAdapter = new CommonPagerAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin(Util.dip2px(10));

        mMoreAdapter = new CommonPagerAdapter<>(false, (container, content) -> {

            ImageView imageView = new ImageView(container.getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnClickListener(v -> Util.openTarget(container.getContext(), content.schema, ""));
            Util.fillImage(container.getContext(), content.img, imageView);
            return imageView;

        });
        mMorePager.setAdapter(mMoreAdapter);
        mMorePager.setOffscreenPageLimit(3);

        mMorePager.setPageMargin(Util.dip2px(10));

        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                if (position > -1 && position < 1) {
                    float factor = Math.max(0.85f, 1 - Math.abs(position) * 0.8f);
                    page.setScaleX(factor);
                    page.setScaleY(factor);
                    //page.setTranslationZ();
                } else {
                    //float factor=Math.max(0.8f,1 - Math.abs(position)*0.1f);
                    page.setScaleX(0.85f);
                    page.setScaleY(0.85f);
                }

            }
        });
        mPresenter.getActivityInfo(mId);
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        mLocation = bdLocation.getCity();
        //bdLocation.
        mLocationView.post(new Runnable() {
            @Override
            public void run() {
                showHint();
            }
        });
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

    private void getLocation() {
        if (mLocationUtil == null) {
            mLocationUtil = new LocationUtil();
        }
        mLocationUtil.initLocation(this);
    }

    @OnClick(R.id.back_view)
    void back() {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (retry) {
            getLocation();
        }
    }

    @Override
    public void success(ReserveInfo data) {
        mInfo = data;
        fillView(data);
        mRefreshLayout.finishRefreshing();
    }

    @Override
    public void failed() {
        mRefreshLayout.finishRefreshing();

    }

    private static UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            KLog.d("plat", "platform" + platform);
            //Toast.makeText(getContext(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            //Toast.makeText(getContext(),platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                KLog.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            // Toast.makeText(getContext(),platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


    @OnClick(R.id.share_view)
    void share() {
        AnalyticsUtil.reportEvent("drawup_share");
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            doShare();
                        }
                    }
                });

    }

    void doShare() {
        Bitmap bitmap = getViewBitmap(mShareLayout);
        UMImage umImage = new UMImage(this, bitmap);
        new ShareAction(this)
                .withMedia(umImage)
                .setDisplayList(Util.getDisplayList(this))
                .setCallback(umShareListener)
                .open();

    }

    @Override
    public void onSuccess(Boolean data) {
        if (data) {
            SystemUtil.reportServerError("预约成功！");
            mInfo.status = 2;
            mReserveView.setText(R.string.my_reservation);
            //Util.startActivity(this, ReserveSuccessActivity.class);
        } else {

        }
    }

    @Override
    public void onFailure(int code) {
    }

    private void fillView(ReserveInfo data) {
        if (data == null) {
            return;
        }
        mAdapter.addItems(data.reservePic);
        mViewPager.setCurrentItem(mAdapter.getCount() / 2);

        mMoreAdapter.addItems(data.more);
        mContainer.removeAllViews();
        if (data.address != null && !data.address.isEmpty()) {
            TextView nameView;
            for (final Location location : data.address) {
                KLog.d(location.shopName);
                View view = LayoutInflater.from(this).inflate(R.layout.included_location, mContainer, false);
                nameView = view.findViewById(R.id.shop_name);
                nameView.setText(location.shopName);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebViewActivity.start(ReservationActivity.this, location.url);
                    }
                });
                mContainer.addView(view);
            }
        }
        mStepAction.setText(data.getAction());
        mTitleView.setText(data.title);
        mStep1Title.setText(data.guide.get(0).title);
        mStep2Title.setText(data.guide.get(1).title);
        mStep3Title.setText(data.guide.get(2).title);
        mStep2SubTitle.setText(data.guide.get(1).subtitle);
        mStep3SubTitle.setText(data.guide.get(2).subtitle);
        mRuleView.setText(data.description);
        mReserveView.setText(data.getStatus());
        mReserveView.setBackgroundColor(data.getBgColor());
        mReserveView.setTextColor(data.getTextColor());
        showHint();

        mShareTitle.setText(data.title);
        mShareTime.setText("预约时间：" + data.getTime());

        Glide.with(this).load(data.reservePic.get(0)).into(mImageView);

    }

    private void showHint() {
        if (mLocation == null) {
            mLocationView.setText(getString(R.string.location_failed));
            return;
        }
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).checkLocation(mId, mLocation).enqueue(new NetCallback<Boolean>() {
            @Override
            public void onSucceed(Boolean data) {
                if (data) {
                    mLocationView.setText(getString(R.string.in_area_hint));
                } else {
                    mLocationView.setText(getString(R.string.out_are_hint));
                }
            }
        });

    }

    @OnClick(R.id.register_view)
    void open() {
        if (mInfo == null) {
            return;
        }
        CollectionInfoActivity.start(this, mId, mInfo.status != 2);
    }

    @OnClick(R.id.reserve_view)
    void reserve() {
        if (mInfo != null) {
            if (mInfo.status == 1) {
                if (mLocation != null) {
                    mExtraPresenter.reserve(mId, mLocation);
                }
            } else if (mInfo.status == 2) {
                ReserveSuccessActivity.start(this, mId);
            }
        }

    }

    private Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
//        int color = v.getDrawingCacheBackgroundColor();
//        v.setDrawingCacheBackgroundColor(0);
//
//        if (color != 0) {
//            v.destroyDrawingCache();
//        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            KLog.e("Folder", "failed getViewBitmap(" + v + ")", new RuntimeException());
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        //v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    private void openSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationUtil != null) {
            mLocationUtil.clear(this);
        }
        mPresenter.onDestory();
        mExtraPresenter.onDestory();
    }
}
