package com.tmmt.innersect.mvp.model;
import com.tmmt.innersect.R;
import com.tmmt.innersect.utils.Util;

import java.util.Calendar;
import java.util.List;

/**
 * Created by flame on 2017/4/13.
 */

public class OrderDetailInfo {
    public static final int UNPAID = 1;
    public static final int TRANSPORT = 2;//fa huo zhong;
    public static final int UNRECEIVED = 3; //yifahuo
    public static final int RECEIVED = 4; //yiwanceng
    public static final int CANCEL = 5;
    private static final long EXIT_TIME = 30 * 60 * 1000;

    public Address address;
    public long createTime;
    public String currShipping; //wuliu
    String orderDesc;
    public String orderNo;
    public int orderStatus;
    public String orderTitle;
    public String salesType;
    public long paidTime;
    public float deliveryAmount;
    public boolean isShowQrCode;
    public boolean isShowCancel;
    public int refundStatus;
    public String refundNo;
    public int aas;
    public float totalAmount; //paid amount;
    public float discountAmount;
    public String gatewayCode;
    public float payableAmount;
    public String fromSrc;
    public String shippingMethod;
    public String shop;
    public List<Commodity> product;
    long validTime;

//    public boolean isPreSale(){
//        if(product==null|| product.isEmpty()){
//            return false;
//        }
//        return product.get(0).isPreSale();
//    }

    public String getApplyResult(){
        if(refundStatus==10){
            return "退款中";
        }
        if(refundStatus==20){
            return "退款成功";
        }
        if(refundStatus==21){
            return "拦截失败";
        }
        if(refundStatus==22){
            return "退款申请未通过";
        }
        return "";

    }

    public String getPayStyle(){
        if(gatewayCode==null){
            return Util.getString(R.string.ali_pay);
        }
        if(gatewayCode.equals("aliAppPay")){
            return Util.getString(R.string.ali_pay);
        }
        if(gatewayCode.equals("wxAppPay")||"wxXppPay".equals(gatewayCode)){
            return "微信支付";
        }
        if(gatewayCode.equals("paypalPay")){
            return "PaypPal";
        }
        return "no";
    }

    public String getOrderStatus(){
        switch (orderStatus){
            case 0:
            case 1:
                return Util.getString(R.string.un_pay);
            case 2:
            case 3:
            case 4:
                return Util.getString(R.string.order_cancel);
            case 12:
            case 5:
            case 7:
                if(shop!=null){
                   return "待提货";
                }
                return Util.getString(R.string.on_transport);
            case 6:
                return "oms fail";
            case 8:
                return Util.getString(R.string.unreceived);
            case 9:
                return Util.getString(R.string.finish);

        }
        return Util.getString(R.string.un_pay);

    }
    public  int getOrderCode(){
        switch (orderStatus){
            case 0:
            case 1:
                return UNPAID;
            case 2:
            case 3:
            case 4:
                return CANCEL;
            case 12:
            case 5:
            case 7:
                return TRANSPORT;
            case 8:
                return UNRECEIVED;
            case 9:
                return RECEIVED;
        }
        return UNPAID;
    }

    public String getShip(){
        if("11".equals(shippingMethod)){
            return Util.getString(R.string.transport);
        }else {
            return Util.getString(R.string.pick_up);
        }
    }

    public String getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(createTime);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        return day + "/" + month;
    }

    public int getTotalNum() {
        int count = 0;
        for (Commodity commodity : product) {
            count += commodity.quantity;
        }
        return count;
    }

    public String getOrderTitle(){
        if(orderTitle==null){
            return "no name";
        }
        String name=orderTitle.replaceAll("\\\\n"," ");
        return name;
    }

    public String getContactInfo(){
        return address.getName()+"  "+address.mobile;
    }

    public String getAddress(){
        return address.getDetail();
    }

    public String getOrderDesc(){
        int code=getOrderCode();
        if(code== TRANSPORT){
            if(shop!=null){
                return "请凭提货码取货";
            }
            return Util.getString(R.string.product_wait);
        }
        if(code== RECEIVED){
            return Util.getString(R.string.arrive_hint);
        }
        if(code==CANCEL){
            return "你的订单已取消";
        }
        return "";
    }

    public long getOrderLeftTime() {
        long curr = System.currentTimeMillis();
        //long exit=Integer.valueOf(validTime)*60*1000;
        long left = validTime - curr;
        return left;
    }
}
