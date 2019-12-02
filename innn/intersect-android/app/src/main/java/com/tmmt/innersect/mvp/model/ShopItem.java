package com.tmmt.innersect.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by flame on 2017/5/10.
 */

public class ShopItem implements Parcelable{
    @SerializedName("skuName")
    public String name;
    @Expose
    @SerializedName("skuColor")
    public String color;
    @Expose
    @SerializedName("skuSize")
    public String size;
    @Expose
    @SerializedName(value = "skuCount",alternate = {"quantity"})
    public int quantity;
    @Expose
    public long skuId;
    @Expose
    public String shop;
    @Expose
    public long productId;
    @Expose
    public String userId;

    public int skuStatus;

    public String store;
    public String salesType;

    @SerializedName(value = "skuPrice", alternate = {"salePrice"})
    public float price;
    public int id;
    public String omsSkuId;
    public int maxAvailableCount;
    public int availInv;

    public int pavailInv;

    @Expose
    public String skuThumbnail;
    public transient boolean payment;
    public transient boolean delete;
    public String date;

    public int getLimit(){
        int c=Math.max(availInv,pavailInv);

        return Math.min(maxAvailableCount,c);
    }
    public boolean isPreSale() {
        if(salesType==null){
            return false;
        }
        return salesType.equalsIgnoreCase("presell");
    }

    /* 1 not sail 2,sale out 3,ziti 4 only 5 other
     */
    public int getStatus(){
        if(skuStatus!=1 && skuStatus!=3){
            return 1;
        }
        if((availInv<=0&&pavailInv<=0) || skuStatus==3 ){
            return 2;
        }
        if(pavailInv>=quantity && availInv>=quantity){
            return 3;
        }else if(pavailInv>=quantity){
            return 4;
        }else {
            return 5;
        }
    }

    public int getAvailabeStatus(){
        if(pavailInv>=quantity && availInv>=quantity) {
            return 3;
        }
        if(pavailInv>=quantity){
            return 1;
        }
        if(availInv>=quantity){
            return 2;
        }
        return 0;
    }

    public boolean isAvailabel(){
        if(availInv<=0 || skuStatus!=1 ){
           return true;
        }
        return false;
    }

    public boolean isSaleOut(){
        if(availInv<=0 || skuStatus==3 ){
            return true;
        }
        return false;
    }

    public boolean isNotSale(){
        if(skuStatus!=1 && skuStatus!=3){
            return true;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(color);
        dest.writeString(size);
        dest.writeInt(quantity);
        dest.writeLong(skuId);
        dest.writeFloat(price);
        dest.writeString(skuThumbnail);
        dest.writeString(omsSkuId);
        dest.writeLong(productId);
        dest.writeString(salesType);
        dest.writeString(store);
        dest.writeString(shop);
    }

    public ShopItem(){

    }
    public ShopItem(Parcel in){
        name=in.readString();
        color=in.readString();
        size=in.readString();
        quantity=in.readInt();
        skuId=in.readLong();
        price=in.readFloat();
        skuThumbnail=in.readString();
        omsSkuId=in.readString();
        productId=in.readLong();
        salesType=in.readString();
        store=in.readString();
        shop=in.readString();

    }

    public static final Parcelable.Creator<ShopItem> CREATOR = new Parcelable.Creator<ShopItem>()
    {
        public ShopItem createFromParcel(Parcel in)
        {
            return new ShopItem(in);
        }

        public ShopItem[] newArray(int size)
        {
            return new ShopItem[size];
        }
    };


}
