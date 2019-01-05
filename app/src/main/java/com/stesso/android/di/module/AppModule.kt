package com.stesso.android.di.module

import android.app.Application
import com.stesso.android.datasource.net.ApiService
import com.stesso.android.datasource.net.NetManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by flame on 2018/2/1.
 */
@Module
class AppModule(private val app: Application) {

    @Singleton
    @Provides
    fun provideApp() = app

    @Singleton
    @Provides
    fun provideApi(): ApiService {
        return NetManager.getRetrofit("http://api.stesso.cn/").create(ApiService::class.java)
    }
}
