package com.tmmt.innersect.mvp.model;


import java.math.BigDecimal;
import java.util.List;

/**
 * Created by flame on 2017/4/13.
 */

public class OrderViewModel {

    public String userId;

    public Integer deliveryAddressId;

    public BigDecimal totalAmount;

    public Long onprid;
    //public String shop;

    public List<OrderItem> orderProductList;
    public List<Integer> pidList;
    public List<Integer> cidList;


    public void setDeliveryAddressId(int id){
        deliveryAddressId=id;
    }

}


