package com.tmmt.innersect.mvp.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by flame on 2017/12/19.
 */

public class AwardCommodity {

    public float discounts;
    public Long onprid;
    public List<ShopItem> skus;

    public BigDecimal getTotalPrice(){
        BigDecimal price = new BigDecimal(0);
        if(skus!=null){
            for (ShopItem item : skus) {
                price = price.add(new BigDecimal(String.valueOf(item.price)).multiply(new BigDecimal(item.quantity)));
            }
        }
        return price;
    }
}
