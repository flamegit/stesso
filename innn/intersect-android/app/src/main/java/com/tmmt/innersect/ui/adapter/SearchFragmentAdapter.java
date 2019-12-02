package com.tmmt.innersect.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tmmt.innersect.R;
import com.tmmt.innersect.ui.fragment.SearchFragment;
import com.tmmt.innersect.utils.Util;

/**
 * Created by flame on 2017/4/12.
 */

public class SearchFragmentAdapter extends FragmentPagerAdapter {


    SearchFragment mProductFragment;

    SearchFragment mNewsFragment;


    private static final String[]titles={"商品",Util.getString(R.string.infor)};

    public SearchFragmentAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    public void search(String key){
        if(mProductFragment==null){
            mProductFragment=SearchFragment.getInstance(0);
        }

        if(mNewsFragment==null){
            mNewsFragment=SearchFragment.getInstance(1);
        }

        mProductFragment.search(key);
        mNewsFragment.search(key);
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            if(mProductFragment==null){
                mProductFragment=SearchFragment.getInstance(position);
            }
            return mProductFragment;
        }else {
            if(mNewsFragment==null){
                mNewsFragment=SearchFragment.getInstance(position);
            }
            return mNewsFragment;
        }
    }

}
