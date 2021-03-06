package com.stesso.android

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.jzvd.JzvdStd
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.stesso.android.lib.*
import com.stesso.android.model.Account
import com.stesso.android.model.Commodity
import com.stesso.android.model.VideoItem
import com.stesso.android.shopcart.ShopCartActivity
import com.stesso.android.utils.checkLogin
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    private val adapter = MultiTypeAdapter()
    override fun getLayoutId() = R.layout.fragment_home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
    }

    override fun onResume() {
        super.onResume()
        if (Account.count >= 0) {
            if (Account.count == 0) {
                count_view.visibility = View.INVISIBLE
            } else {
                count_view.visibility = View.VISIBLE
                count_view.text = Account.count.toString()
            }

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        message_view.setOnClickListener {
            context.checkLogin {
                context.openActivity(MessageActivity::class.java)
            }
        }
        shopcart_view.setOnClickListener {
            context?.checkLogin { context?.openActivity(ShopCartActivity::class.java) }
        }

        refreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onRefresh(refreshLayout: TwinklingRefreshLayout) {
                loadData(true)
            }
        })
        loadData(false)
    }

    override fun onPause() {
        super.onPause()
        JzvdStd.goOnPlayOnPause()
    }

    // TODO bug
    private fun loadData(refresh: Boolean) {
        doHttpRequest(apiService.getHomeContent()) {
            if (refresh) {
                refreshLayout.finishRefreshing()
            }
            if (!it?.video.isNullOrEmpty()) {
                adapter.addItem(VideoItem(it?.videoFaceImage, it?.video), BANNER_TYPE)
            }
            val hotList = mutableListOf<Commodity>()
            it?.goodsList?.forEach { commodity ->
                when (commodity.stype) {
                    2, 3 -> {
                        if (hotList.isNotEmpty()) {
                            adapter.addItem(hotList, HOT_COMMODITY, true)
                            hotList.clear()
                        }
                        adapter.addItem(commodity, if (commodity.stype == 2) NEW_COMMODITY else RECOMMEND_TYPE, true)
                    }
                    else -> {
                        hotList.add(commodity)
                    }
                }
            }
            if (hotList.isNotEmpty()) {
                adapter.addItem(hotList, HOT_COMMODITY, true)
                hotList.clear()
            }
        }
    }
}
