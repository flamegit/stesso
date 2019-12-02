package com.tmmt.innersect.ui.fragment;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.CommonData;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.adapter.AdvancedAdapter;
import com.tmmt.innersect.utils.Util;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/10/8.
 */
public class CategoryListFragment extends BaseFragment implements CommonView<List<CommonData>> {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    AdvancedAdapter<CommonData> mAdapter;
    CommonPresenter mPresenter;


    @Override
    int getLayout() {
        return R.layout.fragment_category_list;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        mAdapter=new AdvancedAdapter<>(R.layout.viewholder_category_item,((holder, position, data) -> {
            final Context context=holder.itemView.getContext();
            ImageView imageView=holder.get(R.id.icon_view);
            Glide.with(context).load(data.cover).into(imageView);
            holder.itemView.setOnClickListener(v -> Util.openTarget(context,data.schema,""));
        }));

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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



}
