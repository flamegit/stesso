package com.stesso.android

import android.os.Bundle
import com.stesso.android.lib.EXPRESS_ITEM
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.model.Account
import kotlinx.android.synthetic.main.activity_express_info.*

class ExpressInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_express_info)
        configTitleView(title_view)
        val info =Account.expressInfo
        val adapter =MultiTypeAdapter()
        company_view.text= info?.shipperName
        code_view.text =info?.logisticCode
        recycler_view.adapter =adapter
        adapter.addItems(info?.traces, EXPRESS_ITEM)


    }
}
