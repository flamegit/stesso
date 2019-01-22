package com.stesso.android.lib

import ADDRESS_ID
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.stesso.android.R
import com.stesso.android.model.Commodity
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.stesso.android.CommodityDetailActivity
import com.stesso.android.address.AddAddressActivity
import com.stesso.android.model.Address
import com.stesso.android.model.BannerItem
import com.stesso.android.utils.openActivity

const val HEADER = 1
const val FOOTER = 2
const val TYPE1 = 3
const val GIRL_TYPE = 4
const val NEW_COMMODITY = 5
const val HOT_COMMODITY = 6
const val BANNER_TYPE = 7
const val ADDRESS_TYPE = 8
const val CART_TYPE = 9


class DelegateAdapterFactory {

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
                        holder.itemView.setOnClickListener {
                            v -> v.context.openActivity(CommodityDetailActivity::class.java)
                        }
                        Glide.with(holder.itemView).load(data.picUrl).into(holder.get(R.id.commodity_img))
                        holder.get<TextView>(R.id.name_view).text = data.name
                        holder.get<TextView>(R.id.discount_price).text = "${data.counterPrice}"
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
                        holder.get<View>(R.id.edit_view).setOnClickListener {
                            v -> v.context.openActivity(AddAddressActivity::class.java,ADDRESS_ID,data.id)
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