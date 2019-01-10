package com.stesso.android

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.stesso.android.datasource.net.ApiService
import com.stesso.android.di.component.ActivityComponent
import com.stesso.android.di.module.ActivityModule
import javax.inject.Inject


open class BaseActivity : AppCompatActivity() {


    @Inject
    lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =  View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            val localLayoutParams = window.attributes
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags)
//        }
    }

    protected fun getActivityComponent():ActivityComponent{
        val app = application as App
        return app.component.plus(ActivityModule(this))
    }

}
