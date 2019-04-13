package com.stesso.android

import android.os.Bundle
import kotlinx.android.synthetic.main.activity_order_detail.*

class OrderDetailActivity : PayActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        setContentView(R.layout.activity_order_detail)
        val orderId = intent.getIntExtra(ORDER_ID, 0)

        doHttpRequest(apiService.getOrderDetail(orderId)){

        }

        pay_view.setOnClickListener {
            val dialog = PaymentFragment()
            dialog.setActionCallback { _, payTye ->
                if (payTye == 0) alipay(orderId) else wechatPay(orderId)
            }

            dialog.show(supportFragmentManager, "")
            //BottomSheetDialog(this).setContentView(R.layout.viewholder_pay_method)
        }

    }
}
