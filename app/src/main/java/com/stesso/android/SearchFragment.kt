package com.stesso.android

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.stesso.android.account.SettingActivity
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.lib.NEWS_TYPE
import com.stesso.android.shopcart.ShopCartActivity
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.fragment_news_list.*

class SearchFragment : BaseFragment() {

    private val adapter = MultiTypeAdapter()

    override fun getLayoutId() = R.layout.fragment_news_list

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

        doHttpRequest(apiService.getNewsList(1, 15)) {
            if (it?.data?.isNotEmpty() == true) {
                adapter.addItems(it?.data, NEWS_TYPE)
            }
        }
    }

}