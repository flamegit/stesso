//package com.stesso.android.model
//
//import android.os.Parcel
//import android.os.Parcelable
//
///**
// * Created by flame on 2017/9/14.
// */
//
//class Commodity : Parcelable {
//
//    var skuName: String? = null
//    var productId: Long = 0
//
//    var quantity: Int = 0
//    var skuSize: String? = null
//    var salePrice: Float = 0.toFloat()
//    var skuColor: String? = null
//    var skuThumbnail: String? = null
//    var salesStatus: String? = null
//
//    var colorValId: Int = 0
//
//    var id: Long = 0
//
//    var ascount: Int = 0//
//
//
//    override fun writeToParcel(dest: Parcel, flags: Int) {
//        dest.writeString(skuName)
//        dest.writeLong(productId)
//        dest.writeString(skuColor)
//        dest.writeString(skuSize)
//        dest.writeString(skuThumbnail)
//        dest.writeInt(ascount)
//        dest.writeLong(id)
//    }
//
//    constructor() {}
//
//    constructor(`in`: Parcel) {
//        skuName = `in`.readString()
//        productId = `in`.readLong()
//        skuColor = `in`.readString()
//        skuSize = `in`.readString()
//        skuThumbnail = `in`.readString()
//        ascount = `in`.readInt()
//        id = `in`.readLong()
//    }
//
//    companion object {
//
//
//        val CREATOR: Parcelable.Creator<Commodity> = object : Parcelable.Creator<Commodity> {
//            override fun createFromParcel(`in`: Parcel): Commodity {
//                return Commodity(`in`)
//            }
//
//            override fun newArray(size: Int): Array<Commodity> {
//                return arrayOfNulls(size)
//            }
//        }
//    }
//
//}
