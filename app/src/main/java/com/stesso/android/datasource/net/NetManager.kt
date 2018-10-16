package com.example.flame.kotlinstudy.datasource.net

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by flame on 2018/2/2.
 */

object NetManager {

    fun getRetrofit(baseUrl: String): Retrofit {

        if (baseUrl.isEmpty()) {
            throw IllegalStateException("baseUrl can not be null")
        }

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient(baseUrl))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }


    private fun getClient(baseUrl: String): OkHttpClient {
        if (baseUrl.isEmpty()) {
            throw IllegalStateException("baseUrl can not be null")
        }

        return OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build()
    }

}