package com.stesso.android.datasource.net

import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("app/register")
    fun register(@Body body:JSONObject):Completable

    @POST("app/login")
    fun login(@Body body:JSONObject): Single<JSONObject>
}