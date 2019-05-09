package com.stesso.android.shopcart

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.bumptech.glide.Glide
import com.stesso.android.lib.CommonAdapter
import com.stesso.android.*
import com.stesso.android.lib.DividerItemDecoration
import com.stesso.android.lib.FAVORITE_COMMODITY
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.model.Account
import com.stesso.android.model.CommodityDetail
import com.stesso.android.model.ShopcartDTO
import com.stesso.android.utils.openActivity
import com.stesso.android.widget.QuantityView
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
        adapter = CommonAdapter(R.layout.viewholder_cart_item) { holder, position, data ->
            holder.get<TextView>(R.id.name_view).text = data.goodsName
            Glide.with(holder.itemView).load(data.picUrl).into(holder.get(R.id.commodity_img))
            holder.get<View>(R.id.delete_view).setOnClickListener {
                val body = JSONObject(mapOf(Pair("productIds", data.productId)))
                doHttpRequest(apiService.deleteCartItem(body)) {
                    adapter?.removeItem(position)
                    if (adapter?.itemCount == 0) {
                        group.visibility = View.VISIBLE
                        settlement_view.visibility = View.INVISIBLE
                    }
                }
            }

            val checkBox = holder.get<CheckBox>(R.id.checkbox)
            checkBox.isChecked = data.checked
            checkBox.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed) {
                    data.checked = isChecked
                }
            }
            holder.get<TextView>(R.id.price_view).text = "￥：${data.price}"
            holder.get<TextView>(R.id.info_view).text = data.getInfo()
            val quantityView = holder.get<QuantityView>(R.id.quantity_view)
            quantityView.quantity = data.number
            quantityView.setQuantityChangeListener(object : QuantityView.OnQuantityChangeListener {
                override fun onLimitReached() {}
                override fun onMinReached() {}
                override fun onQuantityChanged(newQuantity: Int, programmatically: Boolean, minus: Boolean) {
                    val body = JSONObject(mapOf(Pair("productId", data.productId), Pair("goodsId", data.goodsId), Pair("number", 1)))
                    val single = if (minus) apiService.minusCartItems(body) else apiService.addCartItem(body)
                    doHttpRequest(single) {
                        shopCart?.updateNum(position, newQuantity)
                    }
                }
            })
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
