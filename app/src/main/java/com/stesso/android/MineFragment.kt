package com.stesso.android

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.stesso.android.account.LoginActivity
import com.stesso.android.account.SettingActivity
import com.stesso.android.lib.HOT_COMMODITY
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.model.Account
import com.stesso.android.shopcart.ShopCartActivity
import com.stesso.android.utils.checkLogin
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
        title_view.setLeftAction {
            context?.checkLogin { context?.openActivity(SettingActivity::class.java) }
        }
        title_view.setRightAction {
            context?.checkLogin { context?.openActivity(ShopCartActivity::class.java) }
        }
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context)

        if (Account.isLogin()) {
            name_view.text =Account.user?.username
            time_view.text =Account.user?.addTime
            group.visibility = View.INVISIBLE

            doHttpRequest(apiService.getCollect(1,1,10)) {
                adapter.addItems(it?.collectList, HOT_COMMODITY)
            }

        } else {
            group.visibility = View.VISIBLE
            time_view.visibility =View.INVISIBLE
            name_view.visibility = View.INVISIBLE
        }

    }

}
