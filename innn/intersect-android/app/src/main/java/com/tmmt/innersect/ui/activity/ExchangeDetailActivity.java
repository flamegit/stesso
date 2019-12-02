package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.RefundItem;
import com.tmmt.innersect.mvp.model.RefundProgress;
import com.tmmt.innersect.ui.adapter.AdvancedAdapter;
import com.tmmt.innersect.ui.adapter.decoration.TimeLineDecoration;
import com.tmmt.innersect.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ExchangeDetailActivity extends BaseViewActivity<RefundProgress> {

    @BindView(R.id.icon_view)
    ImageView mIconView;
    @BindView(R.id.name_view)
    TextView mNameView;
    @BindView(R.id.color_view)
    TextView mColorView;
    @BindView(R.id.size_view)
    TextView mSizeView;
    @BindView(R.id.quantity_view)
    TextView mQuantityView;
    @BindView(R.id.price_view)
    TextView mPriceView;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.first_step)
    ImageView mFirstStep;
    @BindView(R.id.second_step)
    ImageView mSecondStep;

    @BindView(R.id.third_step)
    ImageView mThirdStep;

    @BindView(R.id.first_title)
    TextView mFirstTitle;

    @BindView(R.id.second_title)
    TextView mSecondTitle;

    @BindView(R.id.third_title)
    TextView mThirdTitle;

    @BindView(R.id.line_view)
    View mLine;

    @BindView(R.id.line_first)
    View mLineFirst;

    int mCurrStep;

    int mSelectedStep;

    boolean isPass;
    boolean isRefund;
    boolean isSkip;
    boolean isRefresh;

    String mAsno;

    AdvancedAdapter<RefundProgress.Progress> mAdapter;
    RefundProgress mRefundProgress;

    public static void start(Context context, boolean isRefund,String asno,boolean isSkip){
        Intent intent=new Intent(context,ExchangeDetailActivity.class);
        intent.putExtra(Constants.IS_REFUND,isRefund);
        intent.putExtra(Constants.ORDER_NO,asno);
        intent.putExtra(Constants.SKIP,isSkip);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange_detail;
    }

    @Override
    protected String getTitleString() {
        if(isRefund){
            return "退款退货详情";
        }else {
            return "换货详情";
        }
    }

    @Override
    protected void initView() {
        super.initView();
        isRefresh=false;
        isRefund=getIntent().getBooleanExtra(Constants.IS_REFUND,false);
        isSkip=getIntent().getBooleanExtra(Constants.SKIP,false);
        mAsno=getIntent().getStringExtra(Constants.ORDER_NO);
        mAdapter=new AdvancedAdapter<>(R.layout.viewholder_refund_progress,(holder, position, data) -> {
            holder.<TextView>get(R.id.title_view).setText(data.aname);
            holder.<TextView>get(R.id.title_view).setTextColor(position==0? Color.BLACK:Color.parseColor("#bebebe"));
            holder.<TextView>get(R.id.desc_view).setText(data.getDesc());
            holder.<TextView>get(R.id.desc_view).setTextColor(position==0? Color.BLACK:Color.parseColor("#bebebe"));

            TextView logistics=holder.get(R.id.logistics_view);

            if(data.se==1 || data.so==1){
                logistics.setVisibility(View.VISIBLE);
                if(data.se==1){
                    logistics.setText("查看物流");
                }else {
                    logistics.setText("查看订单");
                }
            }else {
                logistics.setVisibility(View.INVISIBLE);
            }

            logistics.setOnClickListener(v -> {
                if(data.se==1){
                    if(data.exp!=null){
                        LogisticsActivity.start(ExchangeDetailActivity.this,data.exp.name,data.exp.code,data.exp.expn);
                    }
                }else if(data.so==1){
                    Util.startActivity(ExchangeDetailActivity.this,OrdersDetailActivity.class,Constants.ORDER_NO,data.orderNo);
                }
            });

            holder.<TextView>get(R.id.tel_view).setTextColor(position==0? Color.BLACK:Color.parseColor("#bebebe"));
            holder.<TextView>get(R.id.tel_view).setText(data.sc==1?"如有疑问，请拨打电话 "+mRefundProgress.getTel():"");
            holder.get(R.id.add_logistics).setVisibility(data.issb==1?View.VISIBLE:View.GONE);
            holder.get(R.id.add_logistics).setOnClickListener(v -> {
                isRefresh=true;
                PostLogisticsActivity.start(ExchangeDetailActivity.this,mAsno);
            });
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new TimeLineDecoration(this));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter.getRefundProgress(mAsno);
    }

    @Override
    public void success(RefundProgress data) {
        mRefundProgress=data;
        if(data.order.astype==1){
            isRefresh=true;
            mTitleView.setText("退款退货详情");
        }else {
            isRefund=false;
            mTitleView.setText("换货详情");
        }
        parseData(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isRefresh){
            mPresenter.getRefundProgress(mAsno);
        }
    }

    private void parseData(RefundProgress data){
        if(data==null){
            return;
        }
        RefundItem commodity=data.order;
        if(commodity!=null){
            Util.fillImage(this,commodity.skuThumbnail,mIconView);
            mNameView.setText(commodity.skuName);
            mQuantityView.setText(commodity.getQuantity());
            mColorView.setText(commodity.skuColor);
            mSizeView.setText(commodity.skuSize);
            if(isRefund){
                mPriceView.setText(commodity.getRefundAmount());
            }
        }

        mFirstTitle.setText(data.getTitle(0));
        mSecondTitle.setText(data.getTitle(1));

        if(data.steps.size()==2){
            isPass=false;
            mCurrStep=mSelectedStep=2;
            mAdapter.addItems(reverse(data.getProgress(1)));

        }else if(data.steps.size()==3){
            isPass=true;
            mCurrStep=mSelectedStep= data.getStepProgress();
            mAdapter.addItems(reverse(data.getProgress(mCurrStep-1)));
            mThirdTitle.setText(data.getTitle(2));
        }
        setStepIcon();
    }

    @OnClick({R.id.first_layout,R.id.second_layout,R.id.third_layout})
    public void changeStep(View v) {
        int i=1;
        switch (v.getId()){
            case R.id.first_layout:
                i=1;
                break;

            case R.id.second_layout:
                i=2;
                break;

            case R.id.third_layout:
                i=3;
                break;
        }

        if(mRefundProgress!=null && mCurrStep>=i && mSelectedStep!=i){
            mSelectedStep=i;
            setStepIcon();
            List<RefundProgress.Progress> list=mRefundProgress.getProgress(i-1);
            mAdapter.addItems(reverse(list));
        }
    }

    private List<RefundProgress.Progress> reverse(List<RefundProgress.Progress> list){
        List<RefundProgress.Progress> reverseList=new ArrayList<>(list);
        Collections.reverse(reverseList);
        return reverseList;
    }

    /*
        state 1 ongoing 2 fail 3 pass
         */
    private void setStepIcon(){
        switch (mCurrStep){
            case 1:
                mFirstStep.setImageResource(R.mipmap.ongoing);
                break;
            case 2:
                mFirstStep.setImageResource(mSelectedStep==1?R.mipmap.completed:R.mipmap.completed_unselect);
                if(isPass){
                    mSecondStep.setImageResource(mSelectedStep==2?R.mipmap.ongoing:R.mipmap.ongoing_unselected);
                }else {
                    mSecondStep.setImageResource(mSelectedStep==2?R.mipmap.failed:R.mipmap.fail_unselected);
                    mLine.setVisibility(View.GONE);
                    mThirdStep.setVisibility(View.GONE);
                }
                break;
            case 3:
                mFirstStep.setImageResource(mSelectedStep==1?R.mipmap.completed:R.mipmap.completed_unselect);
                mSecondStep.setImageResource(mSelectedStep==2?R.mipmap.completed:R.mipmap.completed_unselect);
                if(mRefundProgress.isPass()){
                    mLine.setBackgroundColor(Color.parseColor("#9A9B9D"));
                    mThirdStep.setImageResource(mSelectedStep==3?R.mipmap.completed:R.mipmap.completed_unselect);
                }else {
                    mThirdStep.setImageResource(mSelectedStep==3?R.mipmap.failed:R.mipmap.fail_unselected);
                }
                break;
        }
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
