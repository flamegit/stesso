package com.stesso.android.model

class CommodityInfoDTO{

    var specificationList:List<Category>? =null
    var productList:List<Product>? =null
    var info:Info?=null

    fun getGallery():List<String>?{
        return info?.gallery
    }

    fun getProductId(specification:String?):Int?{
        productList?.forEach {
            if(specification == it.getSpecification()){
                return it.id
            }
        }
        return null
    }

    data class Info(val name:String,val brief:String,val gallery:List<String>,val detail:String,val id:Int)
    data class Value(val id:Int,val goodsId:Int,val specifications:String,val value:String)
    data class Category(val name:String,val valueList:List<Value>)
    data class Product(val id:Int,val goodsId:Int,val specifications:List<String>,val number:Int){
        fun getSpecification():String{
           return  specifications.reduce { acc, s ->"$acc$s"  }
        }
    }

}