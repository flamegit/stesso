package com.tmmt.innersect.mvp.model;

/**
 * Created by flame on 2017/9/19.
 */

public class AdapterItem {

    public static final int TITLE=1;
    public static final int COMMODITY=2;
    public static final int BRAND=3;
    public static final int UNFOLD=4;
    public static final int LINE=5;
    public static final int END=6;
    public static final int HOT_BRAND=7;
    public static final int CATEGORY=8;
    public static final int HOT_WORD=9;
    public static final int HISTORY=10;
    public static final int EMPTY=11;


    protected int mSpanSize;
    protected int mType;

    public int setType(int type){
        return mType=type;
    }

    public int getType(){
        return mType;
    }

    public void setSpanSize(int spanSize){
        mSpanSize=spanSize;
    }

    public AdapterItem(){
        mSpanSize=2;
        mType=LINE;
    }
    public int getSpanSize(){
        return mSpanSize;
    }

}
