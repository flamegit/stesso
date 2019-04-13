package com.stesso.android

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.stesso.android.lib.CommonPagerAdapter
import com.stesso.android.model.CommodityInfoDTO
import com.stesso.android.shopcart.ShopCartActivity
import com.stesso.android.utils.checkLogin
import com.stesso.android.utils.dip2px
import com.stesso.android.utils.openActivity
import com.stesso.android.utils.toast
import com.stesso.android.widget.QuantityView
import kotlinx.android.synthetic.main.activity_commodity_detail.*
import org.json.JSONObject

class CommodityDetailActivity : BaseActivity() {

    var pagerAdapter: CommonPagerAdapter<String>? = null
    var choseValues: Array<String?>? = null
    var choseViews: Array<TextView?>? = null
    var info: CommodityInfoDTO? = null
    private val paddingSmall = dip2px(8)
    private val paddingBig = dip2px(16)
    private var tagRed = 0
    private var tagGray = 0
    private var fontColor = 0
    private var num = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commodity_detail)
        tagGray = ContextCompat.getColor(this, R.color.divider_color)
        tagRed = ContextCompat.getColor(this, R.color.tag_red)
        fontColor = ContextCompat.getColor(this, R.color.font_4A)
        getActivityComponent().inject(this)
        val goodId = intent.getIntExtra(GOODS_ID, 0)
        configTitleView(title_view) {
            checkLogin { openActivity(ShopCartActivity::class.java) }
        }

        pagerAdapter = CommonPagerAdapter { group, str ->
            val imageView = ImageView(group.context)
            Glide.with(group).load(str).into(imageView)
            imageView
        }
        quantity_view.setQuantityChangeListener(object : QuantityView.OnQuantityChangeListener {
            override fun onLimitReached() {}
            override fun onMinReached() {}
            override fun onQuantityChanged(newQuantity: Int, programmatically: Boolean, minus: Boolean) {
                num = newQuantity
            }
        })

        favorite_view.setOnClickListener {
            val body = JSONObject(mapOf(Pair("type", 0), Pair("valueId", info?.info?.id)))

            doHttpRequest(apiService.addOrDelete(body)) {

            }
        }

        view_pager.adapter = pagerAdapter
        indicator_view.setViewPager(view_pager)
        doHttpRequest(apiService.getCommodityDetail(goodId)) {
            info = it
            fillView(it)
        }
        //TODO
        add_cart_view.setOnClickListener {
            if (choseValues?.get(0) == null) {
                toast("请选择颜色")
                return@setOnClickListener
            }
            if (choseValues?.get(1) == null) {
                toast("请选择尺码")
                return@setOnClickListener
            }
            num = quantity_view.quantity
            val productId = info?.getProductId(choseValues?.reduce { acc, s -> "$acc$s" })
            productId?.let {
                val body = JSONObject(mapOf(Pair("goodsId", goodId), Pair("productId", it), Pair("number", num)))
                doHttpRequest(apiService.addCartItem(body)) {}
            }
        }
    }

    private fun fillView(info: CommodityInfoDTO?) {
        pagerAdapter?.addItems(info?.getGallery())
        product_name.text = info?.info?.name
        val size = info?.specificationList?.size ?: 2
        choseValues = arrayOfNulls(size)
        choseViews = arrayOfNulls(size)
        var index = 0
        info?.specificationList?.forEach {
            chose_layout.addView(createTitleView(it.name))
            chose_layout.addView(createTagLayout(it.valueList, index++))
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.marginStart = if (index == 0) 0 else dip2px(14)
        }
        web_view.loadData(getHtmlData(info?.info?.detail), "text/html; charset=utf-8", "utf-8")
    }

    private fun createTagLayout(valueList: List<CommodityInfoDTO.Value>, index: Int): View {
        val layout = LinearLayout(this)
        valueList.forEach { info ->
            val tagView = createTagView(info.value)
            tagView.setOnClickListener {
                choseValues?.set(index, info.value)
                val lastView = choseViews?.get(index)
                choseViews?.set(index, tagView)
                if (lastView != tagView) {
                    selectView(lastView, false)
                    selectView(tagView, true)
                }
            }
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.marginStart = paddingBig
            layout.addView(tagView, params)
        }
        return layout
    }

    private fun selectView(tagView: TextView?, select: Boolean) {
        if (select) {
            tagView?.setTextColor(tagRed)
            tagView?.background = ContextCompat.getDrawable(this, R.drawable.tag_red_bg)
        } else {
            tagView?.setTextColor(fontColor)
            tagView?.background = ContextCompat.getDrawable(this, R.drawable.tag_gray_bg)
        }
    }

    private fun createTitleView(value: String): View {
        val tagView = TextView(this)
        tagView.setPadding(0, paddingBig, 0, paddingBig)
        tagView.setTextColor(fontColor)
        tagView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13F)
        tagView.text = value
        return tagView
    }

    private fun createTagView(value: String): TextView {
        val tagView = TextView(this)
        tagView.text = value
        tagView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13F)
        tagView.setPadding(paddingBig, paddingSmall, paddingBig, paddingSmall)
        tagView.background = ContextCompat.getDrawable(this, R.drawable.tag_gray_bg)
        return tagView
    }

    private fun getHtmlData(body: String?): String {
        val head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>"
        return "<html>$head<body>$body</body></html>"
    }

}
