//package com.tmmt.innersect.ui.fragment;
//
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.datasource.net.ApiManager;
//import com.tmmt.innersect.datasource.net.NetCallback;
//import com.tmmt.innersect.mvp.model.ModelItem;
//import com.tmmt.innersect.ui.adapter.HomeAdapter;
//
//import java.util.List;
//
//import butterknife.BindView;
//
///**
// * Created by flame on 2017/4/12.
// */
//
//public class NewsFragment2 extends BaseFragment {
//
//    @BindView(R.id.recycler_view1)
//    RecyclerView mRecyclerView1;
//
//    HomeAdapter mAdapter;
//
//    @Override
//    int getLayout() {
//        return R.layout.fragment_news2;
//    }
//
//    @Override
//    protected void initView(View view) {
//        super.initView(view);
//        mAdapter=new HomeAdapter();
//        mRecyclerView1.setLayoutManager(new GridLayoutManager(getContext(),2));
//        mRecyclerView1.setAdapter(mAdapter);
//        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getHomeList().enqueue(new NetCallback<List<ModelItem>>() {
//            @Override
//            public void onSucceed(List<ModelItem> data) {
//                mAdapter.fillContent(data);
//            }
//        });
//
//    }
//}
