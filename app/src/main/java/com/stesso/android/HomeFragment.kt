package com.stesso.android

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.stesso.android.account.SettingActivity
import com.stesso.android.lib.BANNER_TYPE
import com.stesso.android.lib.HOT_COMMODITY
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.lib.NEW_COMMODITY
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
        close_view.setOnClickListener{
            context?.openActivity(SettingActivity::class.java)
        }


        doHttpRequest(apiService.getHomeContent()) {
            if (it?.banner?.isNotEmpty() == true) {
                adapter.addItem(it.banner?.get(0), BANNER_TYPE)
            }
            adapter.addItems(it?.newGoodsList, NEW_COMMODITY, true)
        }
    }

}
