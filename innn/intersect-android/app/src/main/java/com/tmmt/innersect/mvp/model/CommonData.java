package com.tmmt.innersect.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.tmmt.innersect.utils.Util;

/**
 * Created by flame on 2017/9/6.
 */

public class CommonData implements Parcelable{

    public int id;
    public String name;
    @SerializedName(value = "url", alternate = {"picUrl"})
    public String url;
    public String cover;
    public String schema;

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(url);
    }

    public CommonData(Parcel in){
        id=in.readInt();
        name=in.readString();
        url=in.readString();
    }

    public String getFirstLetter(){

        String letter=Util.getPinYinFirstLetter(name);
        if(letter.matches("\\d")){
            return "#";
        }
        return letter.toUpperCase();
    }


    public static final Parcelable.Creator<CommonData> CREATOR = new Parcelable.Creator<CommonData>()
    {
        public CommonData createFromParcel(Parcel in)
        {
            return new CommonData(in);
        }

        public CommonData[] newArray(int size)
        {
            return new CommonData[size];
        }
    };


}
