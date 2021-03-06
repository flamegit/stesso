package com.stesso.android.datasource.net

import android.util.Log
import com.stesso.android.model.Account
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
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
                .addConverterFactory(JSONObjectConverter())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }


    private fun getClient(baseUrl: String): OkHttpClient {
        if (baseUrl.isEmpty()) {
            throw IllegalStateException("baseUrl can not be null")
        }
        return OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(getLogInterceptor(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor { chain ->
                    var request = chain.request()
                    if (Account.isLogin()) {
                        Log.d("token", Account.token)
                        request = request.newBuilder().addHeader("token", Account.token
                                ?: "").build()
                    }
                    val response = chain.proceed(request)
                    if (response.code() == 401) {
                        Account.logout()
                    }
                    response
                }
                .build()
    }

    private fun getLogInterceptor(level: HttpLoggingInterceptor.Level): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor { message -> Log.e("OKHttp-----", message) }
        loggingInterceptor.level = level
        return loggingInterceptor
    }
}