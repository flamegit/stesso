//package com.tmmt.innersect.ui.fragment;
//
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//
//import com.socks.library.KLog;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.mvp.model.ShopItem;
//import com.tmmt.innersect.ui.adapter.CommonAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//
//
///**
// * Created by flame on 2017/4/12.
// */
//
//public class FashionListFragment extends BaseFragment {
//
//
//
//    @BindView(R.id.recycler_view)
//    RecyclerView mRecyclerView;
//    CommonAdapter<ShopItem> mAdapter;
//
//    @Override
//    int getLayout() {
//        return R.layout.fragment_recycler_view;
//
//    }
//
//    @Override
//    protected void initView(View view) {
//
//        mAdapter=new CommonAdapter<>(CommonAdapter.SHOP_BROWSE_TYPE,R.layout.viewholder_shop_cart);
//        List<ShopItem> items=new ArrayList<>();
//        setHasOptionsMenu(true);
//        for(int i=0;i<5;i++){
//            items.add(new ShopItem());
//        }
//        mAdapter.addItems(items);
//        mRecyclerView.setAdapter(mAdapter);
////        mRecyclerView.addItemDecoration(new SectionDecoration(getContext(),new SectionDecoration.DecorationCallback(){
////            @Override
////            public boolean isFirstInGroup(int pos) {
////                return pos%2==0;
////            }
////
////            @Override
////            public String getSectionHeader(int pos) {
////                return ""+pos;
////            }
////        }));
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//    }
//
//    @Override
//    public void onResume() {
//        KLog.i("Fashion -onresume-statscroll");
//        super.onResume();
//
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//    }
//
//}
