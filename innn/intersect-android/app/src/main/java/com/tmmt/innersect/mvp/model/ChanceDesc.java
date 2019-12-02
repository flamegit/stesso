package com.tmmt.innersect.mvp.model;





/**
 * Created by flame on 2017/7/14.
 */

public class ChanceDesc {

     public int resId;
     public String name;
     public int count;
     public int step;

     public String getDesc(int position){
          if(count==0){
              switch (position){
                  case 0:
                      return String.format("获得%d次机会,当日有效",step);
                  case 1:
                      return String.format("每笔订单可获得%d次机会",step);
                  case 2:
                      return String.format("每日可获得%d次机会,当日有效",step);
                  case 3:
                      return String.format("每个版本可获得%d次机会",step);
                  default:
                      return String.format("获得%d次机会,当日有效",step);
              }
          }else {
              switch (position){
                  case 0:
                      return String.format("已获得%d次机会,当日有效",count);
                  case 1:
                  case 2:
                  case 3:
                      return String.format("已获得%d次机会",count);
                  default:
                      return String.format("已获得%d次机会",count);
              }
          }
     }

}

