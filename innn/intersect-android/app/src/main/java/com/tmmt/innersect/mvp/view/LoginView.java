package com.tmmt.innersect.mvp.view;

/**
 * Created by flame on 2017/5/3.
 */

public interface LoginView {

    void unBindMobile();
    void setPassword(boolean setPwd);
    void success(int code);

}
