package com.stesso.android.account

import android.os.Bundle
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.datasource.net.ApiService
import com.stesso.android.utils.applyCompletableSchedulers
import com.stesso.android.utils.toast
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONStringer
import javax.inject.Inject

class RegisterActivity : BaseActivity() {

    @Inject
    lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        setContentView(R.layout.activity_register)

        register_view.setOnClickListener {
            val mobile = account_view.text
            val verifyCode = verify_code.text
            val password = password_view.text

            val jsonStringer = JSONStringer().`object`()
                    .key("mobile").value(mobile)
                    .key("password").value(password)
                    .key("vcode").value(verifyCode)
                    .endObject()

            val body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString())

            api.register(body).compose(applyCompletableSchedulers())
                    .subscribe({
                        toast("success")
                        finish()
                    }, {
                        it.printStackTrace()
                    })


        }
    }
}
