package com.stesso.android.model

import android.os.Parcel
import android.os.Parcelable

class CommodityDetail : Parcelable{
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
    var isSelect = false

    fun getInfo():String{
        if(specifications?.isNotEmpty()==true){
          return  specifications!!.reduce{
                v1,v2 -> v1 +v2

            }
        }
        return ""
    }

    override fun describeContents() = 0

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object CREATOR : Parcelable.Creator<Address> {
        override fun createFromParcel(parcel: Parcel): Address {
            return Address(parcel)
        }

        override fun newArray(size: Int): Array<Address?> {
            return arrayOfNulls(size)
        }
    }
}



