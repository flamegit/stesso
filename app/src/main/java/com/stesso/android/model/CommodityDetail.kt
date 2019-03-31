package com.stesso.android.model

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal

class CommodityDetail() : Parcelable {
    var id = 0
    var userId = 0
    var productId = 0
    var price = 1.0
    var number = 1
    var specifications: List<String>? = null
    var checked = false
    var goodsId: String? = null
    var goodsSn: String? = null
    var goodsName: String? = null
    var addTime: String? = null
    var picUrl: String? = null
    //var isSelect = false


    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        number = parcel.readInt()
        goodsName = parcel.readString()
        picUrl = parcel.readString()
        price = parcel.readDouble()

    }

    fun getTotalPrice():Float{
        return BigDecimal(price).multiply(BigDecimal(number)).toFloat()
    }

    fun getInfo(): String {
        if (specifications?.isNotEmpty() == true) {
            return specifications!!.reduce { v1, v2 ->
                 "$v1 $v2"
            }
        }
        return ""
    }

    override fun describeContents() = 0

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeInt(id)
        parcel?.writeInt(number)
        parcel?.writeString(goodsName)
        parcel?.writeString(picUrl)
        parcel?.writeDouble(price)
    }

    companion object CREATOR : Parcelable.Creator<CommodityDetail> {
        override fun createFromParcel(parcel: Parcel): CommodityDetail {
            return CommodityDetail(parcel)
        }

        override fun newArray(size: Int): Array<CommodityDetail?> {
            return arrayOfNulls(size)
        }
    }
}



