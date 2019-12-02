package com.tmmt.innersect.datasource.net;

import com.socks.library.KLog;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.utils.SystemUtil;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by flame on 2017/8/22.
 */

public abstract class NetCallback<T> implements retrofit2.Callback<HttpBean<T>> {

    @Override
    public void onResponse(Call<HttpBean<T>> call, Response<HttpBean<T>> response) {
        if(response.isSuccessful()){
            if(response.body().code==200){
                onSucceed(response.body().data);
            }else {
                failed(response.body().code);
                SystemUtil.reportServerError(response.body().msg);
            }
        }else {
            KLog.i("response failed"+response.code()+response.message());
            failed(response.code());
        }
    }

    @Override
    public void onFailure(Call<HttpBean<T>> call, Throwable t) {
        KLog.i(t);
        failed(-1);
        SystemUtil.reportNetError(t);
    }

    protected void failed(int code){}

    public abstract void onSucceed(T data);
}
