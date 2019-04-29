package com.stesso.android

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.EditorInfo
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.lib.NEWS_TYPE
import com.stesso.android.lib.SEARCH_GOODS
import com.stesso.android.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_search

    private var position = 0
    private val adapter = MultiTypeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        search_view.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val content = search_view.text.toString()
                if (content.isEmpty()) {
                    return@setOnEditorActionListener false
                }
                loadData(position, content)
                context.hideKeyboard(search_view)
            }
            false
        }

        recycler_view.adapter = adapter
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab?.position ?: 0
                adapter.clear()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
        })

    }

    private fun loadData(position: Int, keyword: String) {
        if (position == 0) {
            recycler_view.layoutManager = GridLayoutManager(context, 2)
            doHttpRequest(apiService.getGoodLists(1, 20, keyword = keyword)) {
                adapter.addItems(it?.goodsList, SEARCH_GOODS)
            }
        } else {
            recycler_view.layoutManager = LinearLayoutManager(context)
            doHttpRequest(apiService.getNewsList(1, 20, keyword)) {
                adapter.addItems(it?.data, NEWS_TYPE)
            }
        }
    }


}