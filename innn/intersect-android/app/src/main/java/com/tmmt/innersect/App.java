package com.tmmt.innersect;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.m7.imkfsdk.MobileApplication;
import com.socks.library.KLog;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tendcloud.tenddata.TCAgent;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.DefaultConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by flame on 2017/4/10.
 */

public class App extends MultiDexApplication {

    private static Context sAppContext;

    public static int sExpireTime=30;

    public static Activity mRunning;
    private RefWatcher refWatcher;
    public static Context getAppContext(){
        return  sAppContext;
    }
    @Override
    public void onCreate() {

        MobileApplication.inject(this);
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        sAppContext=this;
        PlatformConfig.setQQZone(BuildConfig.QQAPPID, BuildConfig.QQAPPKEY);
        PlatformConfig.setWeixin(BuildConfig.WXAPPID,BuildConfig.WXAPPKEY);
        PlatformConfig.setSinaWeibo(BuildConfig.SINAAPPID,BuildConfig.SINAAPPKEY,"https://sns.whalecloud.com/sina2/callback");
        KLog.init(BuildConfig.LOG_DEBUG);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Config.DEBUG=false;
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);

        UMShareAPI.get(this).setShareConfig(config);
        initStrictMode();
        initAnalytics();
        //initLeakCanary();
        // Branch logging for debugging

        Branch.enableLogging();
        // Branch object initialization
        Branch.getAutoInstance(this);
        ZXingLibrary.initDisplayOpinion(this);
        loadConfig();
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                //activity.getWindow().getDecorView()
            }
            @Override
            public void onActivityStarted(Activity activity) {}

            @Override
            public void onActivityResumed(Activity activity) {
                mRunning=activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {}

            @Override
            public void onActivityStopped(Activity activity) {}

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}

            @Override
            public void onActivityDestroyed(Activity activity) {}
        });
    }

    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }

    private void initAnalytics(){
        // ********tc********/
        TCAgent.LOG_ON=true;
        // App ID: 在TalkingData创建应用后，进入数据报表页中，在“系统设置”-“编辑应用”页面里查看App ID。
        // 渠道 ID: 是渠道标识符，可通过不同渠道单独追踪数据。
        TCAgent.init(this);
        // 如果已经在AndroidManifest.xml配置了App ID和渠道ID，调用TCAgent.init(this)即可；或与AndroidManifest.xml中的对应参数保持一致。
        TCAgent.setReportUncaughtExceptions(true);
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            String umengAppkey = appInfo.metaData.getString("UMENG_APPKEY");
            String umengChannel=appInfo.metaData.getString("UMENG_CHANNEL");
            MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this,umengAppkey,umengChannel));

        }catch (PackageManager.NameNotFoundException e){

        }
    }

    private void initStrictMode() {
        if (BuildConfig.LOG_DEBUG && false) {
            StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder()
                            .detectAll()
//                            .penaltyDialog() // 弹出违规提示对话框
                            .penaltyLog() // 在logcat中打印违规异常信息
                            .build());
            StrictMode.setVmPolicy(
                    new StrictMode.VmPolicy.Builder()
                            .detectAll()
                            .penaltyLog()
                            .build());
        }
    }

    public void loadConfig(){
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getDefaultConfig().enqueue(new Callback<DefaultConfig>() {
            @Override
            public void onResponse(Call<DefaultConfig> call, Response<DefaultConfig> response) {
                if(response.body()!=null && response.body().data!=null){
                    //DefaultConfig.WorkTime time=response.body().data.workingDay;
                    try {
                        MobileApplication.message = new JSONObject(response.body().data.workingDay).getString("desc");
                        sExpireTime = Integer.valueOf(response.body().data.orderExpireTime);
                    }catch (Exception e){

                    }
                }

            }

            @Override
            public void onFailure(Call<DefaultConfig> call, Throwable t) {
                KLog.d(t);
            }

        });
    }
    private void initLeakCanary() {
        if (BuildConfig.LOG_DEBUG && false) {
            refWatcher = LeakCanary.install(this);
        } else {
            refWatcher = installLeakCanary();
        }
    }
    protected RefWatcher installLeakCanary() {
        return RefWatcher.DISABLED;
    }

}
