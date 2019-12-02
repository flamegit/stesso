package com.tmmt.innersect.ui.fragment;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tmmt.innersect.R;
import com.tmmt.innersect.ui.adapter.FragmentAdapter;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/10/8.
 */
public class CategoryFragment extends BaseFragment  {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @Override
    int getLayout() {
        return R.layout.fragment_category;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        FragmentAdapter adapter=new FragmentAdapter(getChildFragmentManager(),
                new String[]{"品牌","分类"}){

            @Override
            public Fragment getItem(int position) {
                if(position==0){
                    return new BrandListFragment();
                }
                return new CategoryListFragment();
            }
        };

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
