package com.stesso.android.model

class NewsDetailDTO{

    var topic:NewsContent? =null

    data class NewsContent(val id :Int =0,val title:String?, val subtitle:String, val picUrl:String?,val content:String?)
}