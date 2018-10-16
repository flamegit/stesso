package com.stesso.android

import android.app.Application
import com.example.flame.kotlinstudy.di.component.AppComponent
import com.example.flame.kotlinstudy.di.component.DaggerAppComponent
import com.example.flame.kotlinstudy.di.module.AppModule

/**
 * Created by flame on 2018/9/17.
 */
class App : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        instance=this
        super.onCreate()
        component=DaggerAppComponent.builder().appModule(AppModule(this)).build()

        //toast(""+android.os.Process.myPid())
    }

    companion object {
        private var instance: App? = null
        fun instance() = instance?:throw Throwable("instance 还未初始化")
    }


}