package com.stesso.android

import GOODS_ID
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.stesso.android.lib.CommonPagerAdapter
import com.stesso.android.model.CommodityInfoDTO
import com.stesso.android.shopcart.ShopCartActivity
import com.stesso.android.utils.checkLogin
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.activity_commodity_detail.*
import org.json.JSONObject

class CommodityDetailActivity : BaseActivity() {

    var pagerAdapter: CommonPagerAdapter<String>? = null
    var choseArray: Array<String?>? = null
    var info: CommodityInfoDTO? =null

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
            info =it
            pagerAdapter?.addItems(it?.getGallery())
            product_name.text = it?.info?.name
            val size = it?.specificationList?.size ?: 2
            choseArray = arrayOfNulls(size)
            var index = 0
            it?.specificationList?.forEach {
                chose_layout.addView(createTitleView(it.name))
                chose_layout.addView(createTagLayout(it.valueList, index++))
            }
            web_view.loadData(getHtmlData(it?.info?.detail), "text/html; charset=utf-8", "utf-8")
        }
        add_cart_view.setOnClickListener {
            var productId = info?.getProductId(choseArray?.reduce{  acc, s ->"$acc$s" })
            productId?.let {
                val body = JSONObject(mapOf(Pair("goodsId", goodId), Pair("productId", it), Pair("number", 1)))
                doHttpRequest(apiService.addCartItem(body)) {}
            }
        }
    }

    private fun createTagLayout(valueList: List<CommodityInfoDTO.Value>, index: Int): View {
        val layout = LinearLayout(this)
        valueList.forEach {info->
            val tagView = createTagView(info.value)
            tagView.setOnClickListener{
                choseArray?.set(index,info.value)
                tagView.setTextColor(Color.RED)
            }
            layout.addView(tagView)
        }
        return layout
    }


    private fun createTitleView(value: String): View {
        val tagView = TextView(this)
        tagView.text = value
        return tagView
    }

    private fun createTagView(value: String): TextView {
        val tagView = TextView(this)
        tagView.text = value
        tagView.setPadding(16, 16, 16, 16)
        tagView.background = ContextCompat.getDrawable(this, R.drawable.stroke_gray_bg)
        tagView.setOnClickListener {

        }
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
