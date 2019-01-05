package com.stesso.android.di.component

import com.stesso.android.di.module.ActivityModule
import com.example.flame.kotlinstudy.di.scope.ActivityScope
import com.stesso.android.account.LoginActivity
import dagger.Subcomponent

/**
 * Created by flame on 2018/2/1.
 */

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity:LoginActivity)

}