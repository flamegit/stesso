package com.tmmt.innersect.datasource.net;

/**
 * Created by flame on 2017/5/3.
 */

public interface RequestCallback<T> {

    void onSuccess(T data);
    void onError(String msg);

}
