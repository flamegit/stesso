package com.stesso.android.model

import android.os.Parcel
import android.os.Parcelable

class Address() : Parcelable {
    var id = 0
    var name: String? = null
    var mobile: String? = null
    var detailedAddress: String? = null
    var isDefault = false
    var provinceId = 0
    var cityId = 0
    var areaId = 0
    var address: String? = null
    var cityName: String? =null
    var provinceName: String? =null
    var areaName: String? =null


    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        name = parcel.readString()
        mobile = parcel.readString()
        detailedAddress = parcel.readString()
        //isDefault = parcel.readByte() != 0.toByte()
        //provinceId = parcel.readInt()
        //cityId = parcel.readInt()
        //areaId = parcel.readInt()
        //address = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel?, p1: Int) {
        parcel?.writeInt(id)
        parcel?.writeString(name)
        parcel?.writeString(mobile)
        parcel?.writeString(detailedAddress)

    }

    override fun describeContents() = 0


    companion object CREATOR : Parcelable.Creator<Address> {
        override fun createFromParcel(parcel: Parcel): Address {
            return Address(parcel)
        }

        override fun newArray(size: Int): Array<Address?> {
            return arrayOfNulls(size)
        }
    }
}