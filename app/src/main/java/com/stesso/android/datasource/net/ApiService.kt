package com.stesso.android.datasource.net

import io.reactivex.Completable
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("app/register")
    fun register(@Body body:JSONObject):Completable

    @POST("app/login")
    fun login(@Body body:JSONObject): Single<JSONObject>

    @GET("wechat/goods/info/{goodId}")
    fun getGoodsDetail(@Path("goodId") goodId:Long):Single<JSONObject>
}