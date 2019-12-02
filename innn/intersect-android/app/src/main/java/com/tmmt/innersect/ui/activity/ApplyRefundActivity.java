package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Address;
import com.tmmt.innersect.mvp.model.Commodity;
import com.tmmt.innersect.mvp.model.Problem;
import com.tmmt.innersect.mvp.model.RefundInfo;
import com.tmmt.innersect.ui.adapter.AdvancedAdapter;

import butterknife.BindView;
import butterknife.OnClick;

public class ApplyRefundActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    AdvancedAdapter<Problem> mAdapter;

    Commodity mCommodity;
    Address mAddress;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_refund;
    }

    public static void start(Context context,Commodity commodity, Address address){
        Intent intent=new Intent(context,ApplyRefundActivity.class);
        intent.putExtra(Constants.ADDRESS,address);
        intent.putExtra(Constants.COMMODITY,commodity);
        context.startActivity(intent);
    }

    @Override
    protected String getTitleString() {
        return "申请售后";
    }

    @Override
    protected void initView() {
        super.initView();

        mCommodity=getIntent().getParcelableExtra(Constants.COMMODITY);
        mAddress=getIntent().getParcelableExtra(Constants.ADDRESS);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AdvancedAdapter<>(R.layout.viewholder_problem,
                (holder, position, data) -> {
                    holder.<TextView>get(R.id.title_view).setText(data.title);
                    holder.<TextView>get(R.id.content_view).setText(data.note);
                    final View contentView = holder.get(R.id.content_view);
                    final ImageView arrowView = holder.get(R.id.arrow_view);
                    holder.get(R.id.title_layout).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (contentView.getVisibility() == View.GONE) {
                                contentView.setVisibility(View.VISIBLE);
                                arrowView.setImageResource(R.mipmap.black_up_arrow);
                            } else {
                                contentView.setVisibility(View.GONE);
                                arrowView.setImageResource(R.mipmap.gray_down_arrow);
                            }
                        }
                    });
                });

        mRecyclerView.setAdapter(mAdapter);

        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getRefundInfo().enqueue(new NetCallback<RefundInfo>() {
            @Override
            public void onSucceed(RefundInfo data) {
               mAdapter.addItems(data.faqs);
            }
        });
    }


    @OnClick({R.id.exchange_view,R.id.refund_view})
    void exchange(View view){
        if(view.getId()==R.id.exchange_view){
            ExchangeCommodityActivity.start(this,false,mCommodity,mAddress);
        }else {
            ExchangeCommodityActivity.start(this,true,mCommodity,mAddress);
        }
    }

}
