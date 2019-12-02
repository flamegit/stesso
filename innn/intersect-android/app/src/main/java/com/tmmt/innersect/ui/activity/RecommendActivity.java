package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.Information;
import com.tmmt.innersect.mvp.model.InformationModel;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.adapter.AdvancedAdapter;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.Util;
import com.tmmt.innersect.widget.RefreshHeader;

import butterknife.BindView;

public class RecommendActivity extends BaseActivity implements CommonView<InformationModel> {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    AdvancedAdapter<Information> mAdapter;

    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout mRefreshLayout;
    CommonPresenter mPresenter;
    boolean isAppend = false;
    Integer mColumnId;
    public static void start(Context context, int columnId,String title){
        Intent intent=new Intent(context,RecommendActivity.class);
        intent.putExtra(Constants.ACTIVITY_ID,columnId);
        intent.putExtra(Constants.TITLE,title);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();

        mColumnId=getIntent().getIntExtra(Constants.ACTIVITY_ID,0);
        mPresenter=new CommonPresenter();
        mPresenter.attachView(this);
        mAdapter = new AdvancedAdapter<>(R.layout.viewholder_news,((holder, position, data) -> {

            final Context context = holder.itemView.getContext();
            holder.<TextView>get(R.id.title_view).setText(data.title);
            holder.<TextView>get(R.id.category_view).setVisibility(View.GONE);
            holder.<TextView>get(R.id.time_view).setText(data.getTime());
            ImageView imageView = holder.get(R.id.info_image);

            imageView.setOnClickListener(v -> {
                InfoDetailActivity.start(context,data.id);
            });
            Util.loadImage(context,data.imgsrc,imageView);

        }));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(false);

        mRefreshLayout.setHeaderView(new RefreshHeader(this));
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mPresenter.loadContent(System.currentTimeMillis(), false,mColumnId);
                isAppend = false;
                AnalyticsUtil.reportEvent("home_pulldown_refresh");
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                long time = getTime();
                mPresenter.loadContent(time, true,mColumnId);
                isAppend = true;
                AnalyticsUtil.reportEvent("home_news_pullup_load");
            }
        });
        mPresenter.loadContent(System.currentTimeMillis(), false,mColumnId);
    }

    @Override
    protected int getLayoutId() {
       return R.layout.activity_recommend;
    }

    @Override
    protected String getTitleString() {
        return getIntent().getStringExtra(Constants.TITLE);
    }

    private long getTime(){
        int c=mAdapter.getItemCount();
        Information data=mAdapter.getItem(c-1);
        if(data==null){
            return System.currentTimeMillis();
        }else {
            return data.ptime;
        }
    }

    @Override
    public void success(InformationModel data) {
        if (isAppend) {
            mRefreshLayout.finishLoadmore();
        } else {
            mRefreshLayout.finishRefreshing();
        }
        if (data != null && data.code == 200) {
            InformationModel.Data info=data.data;
            if (info.isEnd == 1) {
                //mAdapter.setShowFooter();
                mRefreshLayout.setEnableLoadmore(false);
            }
            if (isAppend) {
                mAdapter.appendItems(info.retvalList);
            } else {
                mAdapter.addItems(info.retvalList);
            }
        }
    }

    @Override
    public void failed() {
        if (isAppend) {
            mRefreshLayout.finishLoadmore();
        } else {
            mRefreshLayout.finishRefreshing();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }
}

