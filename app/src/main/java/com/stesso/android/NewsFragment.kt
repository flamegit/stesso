package com.stesso.android

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.stesso.android.account.SettingActivity
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.lib.NEWS_TYPE
import com.stesso.android.model.News
import com.stesso.android.shopcart.ShopCartActivity
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.fragment_news_list.*

class NewsFragment : BaseFragment() {

    private val adapter = MultiTypeAdapter()
    var newsList: List<News>? = null

    override fun getLayoutId() = R.layout.fragment_news_list

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getFragmentComponent().inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(context)

        doHttpRequest(apiService.getNewsList(1, 100)) {
            if (it?.data?.isNotEmpty() == true) {
                newsList = it?.data
                adapter.addItems(newsList, NEWS_TYPE)
            }
        }

        adapter.setOnItemClick { position, _, _, _ ->
            val intent = Intent(context, NewsDetailActivity::class.java)
            intent.putExtra(NEWS_ID, newsList?.get(position)?.id)
            //intent.putIntegerArrayListExtra(NEWS_ID_LIST, ArrayList(newsList?.map { it.id }))
            intent.putExtra(NEWS_ID_LIST, newsList?.map { it.id }?.toIntArray())
            startActivity(intent)
        }
    }

}