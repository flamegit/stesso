package com.tmmt.innersect.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tmmt.innersect.BuildConfig;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.ui.activity.HomeActivity;
import com.tmmt.innersect.ui.activity.OrdersListActivity;
import com.tmmt.innersect.ui.activity.SuccessActivity;
import com.tmmt.innersect.utils.Util;
import com.umeng.weixin.callback.WXCallbackActivity;


public class WXPayEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    public static String orderNo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, BuildConfig.WXAPPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {}

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if(resp.errCode==0){
                Toast.makeText(this,"支付成功",Toast.LENGTH_SHORT).show();
                Intent homeIntent=new Intent(this,HomeActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                this.startActivity(homeIntent);
                this.startActivity(new Intent(this,OrdersListActivity.class));
                if(orderNo!=null){
                    Util.startActivity(WXPayEntryActivity.this,SuccessActivity.class, Constants.ORDER_NO,orderNo);
                }
            }else {
                Toast.makeText(this,"支付失败",Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

}
