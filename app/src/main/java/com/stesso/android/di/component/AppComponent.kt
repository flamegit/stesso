package com.example.flame.kotlinstudy.di.component

import com.example.flame.kotlinstudy.di.module.ActivityModule
import com.example.flame.kotlinstudy.di.module.AppModule
import com.example.flame.kotlinstudy.di.module.FragmentModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by flame on 2018/2/1.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent  {
    fun plus(module: ActivityModule):ActivityComponent
    fun plus(module:FragmentModule):FragmentComponent
}