package com.stesso.android

import android.os.Bundle

class MessageActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        getActivityComponent().inject(this)

        doHttpRequest(apiService.getMsgList(1,20,null)){

        }
    }
}
