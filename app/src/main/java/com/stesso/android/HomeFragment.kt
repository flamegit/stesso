package com.example.flame.kotlinstudy.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stesso.android.utils.openActivity
import com.stesso.android.R
import com.stesso.android.account.LoginActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //val url = it.getString(Constants.KEY_URL)
            //siteType = it.getInt(Constants.KEY_SITE_TYPE)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sign_up_view.setOnClickListener {
            context?.openActivity(LoginActivity::class.java)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String, siteType: Int) =
                HomeFragment().apply {
                    arguments = Bundle().apply {
                       // putString(Constants.KEY_URL, url)
                       // putInt(Constants.KEY_SITE_TYPE, siteType)
                    }
                }
    }
}
