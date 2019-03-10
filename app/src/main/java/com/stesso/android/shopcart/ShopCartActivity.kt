package com.stesso.android.shopcart

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.flame.kotlinstudy.lib.CommonAdapter
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.SettlementActivity
import com.stesso.android.model.Account
import com.stesso.android.model.CommodityDetail
import com.stesso.android.model.ShopcartDTO
import com.stesso.android.utils.openActivity
import com.stesso.android.widget.QuantityView
import kotlinx.android.synthetic.main.activity_shop_cart.*
import org.json.JSONObject

class ShopCartActivity : BaseActivity() {

    // private val adapter = MultiTypeAdapter()
    private var adapter: CommonAdapter<CommodityDetail>? = null
    private var shopCart:ShopcartDTO? = null

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
                val body = JSONObject(mapOf(Pair("productIds",data.productId)))
                doHttpRequest(apiService.deleteCartItem(body)) {
                    adapter?.removeItem(position)
                }
            }
            val checkBox = holder.get<CheckBox>(R.id.checkbox)
            checkBox.isChecked = data.checked
            checkBox.setOnCheckedChangeListener { view, programmatically ->
                if (!programmatically) {
                    val body = JSONObject(mapOf(Pair("productIds",data.productId.toString()), Pair("isChecked",if(view.isChecked) 1 else 0)))
                    doHttpRequest(apiService.selectCommodity(body)){
                   }
                }
            }
            holder.get<TextView>(R.id.price_view).text ="￥：${data.price}"
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
                    }
                }
            })
        }

        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        doHttpRequest(apiService.getCartItems()) {
            shopCart = it
            adapter?.addItems(it?.cartList, false)
        }
    }
}
