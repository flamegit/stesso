package com.stesso.android.shopcart

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.SettlementActivity
import com.stesso.android.lib.CART_TYPE
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.activity_shop_cart.*

class ShopCartActivity : BaseActivity() {

    private val adapter = MultiTypeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_cart)
        configTitleView(title_view)
        getActivityComponent().inject(this)

        settlement_view.setOnClickListener {
            openActivity(SettlementActivity::class.java)
        }

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        doHttpRequest(apiService.getCartItems()){
            adapter.addItems(it?.cartList, CART_TYPE)

        }
    }
}
