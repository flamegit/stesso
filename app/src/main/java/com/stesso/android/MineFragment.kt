package com.stesso.android

import android.os.Bundle
import com.stesso.android.account.LoginActivity
import com.stesso.android.model.Account
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_mine
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sign_up_view.setOnClickListener {
            context?.openActivity(LoginActivity::class.java)
        }
        name_view.text = if (Account.isLogin()) "登录" else "未登录"
        doHttpRequest(apiService.getCollect(0)) {

        }
    }


}
