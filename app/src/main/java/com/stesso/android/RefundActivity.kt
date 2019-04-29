package com.stesso.android

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import com.bumptech.glide.Glide
import com.stesso.android.lib.CommonViewHolder
import com.stesso.android.model.CommodityDetail
import kotlinx.android.synthetic.main.activity_refund.*

class RefundActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refund)

        val goodsList = intent.getParcelableArrayListExtra<CommodityDetail>(GOODS_LIST)

        goodsList?.forEach {
            //if(it is CommodityDetail){
            val view = LayoutInflater.from(this).inflate(R.layout.viewholder_settlement_item, goods_container, true)
            val holder = CommonViewHolder(view)
            holder.get<TextView>(R.id.name_view).text = it.goodsName
            Glide.with(holder.itemView).load(it.picUrl).into(holder.get(R.id.commodity_img))
            holder.get<TextView>(R.id.info_view).text = "${it.getInfo()}  ${it.number}件"
            holder.get<TextView>(R.id.price_view).text = "￥：${it.price}"
            //}
        }

    }
}
