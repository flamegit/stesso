package com.stesso.android.datasource.net

import io.reactivex.Completable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("app/register")
    fun register(@Body body:RequestBody):Completable
}