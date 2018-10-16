package com.example.flame.kotlinstudy.di.component

import com.example.flame.kotlinstudy.di.module.FragmentModule
import com.example.flame.kotlinstudy.di.scope.FragmentScope
import dagger.Subcomponent

/**
 * Created by flame on 2018/2/1.
 */

@FragmentScope
@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {

}