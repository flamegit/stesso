package com.stesso.android

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.stesso.android.datasource.net.ApiService
import com.stesso.android.di.component.ActivityComponent
import com.stesso.android.di.module.ActivityModule
import com.stesso.android.utils.applySingleSchedulers
import com.stesso.android.utils.toast
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.json.JSONObject
import javax.inject.Inject


open class BaseActivity : AppCompatActivity() {

    private val disposableContainer = CompositeDisposable()

    @Inject
    lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            val localLayoutParams = window.attributes
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags)
//        }
    }

    protected fun getActivityComponent(): ActivityComponent {
        val app = application as App
        return app.component.plus(ActivityModule(this))
    }


    protected fun doHttpRequest(single: Single<JSONObject>, onSuccess: (JSONObject) -> Unit) {

        val disposable = single.compose(applySingleSchedulers())
                .subscribe({ jsonObject ->
                    val code = jsonObject.optInt("code")
                    if (code != 0) {
                        toast(jsonObject.optString("msg"))
                    } else {
                        onSuccess(jsonObject)
                    }
                }, {

                })
        disposableContainer.add(disposable)
    }

    override fun onDestroy() {
        disposableContainer.dispose()
        super.onDestroy()
    }

}
