package com.stesso.android

import android.os.Bundle
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.lib.ORDER_ADDRESS
import com.stesso.android.lib.ORDER_GOODS
import com.stesso.android.lib.ORDER_INFO
import kotlinx.android.synthetic.main.activity_order_detail.*

class OrderDetailActivity : PayActivity() {

    private val adapter = MultiTypeAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        setContentView(R.layout.activity_order_detail)
        val orderId = intent.getIntExtra(ORDER_ID, 0)

        recycler_view.adapter =adapter

        doHttpRequest(apiService.getOrderDetail(orderId)){
            adapter.addItems(it?.orderGoods, ORDER_GOODS)
            adapter.addItem(it?.orderInfo, ORDER_ADDRESS,true)
            adapter.addItem(it?.orderInfo, ORDER_INFO,true)
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
