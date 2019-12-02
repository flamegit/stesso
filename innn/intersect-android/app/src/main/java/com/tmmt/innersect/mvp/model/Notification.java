package com.tmmt.innersect.mvp.model;


import android.content.Context;

import com.tmmt.innersect.ui.activity.ExchangeDetailActivity;
import com.tmmt.innersect.ui.activity.OrdersDetailActivity;
import com.tmmt.innersect.utils.Util;

import java.util.List;

/**
 * Created by flame on 2017/7/24.
 */

public class Notification {

    String asno;
    long ct;
    public String msg;
    int quantity;
    public String title;
    String type;
    String orderNo;
    List<Product> products;
    public boolean read;

    public String getThumbnail(){
        if(products!=null && !products.isEmpty()){
            return products.get(0).skuThumbnail;
        }
        return "no";
    }

    static class Product{
        String skuThumbnail;
    }

    public String getCount(){
        return quantity+"件商品";
    }

    public void jump(Context context){
        if("order".equals(type)){
            Util.startActivity(context,OrdersDetailActivity.class,"orderNo",orderNo);
        }else if("asale".equals(type)){
            ExchangeDetailActivity.start(context,false,asno,false);
        }else if("cust".equals(type)){
           Util.startMoor(context);
        }
    }

    public String getTime(){
        return Util.getFormatDate(ct,"yyyy.MM.dd HH:mm");
    }









}



