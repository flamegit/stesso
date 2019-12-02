package com.tmmt.innersect.mvp.model;

import java.math.BigDecimal;

/**
 * Created by flame on 2017/9/13.
 */

public class SettlementInfo {

    public String totalPrice;
    public String discount;
    public String transport;
    public boolean showHint;

    public String hint;

    public boolean isMinus;

    public SettlementInfo(String price,String discount,String transport){
        this.totalPrice=price;
        this.discount=discount;
        this.transport=transport;
        showHint=false;
    }

    public BigDecimal getTotalPrice(BigDecimal price){
        BigDecimal total= price.add(new BigDecimal(transport).subtract(new BigDecimal(discount)));
        if(total.floatValue()<0){
            isMinus=true;
            if(Float.valueOf(transport)>0){
                return new BigDecimal(transport);
            }
            return new BigDecimal("0.1");
        }
        return total;
    }
}
