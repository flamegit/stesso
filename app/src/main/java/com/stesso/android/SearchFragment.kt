package com.stesso.android

import android.os.Bundle
import android.support.design.widget.TabLayout

import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_search

    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab?.position ?: 0
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })


    }

    private fun loadData(position: Int, keyword: String) {
        if (position == 0) {
            doHttpRequest(apiService.getGoodLists(1, 20, keyword = keyword)) {

            }
        } else {
            doHttpRequest(apiService.getNewsList(1, 20, keyword)) {

            }
        }
    }


}