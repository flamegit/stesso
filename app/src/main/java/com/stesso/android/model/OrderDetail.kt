package com.stesso.android.model

data class OrderDetail(val orderGoods: ArrayList<CommodityDetail>, val orderInfo: OrderInfo, val expressInfo: ExpressInfo?)
