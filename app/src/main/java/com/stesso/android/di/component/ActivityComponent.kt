package com.example.flame.kotlinstudy.di.component

import com.example.flame.kotlinstudy.di.module.ActivityModule
import com.example.flame.kotlinstudy.di.scope.ActivityScope
import dagger.Subcomponent

/**
 * Created by flame on 2018/2/1.
 */

@ActivityScope
@Subcomponent(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {}