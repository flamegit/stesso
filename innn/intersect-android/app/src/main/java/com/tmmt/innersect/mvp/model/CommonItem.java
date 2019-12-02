package com.tmmt.innersect.mvp.model;

import java.util.List;

/**
 * Created by flame on 2017/11/24.
 */

public class CommonItem {

    public String schema;
    public String picUrl;
    public int productId;
    public float originalPrice;
    public float salePrice;
    public String name;
    public int type;
    public String brand;
    public long startAt;
    public long endAt;
    public String bgUrl;
    public List<CommodityItem2> plist;
    public String headerText;
    public String headerSubText;
    public String headerBgUrl;
    int status;
    public boolean isSaleOut(){
        return status!=2;
    }

}
