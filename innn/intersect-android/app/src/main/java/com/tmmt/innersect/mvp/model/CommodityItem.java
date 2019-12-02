package com.tmmt.innersect.mvp.model;

import java.util.List;

/**
 * Created by flame on 2017/9/19.
 */

public class CommodityItem extends AdapterItem {

    private boolean isSpan;

    public CommodityItem() {
        mSpanSize=1;
        isSpan=false;
    }
    public String brandName;
    public List<ColorInfo> colorValList;
    public String name;
    public float originalPrice;
    public long productId;
    public float salePrice;
    public String thumbnail;
    public String thumbnail2;
    int status;
    public int colorValId;

    public boolean isSaleOut(){
        return status==3;
    }

    public String getThumbnail(){
        return isSpan ? thumbnail2 : thumbnail;
    }

    public void setSpan(boolean span){
        isSpan=span;
        if(span){
            setSpanSize(2);
        }
    }

    @Override
    public int getType() {
        return AdapterItem.COMMODITY;
    }
}
