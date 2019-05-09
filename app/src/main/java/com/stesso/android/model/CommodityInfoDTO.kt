package com.stesso.android.model

class CommodityInfoDTO{

    var specificationList:List<Category>? =null
    var productList:List<Product>? =null
    var info:Info?=null
    var userHasCollect = 0

    fun getGallery():List<String>?{
        return info?.gallery
    }

    fun getProduct(specification:String?):Product?{
        productList?.forEach {
            if(specification == it.getSpecification()){
                return it
            }
        }
        return null
    }

    data class Info(val name:String,val brief:String,val gallery:List<String>,val detail:String,val id:Int,val counterPrice:Float,val retailPrice:Float )
    data class Value(val id:Int,val goodsId:Int,val specifications:String,val value:String)
    data class Category(val name:String,val valueList:List<Value>)
    data class Product(val id:Int,val goodsId:Int,val specifications:List<String>,val number:Int,val price:Float){
        fun getSpecification():String{
           return  specifications.reduce { acc, s ->"$acc$s"  }
        }
    }

}