package com.stesso.android

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.stesso.android.account.LoginActivity
import com.stesso.android.lib.HOT_COMMODITY
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.model.Account
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.fragment_mine.*


class MineFragment : BaseFragment() {

    private val adapter = MultiTypeAdapter()
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
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context)

        if (Account.isLogin()) {
            group.visibility = View.INVISIBLE
        } else {

        }
        doHttpRequest(apiService.getCollect(0)) {
            adapter.addItems(it?.collectList, HOT_COMMODITY)
        }
    }

}
