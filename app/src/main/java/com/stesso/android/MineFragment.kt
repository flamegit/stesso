package com.example.flame.kotlinstudy.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stesso.android.utils.openActivity
import com.stesso.android.R
import com.stesso.android.account.LoginActivity
import com.stesso.android.model.Account
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //val url = it.getString(Constants.KEY_URL)
            //siteType = it.getInt(Constants.KEY_SITE_TYPE)

        }
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sign_up_view.setOnClickListener {
            context?.openActivity(LoginActivity::class.java)
        }
        name_view.text = if (Account.isLogin()) "登录" else "未登录"
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String, siteType: Int) =
                MineFragment().apply {
                    arguments = Bundle().apply {
                        // putString(Constants.KEY_URL, url)
                        // putInt(Constants.KEY_SITE_TYPE, siteType)
                    }
                }
    }
}
