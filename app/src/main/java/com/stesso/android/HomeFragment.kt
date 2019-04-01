package com.stesso.android

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.stesso.android.account.SettingActivity
import com.stesso.android.lib.*
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context)
        title_view.setLeftAction {
            context?.checkLogin { context?.openActivity(SettingActivity::class.java) }
        }
        title_view.setRightAction {
            context?.checkLogin { context?.openActivity(ShopCartActivity::class.java) }
        }

        doHttpRequest(apiService.getHomeContent()) {
            adapter.addItem(VideoItem(it?.videoFaceImage,it?.video), BANNER_TYPE)
        }
    }
}
