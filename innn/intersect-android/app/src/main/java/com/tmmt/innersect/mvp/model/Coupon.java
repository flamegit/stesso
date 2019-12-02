package com.tmmt.innersect.mvp.model;


import com.tmmt.innersect.utils.Util;

/**
 * Created by flame on 2017/11/6.
 */

public class Coupon {
    public int cid;
    public String couponDesc;
    int ctype;          //11:满减；12:立减
    public float discounts;  //立减金额；优惠金额
    public float fullAmount;
    public int multiuse;
    public String ruleDesc;
    int scopeType;  //10：全场通用；11:指定商品可用; 12:部分品类可用
    public String scopeTypeDesc;
    public int status;   //状态：0:init,1:locked,2:used,3:invalid ,
    public String title;
    public int usable; //可用状况：0:可用,1:不可用 ,
    long vte;
    long vts;

    long bindTime;
    String userId;


    public String getScope(){
       return couponDesc;
//        if(scopeType==10){
//            return Util.getString(R.string.scope_all);
//        }
//        if(scopeType==11){
//            return Util.getString(R.string.scope_special);
//        }else {
//            return Util.getString(R.string.scope_part);
//        }
    }

    public String getTime(){
        String start=Util.getFormatDate(vts,"yyyy.MM.dd");
        String end=Util.getFormatDate(vte,"yyyy.MM.dd");
        return start+"-"+end;
    }


}
