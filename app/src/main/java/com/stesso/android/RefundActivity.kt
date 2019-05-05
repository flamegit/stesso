package com.stesso.android

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.TextView
import com.bumptech.glide.Glide
import com.stesso.android.lib.CommonViewHolder
import com.stesso.android.model.CommodityDetail
import com.stesso.android.model.SuggestionDTO
import kotlinx.android.synthetic.main.activity_refund.*

class RefundActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refund)
        getActivityComponent().inject(this)

        val goodsList = intent.getParcelableArrayListExtra<CommodityDetail>(GOODS_LIST)
        val orderId = intent.getIntExtra(ORDER_ID, 0)
        goodsList?.forEach {
            //if(it is CommodityDetail){
            val view = LayoutInflater.from(this).inflate(R.layout.refund_item, goods_container, true)
            val holder = CommonViewHolder(view)
            holder.get<TextView>(R.id.name_view).text = it.goodsName
            Glide.with(holder.itemView).load(it.picUrl).into(holder.get(R.id.commodity_img))
            holder.get<TextView>(R.id.info_view).text = "${it.desc}  ${it.number}件"
            holder.get<TextView>(R.id.price_view).text = "￥：${it.price}"
            holder.get<CheckBox>(R.id.checkbox).setOnCheckedChangeListener { _, isChecked ->
                it.checked = isChecked
            }

        }

        fun getIdList(): String? {
            var idlist = ""
            goodsList?.forEach {
                if (it.checked) {
                    if (idlist.isNotEmpty()) {
                        idlist += ", "
                    }
                    idlist += "${it.productId}"
                }
            }
            return idlist
        }
        submit_view.setOnClickListener {
            val mobile = mobile_view.text.toString()
            val body = SuggestionDTO(mobile, 2, "refund", orderId.toString(), getIdList())
            doHttpRequest(apiService.submit(body)) {

            }
        }
    }
}
