package com.stesso.android

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.stesso.android.di.component.ActivityComponent
import com.stesso.android.di.module.ActivityModule


open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =  View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    protected fun getActivityComponent():ActivityComponent{
        val app = application as App
        return app.component.plus(ActivityModule(this))
    }

}
