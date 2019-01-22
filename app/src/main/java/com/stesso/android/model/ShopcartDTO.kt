package com.stesso.android.model

class ShopcartDTO {
    var cartTotal: Info? = null
    var cartList: List<CommodityDetail>? = null

    class Info {
        var goodsCount = 0
        var checkGoodsCount = 0
        var goodsAmount = 1.00
        var checkedGoodsAmount = 1.00
    }
}