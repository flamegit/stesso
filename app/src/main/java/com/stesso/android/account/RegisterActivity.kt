package com.stesso.android.account

import android.os.Bundle
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.utils.checkLoginInfo
import com.stesso.android.utils.toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        setContentView(R.layout.activity_register)

        get_verify_code.setOnClickListener {
            val mobile = account_view.text.toString()

            doHttpRequest(apiService.getVerifyCode(mobile)) {

            }

            get_verify_code.isEnabled = false

            Observable.intervalRange(0, 65, 0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it > 60) {
                            get_verify_code.isEnabled = true
                        } else {
                            get_verify_code.text ="等待 ${60-it} s"
                        }
                    }

        }

        register_view.setOnClickListener {
            val mobile = account_view.text.toString()
            val verifyCode = verify_code.text.toString()
            val password = password_view.text.toString()
            if (checkLoginInfo(mobile, password, verifyCode)) {
                val body = JSONObject(mapOf(Pair("mobile", mobile), Pair("password", password), Pair("vcode", verifyCode)))
                doHttpRequest(apiService.register(body)) {
                    toast("注册成功")
                    onBackPressed()
                }
            }
        }
    }
}
