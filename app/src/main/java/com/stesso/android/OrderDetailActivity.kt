package com.stesso.android

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import com.stesso.android.lib.*
import com.stesso.android.model.OrderDetail
import com.stesso.android.model.OrderInfo
import kotlinx.android.synthetic.main.activity_order_detail.*
import org.json.JSONObject

class OrderDetailActivity : PayActivity() {

    private val adapter = MultiTypeAdapter()
    private var orderDetail: OrderDetail? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        setContentView(R.layout.activity_order_detail)
        configTitleView(title_view)
        val orderId = intent.getIntExtra(ORDER_ID, 0)
        recycler_view.adapter = adapter
        adapter.setOnItemClick { _, data, action ->
            if (data is OrderInfo) {
                when (action) {
                    //
                    1 -> {
                        AlertDialog.Builder(this).setTitle("取消订单")
                                .setMessage("确定取消订单？")
                                .setNegativeButton("取消") { dialog, _ ->
                                    dialog.dismiss()
                                }.setPositiveButton("确定") { dialog, _ ->
                                    val body = JSONObject(mapOf(Pair("orderId", data.id)))
                                    doHttpRequest(apiService.cancelOrder(body)) {
                                        onBackPressed()
                                    }
                                }.show()
                    }
                    2 -> {
                        val dialog = PaymentFragment()
                        dialog.setActionCallback { _, payTye ->
                            if (payTye == 0) alipay(orderId) else wechatPay(orderId)
                        }
                        dialog.show(supportFragmentManager, "")
                    }
                    3 -> {
                        val body = JSONObject(mapOf(Pair("orderId", data.id)))
                        doHttpRequest(apiService.confirmOrder(body)) {
                            //onBackPressed()
                        }
                    }
                    4 -> {
                        val intent = Intent(this, RefundActivity::class.java)
                        intent.putExtra(ORDER_ID, orderId)
                        intent.putParcelableArrayListExtra(GOODS_LIST, orderDetail?.orderGoods)
                        startActivity(intent)
                    }
                    5 -> {

                    }
                    else -> {

                    }
                }
            }
        }
        doHttpRequest(apiService.getOrderDetail(orderId)) {
            orderDetail = it
            adapter.addItems(it?.orderGoods, ORDER_GOODS)
            adapter.addItem(it?.orderInfo, ORDER_STATUS, true)
            if (it?.expressInfo != null) {
                adapter.addItem(it.expressInfo, EXPRESS_INFO, true)
            }
            adapter.addItem(it?.orderInfo, ORDER_ADDRESS, true)
            adapter.addItem(it?.orderInfo, ORDER_PRICE, true)
            adapter.addItem(it?.orderInfo, ORDER_INFO, true)
        }

//        pay_view.setOnClickListener {
//            val dialog = PaymentFragment()
//            dialog.setActionCallback { _, payTye ->
//                if (payTye == 0) alipay(orderId) else wechatPay(orderId)
//            }
//            dialog.show(supportFragmentManager, "")
//        }

    }
}
