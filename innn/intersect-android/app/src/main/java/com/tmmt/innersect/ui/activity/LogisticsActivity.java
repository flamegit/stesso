package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.DeliverInfo;
import com.tmmt.innersect.ui.adapter.AdvancedAdapter;
import com.tmmt.innersect.ui.adapter.decoration.TimeLineDecoration;

import butterknife.BindView;


public class LogisticsActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    @BindView(R.id.com_view)
    TextView mComView;
    @BindView(R.id.deliver_code)
    TextView mCodeView;

    AdvancedAdapter<DeliverInfo.ShopingInfo> mAdapter;

    boolean isRefund;

    String mComNo;
    String mLogisticsNo;
    String mCompany;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_logistics;
    }

    @Override
    protected String getTitleString() {
        return "物流信息";
    }


    public static void start(Context context, String company, String companyNO, String no){
        Intent intent=new Intent(context,LogisticsActivity.class);
        intent.putExtra(Constants.COMPANY_NO,companyNO);
        intent.putExtra(Constants.COMPANY,company);
        intent.putExtra(Constants.LOGISTICS_NO,no);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();

        mComNo=getIntent().getStringExtra(Constants.COMPANY_NO);
        mCompany=getIntent().getStringExtra(Constants.COMPANY);
        mLogisticsNo=getIntent().getStringExtra(Constants.LOGISTICS_NO);
        isRefund=(mComNo!=null);

        mRecyclerView=findViewById(R.id.recycler_view);
        mAdapter=new AdvancedAdapter<>(R.layout.viewholder_logistics,(holder, position, data) -> {
            holder.<TextView>get(R.id.logistics_view).setText(data.context);
            holder.<TextView>get(R.id.time_view).setText(data.time);
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new TimeLineDecoration(this));
        String orderNo=getIntent().getStringExtra(Constants.ORDER_NO);

        if(isRefund){
            KLog.d(mComNo);
            KLog.d(mLogisticsNo);
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getLogisticsInfo(mComNo,mLogisticsNo).enqueue(new NetCallback<DeliverInfo>() {
                @Override
                public void onSucceed(DeliverInfo data) {
                    fillView(data);
                }
            });

        }else {
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getDeliverInfo(orderNo).enqueue(new NetCallback<DeliverInfo>() {
                @Override
                public void onSucceed(DeliverInfo data) {
                    fillView(data);
                }
            });
        }
    }

    private void fillView(DeliverInfo info){
        if(info==null){
            return;
        }

        if(isRefund){
            mCodeView.setText("运单编号："+mLogisticsNo);
            mComView.setText("快递公司："+mCompany);
        }else {
            mCodeView.setText("运单编号："+info.shippingNo);
            mComView.setText("快递公司："+info.shippingCom);
        }
        mAdapter.addItems(info.shippingInfo);
    }

}
