package com.stesso.android.lib

import com.stesso.android.ADDRESS_ID
import com.stesso.android.GOODS_ID
import com.stesso.android.NEWS_ID
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.stesso.android.*
import com.stesso.android.address.AddAddressActivity
import com.stesso.android.datasource.net.ApiService
import com.stesso.android.model.*
import com.stesso.android.utils.dip2px
import com.stesso.android.utils.doHttpRequest
import com.stesso.android.utils.openActivity
import com.stesso.android.widget.QuantityView
import org.json.JSONObject
import javax.inject.Inject

const val HEADER = 1
const val FOOTER = 2
const val TYPE1 = 3
const val GIRL_TYPE = 4
const val NEW_COMMODITY = 5
const val HOT_COMMODITY = 6
const val BANNER_TYPE = 7
const val ADDRESS_TYPE = 8
const val CART_TYPE = 9
const val RECOMMEND_TYPE = 10
const val NEWS_TYPE = 11
const val SETTLEMENT_ADDRESS = 12
const val EMPTY_ADDRESS = 13

const val SETTLEMENT_PAY = 14
const val SETTLEMENT_INFO = 15
const val SETTLEMENT_ITEM = 16
const val ORDER_LIST = 17



class DelegateAdapterFactory {

    @Inject
    lateinit var apiService: ApiService

    var onItemClick: (position: Int, data: Any?, action: Int) -> Unit = { _, _, _ -> }

    init {
        App.instance().component.inject(this)
    }

    fun getDelegateAdapter(type: Int): ViewTypeDelegateAdapter {
        return when (type) {
            HEADER -> HeaderDelegateAdapter()
            FOOTER -> FooterDelegateAdapter()
            TYPE1 -> HeaderDelegateAdapter()
            BANNER_TYPE -> object : BaseDelegateAdapter(R.layout.viewholder_top_vedio) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is BannerItem) {
                        val jzvdStd = holder.get<JzvdStd>(R.id.video_player)
                        jzvdStd.setUp(data.url, data.name, Jzvd.SCREEN_WINDOW_NORMAL)
                        //jzvdStd.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640")
                    }
                }
            }
            NEW_COMMODITY -> object : BaseDelegateAdapter(R.layout.viewholder_new_commodity) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is Commodity) {
                        holder.itemView.setOnClickListener { v ->
                            v.context.openActivity(CommodityDetailActivity::class.java, GOODS_ID, data.id)
                        }
                        Glide.with(holder.itemView).load(data.picUrl).into(holder.get(R.id.commodity_img))
                        holder.get<TextView>(R.id.name_view).text = data.name
                        holder.get<TextView>(R.id.discount_price).text = "${data.counterPrice}"
                        holder.get<View>(R.id.favorite_view).setOnClickListener {
                            val body = JSONObject(mapOf(Pair("type", 0), Pair("valueId", data.id)))
                            doHttpRequest(apiService.addOrDelete(body)) {}
                        }
                    }
                }
            }
            ORDER_LIST -> object : BaseDelegateAdapter(R.layout.viewholder_order_item){
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if(data is OrderList.OrderInfo) {
                        holder.get<TextView>(R.id.order_no).text = data.orderSn
                        holder.get<TextView>(R.id.order_date).text = data.orderStatusText
                        holder.itemView.setOnClickListener{ v ->
                            v.context.openActivity(OrderDetailActivity::class.java, ORDER_ID, data.id)
                        }
                    }
                }
            }

            SETTLEMENT_INFO -> object :BaseDelegateAdapter(R.layout.viewholder_settlement_info){
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if(data is ShopcartDTO){
                        holder.get<TextView>(R.id.total_price).text ="￥：${data.getTotalPrice()}"
                        holder.get<TextView>(R.id.order_price).text ="￥：${data.getTotalPrice()}"
                    }
                }
            }
            SETTLEMENT_PAY -> object :BaseDelegateAdapter(R.layout.viewholder_pay_method){

            }
            SETTLEMENT_ITEM -> object : BaseDelegateAdapter(R.layout.viewholder_settlement_item) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is CommodityDetail) {
                        holder.get<TextView>(R.id.name_view).text = data.goodsName
                        Glide.with(holder.itemView).load(data.picUrl).into(holder.get(R.id.commodity_img))
                        holder.get<TextView>(R.id.info_view).text = "${data.getInfo()} ${data.number}件"
                        holder.get<TextView>(R.id.price_view).text ="￥：${data.price}"
                    }
                }
            }
            CART_TYPE -> object : BaseDelegateAdapter(R.layout.viewholder_cart_item) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is CommodityDetail) {
                        holder.get<TextView>(R.id.name_view).text = data.goodsName
                        Glide.with(holder.itemView).load(data.picUrl).into(holder.get(R.id.commodity_img))
                        holder.get<View>(R.id.delete_view).setOnClickListener {
                            onItemClick(position, data, 1)
                        }
                        val checkBox = holder.get<CheckBox>(R.id.checkbox)
                        checkBox.isChecked = data.checked
//                        holder.get<CheckBox>(R.id.checkbox).setOnClickListener{
//                            if()
//                        }
                        //holder.get<TextView>(R.id.info_view).text = data.goodsName
                        val quantityView = holder.get<QuantityView>(R.id.quantity_view)
                        quantityView.quantity = data.number
                        quantityView.setQuantityChangeListener(object : QuantityView.OnQuantityChangeListener {
                            override fun onLimitReached() {}
                            override fun onMinReached() {}
                            override fun onQuantityChanged(newQuantity: Int, programmatically: Boolean, minus: Boolean) {}
                        })
                    }
                }
            }
            HOT_COMMODITY -> object : BaseDelegateAdapter(R.layout.viewholder_hot_commodity) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is List<*>) {
                        val group = holder.get<ViewGroup>(R.id.container)
                        var index = 0
                        data.forEach {
                            if (it is Commodity) {
                                val itemView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.hot_commodity_item, group, false)
                                itemView.findViewById<TextView>(R.id.name_view).text = it.name
                                Glide.with(holder.itemView).load(it.picUrl).into(itemView.findViewById(R.id.commodity_img))
                                val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                                params.marginStart = if (index++ == 0) 0 else dip2px(14)
                                group.addView(itemView, params)
                            }
                        }
                        group.scrollBy(120, 0)
                    }
                }
            }
            ADDRESS_TYPE -> object : BaseDelegateAdapter(R.layout.viewholder_address) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is Address) {
                        holder.get<View>(R.id.default_view).visibility = if (data.isDefault) View.VISIBLE else View.INVISIBLE
                        holder.get<TextView>(R.id.name_view).text = "收货人：${data.name}"
                        holder.get<TextView>(R.id.tel_view).text = data.mobile
                        holder.get<TextView>(R.id.address_detail).text = "收获地址：${data.detailedAddress}"
                        holder.get<View>(R.id.edit_view).setOnClickListener { v ->
                            v.context.openActivity(AddAddressActivity::class.java, ADDRESS_ID, data.id)
                        }
                        holder.itemView.setOnClickListener {
                            onItemClick(position, data, 0)
                        }
                    }
                }
            }
            SETTLEMENT_ADDRESS -> object : BaseDelegateAdapter(R.layout.viewholder_settlement_address) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is Address) {
                        holder.get<TextView>(R.id.name_view).text = "收货人：${data.name}"
                        holder.get<TextView>(R.id.tel_view).text = data.mobile
                        holder.get<TextView>(R.id.address_detail).text = "收获地址：${data.detailedAddress}"
                        holder.itemView.setOnClickListener {
                            onItemClick(position, data, 0)
                        }
                    }
                }
            }

            RECOMMEND_TYPE -> object : BaseDelegateAdapter(R.layout.viewholder_recommend_commodity) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is Commodity) {
                        holder.itemView.setOnClickListener { v ->
                            v.context.openActivity(CommodityDetailActivity::class.java, GOODS_ID, data.id)
                        }
                        Glide.with(holder.itemView).load(data.picUrl).into(holder.get(R.id.commodity_img))
                        holder.get<TextView>(R.id.name_view).text = data.name
                        holder.get<TextView>(R.id.brief_view).text = data.brief
                        holder.get<TextView>(R.id.discount_price).text = "￥：${data.counterPrice}"
                        holder.get<TextView>(R.id.price_view).text = "￥：${data.retailPrice}"

                    }
                }
            }
            NEWS_TYPE -> object : BaseDelegateAdapter(R.layout.viewholer_news_list) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is NewsDTO.News) {
                        holder.get<TextView>(R.id.title_view).text = data.title
                        Glide.with(holder.itemView).load(data.picUrl).into(holder.get(R.id.cover_view))
                        holder.itemView.setOnClickListener { v ->
                            v.context.openActivity(NewsDetailActivity::class.java, NEWS_ID, data.id)
                        }
                        holder.get<View>(R.id.favorite_view).setOnClickListener {
                            val body = JSONObject(mapOf(Pair("type", 1), Pair("valueId", data.id)))
                            doHttpRequest(apiService.addOrDelete(body)) {}
                        }

                    }
                }
            }
            else -> {
                DefaultDelegateAdapter()
            }
        }
    }
}