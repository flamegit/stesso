package com.tmmt.innersect.mvp.model;

import java.util.List;

/**
 * Created by flame on 2017/8/24.
 */

public class DiscountInfo {
    public float deliveryAmount;
    public String ticketOrderTips;
    public List<Discount> discountList;

    public static class Discount{
        public int discounts;
        public int relateId;
        String aliasName;
    }

}
