package com.tmmt.innersect.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.PopupInfo;
import com.tmmt.innersect.mvp.model.ShopCart;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.presenter.ShopCartPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.mvp.view.ExtraView;
import com.tmmt.innersect.ui.adapter.PopupAdapter;
import com.tmmt.innersect.utils.Util;
import com.tmmt.innersect.widget.RefreshHeader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PopupActivity extends AppCompatActivity implements CommonView<PopupInfo>,ExtraView{

    @BindView(R.id.day_view)
    TextView mDayView;
    @BindView(R.id.hour_view)
    TextView mHourView;
    @BindView(R.id.minute_view)
    TextView mMinuteView;
    @BindView(R.id.second_view)
    TextView mSecondView;
    @BindView(R.id.top_image)
    ImageView mTopImage;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.title_view)
    TextView mTitleView;
    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    @BindView(R.id.top_bar)
    View mTopBar;
    @BindView(R.id.shop_cart_size)
    TextView mCartSizeView;
    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout mRefreshLayout;
    CountDownTimer mTimer;
    PopupAdapter mAdapter;
    CommonPresenter mPresenter;
    ShopCartPresenter mCartPresenter;
    String mAid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        ButterKnife.bind(this);
        mAid=getIntent().getStringExtra(Constants.INFO_ID);
        mPresenter=new CommonPresenter();
        mPresenter.attachView(this);
        mCartPresenter=new ShopCartPresenter();
        mCartPresenter.attachView(this);
        mAdapter=new PopupAdapter(mAid);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.finishRefreshing();
                    }
                },500);
            }
        });
        mRefreshLayout.setHeaderView(new RefreshHeader(this));
        mRecyclerView.setNestedScrollingEnabled(false);
        mPresenter.getPopInfo(String.valueOf(mAid));

        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float h=Util.dip2px(200);
                float alpha=scrollY/h;
                mTopBar.setAlpha(alpha);
                mTitleView.setAlpha(alpha);
            }
        });

        mCartPresenter.loadShopCart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartSize();
    }

    @OnClick(R.id.back_view)
    void back(){
        onBackPressed();
    }

    @OnClick(R.id.shop_cart)
    void openCart(){
        Intent intent=new Intent(this,ShopCartActivity.class);
        Util.checkLogin(this,intent);
    }

    @Override
    public void success(PopupInfo data) {
        fillView(data);
    }


    @Override
    public void onSuccess(Object data) {
        setCartSize();
    }

    @Override
    public void onFailure(int code) {}

    private void setCartSize() {
        ShopCart shopCart = ShopCart.getInstance();
        int size = shopCart.getCommodityCount();
        if(size>0){
            mCartSizeView.setVisibility(View.VISIBLE);
            mCartSizeView.setText(String.valueOf(size));
        }else {
            mCartSizeView.setVisibility(View.GONE);
        }
    }

    private void fillView(final PopupInfo data){
        if(data==null){
            return;
        }
        mTitleView.setText(data.activity.title);
        Glide.with(this).load(data.activity.thumbnail).into(mTopImage);
        mTopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDetailActivity.start(PopupActivity.this,data.activity.articleId);
            }
        });
        long end=data.activity.et;
        long curr=System.currentTimeMillis();
        if(end>curr){
            mTimer=new CountDownTimer(end-curr,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long total=millisUntilFinished/1000;
                    long s=total%60;
                    long min=total/60;
                    long m=min%60;
                    long hour=min/60;
                    long h=hour%24;
                    long d=hour/24;
                    mDayView.setText(String.format("%02d",d));
                    mHourView.setText(String.format("%02d",h));
                    mMinuteView.setText(String.format("%02d",m));
                    mSecondView.setText(String.format("%02d",s));
                }
                @Override
                public void onFinish() {}
            };
            mTimer.start();
        }
        mAdapter.fillContent(data);
    }

    @Override
    public void failed() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTimer!=null){
            mTimer.cancel();
        }
        mPresenter.onDestory();
        mCartPresenter.onDestory();
    }
}
