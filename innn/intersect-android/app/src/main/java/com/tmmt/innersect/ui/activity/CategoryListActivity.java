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

public class CategoryListActivity extends BaseActivity  implements CommonView<List<CommonData>>{

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    BrandListAdapter mAdapter;
    String mTitle;
    CommonPresenter mPresenter;

    @Override
    protected void initView() {
        super.initView();
        mAdapter=new BrandListAdapter(2,1);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mPresenter=new CommonPresenter();
        mPresenter.attachView(this);
        mPresenter.getCategoryList();
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
        return getString(R.string.category_list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }
}
