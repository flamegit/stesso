package com.tmmt.innersect.mvp.model;

import java.util.Calendar;
import java.util.List;

/**
 * Created by flame on 2017/7/24.
 */

public class Information {

    public static final int M=60*1000;

    public String imgsrc;
    public long ptime;
    public int id;
    public String title;
    public String content;
    public String productPic;
    public String columnName;
    public int productCount;
    public List<Product> productList;
    public int columnId;
    public String  getTime(){
        long time=System.currentTimeMillis()-ptime;
        long t=time/M;
        if(t<=15){
            return "刚刚";
        }
        if(t<=60){
            return "1小时前";
        }
        t=t/60;
        if(t<=24){
            return "今天";
        }
        if(t<=48){
            return "昨天";
        }

        Calendar calendar = Calendar.getInstance();
        int y=calendar.get(Calendar.YEAR);
        calendar.setTimeInMillis(ptime);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);
        if(year!=y){
            return year+"年"+month+"月"+day+"日";
        }else {
            return month+"月"+day+"日";
        }
    }

    public static class Product {

        public String brandName;
        public String name;
        public int productId;
        public String thumbnail;
    }

}
