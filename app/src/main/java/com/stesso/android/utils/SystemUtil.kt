//package com.stesso.android.utils
//
//import android.app.Activity
//import android.app.ActivityManager
//import android.content.ActivityNotFoundException
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageInfo
//import android.content.pm.PackageManager
//import android.net.ConnectivityManager
//import android.net.NetworkInfo
//import android.net.Uri
//import android.telephony.TelephonyManager
//import android.view.KeyCharacterMap
//import android.view.KeyEvent
//import android.view.View
//import android.view.ViewConfiguration
//import android.view.inputmethod.InputMethodManager
//import android.widget.Toast
//
//import java.io.IOException
//import java.util.ArrayList
//import java.util.Locale
//import java.util.TimeZone
//
//object SystemUtil {
//
//    /**
//     * 获取当前手机系统语言。
//     *
//     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
//     */
//    val systemLanguage: String
//        get() = Locale.getDefault().language
//
//    /**
//     * 获取当前系统上的语言列表(Locale列表)
//     *
//     * @return  语言列表
//     */
//    val systemLanguageList: Array<Locale>
//        get() = Locale.getAvailableLocales()
//
//    /**
//     * 获取当前手机系统版本号
//     *
//     * @return  系统版本号
//     */
//    val systemVersion: String
//        get() = android.os.Build.VERSION.RELEASE
//
//    /**
//     * 获取手机型号
//     *
//     * @return  手机型号
//     */
//    val systemModel: String
//        get() = android.os.Build.MODEL
//
//    /**
//     * 获取手机厂商
//     *
//     * @return  手机厂商
//     */
//    val deviceBrand: String
//        get() = android.os.Build.BRAND
//
//    /**
//     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
//     *
//     * @return  手机IMEI
//     */
////    val imei: String
////        get() {
////            if (!AccountInfo.getInstance().isLogin()) {
////                return "imei"
////            }
////
////            val tm = App.getAppContext().getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
////            return if (tm != null) {
////                if (tm.deviceId == null) {
////                    "imei"
////                } else tm.deviceId
////            } else "imei"
////        }
////
////
////    val channel: String?
////        get() {
////            try {
////                val context = App.getAppContext()
////                val appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
////                        PackageManager.GET_META_DATA)
////                return appInfo.metaData.getString("UMENG_CHANNEL")
////            } catch (e: PackageManager.NameNotFoundException) {
////
////            }
////
////            return "general"
////
////        }
////
////    val packageInfo: PackageInfo?
////        get() {
////            var pi: PackageInfo? = null
////            val context = App.getAppContext()
////            try {
////                val pm = context.getPackageManager()
////                pi = pm.getPackageInfo(context.getPackageName(),
////                        PackageManager.GET_CONFIGURATIONS)
////
////                return pi
////            } catch (e: Exception) {
////                e.printStackTrace()
////            }
////
////            return pi
////        }
////
////    val isNetworkAvailable: Boolean
////        get() {
////            val connectivityManager = App.getAppContext()
////                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
////            if (connectivityManager != null) {
////                val info = connectivityManager.activeNetworkInfo
////                if (info != null && info.isConnected) {
////                    if (info.state == NetworkInfo.State.CONNECTED) {
////                        return true
////                    }
////                }
////            }
////            return false
////        }
//
//    val timeZone: String
//        get() {
//            val tz = TimeZone.getDefault()
//            val s = tz.getDisplayName(false, TimeZone.SHORT) + " " + tz.id
//            return Uri.encode(s)
//        }
//
//    fun checkDeviceHasNavigationBar(activity: Context): Boolean {
//
//        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
//        val hasMenuKey = ViewConfiguration.get(activity)
//                .hasPermanentMenuKey()
//        val hasBackKey = KeyCharacterMap
//                .deviceHasKey(KeyEvent.KEYCODE_BACK)
//
//        return if (!hasMenuKey && !hasBackKey) {
//            // 做任何你需要做的,这个设备有一个导航栏
//            true
//        } else false
//    }
//
//    fun getNavigationBarHeight(context: Context): Int {
//        val resources = context.resources
//        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
//        return resources.getDimensionPixelSize(resourceId)
//    }
//
//    fun isBackground(context: Context): Boolean {
//        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        val appProcesses = activityManager.runningAppProcesses
//        for (appProcess in appProcesses) {
//            if (appProcess.processName == context.packageName) {
//                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
//                    KLog.i("后台", appProcess.processName)
//                    return true
//                } else {
//                    KLog.i("前台", appProcess.processName)
//                    return false
//                }
//            }
//        }
//        return false
//    }
//
//    fun isAppAvilible(context: Context, packageName: String): Boolean {
//        val packageManager = context.packageManager//获取packagemanager
//        val packageInfo = packageManager.getInstalledPackages(0)//获取所有已安装程序的包信息
//        val NameList = ArrayList<String>()//用于存储所有已安装程序的包名
//        //从packageInfo中取出包名，放入NameList中
//        if (packageInfo != null) {
//            for (i in packageInfo.indices) {
//                val pn = packageInfo[i].packageName
//                NameList.add(pn)
//            }
//        }
//        return NameList.contains(packageName)//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
//    }
//
//
//    fun goToMarket(context: Context, packageName: String) {
//        val uri = Uri.parse("market://details?id=$packageName")
//        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
//        try {
//            context.startActivity(goToMarket)
//        } catch (e: ActivityNotFoundException) {
//            //e.printStackTrace();
//            Toast.makeText(context, "您没有安装应用市场", Toast.LENGTH_SHORT).show()
//
//        }
//
//    }
//
//    fun reportNetError(t: Throwable) {
//        if (t is IOException) {
//            Toast.makeText(App.getAppContext(), "网络异常，请检查网络", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    fun reportServerError(msg: String) {
//        Toast.makeText(App.getAppContext(), msg, Toast.LENGTH_SHORT).show()
//    }
//
//    fun toast(msg: String) {
//        Toast.makeText(App.getAppContext(), msg, Toast.LENGTH_SHORT).show()
//    }
//
//    fun getNetWorkType(context: Context): String {
//
//        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val networkInfo = manager.activeNetworkInfo
//        if (networkInfo != null && networkInfo.isConnected) {
//
//            val type = networkInfo.typeName
//            if (type.equals("WIFI", ignoreCase = true)) {
//                return "WIFI"
//            } else if (type.equals("MOBILE", ignoreCase = true)) {
//                return if (isFastMobileNetwork(context)) "4G" else "2G"
//            }
//        }
//        return "no net"
//    }
//
//
//    private fun isFastMobileNetwork(context: Context): Boolean {
//        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//        when (telephonyManager.networkType) {
//            TelephonyManager.NETWORK_TYPE_1xRTT -> return false // ~ 50-100 kbps
//            TelephonyManager.NETWORK_TYPE_CDMA -> return false // ~ 14-64 kbps
//            TelephonyManager.NETWORK_TYPE_EDGE -> return false // ~ 50-100 kbps
//            TelephonyManager.NETWORK_TYPE_EVDO_0 -> return true // ~ 400-1000 kbps
//            TelephonyManager.NETWORK_TYPE_EVDO_A -> return true // ~ 600-1400 kbps
//            TelephonyManager.NETWORK_TYPE_GPRS -> return false // ~ 100 kbps
//            TelephonyManager.NETWORK_TYPE_HSDPA -> return true // ~ 2-14 Mbps
//            TelephonyManager.NETWORK_TYPE_HSPA -> return true // ~ 700-1700 kbps
//            TelephonyManager.NETWORK_TYPE_HSUPA -> return true // ~ 1-23 Mbps
//            TelephonyManager.NETWORK_TYPE_UMTS -> return true // ~ 400-7000 kbps
//            TelephonyManager.NETWORK_TYPE_EHRPD -> return true // ~ 1-2 Mbps
//            TelephonyManager.NETWORK_TYPE_EVDO_B -> return true // ~ 5 Mbps
//            TelephonyManager.NETWORK_TYPE_HSPAP -> return true // ~ 10-20 Mbps
//            TelephonyManager.NETWORK_TYPE_IDEN -> return false // ~25 kbps
//            TelephonyManager.NETWORK_TYPE_LTE -> return true // ~ 10+ Mbps
//            TelephonyManager.NETWORK_TYPE_UNKNOWN -> return false
//            else -> return false
//        }
//    }
//
//    fun hideKey(context: Context, view: View) {
//        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
//
//
//    }
//
//
//}