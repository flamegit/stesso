package com.stesso.android

import android.os.Bundle
import com.stesso.android.utils.openActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setStatusBarDark()
        Observable.just("jump").delay(2000, TimeUnit.MILLISECONDS,AndroidSchedulers.mainThread())
                .subscribe {
                    openActivity(MainActivity::class.java)
                    finish()
                }


    }
}
