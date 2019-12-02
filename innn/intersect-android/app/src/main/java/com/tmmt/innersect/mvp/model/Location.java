package com.tmmt.innersect.mvp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by flame on 2017/9/20.
 */

public class Location {

    public String location;

    @SerializedName(value = "shopName", alternate = {"name"})
    public String shopName;

    public String url;
}
