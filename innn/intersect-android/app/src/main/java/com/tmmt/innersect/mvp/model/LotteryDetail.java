package com.tmmt.innersect.mvp.model;





/**
 * Created by flame on 2017/7/14.
 */

public class LotteryDetail {

     public int id;
     public boolean isLogin;
     public String name;
     public String rule;
     public int drawChance;
     public int status;
     String bgColor;
     public String sourcePackUrl;
     public String sourcePackVersion;
     public String bgUrl;
     public long expirTime;

     public boolean isActive(){
          return status==1;
     }

}



