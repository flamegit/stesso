package com.tmmt.innersect.mvp.model;

/**
 * Created by flame on 2017/9/19.
 */

public class BaseItem {



    protected String[] types={
            "weekly","choice","brands","popup","discounts_title","discounts","show_all","lottery"
    };

    protected int mSpanSize;
    protected String type;

    public void setSpanSize(int spanSize){
        mSpanSize=spanSize;
    }

    public BaseItem(){
        mSpanSize=3;
    }

    public int getSpanSize(){
        return mSpanSize;
    }

    public int getType(){
        for(int i=0;i<types.length;i++){
            if(types[i].equals(type)){
                return i;
            }
        }
        return 1000;
    }
}
