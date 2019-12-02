package com.tmmt.innersect.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tmmt.innersect.App;
import com.tmmt.innersect.common.AccountInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class SystemUtil {
  
    /** 
     * 获取当前手机系统语言。 
     * 
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN” 
     */  
    public static String getSystemLanguage() {  
        return Locale.getDefault().getLanguage();  
    }  
  
    /** 
     * 获取当前系统上的语言列表(Locale列表) 
     * 
     * @return  语言列表 
     */  
    public static Locale[] getSystemLanguageList() {  
        return Locale.getAvailableLocales();
    }  
  
    /** 
     * 获取当前手机系统版本号 
     * 
     * @return  系统版本号
     */  
    public static String getSystemVersion() {  
        return android.os.Build.VERSION.RELEASE;  
    }  
  
    /** 
     * 获取手机型号 
     * 
     * @return  手机型号 
     */  
    public static String getSystemModel() {  
        return android.os.Build.MODEL;  
    }  
  
    /** 
     * 获取手机厂商 
     * 
     * @return  手机厂商 
     */  
    public static String getDeviceBrand() {  
        return android.os.Build.BRAND;  
    }  
  
    /** 
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限) 
     * 
     * @return  手机IMEI 
     */  
    public static String getIMEI() {
        if(!AccountInfo.getInstance().isLogin()){
            return "imei";
        }

        TelephonyManager tm = (TelephonyManager) App.getAppContext().getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            if(tm.getDeviceId()==null){
                return "imei";
            }
            return tm.getDeviceId();  
        }  
        return "imei";
    }

    public static boolean checkDeviceHasNavigationBar(Context activity) {

        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    KLog.i("后台", appProcess.processName);
                    return true;
                }else{
                    KLog.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    public static boolean isAppAvilible(Context context, String packageName){
        final PackageManager packageManager = context.getPackageManager();//获取packagemanager
        List<PackageInfo> packageInfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        List<String> NameList = new ArrayList<String>();//用于存储所有已安装程序的包名
        //从packageInfo中取出包名，放入NameList中
        if(packageInfo != null){
            for(int i = 0; i < packageInfo.size(); i++){
                String pn = packageInfo.get(i).packageName;
                NameList.add(pn);
            }
        }
        return NameList.contains(packageName);//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }


    public static void goToMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            //e.printStackTrace();
            Toast.makeText(context, "您没有安装应用市场", Toast.LENGTH_SHORT).show();

        }
    }

    public static void reportNetError(Throwable t){
        if(t instanceof IOException){
            Toast.makeText(App.getAppContext(),"网络异常，请检查网络",Toast.LENGTH_SHORT).show();
        }
    }

    public static void reportServerError(String msg){
        Toast.makeText(App.getAppContext(),msg,Toast.LENGTH_SHORT).show();
    }

    public static void toast(String msg){
        Toast.makeText(App.getAppContext(),msg,Toast.LENGTH_SHORT).show();
    }


    public static String getChannel(){
        try{
            Context context=App.getAppContext();
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            String umengChannel=appInfo.metaData.getString("UMENG_CHANNEL");
            return umengChannel;
        }catch (PackageManager.NameNotFoundException e){

        }
        return "general";

    }

    public static PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        Context context=App.getAppContext();
        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getNetWorkType(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            String type = networkInfo.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                return "WIFI";
            } else if (type.equalsIgnoreCase("MOBILE")) {
                return isFastMobileNetwork(context) ? "4G" : "2G";
            }
        }
        return "no net";
    }


    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }
    public static String getTimeZone(){
        TimeZone tz = TimeZone.getDefault();
        String s = tz.getDisplayName(false, TimeZone.SHORT)+" "+tz.getID();
        return Uri.encode(s);
    }

    public static void hideKey(Context context,View view){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }


}  