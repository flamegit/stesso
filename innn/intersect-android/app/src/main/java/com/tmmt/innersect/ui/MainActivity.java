//package com.tmmt.innersect.ui;
//
//
//import android.Manifest;
//import android.annotation.TargetApi;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.design.widget.TabLayout;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.LinearLayout;
//
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.AccountInfo;
//import com.tmmt.innersect.ui.activity.CommodityDetailActivity;
//import com.tmmt.innersect.ui.activity.LoginActivity;
//import com.tmmt.innersect.ui.activity.PersonInfoActivity;
//import com.tmmt.innersect.ui.adapter.FragmentAdapter;
//import com.tmmt.innersect.utils.LocationUtil;
//import com.tmmt.innersect.utils.Util;
//
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//
//public class MainActivity extends AppCompatActivity {
//
//    @BindView(R.id.fragment_pager)
//    ViewPager mFragmentPager;
//    @BindView(R.id.tab_layout)
//    TabLayout mTabLayout;
//
//    private String permissionInfo;
//    private final int SDK_PERMISSION_REQUEST = 127;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main2);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        ButterKnife.bind(this);
//        mFragmentPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
//        mTabLayout.setupWithViewPager(mFragmentPager);
//        setUpIndicatorWidth();
//        getPersimmions();
//        LocationUtil.initLocation();
//    }
//
//    private void setUpIndicatorWidth() {
//        Class<?> tabLayoutClass = mTabLayout.getClass();
//        Field tabStrip = null;
//        try {
//            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
//            tabStrip.setAccessible(true);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//        LinearLayout layout = null;
//        try {
//            if (tabStrip != null) {
//                layout = (LinearLayout) tabStrip.get(mTabLayout);
//            }
//            for (int i = 0; i < layout.getChildCount(); i++) {
//                View child = layout.getChildAt(i);
//                child.setPadding(0, 0, 0, 0);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    params.setMarginStart(Util.dip2px(30));
//                    params.setMarginEnd(Util.dip2px(30));
//                }
//                child.setLayoutParams(params);
//                child.invalidate();
//            }
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//    @TargetApi(23)
//    private void getPersimmions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ArrayList<String> permissions = new ArrayList<String>();
//            /***
//             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
//             */
//            // 定位精确位置
//            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
//            }
//            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//            }
//			/*
//			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
//			 */
//            // 读写权限
//            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
//            }
//            // 读取电话状态权限
//            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
//                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
//            }
//
//            if (permissions.size() > 0) {
//                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
//            }
//        }
//    }
//
//    @TargetApi(23)
//    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
//        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
//            if (shouldShowRequestPermissionRationale(permission)){
//                return true;
//            }else{
//                permissionsList.add(permission);
//                return false;
//            }
//
//        }else{
//            return true;
//        }
//    }
//
//    @TargetApi(23)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        // TODO Auto-generated method stub
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//    }
//
//    @OnClick(R.id.search_view)
//    public void openSearch(){}
//
//    @OnClick(R.id.person_view)
//    public void openSetting(){
//        if( AccountInfo.getInstance().isLogin()){
//            Util.startActivity(this,PersonInfoActivity.class);
//        }else {
//            Util.startActivity(this,LoginActivity.class);
//        }
//    }
//
//    @OnClick(R.id.change_gender)
//    public void openShopCart(){
//        Util.startActivity(this,CommodityDetailActivity.class);}
//
//}
