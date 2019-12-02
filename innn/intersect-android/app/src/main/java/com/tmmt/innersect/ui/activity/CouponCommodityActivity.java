
package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.SearchResult;
import com.tmmt.innersect.mvp.model.ShopCart;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.adapter.PopupAdapter;
import com.tmmt.innersect.utils.Util;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CouponCommodityActivity extends AppCompatActivity implements CommonView<SearchResult> {
    static final int DEFAULT = 1;
    static final int PRICE_UP = 2;
    static final int PRICE_DOWN = 3;
    static final int HOT = 4;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.up_view)
    ImageView mUpView;
    @BindView(R.id.down_view)
    ImageView mDownView;
    @BindView(R.id.default_sort)
    TextView mDefaultSort;
    @BindView(R.id.hot_sort)
    TextView mHotSort;
    @BindView(R.id.price_view)
    TextView mPriceView;
    @BindView(R.id.top_image)
    ImageView mTopImage;
    @BindView(R.id.tool_bar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.title_view)
    TextView mTitleView;
    @BindView(R.id.shop_cart_size)
    TextView mCartSize;

    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    @BindView(R.id.loading_view)
    View mLoading;
    @BindView(R.id.bottom_view)
    View mBottomView;
    CommonPresenter mPresenter;
    PopupAdapter mAdapter;

    int cid;
    int sort;
    int from;
    boolean isEnd;
    boolean isLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        from=0;
        isEnd=false;
        cid=getIntent().getIntExtra(Constants.ACTIVITY_ID,0);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_discount_list);
        ButterKnife.bind(this);
        sort = DEFAULT;
        changeView(DEFAULT);
        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) && !isEnd) {
                        if(isLoad){
                            mLoading.setVisibility(View.VISIBLE);
                        }
                        load();
                    }
                }
            });

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mAdapter=new PopupAdapter("");
        mRecyclerView.setAdapter(mAdapter);
        int count=ShopCart.getInstance().getCommodityCount();
        if(count!=0){
            mCartSize.setVisibility(View.VISIBLE);
            mCartSize.setText(String.valueOf(count));
        }
        mPresenter=new CommonPresenter();
        mPresenter.attachView(this);
        mTopImage.setVisibility(View.GONE);
        load();
    }

    public static void start(Context context, String title,int cid) {
        Intent intent = new Intent(context, CouponCommodityActivity.class);
        intent.putExtra(Constants.TITLE, title);
        intent.putExtra(Constants.ACTIVITY_ID,cid);
        context.startActivity(intent);
    }

    @OnClick(R.id.back_view)
    void back() {
        onBackPressed();
    }

    @OnClick({R.id.default_sort, R.id.price_sort, R.id.hot_sort})
    void changeSort(View view) {
        if (view.getId() == R.id.default_sort) {
            if(sort==DEFAULT){
                return;
            }
            sort = DEFAULT;
        }
        if (view.getId() == R.id.price_sort) {
            if (sort == DEFAULT || sort == HOT) {
                sort = PRICE_DOWN;
            } else if (sort == PRICE_DOWN) {
                sort = PRICE_UP;
            } else {
                sort = PRICE_DOWN;
            }
        }
        if (view.getId() == R.id.hot_sort) {
            if(sort==HOT){
                return;
            }
            sort = HOT;
        }
        changeView(sort);
        isEnd=false;
        from=0;
        load();
    }

    private void changeView(int sort) {
        if (sort == DEFAULT) {
            mDefaultSort.setTextColor(Color.BLACK);
            mHotSort.setTextColor(Color.parseColor("#BEBEBE"));
        }
        if (sort == HOT) {
            mHotSort.setTextColor(Color.BLACK);
            mDefaultSort.setTextColor(Color.parseColor("#BEBEBE"));
        }
        if (sort == HOT || sort == DEFAULT) {
            mPriceView.setTextColor(Color.parseColor("#BEBEBE"));
            mUpView.setImageResource(R.mipmap.gray_up_triangle);
            mDownView.setImageResource(R.mipmap.gray_down_triangle);
        }
        if (sort == PRICE_UP) {
            mUpView.setImageResource(R.mipmap.black_up_triangle);
            mDownView.setImageResource(R.mipmap.gray_down_triangle);
        }
        if (sort == PRICE_DOWN) {
            mUpView.setImageResource(R.mipmap.gray_up_triangle);
            mDownView.setImageResource(R.mipmap.black_down_triangle);
        }
        if (sort == PRICE_DOWN || sort == PRICE_UP) {
            mPriceView.setTextColor(Color.BLACK);
            mDefaultSort.setTextColor(Color.parseColor("#BEBEBE"));
            mHotSort.setTextColor(Color.parseColor("#BEBEBE"));
        }
    }

    @OnClick(R.id.shop_cart)
    void open(){
        Intent intent=new Intent(this,ShopCartActivity.class);
        Util.checkLogin(this,intent);
    }

    @Override
    public void success(SearchResult data) {
        mLoading.setVisibility(View.GONE);
        isLoad=true;
        if (data == null) {
            isEnd=true;
            return;
        }
        mAdapter.fillContent(data.products,from!=0);
        from+=data.size;
        if(from>=data.total){
            isEnd=true;
            mBottomView.setVisibility(View.VISIBLE);

        }
        //mTopImage.setVisibility(View.GONE);
        mTitleView.setText(getIntent().getStringExtra(Constants.TITLE));
        toolbarLayout.setTitleEnabled(false);

    }

    private void load(){
        mPresenter.getCouponProduct(cid,sort,from);
    }

    @Override
    public void failed() {
        isEnd=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }
}

