package com.stesso.android

import GOODS_ID
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
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
import kotlinx.android.synthetic.main.activity_commodity_detail.*
import org.json.JSONObject

class CommodityDetailActivity : BaseActivity() {

    var pagerAdapter: CommonPagerAdapter<String>? = null
    var choseValues: Array<String?>? = null
    var chosePositions: Array<Int?>? = null

    var info: CommodityInfoDTO? = null

    val paddingSmall = dip2px(16)
    val paddingBig = dip2px(32)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_commodity_detail)
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
        view_pager.adapter = pagerAdapter
        indicator_view.setViewPager(view_pager)
        doHttpRequest(apiService.getCommodityDetail(goodId)) {
            info = it
            fillView(it)
        }
        add_cart_view.setOnClickListener {
            var productId = info?.getProductId(choseValues?.reduce { acc, s -> "$acc$s" })
            productId?.let {
                val body = JSONObject(mapOf(Pair("goodsId", goodId), Pair("productId", it), Pair("number", 1)))
                doHttpRequest(apiService.addCartItem(body)) {}
            }
        }
    }

    private fun fillView(info:CommodityInfoDTO?){
        pagerAdapter?.addItems(info?.getGallery())
        product_name.text = info?.info?.name
        val size = info?.specificationList?.size ?: 2
        choseValues = arrayOfNulls(size)
        chosePositions = arrayOfNulls(size)
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
                tagView.setTextColor(Color.RED)
                lightView(layout,tagView)
            }
            layout.addView(tagView)
        }
        return layout
    }

    private fun lightView(parent: ViewGroup, tagView: TextView) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i) as TextView
            if (child == tagView) {
                child.setTextColor(Color.RED)
            }else{
                child.setTextColor(Color.BLACK)
            }
        }
    }


    private fun createTitleView(value: String): View {
        val tagView = TextView(this)
        tagView.setPadding(paddingSmall,paddingSmall,paddingSmall,paddingSmall)
        tagView.text = value
        return tagView
    }

    private fun createTagView(value: String): TextView {
        val tagView = TextView(this)
        tagView.text = value
        tagView.setPadding(paddingBig, paddingSmall, paddingBig, paddingSmall)
        tagView.background = ContextCompat.getDrawable(this, R.drawable.stroke_gray_bg)
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
