package com.tmmt.innersect.mvp.model;

/**
 * Created by flame on 2017/9/20.
 */

public class UnfoldItem extends AdapterItem {

    public int brandId;

    public UnfoldItem(int brandId){
        this.brandId=brandId;
    }

    @Override
    public int getType() {
        return AdapterItem.UNFOLD;
    }
}
