package com.stesso.android.model

data class OrderInfo(val id:Int,val orderStatusText: String, val actualPrice: Float, val orderSn: String, val address: String, val consignee: String,
                     val mobile: String, val orderStatus: Int, val payType: String, val payTime: String, val goodsPrice: Float,val addTime:String)
