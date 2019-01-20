package com.stesso.android.account

import android.os.Bundle
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.utils.applyCompletableSchedulers
import com.stesso.android.utils.checkLoginInfo
import com.stesso.android.utils.toast
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import org.json.JSONStringer

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        setContentView(R.layout.activity_register)

        register_view.setOnClickListener {
            val mobile = account_view.text.toString()
            val verifyCode = verify_code.text.toString()
            val password = password_view.text.toString()
            if (checkLoginInfo(mobile, password, verifyCode)) {
                val jsonStringer = JSONStringer().`object`()
                        .key("mobile").value(mobile)
                        .key("password").value(password)
                        .key("vcode").value(verifyCode)
                        .endObject()

                apiService.register(JSONObject(jsonStringer.toString()))
                        .compose(applyCompletableSchedulers())
                        .subscribe({
                            toast("success")
                            finish()
                        }, {
                            it.printStackTrace()
                        })
            }

        }
    }
}
