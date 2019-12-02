package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.RefundItem;
import com.tmmt.innersect.ui.adapter.DataBindingAdapter;

import java.util.List;

import butterknife.BindView;

public class RefundHistoryActivity extends BaseViewActivity<List<RefundItem>> {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    DataBindingAdapter<RefundItem> mAdapter;

    @Override
    protected String getTitleString() {
        return "退换货记录";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_refund_history;
    }

    public static void start(Context context, String orderNo){
        Intent intent=new Intent(context,RefundHistoryActivity.class);
        if(orderNo!=null){
            intent.putExtra(Constants.ORDER_NO,orderNo);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        String orderNo=getIntent().getStringExtra(Constants.ORDER_NO);
        mPresenter.getRefundList(orderNo);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DataBindingAdapter<>(R.layout.viewholder_refund_history, ((holder, position, data) -> {
            ExchangeDetailActivity.start(RefundHistoryActivity.this,data.astype==1,data.asno,false);
        }));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void success(List<RefundItem> data) {
        if(data==null ||data.isEmpty()){
            showEmptyView();
        }else {
            mAdapter.addItems(data);
        }
    }
}
