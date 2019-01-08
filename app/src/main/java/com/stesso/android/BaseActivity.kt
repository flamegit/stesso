package com.stesso.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.stesso.android.di.component.ActivityComponent
import com.stesso.android.di.module.ActivityModule

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    protected fun getActivityComponent():ActivityComponent{
        val app = application as App
        return app.component.plus(ActivityModule(this))
    }

}
