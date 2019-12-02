package com.tmmt.innersect.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.CommonData;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.adapter.BrandListAdapter;

import java.util.List;

import butterknife.BindView;

public class BrandListActivity extends BaseActivity  implements CommonView<List<CommonData>>{

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    BrandListAdapter mAdapter;
    String mTitle;
    CommonPresenter mPresenter;

    @Override
    protected void initView() {
        super.initView();
        mAdapter=new BrandListAdapter(3,0);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mPresenter=new CommonPresenter();
        mPresenter.attachView(this);
        mPresenter.getBrandList();
    }

    @Override
    public void success(List<CommonData> data) {
        mAdapter.addItems(data);
    }

    @Override
    public void failed() {}

    @Override
    protected int getLayoutId() {
        return R.layout.activity_brand_list;
    }

    @Override
    protected String getTitleString() {
        if(mTitle!=null){
            return mTitle;
        }
        return getString(R.string.brand_list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }
}
