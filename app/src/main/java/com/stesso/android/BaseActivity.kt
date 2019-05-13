package com.stesso.android

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.stesso.android.datasource.net.ApiService
import com.stesso.android.di.component.ActivityComponent
import com.stesso.android.di.module.ActivityModule
import com.stesso.android.model.Account
import com.stesso.android.model.RootNode
import com.stesso.android.utils.applySingleSchedulers
import com.stesso.android.utils.createProgressDialog
import com.stesso.android.utils.toast
import com.stesso.android.widget.TitleBar
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


open class BaseActivity : AppCompatActivity() {

    protected val disposableContainer = CompositeDisposable()

    @Inject
    lateinit var apiService: ApiService

    lateinit var progressDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = createProgressDialog()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    protected fun setStatusBarDark() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
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

    protected fun configRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    protected fun <T> doHttpRequest(single: Single<RootNode<T>>, quite: Boolean = false, onSuccess: (T?) -> Unit) {
        if (!quite) {
            progressDialog.show()
        }
        val disposable = single.compose(applySingleSchedulers())
                .doFinally {
                    if (!quite) {
                        progressDialog.dismiss()
                    }
                }
                .subscribe({ rootNode ->
                    if (rootNode.errno != 0) {
                        if (rootNode.errno == 501) {
                            Account.logout()
                        }
                        if (!quite) {
                            toast(rootNode.errmsg ?: "")
                        }
                    } else {
                        onSuccess(rootNode.data)
                    }
                }, {
                    toast(it.message.toString())
                })
        disposableContainer.add(disposable)
    }

    override fun onDestroy() {
        disposableContainer.dispose()
        super.onDestroy()
    }

}
