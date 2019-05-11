package com.stesso.android.shopcart

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.stesso.android.*
import com.stesso.android.lib.*
import com.stesso.android.model.Account
import com.stesso.android.model.CommodityDetail
import com.stesso.android.model.EmptyItem
import com.stesso.android.model.ShopcartDTO
import kotlinx.android.synthetic.main.activity_shop_cart.*
import org.json.JSONObject

class ShopCartActivity : BaseActivity() {

    private var adapter = MultiTypeAdapter()
    private var shopCart: ShopcartDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_cart)
        configTitleView(title_view)
        getActivityComponent().inject(this)
        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.adapter = adapter
        doHttpRequest(apiService.getCartItems()) {
            shopCart = it
            Account.count = it?.cartTotal?.goodsCount ?: 0
            if (it?.cartList?.isEmpty() == true) {
                adapter.addItem(EmptyItem(), EMPTY_CART, spanSize = 2)
            } else {
                adapter.addItems(it?.cartList, CART_TYPE, spanSize = 2)
                adapter.addItem("", ACTION_BUTTON, true, 2)
            }
            doHttpRequest(apiService.getRelatedGoods()) {
                if (it?.goodsList?.isEmpty() == false) {
                    adapter.addItem("", TITLE_TYPE, true, 2)
                    adapter.addItems(it?.goodsList, FAVORITE_COMMODITY, true)
                }
            }


        }
        adapter.setOnItemClick { position, data, action, extra ->
            if (data is CommodityDetail) {
                when (action) {
                    1 -> {
                        val body = JSONObject(mapOf(Pair("productIds", data.productId)))
                        doHttpRequest(apiService.deleteCartItem(body)) {
                            adapter.removeItem(position)
                            if (adapter.getItemType(0) != CART_TYPE) {
                                adapter.changeItem(0, EmptyItem(), EMPTY_CART, 2)
                            }
                        }
                    }
                    2, 3 -> {
                        val body = JSONObject(mapOf(Pair("productId", data.productId), Pair("goodsId", data.goodsId), Pair("number", 1)))
                        val single = if (action == 2) apiService.minusCartItems(body) else apiService.addCartItem(body)
                        doHttpRequest(single) {
                            shopCart?.updateNum(position, extra as Int)
                        }
                    }
                }
            }
        }
    }
}
