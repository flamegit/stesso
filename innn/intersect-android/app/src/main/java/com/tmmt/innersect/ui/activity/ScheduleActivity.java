//package com.tmmt.innersect.ui.activity;
//
//
//import android.content.Intent;
//import android.os.Build;
//import android.support.design.widget.TabLayout;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.mvp.model.ShopCart;
//import com.tmmt.innersect.mvp.presenter.ShopCartPresenter;
//import com.tmmt.innersect.mvp.view.ShopCartView;
//import com.tmmt.innersect.ui.adapter.FragmentAdapter;
//import com.tmmt.innersect.utils.Util;
//
//import java.lang.reflect.Field;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class ScheduleActivity extends BaseActivity implements ShopCartView{
//
//    @BindView(R.id.view_pager)
//    ViewPager mViewPager;
//    @BindView(R.id.tab_layout)
//    TabLayout mTabLayout;
//
//    @BindView(R.id.shop_cart_size)
//    TextView mCartSizeView;
//
//    ShopCartPresenter mPresenter;
//
//    @Override
//    protected void initView() {
//        super.initView();
//
//        mPresenter=new ShopCartPresenter();
//        mPresenter.attachView(this);
//        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
//        mTabLayout.setupWithViewPager(mViewPager);
//        //setUpIndicatorWidth();
//        mPresenter.loadShopCart();
//    }
//
//    @OnClick(R.id.shop_cart)
//    void openCart(){
//        Intent intent=new Intent(this,ShopCartActivity.class);
//        Util.checkLogin(this,intent);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        setCartSize();
//    }
//
//    @Override
//    public void fillCart() {
//        setCartSize();
//    }
//
//    private void setCartSize() {
//        ShopCart shopCart = ShopCart.getInstance();
//        int size = shopCart.getCommodityCount();
//        if(size>0){
//            mCartSizeView.setVisibility(View.VISIBLE);
//            mCartSizeView.setText(String.valueOf(size));
//        }else {
//            mCartSizeView.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mPresenter.onDestory();
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_schedule;
//    }
//
//    private void setUpIndicatorWidth() {
//        Class<?> tabLayoutClass = mTabLayout.getClass();
//        Field tabStrip = null;
//        try {
//            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
//            tabStrip.setAccessible(true);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//        LinearLayout layout = null;
//        try {
//            if (tabStrip != null) {
//                layout = (LinearLayout) tabStrip.get(mTabLayout);
//            }
//            for (int i = 0; i < layout.getChildCount(); i++) {
//                View child = layout.getChildAt(i);
//                child.setPadding(0, 0, 0, 0);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    params.setMarginStart(Util.dip2px(30));
//                    params.setMarginEnd(Util.dip2px(30));
//                }
//                child.setLayoutParams(params);
//                child.invalidate();
//            }
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//
//
//
//
//
