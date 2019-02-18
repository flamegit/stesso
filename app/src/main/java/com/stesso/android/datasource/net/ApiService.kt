package com.stesso.android.datasource.net

import com.stesso.android.model.*
import io.reactivex.Completable
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("app/register")
    fun register(@Body body: JSONObject): Single<RootNode<String>>

    @POST("app/login")
    fun login(@Body body: JSONObject): Single<RootNode<UserDTO>>

    @GET("wechat/goods/info/{goodId}")
    fun getDetail(): Single<JSONObject>

    @GET("wx/index/index")
    fun getHomeContent(): Single<RootNode<HomeInfo>>

    @GET("wx/region/list")
    fun getRegionList(@Query("pid") pid: Int): Single<RootNode<List<Region>>>

    @GET("wx/address/list")
    fun getAddressList(): Single<RootNode<List<Address>>>

    @GET("wx/address/detail")
    fun getAddressDetail(@Query("id") id:Int): Single<RootNode<Address>>

    @GET("wx/goods/detail")
    fun getCommodityDetail(@Query("id") id:Int): Single<RootNode<CommodityInfoDTO>>

    @POST("wx/address/save")
    fun saveAddress(@Body body: Address): Single<RootNode<String>>

    @POST("wx/address/delete")
    fun deleteAddress(): Completable

    @POST("wx/cart/delete")
    fun deleteCartItem(): Completable

    @GET("wx/cart/index")
    fun getCartItems(): Single<RootNode<ShopcartDTO>>

    @POST("wx/cart/add")
    fun addCartItem(@Body body: JSONObject): Single<RootNode<String>>

    @POST("wx/auth/sendValidCode")
    fun getVerifyCode(@Body body: JSONObject):Single<RootNode<String>>

    @GET("wx/topic/list")
    fun getNewsList(@Query("page") page:Int,@Query("size") size:Int):Single<RootNode<List<NewsDTO>>>


}