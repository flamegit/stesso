package com.stesso.android.shopcart

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.stesso.android.lib.CommonAdapter
import com.stesso.android.*
import com.stesso.android.lib.DividerItemDecoration
import com.stesso.android.lib.FAVORITE_COMMODITY
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.model.Account
import com.stesso.android.model.CommodityDetail
import com.stesso.android.model.ShopcartDTO
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.activity_shop_cart.*
import org.json.JSONObject

class ShopCartActivity : BaseActivity() {

    private var adapter: CommonAdapter<CommodityDetail>? = null
    private var adapter2 = MultiTypeAdapter()
    private var shopCart: ShopcartDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_cart)
        configTitleView(title_view)
        getActivityComponent().inject(this)

        settlement_view.setOnClickListener {
            Account.shopCart = shopCart
            openActivity(SettlementActivity::class.java)
        }

        adapter2.setOnItemClick { position, data, action, extra ->
            when (action) {
                1 -> {
                    adapter?.removeItem(position)
                    if (adapter?.itemCount == 0) {
                        group.visibility = View.VISIBLE
                        settlement_view.visibility = View.INVISIBLE
                    }
                }
                2, 3 -> {
                    if (data is CommodityDetail) {
                        val body = JSONObject(mapOf(Pair("productId", data.productId), Pair("goodsId", data.goodsId), Pair("number", 1)))
                        val single = if (action == 2) apiService.minusCartItems(body) else apiService.addCartItem(body)
                        doHttpRequest(single) {
                            shopCart?.updateNum(position, extra as Int)
                        }
                    }
                }
            }

            select_view.setOnClickListener {
                openActivity(MainActivity::class.java, INDEX, 0)
            }

            recycler_view.adapter = adapter
            recycler_view.layoutManager = LinearLayoutManager(this)
            recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL, false))
            doHttpRequest(apiService.getCartItems()) {
                shopCart = it
                Account.count = it?.cartTotal?.goodsCount ?: 0
                //title_view.setCount(Account.count)
                if (it?.cartList?.isEmpty() == true) {
                    group.visibility = View.VISIBLE
                } else {
                    settlement_view.visibility = View.VISIBLE
                    group.visibility = View.INVISIBLE
                    adapter?.addItems(it?.cartList, false)
                }
            }

            related_view.adapter = adapter2
            related_view.layoutManager = GridLayoutManager(this, 2)

            doHttpRequest(apiService.getRelatedGoods()) {
                adapter2.addItems(it?.goodsList, FAVORITE_COMMODITY)
            }
        }
    }
}
