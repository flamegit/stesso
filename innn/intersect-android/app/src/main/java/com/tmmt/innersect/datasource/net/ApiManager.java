/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.tmmt.innersect.datasource.net;

import android.support.annotation.NonNull;

import com.socks.library.KLog;
import com.tmmt.innersect.BuildConfig;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.utils.OkhttpUtil;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.utils.Util;

import java.io.IOException;
import java.util.Locale;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 */
public class ApiManager {
    private NetApi mApi;
    /**
     * 设缓存有效期为两天
     */
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    private static final long CACHE_SIZE = 1024 * 1024 * 100;

    public static final int TEST_LOCAL_TYPE=1;
    public static final int TEST_REMOTE_TYPE=2;
    public static final int TEST_TYPE=3;
    private static final String TEST_LOCAL="";
    private static final String TEST_REMOTE= BuildConfig.ENDURL+"innersect-api/";

    private static final String TEST="http://192.168.1.97:8088/";

    /**
     * 查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
     * max-stale 指示客户机可以接收超出超时期间的响应消息。如果指定max-stale消息的值，那么客户机可接收超出超时期指定值之内的响应消息。
     */
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    /**
     * 查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
     * (假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
     */
    private static final String CACHE_CONTROL_AGE = "max-age=0";
    private static OkHttpClient sOkHttpClient;
    //private static volatile ApiManager sApiManager;
    private static volatile ApiManager[] sManager = new ApiManager[4];

    private ApiManager(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .client(getOkHttpClient()).addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        mApi = retrofit.create(NetApi.class);
    }

    private OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            sOkHttpClient= OkhttpUtil.getOkhttpClient();
//            Cache cache = new Cache(new File(App.getAppContext().getCacheDir(), "HttpCache"),CACHE_SIZE);
//            sOkHttpClient = new OkHttpClient.Builder().cache(cache)
//                    .connectTimeout(60, TimeUnit.SECONDS)
//                    .readTimeout(60, TimeUnit.SECONDS)
//                    .writeTimeout(60, TimeUnit.SECONDS)
//                    //.addInterceptor(mRewriteCacheControlInterceptor)
//                    //.addNetworkInterceptor(mRewriteCacheControlInterceptor)
//                    .addInterceptor(mLoggingInterceptor).build();
        }
        return sOkHttpClient;
    }

    /**
     * 云端响应头拦截器，用来配置缓存策略
     * Dangerous interceptor that rewrites the server's cache-control header.
     */
    private final Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!SystemUtil.isNetworkAvailable()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                KLog.d("no network");
            }
            Response originalResponse = chain.proceed(request);
            if (SystemUtil.isNetworkAvailable()) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_STALE_SEC)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };

    private final Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long t1 = System.nanoTime();
            Request req=addHeader(request);
            KLog.i(String.format("Sending request %s%s", request.url(),req.headers()));
            Response response = chain.proceed(req);
            KLog.i(response.code());
            if(response.code()==401){
                AccountInfo.getInstance().logout();
            }
            long t2 = System.nanoTime();
            KLog.i(String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    };

    //TODO rethink
    public static ApiManager getInstance(int type) {
        ApiManager apiManager = sManager[type];
        if (apiManager == null) {
            synchronized (ApiManager.class) {
                if (apiManager == null) {
                    apiManager = new ApiManager(getUrl(type));
                }
            }
            sManager[type]=apiManager;
        }
        return apiManager;
    }

    public static NetApi getApi(int type) {
        return getInstance(type).mApi;
    }

    /**
     * 根据网络状况获取缓存的策略
     */
    @NonNull
    private String getCacheControl() {
        return SystemUtil.isNetworkAvailable() ? CACHE_CONTROL_AGE : CACHE_CONTROL_CACHE;
    }
    private static String getUrl(int type){
        switch (type){
            case TEST_LOCAL_TYPE:
                return TEST_LOCAL;
            case TEST_REMOTE_TYPE:
                return TEST_REMOTE;
            case TEST_TYPE:
                return TEST;

        }
        return TEST_REMOTE;
    }

    private Request addHeader(Request request){
        AccountInfo account= AccountInfo.getInstance();
        if(!account.isLogin()){
            return request;
        }
        final Buffer buffer = new Buffer();
        String body=null;
        try{
            if(request.body()!=null){
                request.body().writeTo(buffer);
                body=buffer.readUtf8();
            }
        }catch (IOException e){
            KLog.d(e);
        }
        String path=request.url().url().getFile();
        String sign=Util.getSign(request.method(),path,body);
        Request req = request.newBuilder()
                .header("session-token",account.getToken())
                .header("Accept", "application/json")
                .header("app","innesect")
                .header("channel","android")
                .header("build","1.1.0")
                .header("os","android")
                .header("device","")
                .header("timezone","")
                .header("country","")
                .header("language","")
                .header("User-Agent","innersect")
                .header("network","wifi")
                .header("session-id", SystemUtil.getIMEI())
                .header("X-Request-sign",sign)
                .build();
        return req;
    }
}
