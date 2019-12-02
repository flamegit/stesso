package com.tmmt.innersect.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by flame on 2017/9/14.
 */

public class Commodity implements Parcelable{

    public String skuName;
    public long productId;
    //SkuInfo skuInfo;
    public int quantity;
    public String skuSize;
    public float salePrice;
    public String skuColor;
    public String skuThumbnail;
    public String salesStatus;

    public  int colorValId;

    public long id;

    public int ascount;//

    public int aas; //0 no
    public String asdesc;
    public int isas; //0不可以

    public String getName() {
        if (skuName == null) {
            return "no name";
        }
        String name = skuName.replaceAll("\\\\n", " ");
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(skuName);
        dest.writeLong(productId);
        dest.writeString(skuColor);
        dest.writeString(skuSize);
        dest.writeString(skuThumbnail);
        dest.writeInt(ascount);
        dest.writeLong(id);
    }

    public Commodity(){}

    public Commodity(Parcel in){
        skuName=in.readString();
        productId=in.readLong();
        skuColor=in.readString();
        skuSize=in.readString();
        skuThumbnail=in.readString();
        ascount=in.readInt();
        id=in.readLong();
    }


    public static final Parcelable.Creator<Commodity> CREATOR = new Parcelable.Creator<Commodity>()
    {
        public Commodity createFromParcel(Parcel in)
        {
            return new Commodity(in);
        }
        public Commodity[] newArray(int size)
        {
            return new Commodity[size];
        }
    };

    public boolean isPreSale() {
        return salesStatus.equalsIgnoreCase("presell");
    }

//    public String getSkuDate() {
//        if (skuInfo != null && skuInfo.sku_date != null) {
//            return skuInfo.sku_date.replace(",", "");
//        }
//        return " ";
//    }
//    static class SkuInfo{
//        String sku_date;
//    }
}
