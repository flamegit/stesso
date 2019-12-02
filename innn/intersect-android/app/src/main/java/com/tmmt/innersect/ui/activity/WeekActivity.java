//package com.tmmt.innersect.ui.activity;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.mvp.model.CommodityItem;
//import com.tmmt.innersect.mvp.presenter.CommonPresenter;
//import com.tmmt.innersect.mvp.view.CommonView;
//import com.tmmt.innersect.ui.adapter.PopupAdapter;
//import com.tmmt.innersect.utils.Util;
//import java.util.List;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class WeekActivity extends AppCompatActivity implements CommonView<List<CommodityItem>> {
//
//    static final String DEFAULT = "1";
//    static final String PRICE_UP = "2";
//    static final String PRICE_DOWN = "3";
//    static final String HOT = "4";
//
//    @BindView(R.id.recycler_view)
//    RecyclerView mRecyclerView;
//    @BindView(R.id.up_view)
//    ImageView mUpView;
//    @BindView(R.id.down_view)
//    ImageView mDownView;
//    @BindView(R.id.default_sort)
//    TextView mDefaultSort;
//    @BindView(R.id.hot_sort)
//    TextView mHotSort;
//    @BindView(R.id.price_view)
//    TextView mPriceView;
//
//    CommonPresenter mPresenter;
//    PopupAdapter mAdapter;
//    String mSort;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_week);
//        ButterKnife.bind(this);
//        mSort = DEFAULT;
//        changeView(DEFAULT);
//        mAdapter = new PopupAdapter("");
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        mPresenter = new CommonPresenter();
//        mPresenter.attachView(this);
//        mPresenter.getWeekList(mSort);
//    }
//
//    @OnClick(R.id.back_view)
//    void back() {
//        onBackPressed();
//    }
//
//    @OnClick({R.id.default_sort, R.id.price_sort, R.id.hot_sort})
//    void changeSort(View view) {
//        if (view.getId() == R.id.default_sort) {
//            mSort = DEFAULT;
//        }
//        if (view.getId() == R.id.price_sort) {
//            if (mSort == DEFAULT || mSort == HOT) {
//                mSort = PRICE_DOWN;
//            } else if (mSort == PRICE_DOWN) {
//                mSort = PRICE_UP;
//            } else {
//                mSort = PRICE_DOWN;
//            }
//        }
//        if (view.getId() == R.id.hot_sort) {
//            mSort = HOT;
//        }
//        changeView(mSort);
//        mPresenter.getWeekList(mSort);
//
//    }
//
//    private void changeView(String sort) {
//        if (sort == DEFAULT) {
//            mDefaultSort.setTextColor(Color.BLACK);
//            mHotSort.setTextColor(Color.parseColor("#BEBEBE"));
//        }
//        if (sort == HOT) {
//            mHotSort.setTextColor(Color.BLACK);
//            mDefaultSort.setTextColor(Color.parseColor("#BEBEBE"));
//        }
//        if (sort == HOT || sort == DEFAULT) {
//            mPriceView.setTextColor(Color.parseColor("#BEBEBE"));
//            mUpView.setImageResource(R.mipmap.gray_up_triangle);
//            mDownView.setImageResource(R.mipmap.gray_down_triangle);
//        }
//        if (sort == PRICE_UP) {
//            mUpView.setImageResource(R.mipmap.black_up_triangle);
//            mDownView.setImageResource(R.mipmap.gray_down_triangle);
//        }
//        if (sort == PRICE_DOWN) {
//            mUpView.setImageResource(R.mipmap.gray_up_triangle);
//            mDownView.setImageResource(R.mipmap.black_down_triangle);
//        }
//
//        if (sort == PRICE_DOWN || sort == PRICE_UP) {
//            mPriceView.setTextColor(Color.BLACK);
//            mDefaultSort.setTextColor(Color.parseColor("#BEBEBE"));
//            mHotSort.setTextColor(Color.parseColor("#BEBEBE"));
//        }
//    }
//
//    @OnClick(R.id.shop_cart)
//    void open(){
//        Intent intent=new Intent(this,ShopCartActivity.class);
//        Util.checkLogin(this,intent);
//    }
//
//    @Override
//    public void success(List<CommodityItem> data) {
//        if (data == null) {
//            return;
//        }
//        mAdapter.fillContent(data);
//        mRecyclerView.scrollTo(0,0);
//    }
//
//    @Override
//    public void failed() {}
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mPresenter.onDestory();
//    }
//}
