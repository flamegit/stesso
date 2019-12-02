package com.tmmt.innersect.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.Common;
import com.tmmt.innersect.ui.adapter.AdvancedAdapter;
import com.tmmt.innersect.ui.adapter.decoration.TimeLineDecoration;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class CancelProgressActivity extends BaseViewActivity<List<Common>> {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    AdvancedAdapter<Common> mAdapter;
    boolean isSkip;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cancel_progress;
    }

    @Override
    protected String getTitleString() {
        return "申请取消订单";
    }


    public static void start(Context context, String no){
        Intent intent =new Intent(context,CancelProgressActivity.class);
        intent.putExtra(Constants.ORDER_NO,no);
        context.startActivity(intent);
    }

    public static void start(Context context, String no,boolean skip){
        Intent intent =new Intent(context,CancelProgressActivity.class);
        intent.putExtra(Constants.ORDER_NO,no);
        intent.putExtra(Constants.SKIP,skip);
        context.startActivity(intent);
    }


    @Override
    protected void initView() {
        super.initView();
        String no=getIntent().getStringExtra(Constants.ORDER_NO);
        isSkip= getIntent().getBooleanExtra(Constants.SKIP,false);
        mPresenter.getCancelProgress(no);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter=new AdvancedAdapter<>(R.layout.viewholder_cancel_progress,((holder, position, data) -> {
            TextView titleView=holder.get(R.id.title_view);
            titleView.setText(data.getTitle());
            TextView noteView=holder.get(R.id.note_view);
            noteView.setText(data.getNote());
            titleView.setTextColor(position==0?Color.BLACK:Color.parseColor("#bebebe"));
            noteView.setTextColor(position==0?Color.BLACK:Color.parseColor("#bebebe"));
            holder.<TextView>get(R.id.time_view).setText(data.getTime());
        }));
        mRecyclerView.addItemDecoration(new TimeLineDecoration(this));

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void success(List<Common> data) {
        Collections.reverse(data);
        mAdapter.addItems(data);
    }

    @Override
    public void onBackPressed() {
        if(isSkip){
            Intent intent=new Intent(this,OrdersDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity(intent);
            //super.onBackPressed();
        }else {
            super.onBackPressed();
        }
    }
}
