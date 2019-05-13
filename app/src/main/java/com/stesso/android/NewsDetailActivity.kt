package com.stesso.android

import android.os.Bundle
import com.stesso.android.model.News
import com.stesso.android.utils.share
import com.stesso.android.utils.toast
import kotlinx.android.synthetic.main.activity_news_detail.*
import org.json.JSONObject

class NewsDetailActivity : BaseActivity() {
    var news: News? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        setContentView(R.layout.activity_news_detail)
        configTitleView(title_view)
        doHttpRequest(apiService.getNewsDetail(intent.getIntExtra(NEWS_ID, 0))) {
            news = it?.topic
            //title_view.setTitle(news?.title?:"NOW")
            favorite_view.setImageResource(if (it?.userHasCollect == 0) R.drawable.gray_mouth else R.drawable.red_mouth)
            web_view.loadData(getHtmlData(it?.topic?.content), "text/html; charset=utf-8", "utf-8")
        }

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

    private fun getHtmlData(body: String?): String {
        val head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>"
        return "<html>$head<body>$body</body></html>"
    }
}
