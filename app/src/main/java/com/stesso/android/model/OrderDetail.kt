package com.stesso.android.model

data class OrderDetail(val orderGoods: List<CommodityDetail>, val orderInfo: OrderInfo, val expressInfo: ExpressInfo?)
