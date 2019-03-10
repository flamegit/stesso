package com.stesso.android.model

data class WechatPayDTO(val sign:String,val prepayId:String,val partnerId:String,val appId:String,val packageValue:String,
                        val timeStamp:String,val nonceStr:String)


