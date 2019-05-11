package com.stesso.android.lib

import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import cn.jzvd.Jzvd
import cn.jzvd.JzvdStd
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.stesso.android.*
import com.stesso.android.address.AddAddressActivity
import com.stesso.android.address.AddressListActivity
import com.stesso.android.datasource.net.ApiService
import com.stesso.android.model.*
import com.stesso.android.utils.dip2px
import com.stesso.android.utils.openActivity
import com.stesso.android.utils.parseTime
import com.stesso.android.utils.parseTimeMillis
import com.stesso.android.widget.QuantityView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit
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
const val FAVORITE_NEWS = 18
const val FAVORITE_COMMODITY = 19
const val ORDER_INFO = 20
const val ORDER_PRICE = 21
const val ORDER_ADDRESS = 22
const val ORDER_GOODS = 23
const val ORDER_STATUS = 24
const val EXPRESS_INFO = 25
const val SEARCH_GOODS = 26


class DelegateAdapterFactory {

    @Inject
    lateinit var apiService: ApiService

    var onItemClick: (position: Int, data: Any?, action: Int,extra:Any?) -> Unit = { _, _, _,_ -> }

    init {
        App.instance().component.inject(this)
    }

    fun getDelegateAdapter(type: Int): ViewTypeDelegateAdapter {
        return when (type) {
            HEADER -> HeaderDelegateAdapter()
            FOOTER -> FooterDelegateAdapter()
            TYPE1 -> HeaderDelegateAdapter()
            BANNER_TYPE -> object : BaseDelegateAdapter(R.layout.viewholder_top_vedio) {

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
                    val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
                    val jzvdStd = view.findViewById<JzvdStd>(R.id.video_player)
                    jzvdStd.backButton.visibility = View.INVISIBLE
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        jzvdStd.outlineProvider = JzViewOutlineProvider(20f)
                        jzvdStd.clipToOutline = true
                    }
                    return CommonViewHolder(view)
                }

                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is VideoItem) {
                        val jzvdStd = holder.get<JzvdStd>(R.id.video_player)
                        jzvdStd.setUp(data.url, "", Jzvd.SCREEN_WINDOW_LIST)
                        //jzvdStd.titleTextView.text = "STESSO"
                        val options = RequestOptions.bitmapTransform(RoundedCorners(10)).centerCrop()
                        Glide.with(holder.itemView).load(data.cover).apply(options).into(jzvdStd.thumbImageView)
                    }
                }
            }
            EMPTY_ADDRESS -> object : BaseDelegateAdapter(R.layout.viewholder_empty_address) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is EmptyAddress) {
                        holder.itemView.setOnClickListener { v ->
                            v.context.openActivity(AddressListActivity::class.java)
                        }
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
                        holder.get<TextView>(R.id.brief_view).text = data.brief
                        holder.get<TextView>(R.id.discount_price).text = "${data.counterPrice}"
                    }
                }
            }

            FAVORITE_COMMODITY, SEARCH_GOODS -> object : BaseDelegateAdapter(R.layout.viewholder_goods_itemt) {
                private fun bindView(holder: CommonViewHolder, commodity: Commodity?) {
                    holder.itemView.setOnClickListener { v ->
                        v.context.openActivity(CommodityDetailActivity::class.java, GOODS_ID, commodity?.id)
                    }
                    Glide.with(holder.itemView).load(commodity?.picUrl).into(holder.get(R.id.commodity_img))
                    holder.get<TextView>(R.id.name_view).text = commodity?.name
                    holder.get<TextView>(R.id.discount_price).text = "￥:${commodity?.retailPrice}"
                    val priceView = holder.get<TextView>(R.id.price_view)
                    priceView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG //中划线
                    priceView.text = "￥:${commodity?.counterPrice}"
                }

                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is FavoriteCommodity) {
                        bindView(holder, data.info)
                    } else if (data is Commodity) {
                        bindView(holder, data)
                    }
                }
            }
            ORDER_LIST -> object : BaseDelegateAdapter(R.layout.order_item) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is OrderList.OrderItem) {
                        holder.get<TextView>(R.id.name_view).text = data.goodsList[0].goodsName
                        holder.get<TextView>(R.id.state_view).text = data.orderStatusText
                        holder.get<TextView>(R.id.price_view).text = "总计：${data.actualPrice}"
                        Glide.with(holder.itemView).load(data.goodsList[0].picUrl).into(holder.get(R.id.commodity_img))
                        holder.itemView.setOnClickListener { v ->
                            v.context.openActivity(OrderDetailActivity::class.java, ORDER_ID, data.id)
                        }
                    }
                }
            }
            SETTLEMENT_INFO -> object : BaseDelegateAdapter(R.layout.viewholder_settlement_info) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is ShopcartDTO) {
                        holder.get<TextView>(R.id.total_price).text = "￥:${data.getTotalPrice()}"
                        holder.get<TextView>(R.id.order_price).text = "￥:${data.getTotalPrice()}"
                    }
                }
            }
            SETTLEMENT_PAY -> object : BaseDelegateAdapter(R.layout.viewholder_pay_method) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    val alipayView = holder.get<CheckBox>(R.id.alipay_view)
                    val wechatPayView = holder.get<CheckBox>(R.id.wechat_pay)
                    alipayView.isEnabled = false
                    alipayView.setOnCheckedChangeListener { button, isChecked ->
                        if (isChecked and button.isPressed) {
                            onItemClick(position, data, 0,null)
                            button.isEnabled = false
                            wechatPayView.isEnabled = true
                            wechatPayView.isChecked = false
                        }
                    }
                    wechatPayView.setOnCheckedChangeListener { button, isChecked ->
                        if (isChecked and button.isPressed) {
                            onItemClick(position, data, 1,null)
                            button.isEnabled = false
                            alipayView.isEnabled = true
                            alipayView.isChecked = false
                        }
                    }
                }
            }
            SETTLEMENT_ITEM, ORDER_GOODS -> object : BaseDelegateAdapter(R.layout.viewholder_settlement_item) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is CommodityDetail) {
                        holder.get<TextView>(R.id.name_view).text = data.goodsName
                        Glide.with(holder.itemView).load(data.picUrl).into(holder.get(R.id.commodity_img))
                        holder.get<TextView>(R.id.info_view).text = "${data.getInfo()}  ${data.number}件"
                        holder.get<TextView>(R.id.price_view).text = "￥：${data.price}"
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
                            onItemClick(holder.adapterPosition, data, 1,null)
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
                                onItemClick(position, data, if (minus) 2 else 3,newQuantity)
                            }
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
                            onItemClick(position, data, 0,null)
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
                            onItemClick(position, data, SETTLEMENT_ADDRESS,null)
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
                        val discountView = holder.get<TextView>(R.id.discount_price)
                        val priceView = holder.get<TextView>(R.id.price_view)
                        discountView.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG //中划线
                        if (data.counterPrice != data.retailPrice) {
                            priceView.text = "￥：${data.retailPrice}"
                        }
                    }
                }
            }
            FAVORITE_NEWS -> object : BaseDelegateAdapter(R.layout.viewholer_news_list) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is FavoriteNews) {
                        val news = data.info
                        holder.get<TextView>(R.id.title_view).text = news?.title
                        holder.get<TextView>(R.id.time_view).text = parseTime(news?.addTime ?: "")
                        Glide.with(holder.itemView).load(news?.picUrl).into(holder.get(R.id.cover_view))
                        holder.itemView.setOnClickListener { v ->
                            v.context.openActivity(NewsDetailActivity::class.java, NEWS_ID, news?.id)
                        }
                    }
                }
            }
            NEWS_TYPE -> object : BaseDelegateAdapter(R.layout.viewholer_news_list) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is News) {
                        holder.get<TextView>(R.id.title_view).text = data.title
                        holder.get<TextView>(R.id.time_view).text = parseTime(data.addTime)
                        Glide.with(holder.itemView).load(data.picUrl).into(holder.get(R.id.cover_view))
                        holder.itemView.setOnClickListener { v ->
                            v.context.openActivity(NewsDetailActivity::class.java, NEWS_ID, data.id)
                        }
                    }
                }
            }
            ORDER_INFO -> object : BaseDelegateAdapter(R.layout.viewholder_order_info) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is OrderInfo) {
                        holder.get<TextView>(R.id.order_date).text = parseTime(data.addTime)
                        holder.get<TextView>(R.id.order_no).text = data.orderSn
                        holder.get<TextView>(R.id.pay_type).text = data.payType
                    }
                }
            }
            ORDER_PRICE -> object : BaseDelegateAdapter(R.layout.viewholder_settlement_info) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is OrderInfo) {
                        holder.get<TextView>(R.id.total_price).text = "￥:${data.actualPrice}"
                        holder.get<TextView>(R.id.order_price).text = "￥:${data.goodsPrice}"
                    }
                }
            }
            ORDER_ADDRESS -> object : BaseDelegateAdapter(R.layout.viewholder_order_address) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is OrderInfo) {
                        holder.get<TextView>(R.id.name_view).text = "${data.consignee}"
                        holder.get<TextView>(R.id.tel_view).text = data.mobile
                        holder.get<TextView>(R.id.address_detail).text = "${data.address}"
                    }
                }
            }
            EXPRESS_INFO -> object : BaseDelegateAdapter(R.layout.viewholder_order_express) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is ExpressInfo) {
                        holder.get<TextView>(R.id.company_view).text = "${data.shipperName}"
                        holder.get<TextView>(R.id.code_view).text = "${data.logisticCode}"
                        if (data.traces.isNotEmpty()) {
                            holder.get<TextView>(R.id.time_view).text = "${data.traces[0].acceptTime}"
                            holder.get<TextView>(R.id.station_view).text = "${data.traces[0].acceptStation}"
                        }
                    }
                }
            }
            ORDER_STATUS -> object : BaseDelegateAdapter(R.layout.viewholder_order_status) {
                override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data: Any?) {
                    super.onBindViewHolder(holder, position, data)
                    if (data is OrderInfo) {
                        val action1View = holder.get<TextView>(R.id.action_one)
                        val action2View = holder.get<TextView>(R.id.action_two)
                        val descView = holder.get<TextView>(R.id.desc_view)
                        when (data.orderStatus) {
                            101 -> {
                                action1View.visibility = View.VISIBLE
                                action2View.text = "继续支付"
                                action1View.text = "取消订单"
                                var left = (parseTimeMillis(data.addTime) - 27000 * 1000 - DateTime.now().millis) / 1000
                                if (left > 0) {
                                    descView.visibility = View.VISIBLE
                                    Observable.intervalRange(0, left, 0, 1, TimeUnit.SECONDS)
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe {
                                                left -= 1
                                                val m = left / 60
                                                val s = left % 60
                                                descView.text = "${m}分${s}秒后关闭"
                                            }
                                } else {
                                    descView.visibility = View.GONE
                                }
                                action2View.setTextColor(Color.parseColor("#FFFFFF"))
                                action2View.setBackgroundResource(R.drawable.solid_red_bg)
                                action1View.setOnClickListener {
                                    onItemClick(position, data, 1,null)
                                }
                                action2View.setOnClickListener {
                                    onItemClick(position, data, 2,null)
                                }
                            }
                            //已付款
                            201 -> {
                                action1View.text = "等待发货"
                            }
                            301 -> {
                                action1View.visibility = View.VISIBLE
                                action2View.setTextColor(Color.parseColor("#FFFFFF"))
                                action2View.setBackgroundResource(R.drawable.solid_red_bg)
                                action2View.text = "确认收货"
                                action1View.text = "运送中"
                                action2View.setOnClickListener {
                                    onItemClick(position, data, 3,null)
                                }
                            }
                            401, 402 -> {
                                action1View.visibility = View.VISIBLE
                                action2View.text = "已送达"
                                action1View.text = "退货"
                                action1View.setOnClickListener {
                                    onItemClick(position, data, 4,null)
                                }
                            }
                            //售后
                            501 -> {

                            }
                            //售后完成
                            502 -> {
                            }
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