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
    fun register(@Body body: JSONObject): Completable

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

    @POST("wx/address/save")
    fun saveAddress(@Body body: Address): Single<RootNode<String>>

    @POST("wx/address/delete")
    fun deleteAddress(): Completable


}