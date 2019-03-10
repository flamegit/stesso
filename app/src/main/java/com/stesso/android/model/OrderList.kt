package com.stesso.android.model

data class OrderList(val data:List<OrderInfo>,val count:Int){
    data class OrderInfo(val orderStatusText:String,val actualPrice:Float,val orderSn:String,val goodsList:List<GoodsInfo>,val id:Int)
    data class GoodsInfo(val number:Int,val goodsName:String,val picUrl:String,val id:Int)
}