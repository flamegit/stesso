package com.stesso.android

import android.os.Bundle
import com.stesso.android.model.Account
import com.stesso.android.utils.toast
import kotlinx.android.synthetic.main.activity_modify_name.*

class ModifyNameActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = Account.user
        setContentView(R.layout.activity_modify_name)
        getActivityComponent().inject(this)
        configTitleView(title_view) {

            if (name_view.text.isNullOrEmpty()) {
                toast("昵称为空")
                return@configTitleView
            }
            val body = mapOf(Pair("nickName", name_view.text.toString()), Pair("gender", user?.gender), Pair("birthday", user?.birthday
                    ?: ""))
            doHttpRequest(apiService.updateUserInfo(body)) {
                Account.user = it
                onBackPressed()
            }
        }
    }

}
