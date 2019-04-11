package com.stesso.android

import android.app.Application
import cn.jpush.android.api.JPushInterface
import com.stesso.android.di.component.AppComponent
import com.stesso.android.di.component.DaggerAppComponent
import com.stesso.android.di.module.AppModule
import com.tendcloud.tenddata.TCAgent


/**
 * Created by flame on 2018/9/17.
 */
class App : Application() {

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
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)

    }

    companion object {
        private var instance: App? = null
        fun instance() = instance ?: throw Throwable("instance 还未初始化")
    }


}