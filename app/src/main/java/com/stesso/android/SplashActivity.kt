package com.stesso.android

import android.os.Bundle
import android.view.View
import com.stesso.android.model.Account
import com.stesso.android.utils.openActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setStatusBarDark()
        getActivityComponent().inject(this)

        disposableContainer.add(RxPermissions(this).request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_PHONE_STATE)
                .subscribe {
                    if (it) { }
                })

        disposableContainer.add(Observable.just("jump").delay(2000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .subscribe {
                    openActivity(MainActivity::class.java)
                    finish()
                })

        doHttpRequest(apiService.getCartItems(), true) {
            Account.count = it?.cartTotal?.goodsCount ?: 0
        }
    }
}
