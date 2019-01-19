package com.stesso.android.datasource.net

import com.stesso.android.model.HomeInfo
import com.stesso.android.model.RootNode
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("app/register")
    fun register(@Body body:JSONObject):Completable

    @POST("app/login")
    fun login(@Body body:JSONObject): Single<JSONObject>

    @GET("wechat/goods/info/{goodId}")
    fun getDetail():Single<JSONObject>

    @GET("wx/index/index")
    fun getHomeContent():Single<RootNode<HomeInfo>>
}