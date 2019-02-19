package com.stesso.android

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.stesso.android.account.SettingActivity
import com.stesso.android.lib.*
import com.stesso.android.shopcart.ShopCartActivity
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
            context?.openActivity(SettingActivity::class.java)
        }
        title_view.setRightAction {
            context?.openActivity(ShopCartActivity::class.java)
        }

        doHttpRequest(apiService.getHomeContent()) {
            Log.d("dd","suddcee")
            if (it?.banner?.isNotEmpty() == true) {
                adapter.addItem(it.banner?.get(0), BANNER_TYPE)
            }
            adapter.addItems(it?.newGoodsList, NEW_COMMODITY, true)
            adapter.addItem(it?.hotGoodsList, HOT_COMMODITY,true)
            adapter.addItems(it?.recommendGoodsList, RECOMMEND_TYPE,true)
        }
    }

}
