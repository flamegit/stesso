package com.tmmt.innersect.mvp.model;

import android.content.Context;
import android.view.View;

import com.tmmt.innersect.R;
import com.tmmt.innersect.ui.activity.AwardSettlementActivity;
import com.tmmt.innersect.ui.activity.CouponActivity;
import com.tmmt.innersect.ui.activity.QualificationActivity;
import com.tmmt.innersect.utils.Util;

/**
 * Created by flame on 2017/7/14.
 */

public class LotteryInfo {
     public int id;
     public String title;
     public String imgUrl;
     public long ctime;
     public long vte;
     public long vts;
     public int status;
     public int prizeType;
     public long prizeId;

     public int getResId(){
          if(status!=0){
               return R.mipmap.used_indicator;
          }else if(System.currentTimeMillis()>vte){
               return R.mipmap.expire_icon;
          }
          return R.mipmap.used_indicator;

     }

     public int showIndicator(){
          if(status!=0 || System.currentTimeMillis()>vte){
               return View.VISIBLE;
          }
          return View.GONE;
     }

     public boolean isActive(){
          if(status==0 && System.currentTimeMillis()<vte){
               return true;
          }
          return false;
     }


     public String getTime(){
          String start= Util.getFormatDate(vts,"yyyy.MM.dd");
          String end=Util.getFormatDate(vte,"yyyy.MM.dd");
          return "有效期:\n"+start+"-"+end;
     }

     public int  getStatus(){
          if(status==0 && System.currentTimeMillis()<vte){
               return View.VISIBLE;
          }else{
              return View.GONE;
          }
     }

     public void openTarget(Context context){
          if(prizeType==11){
               CouponActivity.start(context,false);
          }else if(prizeType==12){
               AwardSettlementActivity.start(context,prizeId);
          }else if(prizeType==13){
               QualificationActivity.start(context,prizeId);
          }
     }

//     ctime (string, optional): 中奖时间 ,
//     id (integer, optional): ID ,
//     imgUrl (string, optional): 图片地址 ,
//     prizeId (integer, optional): 奖品ID ,
//     prizeType (integer, optional): 奖品类型：11:优惠券；12:线上购买权；13:线下商品购买权 ,
//     status (integer, optional): 状态：0:init,1:locked,2:used,3:invalid ,
//     title (string, optional): 奖品名称 ,
//     vte (string, optional): 有效结束时间 ,
//     vts (string, optional): 有效开始时间
}

