package com.stesso.android.lib

import ADDRESS_ID
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.stesso.android.R
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.stesso.android.App
import com.stesso.android.CommodityDetailActivity
import com.stesso.android.address.AddAddressActivity
import com.stesso.android.datasource.net.ApiService
import com.stesso.android.model.*
import com.stesso.android.utils.openActivity
import com.stesso.android.widget.QuantityView
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
const val NEWS_TYPE = 10


class DelegateAdapterFactory {

    @Inject
    lateinit var apiService: ApiService

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
                            v.context.openActivity(CommodityDetailActivity::class.java)
                        }
                        Glide.with(holder.itemView).load(data.picUrl).into(holder.get(R.id.commodity_img))
                        holder.get<TextView>(R.id.name_view).text = data.name
                        holder.get<TextView>(R.id.discount_price).text = "${data.counterPrice}"
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

                        }
                        //holder.get<TextView>(R.id.info_view).text = data.goodsName
                        val quantityView = holder.get<QuantityView>(R.id.quantity_view)
                        quantityView.setQuantityChangeListener(object : QuantityView.OnQuantityChangeListener {
                            override fun onLimitReached() {}
                            override fun onMinReached() {}
                            override fun onQuantityChanged(newQuantity: Int, programmatically: Boolean) {}
                        })
                    }
                }
            }
            ADDRESS_TYPE -> object : BaseDelegateAdapter(R.layout.viewholder_address) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is Address) {
                        holder.get<TextView>(R.id.name_view).text = data.name
                        holder.get<TextView>(R.id.tel_view).text = data.mobile
                        holder.get<TextView>(R.id.address_view).text = data.detailedAddress
                        holder.get<View>(R.id.edit_view).setOnClickListener { v ->
                            v.context.openActivity(AddAddressActivity::class.java, ADDRESS_ID, data.id)
                        }

                    }
                }
            }
            NEWS_TYPE -> object : BaseDelegateAdapter(R.layout.viewholer_news_list) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is NewsDTO) {
                        holder.get<TextView>(R.id.title_view).text = data.title
                        Glide.with(holder.itemView).load(data.picUrl).into(holder.get(R.id.cover_view))
                    }
                }
            }
            else -> {
                DefaultDelegateAdapter()
            }
        }
    }
}