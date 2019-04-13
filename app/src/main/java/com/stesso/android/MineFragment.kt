package com.stesso.android

import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.stesso.android.account.LoginActivity
import com.stesso.android.account.SettingActivity
import com.stesso.android.lib.*
import com.stesso.android.model.Account
import com.stesso.android.shopcart.ShopCartActivity
import com.stesso.android.utils.checkLogin
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() {

    private val adapter = MultiTypeAdapter()
    override fun getLayoutId() = R.layout.fragment_mine

    private var currIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
    }

    override fun onResume() {
        super.onResume()
        fillView()
    }

    fun fillView() {
        if (Account.isLogin()) {
            name_view.text = Account.user?.username
            time_view.text = Account.user?.addTime
            group.visibility = View.INVISIBLE

            loadData(currIndex)

            info_section.setOnClickListener {
                setIndicatorView(3)
                loadData(3)
            }

            commodity_section.setOnClickListener {
                setIndicatorView(2)
                loadData(2)
            }

            order_icon.setOnClickListener {
                setIndicatorView(1)
                loadData(1)
            }

        } else {
            group.visibility = View.VISIBLE
            time_view.visibility = View.INVISIBLE
            name_view.visibility = View.INVISIBLE
        }
    }

    private fun setIndicatorView(index: Int){
        if (index == currIndex) {
            return
        }
        ViewCompat.animate(indicator_view).translationX(300f).start()


    }

    private fun loadData(index: Int) {
        if (index == currIndex) {
            return
        }
        currIndex = index
        when (index) {
            1 -> {
                doHttpRequest(apiService.getOrderList(1)) {
                    adapter.addItems(it?.data, ORDER_LIST)
                }

            }
            2 -> {
                doHttpRequest(apiService.getCollectCommodity(0, 1, 10)) {
                    adapter.addItems(it?.collectList, FAVORITE_COMMODITY)
                }

            }
            3 -> {
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
        recycler_view.layoutManager = LinearLayoutManager(context)
    }

}
