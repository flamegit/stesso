//package com.tmmt.innersect.ui.fragment;
//
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.mvp.model.ExhibitionInfo;
//
//import com.tmmt.innersect.mvp.presenter.CommonPresenter;
//import com.tmmt.innersect.mvp.view.CommonView;
//import com.tmmt.innersect.ui.adapter.ExhibitionAdapter;
//
//
//import butterknife.BindView;
//
///**
// * Created by flame on 2017/4/12.
// */
//
//public class ExhibitionFragment extends BaseFragment implements CommonView<ExhibitionInfo> {
//
//    @BindView(R.id.recycler_view)
//    RecyclerView mRecyclerView;
//    ExhibitionAdapter mAdapter;
//    CommonPresenter mPresenter;
//
//    @Override
//    int getLayout() {
//        return R.layout.fragment_recyclerview;
//    }
//
//    @Override
//    protected void initView(View view) {
//        super.initView(view);
//        mPresenter=new CommonPresenter();
//        mPresenter.attachView(this);
//        mAdapter = new ExhibitionAdapter();
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        //mPresenter.loadExhibitionInfo();
//    }
//
//    @Override
//    public void success(ExhibitionInfo data) {
//        mAdapter.addItems(data.getList());
//    }
//
//    @Override
//    public void failed() {}
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mPresenter.onDestory();
//    }
//}
