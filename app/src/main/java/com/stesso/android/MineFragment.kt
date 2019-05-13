package com.stesso.android

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.stesso.android.account.LoginActivity
import com.stesso.android.account.SettingActivity
import com.stesso.android.lib.*
import com.stesso.android.model.Account
import com.stesso.android.shopcart.ShopCartActivity
import com.stesso.android.utils.checkLogin
import com.stesso.android.utils.getWindowWidth
import com.stesso.android.utils.openActivity
import com.stesso.android.utils.parseTime
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() {

    private val adapter = MultiTypeAdapter()
    override fun getLayoutId() = R.layout.fragment_mine
    private var currIndex = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
    }

    override fun onResume() {
        super.onResume()
        if (reload) {
            fillView()
        }
    }

    fun fillView() {
        if (Account.isLogin()) {
            val count = Account.count
            if (count >= 0) {
                title_view.setCount(count)
            }
            time_view.visibility = View.VISIBLE
            name_view.visibility = View.VISIBLE
            name_view.text = Account.user?.nickname
            time_view.text = "${parseTime(Account.user?.addTime ?: "")}加入"
            group.visibility = View.INVISIBLE
            loadData( currIndex)
            info_section.setOnClickListener {
                loadData(3)
            }

            commodity_section.setOnClickListener {
                loadData(2)
            }

            order_section.setOnClickListener {
                loadData(1)
            }

        } else {
            group.visibility = View.VISIBLE
            time_view.visibility = View.INVISIBLE
            name_view.visibility = View.INVISIBLE
        }
    }

    private fun setIndicatorView(index: Int) {
        val w = context.getWindowWidth()/3.0F
        ViewCompat.animate(indicator_view).translationX((index - 1) * w).start()
    }

    private fun loadData(index: Int) {
        if (index == currIndex && !reload) {
            return
        }
        reload = false
        setIndicatorView(index)
        currIndex = index
        when (index) {
            1 -> {
                adapter.clear()
                recycler_view.layoutManager = LinearLayoutManager(context)
                doHttpRequest(apiService.getOrderList(0)) {
                    adapter.addItems(it?.data/*?.filter { !it.orderStatusText.startsWith("已取消") }*/, ORDER_LIST)
                }

            }
            2 -> {
                adapter.clear()
                recycler_view.layoutManager = GridLayoutManager(context, 2)
                doHttpRequest(apiService.getCollectCommodity(0, 1, 10)) {
                    adapter.addItems(it?.collectList, FAVORITE_COMMODITY)
                }
            }
            3 -> {
                adapter.clear()
                recycler_view.layoutManager = LinearLayoutManager(context)
                doHttpRequest(apiService.getCollectInfo(1, 1, 10)) {
                    adapter.addItems(it?.collectList, FAVORITE_NEWS)
                }
            }
        }
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
    }

    companion object {
        var reload = false
    }

}
