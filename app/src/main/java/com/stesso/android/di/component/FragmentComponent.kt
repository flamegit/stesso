package com.stesso.android.di.component

import com.stesso.android.di.module.FragmentModule
import com.example.flame.kotlinstudy.di.scope.FragmentScope
import dagger.Subcomponent

/**
 * Created by flame on 2018/2/1.
 */

@FragmentScope
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

}