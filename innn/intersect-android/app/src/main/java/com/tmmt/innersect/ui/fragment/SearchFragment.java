package com.tmmt.innersect.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.SearchNews;
import com.tmmt.innersect.mvp.model.SearchResult;
import com.tmmt.innersect.ui.adapter.NewsAdapter;
import com.tmmt.innersect.ui.adapter.SearchAdapter;
import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;


/**
 * Created by Administrator on 2016/10/8.
 */
public class SearchFragment extends Fragment {

    TwinklingRefreshLayout mRefreshLayout;
    SearchAdapter mSearchAdapter;
    NewsAdapter mNewsAdapter;
    RecyclerView mRecyclerView;
    View mEmptyView;
    String mKey;
    int mType;
    int mSize;
    int mPage;

    public static SearchFragment getInstance(int type) {
        SearchFragment fragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage=0;
    }

    public void search(String key) {
        mKey=key;
        search(false);
    }

    private void search(boolean append) {
        if(!append){
            mPage=0;
            mRefreshLayout.setEnableLoadmore(true);
        }
        mEmptyView.setVisibility(View.INVISIBLE);
        if (mType == 0) {
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).searchProducts(mKey, mPage*mSize).enqueue(new NetCallback<SearchResult>() {
                @Override
                public void onSucceed(SearchResult data) {
                    mSearchAdapter.fillContent(data,append);
                    mRefreshLayout.finishLoadmore();
                    mSize=data.size;
                    if(data.brands==null && data.products==null){
                        mRefreshLayout.setEnableLoadmore(false);
                        if(mPage==0){
                            mEmptyView.setVisibility(View.VISIBLE);
                        }
                    }
                    //mSize=data.size;
                    mPage++;
                }
            });
        } else {
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).searchNews(mKey, mPage*mSize).enqueue(new NetCallback<SearchNews>() {
                @Override
                public void onSucceed(SearchNews data) {
                    if(append){
                        mNewsAdapter.appendItems(data.docs);
                        mRefreshLayout.finishLoadmore();
                    }else {
                        mNewsAdapter.addItems(data.docs);
                    }
                    if(data.docs==null){
                        mRefreshLayout.setEnableLoadmore(false);
                        if(mPage==0){
                            mEmptyView.setVisibility(View.VISIBLE);
                        }
                    }
                    mSize=data.size;
                    mPage++;
                }
            });
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRefreshLayout=view.findViewById(R.id.refresh_layout);
        mEmptyView=view.findViewById(R.id.empty_view);
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                search(true);
            }
        });
        mType = getArguments().getInt(Constants.TYPE, 1);
        if (mType == 0) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
            //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mSearchAdapter = new SearchAdapter();
            mRecyclerView.setAdapter(mSearchAdapter);
        } else {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
            mNewsAdapter = new NewsAdapter(2);
            mRecyclerView.setAdapter(mNewsAdapter);
        }
        return view;
    }


}
