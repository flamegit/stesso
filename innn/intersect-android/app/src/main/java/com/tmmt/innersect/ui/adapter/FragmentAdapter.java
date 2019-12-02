package com.tmmt.innersect.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tmmt.innersect.ui.fragment.CouponFragment;

/**
 * Created by flame on 2017/4/12.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    //private static final String[]titles={Util.getString(R.string.available),Util.getString(R.string.unavailable)};

    private String[] mTitles;

    public FragmentAdapter(FragmentManager fragmentManager,String[] titles){
        super(fragmentManager);
        mTitles=titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return CouponFragment.getInstance(true);
        }else {
            return CouponFragment.getInstance(false);
        }
    }

}
