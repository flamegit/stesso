package com.tmmt.innersect.mvp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by flame on 2017/8/24.
 */

public class Problem implements Parcelable {

    @SerializedName(value = "note", alternate = {"answer"})
    public String note;

    @SerializedName(value = "title", alternate = {"question"})
    public String title;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(note);
        dest.writeString(title);
    }

    public Problem(Parcel in){
        note=in.readString();
        title=in.readString();
    }

    public static final Parcelable.Creator<Problem> CREATOR = new Parcelable.Creator<Problem>()
    {
        public Problem createFromParcel(Parcel in)
        {
            return new Problem(in);
        }

        public Problem[] newArray(int size)
        {
            return new Problem[size];
        }
    };


}
