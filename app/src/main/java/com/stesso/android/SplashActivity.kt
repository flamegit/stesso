package com.stesso.android

import android.os.Bundle
import com.stesso.android.model.Account
import com.stesso.android.utils.openActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setStatusBarDark()
        getActivityComponent().inject(this)

        doHttpRequest(apiService.getCartItems(), true) {
            Account.count = it?.cartTotal?.goodsCount ?: 0
        }

        RxPermissions(this).request(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.READ_PHONE_STATE)
                .subscribe{
                    if(it){
                        //toast("通过")
                    }
                }

        Observable.just("jump").delay(2000, TimeUnit.MILLISECONDS,AndroidSchedulers.mainThread())
                .subscribe {
                    openActivity(MainActivity::class.java)
                    finish()
                }


    }
}
