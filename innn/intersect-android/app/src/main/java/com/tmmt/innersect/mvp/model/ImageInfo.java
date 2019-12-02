package com.tmmt.innersect.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by flame on 2017/9/29.
 */

public  class ImageInfo implements Parcelable {

    @SerializedName(value = "imgUrl", alternate = {"img"})
    public String imgUrl;
    public String title;
    public String desc;

    public ImageInfo(){}

    public ImageInfo(Parcel in){
        imgUrl=in.readString();
        title=in.readString();
        desc=in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
        dest.writeString(title);
        dest.writeString(desc);

    }
    public static final Parcelable.Creator<ImageInfo> CREATOR = new Parcelable.Creator<ImageInfo>()
    {
        public ImageInfo createFromParcel(Parcel in)
        {
            return new ImageInfo(in);
        }

        public ImageInfo[] newArray(int size)
        {
            return new ImageInfo[size];
        }
    };

}