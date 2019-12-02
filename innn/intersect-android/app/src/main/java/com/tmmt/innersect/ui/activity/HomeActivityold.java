//package com.tmmt.innersect.ui.activity;
//
//import android.Manifest;
//import android.content.Intent;
//import android.graphics.Color;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import com.ashokvarma.bottomnavigation.BottomNavigationBar;
//import com.ashokvarma.bottomnavigation.BottomNavigationItem;
//import com.socks.library.KLog;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.AccountInfo;
//import com.tmmt.innersect.datasource.net.ApiManager;
//import com.tmmt.innersect.datasource.net.NetCallback;
//import com.tmmt.innersect.mvp.model.Information;
//import com.tmmt.innersect.mvp.model.UpdateInfo;
//import com.tmmt.innersect.ui.fragment.CommonDialogFragment;
//import com.tmmt.innersect.ui.fragment.HomeFragment;
//import com.tmmt.innersect.ui.fragment.NewsFragment;
//import com.tmmt.innersect.ui.fragment.PersonInfoFragment;
//import com.tmmt.innersect.ui.fragment.RateDialogFragment;
//import com.tmmt.innersect.utils.AnalyticsUtil;
//import com.tmmt.innersect.utils.PermissionUtils;
//import com.tmmt.innersect.utils.SystemUtil;
//import com.tmmt.innersect.utils.Util;
//import com.umeng.socialize.ShareAction;
//import com.umeng.socialize.UMShareAPI;
//import com.umeng.socialize.UMShareListener;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.media.UMWeb;
//import com.uuzuche.lib_zxing.activity.CodeUtils;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//
//public class HomeActivity extends AppCompatActivity implements IShare {
//    private static final int SCAN_CODE = 1;
//    private static final int SHARE_APP_CODE = 2;
//    private static final int SHARE_NEWS_CODE = 3;
//    private static final int RECORD_CODE = 4;
//
//    @BindView(R.id.bottom_navigation_bar)
//    BottomNavigationBar mBottomBar;
//    int mCurrPosition;
//    int mOpenCount;
//
//    Fragment mPreFragment;
//    Fragment mHomeFragment;
//    Fragment mNewsFragment;
//    Fragment mPersonFragment;
//
//    @Override
//    protected void onCreate(final Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
//        checkUpdate();
//        ButterKnife.bind(this);
//        mOpenCount=AccountInfo.getOpenCount();
//        if(mOpenCount==2){
//            RateDialogFragment dialogFragment=new RateDialogFragment();
//            dialogFragment.show(getSupportFragmentManager(),"rate");
//        }
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_fragment);
//        if (fragment == null) {
//            fillFragment(0);
//        }
//        mBottomBar.addItem(new BottomNavigationItem(R.mipmap.home_select, getString(R.string.home))
//                .setInactiveIconResource(R.mipmap.home_unselect)
//                .setActiveColor(Color.BLACK))
//                .addItem(new BottomNavigationItem(R.mipmap.infor_select, getString(R.string.infor))
//                        .setActiveColor(Color.BLACK)
//                        .setInactiveIconResource(R.mipmap.infor_unselect))
//                .addItem(new BottomNavigationItem(R.mipmap.user_select, getString(R.string.me))
//                        .setActiveColor(Color.BLACK)
//                        .setInactiveIconResource(R.mipmap.user_unselect))
//                .initialise();
//
//        mBottomBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(int position) {
//                fillFragment(position);
//            }
//
//            @Override
//            public void onTabUnselected(int position) {
//            }
//
//            @Override
//            public void onTabReselected(int position) {
//            }
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(mCurrPosition==2){
//            if(AccountInfo.getInstance().isLogin()){
//                fillFragment(mCurrPosition);
//            }else {
//                mBottomBar.selectTab(0);
//            }
//        }
//    }
//
//    private void fillFragment(int position) {
//        mCurrPosition = position;
//        switch (position) {
//            case 0:
//                if(mHomeFragment==null){
//                    mHomeFragment = new HomeFragment();
//                }
//                changeFragment(mHomeFragment);
//                break;
//            case 1:
//                if(mNewsFragment==null){
//                    mNewsFragment = new NewsFragment();
//                }
//                changeFragment(mNewsFragment);
//                break;
//            case 2:
//                if(AccountInfo.getInstance().isLogin()){
//                    if(mPersonFragment==null ){
//                        mPersonFragment = new PersonInfoFragment();
//                    }
//                    changeFragment(mPersonFragment);
//
//                }else {
//                    mBottomBar.selectTab(mCurrPosition,false);
//                    Util.startActivity(this,LoginActivity.class);
//                    return;
//                }
//        }
////        getSupportFragmentManager()
////                .beginTransaction()
////                .replace(R.id.content_fragment, mCurrentFragment)
////                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
////                .commit();
//    }
//
//
//    private void changeFragment(Fragment showFragment){
//        if(mPreFragment==showFragment){
//            return;
//        }
//        if(showFragment.isAdded()){
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .show(showFragment)
//                    .commit();
//
//        }else {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.content_fragment,showFragment)
//                    .commit();
//        }
//        if(mPreFragment!=null){
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .hide(mPreFragment)
//                    .commit();
//
//        }
//        mPreFragment=showFragment;
//
//    }
//
//    private static UMShareListener umShareListener = new UMShareListener() {
//        @Override
//        public void onStart(SHARE_MEDIA platform) {
//        }
//
//        @Override
//        public void onResult(SHARE_MEDIA platform) {
//            KLog.d("plat", "platform" + platform);
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, Throwable t) {
//            if (t != null) {
//                KLog.d("throw", "throw:" + t.getMessage());
//            }
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform) {
//        }
//    };
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//        if(requestCode==SCAN_CODE){
//            //处理扫描结果（在界面上显示）
//            if (null != data) {
//                Bundle bundle = data.getExtras();
//                if (bundle == null) {
//                    return;
//                }
//                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
//                    String result = bundle.getString(CodeUtils.RESULT_STRING);
//                    KLog.d(result);
//                    try{
//                        String content=Util.decode(result,"tmmt2017");
//                        KLog.d(content);
//                        Util.openTarget(this,content," ");
//                    }catch (Exception e){}
//
//                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                    SystemUtil.reportServerError("二维码无效");
//                }
//            }
//
//        }
//    }
//
//    public void share() {}
//
//
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
//    private void doShare() {
//        AnalyticsUtil.reportEvent(AnalyticsUtil.MY_SHARE);
//        UMWeb umWeb = new UMWeb("http://innersect.net/share");
//        umWeb.setTitle("加入INNERSECT，顶尖正价潮货抢先GO");
//        umWeb.setDescription("最值得期待潮流APP，汇集全球顶级潮流品牌");
//        umWeb.setThumb(new UMImage(this, R.mipmap.app_icon));
//        new ShareAction(this)
//                .withText("VLONE亚洲首发，加入innersect顶尖正价潮货抢先GO！")
//                .withMedia(umWeb)
//                .setDisplayList(Util.getDisplayList(this))
//                .setCallback(umShareListener)
//                .open();
//    }
//
//    void doShare(Information info) {
//        AnalyticsUtil.reportEvent("home_news_detail_share");
//        UMWeb umWeb = new UMWeb("http://innersect.net/news/" + info.id);
//        if (info.title != null) {
//            umWeb.setTitle(info.title);
//        } else {
//            umWeb.setTitle("加入INNERSECT，顶尖正价潮货抢先GO");
//        }
//        umWeb.setDescription("最值得期待潮流APP，汇集全球顶级潮流品牌");
//        umWeb.setThumb(new UMImage(this, R.mipmap.app_icon));
//        new ShareAction(this)
//                .withText("VLONE亚洲首发，加入innersect顶尖正价潮货抢先GO！")
//                .withMedia(umWeb)
//                .setDisplayList(Util.getDisplayList(this))
//                .setCallback(umShareListener)
//                .open();
//    }
//
//    @Override
//    public void scan() {
////        Util.startActivity(this,ReservationActivity.class);
//        PermissionUtils.checkPermission(this, new String[]{Manifest.permission.CAMERA}, SCAN_CODE, new PermissionUtils.PermissionGrant() {
//            @Override
//            public void onPermissionGranted(int requestCode) {
//                doScan();
//            }
//        });
//    }
//
//    void doScan() {
//        Intent intent = new Intent(this, ScanActivity.class);
//        startActivityForResult(intent, SCAN_CODE);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case SCAN_CODE:
//                PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
//                    @Override
//                    public void onPermissionGranted(int requestCode) {
//                        doScan();
//                    }
//                });
//                break;
//            case SHARE_APP_CODE:
//                PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
//                    @Override
//                    public void onPermissionGranted(int requestCode) {
//                        doShare();
//                    }
//                });
//                break;
//            case SHARE_NEWS_CODE:
//                PermissionUtils.requestPermissionsResult(this, requestCode, permissions, grantResults, new PermissionUtils.PermissionGrant() {
//                    @Override
//                    public void onPermissionGranted(int requestCode) {
//                        doShare();
//                    }
//                });
//                break;
//            case RECORD_CODE:
//        }
//    }
//
//    @Override
//    public void shareApp() {
//        PermissionUtils.checkPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                SHARE_APP_CODE, new PermissionUtils.PermissionGrant() {
//                    @Override
//                    public void onPermissionGranted(int requestCode) {
//                        doShare();
//                    }
//                });
//    }
//
//    @Override
//    public void shareNews(final Information info) {
//        PermissionUtils.checkPermission(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                SHARE_NEWS_CODE, new PermissionUtils.PermissionGrant() {
//                    @Override
//                    public void onPermissionGranted(int requestCode) {
//                        doShare(info);
//                    }
//                });
//    }
//
//    //@OnClick(R.id.shop_cart)
//    void open() {
//        Util.startActivity(this, ScheduleActivity.class);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        AccountInfo.saveOpenCount(++mOpenCount);
//        UMShareAPI.get(this).release();
//    }
//
//}
