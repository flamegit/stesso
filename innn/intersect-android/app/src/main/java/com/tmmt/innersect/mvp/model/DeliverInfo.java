package com.tmmt.innersect.mvp.model;

import java.util.List;

/**
 * Created by flame on 2017/8/17.
 */

public class DeliverInfo {

    public String shippingCom;
    public String shippingNo;
    int shippingState;
    public List<ShopingInfo> shippingInfo;
    public static class ShopingInfo{
        String ftime;
        public String context;
        public String time;
    }

}

