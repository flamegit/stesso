package com.tmmt.innersect.mvp.model;

/**
 * Created by flame on 2017/9/20.
 */

public class TitleItem extends AdapterItem {

    public String mTitle;
    public String mSubTitle;

    public TitleItem(String title,String subTitle){
        mTitle=title;
        mSubTitle=subTitle;
    }

    @Override
    public int getType() {
        return AdapterItem.TITLE;
    }
}
