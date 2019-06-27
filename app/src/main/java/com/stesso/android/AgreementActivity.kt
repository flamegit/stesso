package com.stesso.android

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_agreement.*

class AgreementActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)
        configTitleView(title_view)
        web_view.loadUrl("http://www.stesso.cn/private.html")
    }
}
