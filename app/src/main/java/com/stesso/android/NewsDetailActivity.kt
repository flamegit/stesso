package com.stesso.android

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.lcodecore.tkrefreshlayout.IBottomView
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.stesso.android.model.News
import com.stesso.android.utils.share
import com.stesso.android.utils.toast
import kotlinx.android.synthetic.main.activity_news_detail.*
import org.json.JSONObject

class NewsDetailActivity : BaseActivity() {
    var news: News? = null
    var idList: IntArray? = null
    var curId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        setContentView(R.layout.activity_news_detail)
        configTitleView(title_view)
        idList = intent.getIntArrayExtra(NEWS_ID_LIST)
        curId = intent.getIntExtra(NEWS_ID, 0)
        loadData(curId, null)
        //refreshLayout.setAutoLoadMore(true)
        refreshLayout.setBottomView(BottomView(this))

        refreshLayout.setOnRefreshListener(object : RefreshListenerAdapter() {
            override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                super.onLoadMore(refreshLayout)
                curId = nextId(curId) ?: 0
                loadData(curId, refreshLayout)
            }
        })

        favorite_view.setOnClickListener {
            if (news == null) {
                return@setOnClickListener
            }
            val body = JSONObject(mapOf(Pair("type", 1), Pair("valueId", news?.id)))
            doHttpRequest(apiService.addOrDelete(body)) {
                if (it?.type == "delete") {
                    toast("取消收藏")
                    favorite_view.setImageResource(R.drawable.gray_mouth)
                } else {
                    toast("加入收藏")
                    favorite_view.setImageResource(R.drawable.red_mouth)
                }
            }
        }
        share_view.setOnClickListener {
            this.share()
        }
    }

    private fun loadData(id: Int, refreshLayout: TwinklingRefreshLayout?) {
        doHttpRequest(apiService.getNewsDetail(id)) {
            news = it?.topic
            title_view.setTitle(news?.title ?: "NOW")
            favorite_view.setImageResource(if (it?.userHasCollect == 0) R.drawable.gray_mouth else R.drawable.red_mouth)
            web_view.loadData(getHtmlData(it?.topic?.content), "text/html; charset=utf-8", "utf-8")
            refreshLayout?.finishLoadmore()
        }
    }

    fun nextId(id: Int): Int? {
        val index = idList?.indexOf(id) ?: 0
        val size = idList?.size ?: 0
        if (index + 1 < size) {
            return idList?.get(index + 1)
        }
        return null
    }

    private fun getHtmlData(body: String?): String {
        val head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>"
        return "<html>$head<body>$body</body></html>"
    }

    class BottomView(val context: Context) : IBottomView {

        override fun onPullingUp(fraction: Float, maxBottomHeight: Float, bottomHeight: Float) {}

        override fun onFinish() {}

        override fun onPullReleasing(fraction: Float, maxBottomHeight: Float, bottomHeight: Float) {}

        override fun getView(): View {
            return LayoutInflater.from(context).inflate(R.layout.bottom_view_layout, null)
        }

        override fun reset() {}

        override fun startAnim(maxBottomHeight: Float, bottomHeight: Float) {}
    }
}
