package com.tmmt.innersect.mvp.model;

import com.google.gson.Gson;

/**
 * Created by flame on 2017/4/13.
 */

public class User {
    int categoryType;
    long createTime;
    public String icon;
    boolean isFirstLogin;
    String nick;
    public String name;
    public String secretkey;

    public String countryCode;
    public String mobile;
    int sex;
    int sourcePlatformType;
    int sourceType;
    public String token;
    public String userId;

    public boolean isBindWeixin;
    public boolean isBindQQ;
    public boolean isBindWeibo;
    public boolean isBindFacebook;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}


