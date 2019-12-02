package com.tmmt.innersect.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.socks.library.KLog;
import com.tmmt.innersect.BuildConfig;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Information;
import com.tmmt.innersect.mvp.model.Tips;
import com.tmmt.innersect.mvp.model.UpdateInfo;
import com.tmmt.innersect.ui.fragment.CategoryFragment;
import com.tmmt.innersect.ui.fragment.CommonDialogFragment;
import com.tmmt.innersect.ui.fragment.HomeFragment;
import com.tmmt.innersect.ui.fragment.NewsFragment;
import com.tmmt.innersect.ui.fragment.PersonInfoFragment;
import com.tmmt.innersect.ui.fragment.RateDialogFragment;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.PermissionUtils;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.utils.Util;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Single;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;


public class HomeActivity extends AppCompatActivity implements IShare {

    private static final String CURR_POSITION = "curr_position";
    private static final String PRE_POSITION = "pre_position";
    private static final int SCAN_CODE = 1;
    private static final int SHARE_APP_CODE = 2;
    private static final int SHARE_NEWS_CODE = 3;
    private static final int RECORD_CODE = 4;

    @BindView(R.id.bottom_navigation_bar)
    BottomNavigationBar mBottomBar;

    static final String TAG[]={"home","discover","news","person"};
    int mCurrPosition;
    int mPrePosition;
    int mOpenCount;
    boolean isFirst;

    Subscription mSubscription;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst=true;
        setContentView(R.layout.activity_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        checkUpdate();
        ButterKnife.bind(this);
        if(savedInstanceState!=null){
            mCurrPosition=savedInstanceState.getInt(CURR_POSITION,0);
            mPrePosition=savedInstanceState.getInt(PRE_POSITION,-1);
        }else {
            mPrePosition=-1;
            mCurrPosition=0;
        }

        mOpenCount=AccountInfo.getOpenCount();
        if(mOpenCount==2){
            RateDialogFragment dialogFragment=new RateDialogFragment();
            dialogFragment.show(getSupportFragmentManager(),"rate");
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_fragment);
        if (fragment == null && savedInstanceState==null) {
            fillFragment(mCurrPosition);
        }
        mBottomBar.addItem(new BottomNavigationItem(R.mipmap.home_select, getString(R.string.home))
                    .setInactiveIconResource(R.mipmap.home_unselect)
                    .setActiveColor(Color.BLACK))

                .addItem(new BottomNavigationItem(R.mipmap.discover_selected, "选购")
                        .setActiveColor(Color.BLACK)
                        .setInactiveIconResource(R.mipmap.discover_unselected))

                .addItem(new BottomNavigationItem(R.mipmap.infor_select, "探索")
                        .setActiveColor(Color.BLACK)
                        .setInactiveIconResource(R.mipmap.infor_unselect))
                .addItem(new BottomNavigationItem(R.mipmap.user_select, getString(R.string.me))
                        .setActiveColor(Color.BLACK)
                        .setInactiveIconResource(R.mipmap.user_unselect))
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setMode(BottomNavigationBar.MODE_FIXED)

                .initialise();


        mBottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                fillFragment(position);
            }
            @Override
            public void onTabUnselected(int position) {}
            @Override
            public void onTabReselected(int position) {}
        });

        if(AccountInfo.getInstance().isLogin()){
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).reportAction(1).enqueue(new NetCallback<String>() {
                @Override
                public void onSucceed(String data) {}
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //super.onNewIntent(intent);
        //int index =intent.getIntExtra(Constants.INDEX,0);
        //mBottomBar.selectTab(index);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURR_POSITION,mCurrPosition);
        outState.putInt(PRE_POSITION,mPrePosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCurrPosition==3){
            if(AccountInfo.getInstance().isLogin()){
                fillFragment(mCurrPosition);
            }else {
                mBottomBar.selectTab(0);
            }
        }
        if(mCurrPosition==0){
            if (AccountInfo.getInstance().isLogin()&&(AccountInfo.showDialog(Constants.SHOW_COUPON))) {
                ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getCouponTip().enqueue(new NetCallback<Tips>() {
                    @Override
                    public void onSucceed(Tips data) {
                        if(data!=null && data.count>0){
                            CommonDialogFragment dialogFragment=CommonDialogFragment.newInstance(R.layout.dialog_coupon_hint,String.format("你有%d张优惠券未使用哦",data.count),data.tips);
                            dialogFragment.show(getSupportFragmentManager(), "lottery");
                            dialogFragment.setActionListener(new CommonDialogFragment.ActionListener() {
                                @Override
                                public void doAction() {
                                    Util.startActivity(HomeActivity.this,CouponActivity.class);
                                }
                                @Override
                                public void cancel() {
                                }
                            });
                            AccountInfo.setShowDialog(Constants.SHOW_COUPON);
                        }
                    }
                });
            }
        }
    }

    private static UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {}
        @Override
        public void onResult(SHARE_MEDIA platform) {
            KLog.d("plat", "platform" + platform);
        }
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t != null) {
                KLog.d("throw", "throw:" + t.getMessage());
            }
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {}
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if(requestCode==SCAN_CODE){
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    KLog.d(result);
                    try{
                        String content=Util.decode(result,"tmmt2017");
                        KLog.i(content);
                        Util.openTarget(this,content," ");
                    }catch (Exception e){}

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    SystemUtil.reportServerError("二维码无效");
                }
            }
        }
    }

    public void share() {}

    private void checkUpdate(){
        String version= SystemUtil.getPackageInfo().versionName;
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getAppInfo(version,"android").enqueue(new NetCallback<UpdateInfo>() {

            @Override
            public void onSucceed(UpdateInfo data) {
                UpdateInfo info = data;
                if (info.isForceUpdate != 0) {
                    int layoutId = R.layout.dialog_vlone_update;
                    if (info.isForceUpdate == 2) {
                        layoutId = R.layout.dialog_force_update;
                    }
                    CommonDialogFragment updateDialog = CommonDialogFragment.newInstance(layoutId,info.title,info.changeContent);
                    updateDialog.setCancelable(false);
                    updateDialog.setActionListener(new CommonDialogFragment.ActionListener() {
                        @Override
                        public void doAction() {
                            Uri uri = Uri.parse("http://innersect.net/share/#/");
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                            AnalyticsUtil.reportEvent(AnalyticsUtil.UPDATE_FORCE_CONFIRM);
                        }
                        @Override
                        public void cancel() {
                            AnalyticsUtil.reportEvent(AnalyticsUtil.UPDATE_NORMAL_CANCEL);
                        }
                    });
                    updateDialog.show(getSupportFragmentManager(), "update");
                }
            }
        });
    }


    private void fillFragment(int position) {
        if(mPrePosition==position){
            return;
        }
        mCurrPosition=position;
        if(position==3 && !AccountInfo.getInstance().isLogin()){
            //mBottomBar.selectTab(mCurrPosition,false);
            Util.startActivity(this,LoginActivity.class);
            return;
        }
        Fragment curr=getSupportFragmentManager().findFragmentByTag(TAG[position]);
        if(curr==null){
            switch (position){
                case 0:
                    curr=new HomeFragment();
                    break;
                case 1:
                    curr=new CategoryFragment();
                    break;
                case 2:
                    curr=new NewsFragment();
                    break;
                case 3:
                    curr=new PersonInfoFragment();
                    break;
                default:
                    curr=new HomeFragment();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.content_fragment,curr,TAG[position])
                    .commit();

        }else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .show(curr)
                    //.attach(curr)
                    .commit();
        }
        if(mPrePosition!=-1){
            Fragment pre=getSupportFragmentManager().findFragmentByTag(TAG[mPrePosition]);
            if(pre!=null){
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(pre)
                        //.detach(pre)
                        .commit();
            }
        }
        mPrePosition=mCurrPosition;
    }

    private void doShare() {
        AnalyticsUtil.reportEvent(AnalyticsUtil.MY_SHARE);
        UMWeb umWeb = new UMWeb(BuildConfig.ENDURL+"share");
        umWeb.setTitle("加入INNERSECT，顶尖正价潮货抢先GO");
        umWeb.setDescription("最值得期待潮流APP，汇集全球顶级潮流品牌");
        umWeb.setThumb(new UMImage(this, R.mipmap.app_icon));
        new ShareAction(this)
                .withText("VLONE亚洲首发，加入innersect顶尖正价潮货抢先GO！")
                .withMedia(umWeb)
                .setDisplayList(Util.getDisplayList(this))
                .setCallback(umShareListener)
                .open();
    }

    void doShare(Information info) {
        AnalyticsUtil.reportEvent("home_news_detail_share");
        UMWeb umWeb = new UMWeb("https://m.innersect.net/news/" + info.id);
        if (info.title != null) {
            umWeb.setTitle(info.title);
        } else {
            umWeb.setTitle("加入INNERSECT，顶尖正价潮货抢先GO");
        }
        umWeb.setDescription("最值得期待潮流APP，汇集全球顶级潮流品牌");
        umWeb.setThumb(new UMImage(this, R.mipmap.app_icon));
        new ShareAction(this)
                .withText("加入INNERSECT，顶尖正价潮货抢先GO，最值得期待的潮流APP，汇聚全球顶级潮流品牌")
                .withMedia(umWeb)
                .setDisplayList(Util.getDisplayList(this))
                .setCallback(umShareListener)
                .open();
    }

    @Override
    public void scan() {
//        Util.startActivity(this,SuccessActivity.class);
        PermissionUtils.checkPermission(this, new String[]{Manifest.permission.CAMERA}, SCAN_CODE, new PermissionUtils.PermissionGrant() {
            @Override
            public void onPermissionGranted(int requestCode) {
                doScan();
            }
        });
    }

    void doScan() {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivityForResult(intent, SCAN_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SCAN_CODE:
                PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
                    @Override
                    public void onPermissionGranted(int requestCode) {
                        doScan();
                    }
                });
                break;
            case SHARE_APP_CODE:
                PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
                    @Override
                    public void onPermissionGranted(int requestCode) {
                        doShare();
                    }
                });
                break;
            case SHARE_NEWS_CODE:
                PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
                    @Override
                    public void onPermissionGranted(int requestCode) {
                        doShare();
                    }
                });
                break;
            case RECORD_CODE:
        }
    }

    @Override
    public void shareApp() {
        PermissionUtils.checkPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                SHARE_APP_CODE, new PermissionUtils.PermissionGrant() {
                    @Override
                    public void onPermissionGranted(int requestCode) {
                        doShare();
                    }
                });
    }

    @Override
    public void shareNews(final Information info) {
        PermissionUtils.checkPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                SHARE_NEWS_CODE, new PermissionUtils.PermissionGrant() {
                    @Override
                    public void onPermissionGranted(int requestCode) {
                        doShare(info);
                    }
                });
    }

    void open() {
        //Util.startActivity(this, ScheduleActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSubscription!=null){
            mSubscription.unsubscribe();
        }
        AccountInfo.saveOpenCount(++mOpenCount);
        UMShareAPI.get(this).release();
    }

    @Override
    public void onBackPressed() {
        if(isFirst){
            mSubscription=Single.just(isFirst).delay(2, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(b-> {
                        isFirst = true;
                    });
            isFirst=false;
            SystemUtil.toast("再按一次退出程序");
        }else {
            super.onBackPressed();
        }
    }
}
