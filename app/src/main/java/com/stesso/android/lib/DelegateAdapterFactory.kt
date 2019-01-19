package com.stesso.android.lib

import android.widget.TextView
import com.bumptech.glide.Glide
import com.stesso.android.R
import com.stesso.android.model.Commodity

const val HEADER = 1
const val FOOTER = 2
const val TYPE1 = 3
const val GIRL_TYPE = 4
const val NEW_COMMODITY = 5
const val HOT_COMMODITY = 6
const val VEDIO_TYPE = 7




class DelegateAdapterFactory {

    fun getDelegateAdapter(type: Int): ViewTypeDelegateAdapter {
        when (type) {
            HEADER -> return HeaderDelegateAdapter()
            FOOTER -> return FooterDelegateAdapter()
            TYPE1 -> return HeaderDelegateAdapter()
            VEDIO_TYPE -> return object : BaseDelegateAdapter(1){
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                }
            }
            NEW_COMMODITY -> return object : BaseDelegateAdapter(R.layout.viewholder_new_commodity) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is Commodity) {
                        Glide.with(holder.itemView).load(data.picUrl).into(holder.get(R.id.commodity_img))
                        holder.get<TextView>(R.id.name_view).text = data.name
                        holder.get<TextView>(R.id.discount_price).text = "$data.counterPrice"
                    }
                }
            }
        }
        return DefaultDelegateAdapter()
    }

}