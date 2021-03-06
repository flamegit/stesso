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

import com.bumptech.glide.Glide;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.BrandInfo;
import com.tmmt.innersect.mvp.model.ShopCart;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.adapter.PopupAdapter;
import com.tmmt.innersect.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommodityListActivity extends AppCompatActivity implements CommonView<BrandInfo> {

    static final String DEFAULT="1";
    static final String PRICE_UP="2";
    static final String PRICE_DOWN="3";
    static final String HOT="4";
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
    @BindView(R.id.logo_view)
    ImageView mLogoView;
    @BindView(R.id.desc_view)
    TextView mDescView;
    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;

    @BindView(R.id.loading_view)
    View mLoading;

    @BindView(R.id.bottom_view)
    View mBottomView;
    @BindView(R.id.shop_cart_size)
    TextView mCartSize;

    CommonPresenter mPresenter;
    PopupAdapter mAdapter;
    String mSort;
    String mAid;
    //String mPid;
    long mBrandId;
    boolean isPopup;
    int mIndex;
    boolean isEnd;
    boolean isLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndex=0;
        isEnd=false;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_commodity_list);
        ButterKnife.bind(this);
        mAid=getIntent().getStringExtra(Constants.ACTIVITY_ID);
        mBrandId=getIntent().getLongExtra(Constants.BRAND_ID,1);
        isPopup=getIntent().getBooleanExtra(Constants.IS_POPUP,true);
        mSort=DEFAULT;
        changeView(DEFAULT);
        mAdapter=new PopupAdapter(mAid);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mPresenter=new CommonPresenter();
        mPresenter.attachView(this);

        if(!isPopup){
            mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) && !isEnd) {
                        if(isLoad){
                            mLoading.setVisibility(View.VISIBLE);
                            loadProduct();
                        }
                    }
                }
            });
        }
        int count= ShopCart.getInstance().getCommodityCount();
        if(count!=0){
            mCartSize.setVisibility(View.VISIBLE);
            mCartSize.setText(String.valueOf(count));
        }
        loadProduct();
    }

    @OnClick(R.id.shop_cart)
    void open(){
        Intent intent=new Intent(this,ShopCartActivity.class);
        Util.checkLogin(this,intent);
    }

    public static void start(Context context, String aid, long brandId){
        Intent intent=new Intent(context,CommodityListActivity.class);
        intent.putExtra(Constants.ACTIVITY_ID,aid);
        intent.putExtra(Constants.BRAND_ID,brandId);
        context.startActivity(intent);
    }

    @OnClick(R.id.back_view)
    void back(){
        onBackPressed();
    }

    @OnClick({R.id.default_sort,R.id.price_sort,R.id.hot_sort})
    void changeSort(View view){
        if(view.getId()==R.id.default_sort){
            if(mSort==DEFAULT){
                return;
            }
            mSort=DEFAULT;
        }
        if(view.getId()==R.id.price_sort){
            if(mSort==DEFAULT||mSort==HOT){
                mSort=PRICE_DOWN;
            }else if(mSort==PRICE_DOWN){
                mSort=PRICE_UP;
            }else {
                mSort=PRICE_DOWN;
            }
        }
        if(view.getId()==R.id.hot_sort){
            if(mSort==HOT){
                return;
            }
            mSort=HOT;
        }
        changeView(mSort);
        mIndex=0;
        isEnd=false;
        loadProduct();
    }

    private void changeView(String sort){
        if(sort==DEFAULT){
            mDefaultSort.setTextColor(Color.BLACK);
            mHotSort.setTextColor(Color.parseColor("#BEBEBE"));
        }
        if(sort==HOT){
            mHotSort.setTextColor(Color.BLACK);
            mDefaultSort.setTextColor(Color.parseColor("#BEBEBE"));
        }
        if(sort==HOT||sort==DEFAULT){
            mPriceView.setTextColor(Color.parseColor("#BEBEBE"));
            mUpView.setImageResource(R.mipmap.gray_up_triangle);
            mDownView.setImageResource(R.mipmap.gray_down_triangle);
        }
        if(sort==PRICE_UP){
            mUpView.setImageResource(R.mipmap.black_up_triangle);
            mDownView.setImageResource(R.mipmap.gray_down_triangle);
        }
        if(sort==PRICE_DOWN){
            mUpView.setImageResource(R.mipmap.gray_up_triangle);
            mDownView.setImageResource(R.mipmap.black_down_triangle);
        }

        if(sort==PRICE_DOWN||sort==PRICE_UP){
            mPriceView.setTextColor(Color.BLACK);
            mDefaultSort.setTextColor(Color.parseColor("#BEBEBE"));
            mHotSort.setTextColor(Color.parseColor("#BEBEBE"));
        }
    }

    @Override
    public void success(final BrandInfo data) {
        mLoading.setVisibility(View.GONE);
        isLoad=true;

        if(data==null){
            isEnd=true;
            return;
        }

        Glide.with(this).load(data.bgUrl).into(mTopImage);
        Glide.with(this).load(data.logoUrl).into(mLogoView);
        mDescView.setText(data.desc);
        if(data.products==null || data.products.size()<10){
            isEnd=true;
            if(mIndex!=0 || data.products.size()>0) {
                mBottomView.setVisibility(View.VISIBLE);
            }
        }
        mAdapter.fillContent(data,mIndex!=0);
        mIndex+=10;
        if(data.name!=null){
            toolbarLayout.setTitle(data.name);
        }
//        mMoreView.setOnClickListener(
//           v-> {
//                if(mAid!=null){
//                    InfoDetailActivity.start(CommodityListActivity.this,data.articleId,data.logoUrl);
//                }
//            }
//        );
    }

    private void loadProduct(){
        isLoad=false;
        if(isPopup){
            if(mAid==null){
                //mMoreView.setVisibility(View.GONE);
            }
            mPresenter.getProductList(mAid,mBrandId,mSort);
        }else {
            //mMoreView.setVisibility(View.GONE);
            mPresenter.getCommodityList(mAid,mSort,mBrandId,mIndex);
        }
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
