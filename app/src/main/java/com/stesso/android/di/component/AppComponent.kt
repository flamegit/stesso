package com.stesso.android.di.component

import com.stesso.android.di.module.ActivityModule
import com.stesso.android.di.module.AppModule
import com.stesso.android.di.module.FragmentModule
import com.stesso.android.lib.DelegateAdapterFactory
import dagger.Component
import javax.inject.Singleton

/**
 * Created by flame on 2018/2/1.
 */

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent  {

    fun plus(module: ActivityModule):ActivityComponent
    fun plus(module: FragmentModule):FragmentComponent

    fun inject(factory: DelegateAdapterFactory)

}