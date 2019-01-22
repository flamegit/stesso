package com.stesso.android

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_commodity_detail.*
import org.json.JSONObject

class CommodityDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commodity_detail)
        getActivityComponent().inject(this)

        add_cart_view.setOnClickListener {
            val body = JSONObject(mapOf(Pair("goodsId", 1110016), Pair("productId", 150),Pair("number",1)))
            doHttpRequest(apiService.addCartItem(body)){

            }
        }
    }
}
