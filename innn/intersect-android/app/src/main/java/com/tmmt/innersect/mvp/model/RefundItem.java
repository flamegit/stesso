package com.tmmt.innersect.mvp.model;

import com.tmmt.innersect.R;
import com.tmmt.innersect.utils.Util;

/**
 * Created by flame on 2018/1/9.
 */

public class RefundItem {

    public String asno;
    public int astype;
    long ctime;
    public String skuName;
    public String skuColor;
    public String skuThumbnail;
    public String sdesc;
    int quantity;
    public String skuSize;

    public float refundAmount;

    public String getQuantity(){
        return "x "+quantity;
    }

    public String getType(){
        if(astype==1){
            return Util.getString(R.string.refund_money);
        }else {
            return Util.getString(R.string.exchange_commodity);
        }
    }

    public String getRefundAmount(){

        return String.format("可退款￥%.2f",refundAmount);
    }

    public String getApplyTime(){
        return "申请时间："+ Util.getFormatDate(ctime,"yyyy.MM.dd");
    }

//    asno (string, optional): 售后单号 ,
//    astype (integer, optional): 售后类型：1：退款；2：换货 ,
//    ctime (string, optional): 创建时间 ,
//    daid (integer, optional): 地址ID ,
//    id (integer, optional): 售后单ID ,
//    omsSkuId (string, optional): oms sku id ,
//    orderNo (string, optional): 订单号 ,
//    originalPrice (number, optional): 原价 ,
//    paidAmount (number, optional): 实付金额 ,
//    productId (integer, optional): 商品ID ,
//    quantity (integer, optional): 数量 ,
//    reasonId (integer, optional): 原因ID ,
//    refundAmount (number, optional): 应退金额 ,
//    remark (string, optional): 备注描述 ,
//    salePrice (number, optional): 售价 ,
//    sdesc (string, optional): 状态说明 ,
//    skuColor (string, optional): sku颜色信息 ,
//    skuId (integer, optional): sku id ,
//    skuName (string, optional): 商品名称 ,
//    skuSize (string, optional): sku尺码信息 ,
//    skuThumbnail (string, optional): 图 ,
//    status (integer, optional): 状态 ,
//    topid (integer, optional): 订单商品ID ,
//    userId (string, optional): 用户ID

}
