package com.tmmt.innersect.mvp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by flame on 2017/9/20.
 */

public class BrandInfo {

    public int articleId;
    public int brandId;

    @SerializedName(value = "desc", alternate = {"brandName"})
    public String desc;

    @SerializedName(value = "logoUrl", alternate = {"brandUrl"})
    public String logoUrl;

    @SerializedName(value = "name", alternate = {"headerText"})
    public String name;

    @SerializedName(value = "bgUrl", alternate = {"headerUrl"})
    public String bgUrl;

    public List<CommodityItem> products;


}
