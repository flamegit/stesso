package com.stesso.android.model

data class OrderInfo(val orderStatusText: String, val actualPrice: Float, val orderSn: String, val address: String, val consignee: String,
                     val mobile: String, val orderStatus: String, val payType: String, val payTime: String)
