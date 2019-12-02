//package com.tmmt.innersect.common;
//
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.IBinder;
//import android.os.PowerManager;
//import com.dv.DVSDK;
//import com.dv.DVSDKCallbackListener;
//import com.dv.DVSDKPromotion;
//import com.socks.library.KLog;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.ui.activity.MiddleActivity;
//import com.tmmt.innersect.utils.Util;
//import java.util.HashMap;
//
//public class WorkService extends Service {
//
//    int num;
//
//    public WorkService() {}
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        num=1;
//        KLog.d("work service");
//        DVSDK.setListenerMode(DVSDK.DVSDKListenerMode.DVSDKListenerModeOnline);
//        DVSDK.setServerUrl("Https://china.dov-e.com");
//        HashMap<String,Object> userInfo=new HashMap<>();
//        final String passwd="com.innersect.app.dvsdk";
//        userInfo.put(DVSDK.DVSDKUserCompanyKey,"tmmt");
//        userInfo.put(DVSDK.DVSDKUserGenderKey,true);
//        String saveUser=Util.getUser();
//        if(saveUser==null){
//            final String userName= Util.createUserName();
//            DVSDK.createUser(userName, passwd, userInfo, new DVSDKCallbackListener() {
//                @Override
//                public HashMap<String, Object> onCallback(HashMap<String, Object> hashMap) {
//                    if(hashMap.get(DVSDK.DVSDKErrorKey)!=null){
//                        //handle error
//                        KLog.i("create user failed");
//                        DVSDK.createSession(userName, passwd, new DVSDKCallbackListener() {
//                            @Override
//                            public HashMap<String, Object>  onCallback(HashMap<String, Object> hashMap) {
//                                if(hashMap.get(DVSDK.DVSDKErrorKey)!=null){
//                                    //handle error
//                                    return null;
//                                }
//                                String session=(String)hashMap.get(DVSDK.DVSDKSessionKey);
//                                activeSession(session);
//                                return null;
//                            }
//                        });
//                        return null;
//                    }
//                    Util.saveUser(userName);
//                    KLog.i(userName);
//                    String session=(String)hashMap.get(DVSDK.DVSDKSessionKey);
//                    activeSession(session);
//                    return null;
//                }
//            });
//        }else {
//            DVSDK.createSession(saveUser, passwd, new DVSDKCallbackListener() {
//                @Override
//                public HashMap<String, Object>  onCallback(HashMap<String, Object> hashMap) {
//                    if(hashMap.get(DVSDK.DVSDKErrorKey)!=null){
//                        //handle error
//                        return null;
//                    }
//                    String session=(String)hashMap.get(DVSDK.DVSDKSessionKey);
//                    activeSession(session);
//                    return null;
//                }
//            });
//        }
//    }
//
//    private void activeSession(String session){
//        DVSDK.activate(session, new DVSDKCallbackListener() {
//            @Override
//            public HashMap<String, Object> onCallback(HashMap<String, Object> hashMap) {
//                if(hashMap.get(DVSDK.DVSDKErrorKey)!=null){
//                    //handle error
//                    return null;
//                }
//                KLog.d("activate");
//                listen();
//                return null;
//            }
//        });
//    }
//    private void listen(){
//        DVSDK.setListenerHandler(new DVSDKCallbackListener() {
//            @Override
//            public HashMap<String, Object> onCallback(HashMap<String, Object> hashMap) {
//                DVSDKPromotion promotion=(DVSDKPromotion)hashMap.get(DVSDK.DVSDKPromotionKey);
//                //SystemUtil.reportServerError(promotion.getAction());
//                //KLog.d("listen");
//                showNotification(promotion);
//                return null;
//            }
//        });
//    }
//
//    private void showNotification(DVSDKPromotion promotionObj) {
//
//        PowerManager pm1 = (PowerManager) getSystemService(Context.POWER_SERVICE);
//        PowerManager.WakeLock wl = pm1.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "CHESS");
//        wl.acquire();
//        PackageManager pm = getPackageManager();
//        Intent notificationIntent = pm.getLaunchIntentForPackage(getPackageName());
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        notificationIntent.putExtra(Constants.SCHEMA,promotionObj.getAction());
//        notificationIntent.setClass(this, MiddleActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, num, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        Bitmap bigBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.innersect_icon);
//        String title = "推荐消息";
//        String content= "根据你的位置为你推荐"+promotionObj.content;
//        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification.Builder builder = new Notification.Builder(this);
//        builder.setDefaults(Notification.DEFAULT_ALL)
//                .setLargeIcon(bigBitmap)
//                .setSmallIcon(R.mipmap.app_icon)
//                .setPriority(Notification.PRIORITY_MAX)
//                .setAutoCancel(true)
//                .setContentTitle(title)
//                .setContentIntent(contentIntent)
//                //.setStyle(new Notification.BigTextStyle().bigText(content).setBigContentTitle(title))
//                .setContentText(content);
//
//        Notification n = builder.build();
//        nm.notify(++num, n);
//        wl.release();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        KLog.i("on staart ");
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public void onDestroy() {
//        KLog.i("work service destory");
//        super.onDestroy();
//    }
//}
