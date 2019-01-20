package com.stesso.android.account

import android.os.Bundle
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.model.Account
import com.stesso.android.utils.checkLoginInfo
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getActivityComponent().inject(this)
        setContentView(R.layout.activity_login)

        register_view.setOnClickListener {
            openActivity(RegisterActivity::class.java)
        }
        login_view.setOnClickListener {
            val mobile = account_view.text.toString()
            val password = password_view.text.toString()
            if (checkLoginInfo(mobile, password)) {
                val body = JSONObject(mapOf(Pair("mobile", mobile), Pair("password", password)))
                doHttpRequest(apiService.login(body)) { data ->
                    Account.token = data?.token
                    Account.user = data?.userInfo
                }
            }
        }
    }
}
