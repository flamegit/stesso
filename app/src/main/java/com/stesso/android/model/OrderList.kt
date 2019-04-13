package com.stesso.android.model

data class OrderList(val data: List<OrderItem>, val count: Int) {
    data class OrderItem(val orderStatusText: String, val actualPrice: Float, val orderSn: String, val goodsList: List<GoodsInfo>, val id: Int)
}