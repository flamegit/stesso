package com.stesso.android

import android.os.Bundle
import com.alipay.sdk.app.PayTask
import com.stesso.android.shopcart.ShopCartActivity
import com.stesso.android.utils.applySingleSchedulers
import com.stesso.android.utils.openActivity
import com.stesso.android.utils.toast
import com.stesso.android.wxapi.WXPayEntryActivity
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import io.reactivex.Single


open class PayActivity : BaseActivity() {

    private var wxApi: IWXAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wxApi = WXAPIFactory.createWXAPI(this, BuildConfig.WXAPPID)
        wxApi?.registerApp(BuildConfig.WXAPPID)
    }

    fun alipay(orderNo: Int) {
        //progressDialog.show()
        val disposable = apiService.getAlipayInfo(mapOf(Pair("orderId", orderNo))).flatMap {
            Single.just(it.data)
        }.map {
            PayTask(this).payV2(it, true)
        }.compose(applySingleSchedulers())
                .subscribe(
                        {
                            // progressDialog.dismiss()
                            if (it["resultStatus"] == "9000") {
                                toast("支付成功")
                            } else {
                                //openActivity(OrderDetailActivity::class.java, ORDER_ID, orderNo)
                                toast("支付失败")
                            }
                            ShopCartActivity.reload = true
                            this.openActivity(OrderDetailActivity::class.java, ORDER_ID, orderNo)
                            finish()
                        },
                        {
                            // progressDialog.dismiss()
                            it.printStackTrace()
                        }
                )
        //disposableContainer.add(disposable)
    }

    fun wechatPay(orderNo: Int) {
        doHttpRequest(apiService.getWechatPayInfo(mapOf(Pair("orderId", orderNo)))) { info ->
            val req = PayReq()
            info?.let {
                WXPayEntryActivity.orderNo = orderNo
                req.appId = it.appId
                req.nonceStr = it.nonceStr
                req.packageValue = it.packageValue
                req.partnerId = it.partnerId
                req.prepayId = it.prepayId
                req.sign = it.sign
                req.timeStamp = it.timeStamp
                wxApi?.sendReq(req)
            }
        }
    }
}
