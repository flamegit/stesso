package com.stesso.android

import android.os.Bundle
import org.joda.time.DateTime

class MessageActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        getActivityComponent().inject(this)

        doHttpRequest(apiService.getMsgList(1,20,DateTime.now().toString("yyyy-MM-dd HH:mm"))){

        }
    }
}
