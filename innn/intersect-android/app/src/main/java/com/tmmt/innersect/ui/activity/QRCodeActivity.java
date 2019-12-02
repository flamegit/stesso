package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.tmmt.innersect.BuildConfig;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.OrderDetailInfo;
import com.tmmt.innersect.utils.RxBus;
import com.tmmt.innersect.utils.SystemUtil;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class QRCodeActivity extends BaseActivity {

    @BindView(R.id.code_view)
    ImageView mCodeView;
    Subscription mSubscription;
    public boolean isRunning;
    String mOrderNo;

    @Override
    protected void initView() {
        super.initView();
        isRunning=true;
        mOrderNo=getIntent().getStringExtra(Constants.ORDER_NO);
        refresh();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qrcode;
    }

    @Override
    protected String getTitleString() {
        return "提货码";
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            int size = mCodeView.getWidth();
            Bitmap bitmap = CodeUtils.createImage(getCode(),size,size,null);
            mCodeView.setImageBitmap(bitmap);
        }
    }

    public static void start(Context context, String orderNo){
        Intent intent=new Intent(context,QRCodeActivity.class);
        intent.putExtra(Constants.ORDER_NO,orderNo);
        context.startActivity(intent);
    }

    private String getCode(){
        if(BuildConfig.DEBUG){
            return "http://47.92.38.236:8081/shop/#/order?orderNo="+mOrderNo;
        }else {
            return "https://m.innersect.net/shop/#/order?orderNo="+mOrderNo;
        }
    }

    private void refresh(){

        mSubscription = Observable.interval(5, 5, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(t -> {
                    ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getOrderDetail(mOrderNo)
                            .enqueue(new NetCallback<OrderDetailInfo>() {
                                @Override
                                public void onSucceed(OrderDetailInfo data) {
                                   if(data.getOrderCode()==OrderDetailInfo.RECEIVED){
                                       RxBus.getDefault().post("update");
                                       SystemUtil.toast("提货成功");
                                       mSubscription.unsubscribe();
                                       onBackPressed();
                                   }
                                }
                                @Override
                                protected void failed(int code) {
                                    super.failed(code);
                                }
                            });
                } );

    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning=false;
        if(mSubscription!=null && mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        isRunning=false;
        super.onDestroy();
    }
}
