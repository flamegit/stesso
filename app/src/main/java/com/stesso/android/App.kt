package com.stesso.android

import android.app.Application
import android.support.multidex.MultiDexApplication
import com.stesso.android.di.component.AppComponent
import com.stesso.android.di.component.DaggerAppComponent
import com.stesso.android.di.module.AppModule
import com.tendcloud.tenddata.TCAgent
import com.umeng.commonsdk.UMConfigure
import com.umeng.socialize.PlatformConfig


/**
 * Created by flame on 2018/9/17.
 */
class App : MultiDexApplication() {

    lateinit var component: AppComponent

    override fun onCreate() {
        instance = this
        super.onCreate()
        component = DaggerAppComponent.builder().appModule(AppModule(this)).build()

        TCAgent.LOG_ON=true
        // App ID: 在TalkingData创建应用后，进入数据报表页中，在“系统设置”-“编辑应用”页面里查看App ID。
        // 渠道 ID: 是渠道标识符，可通过不同渠道单独追踪数据。
        TCAgent.init(this)
        // 如果已经在AndroidManifest.xml配置了App ID和渠道ID，调用TCAgent.init(this)即可；或与AndroidManifest.xml中的对应参数保持一致。
        TCAgent.setReportUncaughtExceptions(true)

        //极光推送
//        JPushInterface.setDebugMode(true)
//        JPushInterface.init(this)

        UMConfigure.init(this,"5cc80f934ca3578209000666"
                ,"umeng",UMConfigure.DEVICE_TYPE_PHONE,"")
        PlatformConfig.setWeixin("wx42ca6381207ede20", "845b792eeaadf83283dbb73993cee754")
        PlatformConfig.setSinaWeibo("3613476733", "df4bfd1be4eb6500b8fdb5810f57adf8","https://sns.whalecloud.com/sina2/callback")
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba")

    }

    companion object {
        private var instance: App? = null
        fun instance() = instance ?: throw Throwable("instance 还未初始化")
    }


}