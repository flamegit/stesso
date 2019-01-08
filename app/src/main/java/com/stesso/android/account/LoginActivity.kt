package com.stesso.android.account

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.stesso.android.utils.toast
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.datasource.net.ApiService
import kotlinx.android.synthetic.main.activity_login.*
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


        }


    }

    private fun check():Boolean{
        if(account_view.text.isEmpty()){
            toast("请输入手机号")
            return false
        }
        if(password_view.text.length<6){
            return false

        }
        return true
    }
}
