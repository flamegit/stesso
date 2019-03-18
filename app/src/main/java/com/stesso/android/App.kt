package com.stesso.android

import android.app.Application
import com.stesso.android.di.component.AppComponent
import com.stesso.android.di.component.DaggerAppComponent
import com.stesso.android.di.module.AppModule
import com.stesso.android.utils.toast
import com.tencent.android.tpush.XGIOperateCallback
import com.tencent.android.tpush.XGPushConfig
import com.tencent.android.tpush.XGPushManager

/**
 * Created by flame on 2018/9/17.
 */
class App : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        instance = this
        super.onCreate()
        component = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        XGPushConfig.enableDebug(this, true)
        XGPushManager.bindAccount(this, "XINGE")
        XGPushManager.setTag(this,"XINGE")
        XGPushManager.registerPush(this, object : XGIOperateCallback {
            override fun onFail(p0: Any?, p1: Int, p2: String?) {
                toast("fail")
            }
            override fun onSuccess(p0: Any?, p1: Int) {
                toast("success")
            }
        })
        //toast(""+android.os.Process.myPid())
    }

    companion object {
        private var instance: App? = null
        fun instance() = instance ?: throw Throwable("instance 还未初始化")
    }


}