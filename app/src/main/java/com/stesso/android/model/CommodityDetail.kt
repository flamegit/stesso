package com.stesso.android.model

class CommodityDetail {
    var id = 0
    var userId = 0
    var productId = 0
    var price = 1.00
    var num = 1
    var specifications: List<String>? = null
    var checked = false
    var goodsId: String? = null
    var goodsSn: String? = null
    var goodsName: String? = null
    var addTime: String? = null
    var picUrl: String? = null

    fun getInfo():String{
        if(specifications?.isNotEmpty()==true){
          return  specifications!!.reduce{
                v1,v2 -> v1 +v2

            }
        }
        return ""
    }
}



