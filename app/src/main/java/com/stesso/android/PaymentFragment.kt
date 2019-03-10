package com.stesso.android

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_select_pay.*

class PaymentFragment : BottomSheetDialogFragment() {

    var callback: (Int, Int) -> Unit = { _, _ -> }
    var payType = 0

    fun setActionCallback(action: (Int, Int) -> Unit) {
        callback = action
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_select_pay, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        alipay_view.isEnabled = false
        alipay_view.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked and button.isPressed) {
                payType = 0
                button.isEnabled = false
                wechat_pay.isEnabled = true
                wechat_pay.isChecked = false
            }
        }

        wechat_pay.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked and button.isPressed) {
                payType = 1
                button.isEnabled = false
                alipay_view.isEnabled = true
                alipay_view.isChecked = false
            }
        }
        pay_view.setOnClickListener {
            callback(1, payType)
            dismiss()
        }

    }


}