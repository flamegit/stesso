package com.stesso.android.di.component

import com.stesso.android.di.module.FragmentModule
import com.example.flame.kotlinstudy.di.scope.FragmentScope
import com.stesso.android.BaseFragment
import com.stesso.android.HomeFragment
import com.stesso.android.NewsFragment
import com.stesso.android.account.LoginActivity
import dagger.Subcomponent

/**
 * Created by flame on 2018/2/1.
 */

@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(fragment: HomeFragment)
    fun inject(fragment: NewsFragment)


}