package com.stesso.android.account

import android.os.Bundle
import android.text.Html
import com.stesso.android.AgreementActivity
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.TYPE
import com.stesso.android.model.RootNode
import com.stesso.android.utils.checkLoginInfo
import com.stesso.android.utils.openActivity
import com.stesso.android.utils.toast
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        setContentView(R.layout.activity_register)
        val type = intent.getIntExtra(TYPE, 0)

        get_verify_code.setOnClickListener {
            val mobile = account_view.text.toString()
            val body = JSONObject(mapOf(Pair("mobile", mobile)))

            doHttpRequest(apiService.getVerifyCode(body)) {

            }

            get_verify_code.isEnabled = false

            Observable.intervalRange(0, 65, 0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                    .subscribe {
                        if (it > 60) {
                            get_verify_code.isEnabled = true
                            get_verify_code.text = "获取验证码"

                        } else {
                            get_verify_code.text = "等待 ${60 - it} s"
                        }
                    }

        }

        cancel_view.setOnClickListener {
            onBackPressed()
        }



        if (type == 0) {
            agreement_view.setOnClickListener {
                openActivity(AgreementActivity::class.java)
            }
            agreement_view.text = Html.fromHtml(getString(R.string.agreement))
        }

        register_view.text = if (type == 0) "注册" else "重置密码"

        register_view.setOnClickListener {
            val mobile = account_view.text.toString()
            val verifyCode = verify_code.text.toString()
            val password = password_view.text.toString()
            if (checkLoginInfo(mobile, password, verifyCode)) {
                val body = JSONObject(mapOf(Pair("mobile", mobile), Pair("password", password), Pair("vcode", verifyCode)))
                if (type == 0) {
                    doHttpRequest(apiService.register(body)) {
                        toast("注册成功")
                        onBackPressed()
                    }
                } else {
                    doHttpRequest(apiService.resetPassword(body)) {
                        toast("重置密码成功")
                        onBackPressed()
                    }
                }
            }
        }
    }
}
