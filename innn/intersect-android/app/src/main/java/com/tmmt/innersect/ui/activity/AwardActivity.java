package com.tmmt.innersect.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tmmt.innersect.R;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.LotteryInfo;
import com.tmmt.innersect.mvp.model.PrizeInfo;
import com.tmmt.innersect.ui.adapter.DataBindingAdapter;
import com.tmmt.innersect.ui.adapter.decoration.LinearOffsetItemDecoration;

import java.util.List;

import butterknife.BindView;

public class AwardActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.empty_view)
    View mEmptyView;

    DataBindingAdapter<LotteryInfo> mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_common;
    }

    @Override
    protected String getTitleString() {
        return getString(R.string.my_award);
    }

    @Override
    protected void initView() {
        super.initView();
        mAdapter = new DataBindingAdapter<>(R.layout.viewholder_lottery, (holder, position, data) ->
        {
            if(data.isActive()){
                if(data.prizeType==12){
                    ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getPrizeInfo(data.id).enqueue(new NetCallback<PrizeInfo>() {
                        @Override
                        public void onSucceed(PrizeInfo info) {
                            CommodityDetailActivity.start(AwardActivity.this,info.onpr.productId,data.prizeId,info.onpr.wprice);
                        }
                    });
                }else {
                    data.openTarget(AwardActivity.this);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LinearOffsetItemDecoration(26));
        recyclerView.setAdapter(mAdapter);
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE)
                .getLotteryList(1, 100).enqueue(new NetCallback<List<LotteryInfo>>() {
            @Override
            public void onSucceed(List<LotteryInfo> data) {
                if(data==null ||data.isEmpty()){
                    mEmptyView.setVisibility(View.VISIBLE);
                }else {
                    mAdapter.addItems(data);
                }
            }
        });
    }


}
