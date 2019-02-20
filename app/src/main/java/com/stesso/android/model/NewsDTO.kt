package com.stesso.android.model

class NewsDTO{

    var data:List<News>? =null

    data class News(val id :Int =0,val title:String?, val subtitle:String, val picUrl:String?)
}