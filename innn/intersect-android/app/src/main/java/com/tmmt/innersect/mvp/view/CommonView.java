package com.tmmt.innersect.mvp.view;

/**
 * Created by flame on 2017/5/3.
 */

public interface CommonView<T> {

    void failed();
    void success(T data);

}
