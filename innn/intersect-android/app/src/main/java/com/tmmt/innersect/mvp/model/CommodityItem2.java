package com.tmmt.innersect.mvp.model;

import java.util.List;

/**
 * Created by flame on 2017/9/19.
 */

public class CommodityItem2 extends BaseItem {


    public CommodityItem2() {
        mSpanSize=1;
    }
    public String schema;
    public List<ColorInfo> color;
    public String name;
    public float originalPrice;
    public long productId;
    public float salePrice;
    public String picUrl;
    public String brand;
    int status;
    public boolean isSaleOut(){
        return status==3;
    }

}
