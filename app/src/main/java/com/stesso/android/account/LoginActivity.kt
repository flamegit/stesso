package com.stesso.android.account

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.stesso.android.utils.toast
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.datasource.net.ApiService
import com.stesso.android.utils.applyCompletableSchedulers
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONStringer
import javax.inject.Inject

class LoginActivity : BaseActivity() {


    @Inject
    lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val localLayoutParams = window.attributes
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS or localLayoutParams.flags)
        }

        getActivityComponent().inject(this)
        setContentView(R.layout.activity_login)
        login_view.setOnClickListener {
            val jsonStringer = JSONStringer().`object`()
            jsonStringer.endObject()
            val body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString())


            api.register(body).compose(applyCompletableSchedulers())
                    .subscribe {

                    }


        }
    }

    private fun check(): Boolean {
        if (account_view.text.isEmpty()) {
            toast("请输入手机号")
            return false
        }
        if (password_view.text.length < 6) {
            return false

        }
        return true
    }
}
