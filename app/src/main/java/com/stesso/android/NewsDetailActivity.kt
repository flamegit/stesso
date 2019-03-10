package com.stesso.android


import android.os.Bundle
import kotlinx.android.synthetic.main.activity_news_detail.*

class NewsDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getActivityComponent().inject(this)
        setContentView(R.layout.activity_news_detail)

        doHttpRequest(apiService.getNewsDetail(intent.getIntExtra(NEWS_ID,0))) {
            web_view.loadData(getHtmlData(it?.topic?.content), "text/html; charset=utf-8", "utf-8")

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
