//package com.stesso.android.wxapi
//
//import android.content.Intent
//import android.os.Bundle
//
//import com.tencent.mm.opensdk.modelbase.BaseReq
//import com.tencent.mm.opensdk.modelbase.BaseResp
//import com.tencent.mm.opensdk.openapi.IWXAPI
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
//import com.tencent.mm.opensdk.openapi.WXAPIFactory
//
//
//class WXPayEntryActivity : WXCallbackActivity(), IWXAPIEventHandler {
//
//    private var api: IWXAPI? = null
//    fun onCreate(savedInstanceState: Bundle) {
//        super.onCreate(savedInstanceState)
//        api = WXAPIFactory.createWXAPI(this, BuildConfig.WXAPPID)
//        api!!.handleIntent(getIntent(), this)
//    }
//
//    protected fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//
//        api!!.handleIntent(intent, this)
//    }
//
//    override fun onReq(req: BaseReq) {}
//
//    override fun onResp(resp: BaseResp) {
//
//    }
//
//    companion object {
//
//        var orderNo: String? = null
//    }
//
//}
