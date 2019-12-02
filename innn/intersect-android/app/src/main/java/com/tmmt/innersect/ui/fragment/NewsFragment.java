package com.tmmt.innersect.ui.fragment;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.tmmt.innersect.R;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Information;
import com.tmmt.innersect.mvp.model.InformationModel;
import com.tmmt.innersect.mvp.model.TopNews;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.activity.IShare;
import com.tmmt.innersect.ui.activity.InfoDetailActivity;
import com.tmmt.innersect.ui.activity.RecommendActivity;
import com.tmmt.innersect.ui.adapter.AdvancedAdapter;
import com.tmmt.innersect.ui.adapter.BaseAdapter;
import com.tmmt.innersect.ui.adapter.CommonPagerAdapter;
import com.tmmt.innersect.ui.adapter.NewsAdapter;
import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.Util;
import com.tmmt.innersect.widget.RefreshHeader;

import java.util.List;

import butterknife.BindView;

/**
 * Created by flame on 2017/4/12.
 */

public class NewsFragment extends BaseFragment implements CommonView<InformationModel> {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    AdvancedAdapter<Information> mAdapter;

    //@BindView(R.id.view_pager)
    //ViewPager mViewPager;
    //@BindView(R.id.pager_layout)
    //View mPagerLayout;
    //CommonPagerAdapter<TopNews> mPageAdaper;

    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout mRefreshLayout;
    IShare mIShare;
    CommonPresenter mPresenter;
    boolean isAppend = false;

    @Override
    int getLayout() {
        return R.layout.fragment_news;
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof IShare) {
            mIShare = (IShare) context;
        } else {
            //throw new RuntimeException(context.toString() + " must implement IShare");
        }
        super.onAttach(context);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mPresenter=new CommonPresenter();
        mPresenter.attachView(this);
        mAdapter = new AdvancedAdapter<>(R.layout.viewholder_news,((holder, position, data) -> {

            final Context context = holder.itemView.getContext();
            holder.<TextView>get(R.id.title_view).setText(data.title);
            TextView category=holder.get(R.id.category_view);
            category.setText(data.columnName);
            category.setOnClickListener(v -> RecommendActivity.start(context,data.columnId,data.columnName));
            holder.<TextView>get(R.id.time_view).setText(data.getTime());
            ImageView imageView = holder.get(R.id.info_image);
            imageView.setOnClickListener(v -> {
                InfoDetailActivity.start(context,data.id);
            });
            Util.loadImage(context,data.imgsrc,imageView);
            //Util.fillImage(context,data.imgsrc,imageView);

        }));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setNestedScrollingEnabled(false);

//        mPageAdaper=new CommonPagerAdapter<>(false,(container, data) -> {
//            View pageView = LayoutInflater.from(container.getContext()).inflate(R.layout.included_card_news, container, false);
//            TextView textView = pageView.findViewById(R.id.title_view);
//            textView.setText(data.title);
//            ImageView imageView = pageView.findViewById(R.id.image_view);
//            Util.loadImage(container.getContext(), data.pic, imageView);
//            pageView.setOnClickListener(v -> Util.openTarget(container.getContext(), data.content, " "));
//            return pageView;
//        });
//
//        mViewPager.setPageMargin(Util.dip2px(10));
//        mViewPager.setAdapter(mPageAdaper);

        mRefreshLayout.setHeaderView(new RefreshHeader(getContext()));
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mPresenter.loadContent(System.currentTimeMillis(), false,null);
                isAppend = false;
                AnalyticsUtil.reportEvent("home_pulldown_refresh");
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                long time = getTime();
                mPresenter.loadContent(time, true,null);
                isAppend = true;
                AnalyticsUtil.reportEvent("home_news_pullup_load");
            }
        });
        mPresenter.loadContent(System.currentTimeMillis(), false,null);
//        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getNewsBanner().enqueue(new NetCallback<List<TopNews>>() {
//            @Override
//            public void onSucceed(List<TopNews> data) {
//                if(data==null||data.isEmpty()){
//                    mPagerLayout.setVisibility(View.GONE);
//                }
//                mPageAdaper.addItems(data);
//
//            }
//        });
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
    public void onDetach() {
        super.onDetach();
        mIShare = null;

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
