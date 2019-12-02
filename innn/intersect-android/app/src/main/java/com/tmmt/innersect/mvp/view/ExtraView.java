package com.tmmt.innersect.mvp.view;

/**
 * Created by flame on 2017/10/31.
 */

public interface ExtraView<T> {

    void onFailure(int code);
    void onSuccess(T data);
}
