package com.stesso.android

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.stesso.android.datasource.net.ApiService
import com.stesso.android.di.component.ActivityComponent
import com.stesso.android.di.module.ActivityModule
import com.stesso.android.model.RootNode
import com.stesso.android.utils.applySingleSchedulers
import com.stesso.android.utils.toast
import com.stesso.android.widget.TitleBar
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
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
    }

    protected fun getActivityComponent(): ActivityComponent {
        val app = application as App
        return app.component.plus(ActivityModule(this))
    }

    protected open fun configTitleView(titleBar: TitleBar, leftAction: (View) -> Unit = { onBackPressed() }, rightAction: (View) -> Unit) {
        titleBar.setLeftAction(leftAction)
        titleBar.setRightAction(rightAction)
    }

    protected open fun configTitleView(titleBar: TitleBar) {
        titleBar.setLeftAction { onBackPressed() }

    }

    protected fun configRecyclerView(recyclerView:RecyclerView){
        recyclerView.layoutManager = LinearLayoutManager(this)

    }

//    protected fun doHttpRequest(single: Single<JSONObject>, onSuccess: (JSONObject) -> Unit) {
//        val disposable = single.compose(applySingleSchedulers())
//                .subscribe({ jsonObject ->
//                    val code = jsonObject.optInt("errno")
//                    if (code != 0) {
//                        toast(jsonObject.optString("errmsg"))
//                    } else {
//                        onSuccess(jsonObject)
//                    }
//                }, {
//
//                })
//        disposableContainer.add(disposable)
//    }

    protected fun <T> doHttpRequest(single: Single<RootNode<T>>, onSuccess: (T?) -> Unit) {

        val disposable = single.compose(applySingleSchedulers())
                .subscribe({ rootNode ->
                    if (rootNode.errno != 0) {
                        toast(rootNode.errmsg ?: "")
                    } else {
                        onSuccess(rootNode.data)
                    }
                }, {})
        disposableContainer.add(disposable)
    }

    override fun onDestroy() {
        disposableContainer.dispose()
        super.onDestroy()
    }

}
