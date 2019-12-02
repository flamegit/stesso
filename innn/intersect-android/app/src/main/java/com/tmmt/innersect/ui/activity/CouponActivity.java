package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.ui.adapter.FragmentAdapter;

import butterknife.BindView;
import butterknife.OnClick;

public class CouponActivity extends BaseActivity {

    @BindView(R.id.action_view)
    TextView mActionView;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    public static void start(Context context, boolean select){
        Intent intent=new Intent(context,CouponActivity.class);
        intent.putExtra(Constants.IS_EDIT,select);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coupon;
    }

    @Override
    protected String getTitleString() {
        return getString(R.string.coupon);
    }

    @Override
    protected void initView() {
        super.initView();
        mActionView.setText(getText(R.string.use_instruction));
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),
                new String[]{getString(R.string.available),getString(R.string.unavailable)}));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @OnClick(R.id.action_view)
    void action(){
        WebViewActivity.start(this,"https://m.innersect.net/pages/coupon_intro.html",getString(R.string.use_instruction));
    }
}


