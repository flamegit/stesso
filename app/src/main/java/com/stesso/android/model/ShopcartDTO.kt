package com.stesso.android.model

class ShopcartDTO {
    var cartTotal: Info? = null
    var cartList: List<CommodityDetail>? = null

    class Info {
        var id = 0
        var goodsCount = 0
        var checkGoodsCount = 0
        var goodsAmount = 1.00
        var checkedGoodsAmount = 1.00
    }

    fun getSelectItems(): List<CommodityDetail>? {
        return cartList?.filter { it.checked }
    }

    fun getIdList(): String? {
        var idlist = ""
        cartList?.forEach {
            if (it.checked) {
                if (idlist.isNotEmpty()) {
                    idlist += ", "
                }
                idlist += "${it.id}"
            }
        }
        return idlist
    }

    fun getTotalPrice(): Float {
        var sum = 0.0F
        cartList?.forEach {
            if(it.checked){
                sum += it.getTotalPrice()
            }
        }
        return sum
    }

    fun updateNum(position: Int, count: Int) {
        cartList?.get(position)?.number = count
    }

    fun getCartCount(): Int? {
        return cartList?.size
    }
}