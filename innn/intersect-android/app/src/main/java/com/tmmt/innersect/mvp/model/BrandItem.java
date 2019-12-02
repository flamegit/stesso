package com.tmmt.innersect.mvp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by flame on 2017/9/20.
 */

public class BrandItem extends AdapterItem {
    @SerializedName(value = "brandId", alternate = {"id"})
    public int brandId;
    @SerializedName(value = "desc", alternate = {"schema"})
    public String desc;
    public String logoUrl;
    public String name;
    public String bgUrl;

    public BrandItem(){
        mSpanSize=2;
    }
    public BrandItem(String desc,String logoUrl,String name,String bgUrl,int brandId){
        this.brandId=brandId;
        this.desc=desc;
        this.logoUrl=logoUrl;
        this.name=name;
        this.bgUrl=bgUrl;
    }

    @Override
    public int getType() {
        return AdapterItem.BRAND;
    }
}
