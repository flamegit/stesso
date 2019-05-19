package com.stesso.android.wxapi
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stesso.android.BuildConfig
import com.stesso.android.ORDER_ID
import com.stesso.android.OrderDetailActivity
import com.stesso.android.lib.RxBus
import com.stesso.android.shopcart.ShopCartActivity
import com.stesso.android.utils.openActivity
import com.stesso.android.utils.toast
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class WXPayEntryActivity : AppCompatActivity(), IWXAPIEventHandler {

    private lateinit var api: IWXAPI

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, BuildConfig.WXAPPID)
        api.handleIntent(intent, this)

    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {}
    override fun onResp(resp: BaseResp) {

        if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            RxBus.post(RxBus.Event(resp.errCode,"success"))
            //toast("支付失败")
            finish()
        }
    }
    companion object{
        var orderNo :Int?=0
    }
}