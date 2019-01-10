package com.stesso.android.account

import android.os.Bundle
import android.util.Log
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.model.Account
import com.stesso.android.utils.applySingleSchedulers
import com.stesso.android.utils.checkLoginInfo
import com.stesso.android.utils.openActivity
import com.stesso.android.utils.toast
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
                Log.d(componentName.className, body.toString())
                apiService.login(body).compose(applySingleSchedulers())
                        .subscribe({
                            jsonObject ->
                            val code = jsonObject.optInt("code")
                            if (code != 0) {
                                toast(jsonObject.optString("msg"))
                            } else {
                                Account.token = jsonObject.optString("token")
                            }

                        }, { throwable ->
                            throwable.printStackTrace()
                        })
            }
        }
    }

}
