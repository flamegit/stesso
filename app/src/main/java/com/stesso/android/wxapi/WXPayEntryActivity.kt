package com.stesso.android.wxapi
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stesso.android.BuildConfig
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

    override fun onReq(req: BaseReq) {

    }
    override fun onResp(resp: BaseResp) {
        if (resp.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                toast("支付成功")
            } else {
                toast("${resp.errCode}")
                toast("支付失败")
            }
            finish()
        }
    }
}