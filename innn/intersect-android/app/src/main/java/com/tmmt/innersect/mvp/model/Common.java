package com.tmmt.innersect.mvp.model;


import com.google.gson.annotations.SerializedName;

/**
 * Created by flame on 2017/9/6.
 */

public class Common {

    public int id;

    @SerializedName(value = "item1", alternate = {"ctime"})
    public String item1;

    @SerializedName(value = "item2", alternate = {"sourcePackVersion","reason","title"})
    public String item2;

    @SerializedName(value = "item3", alternate = {"sourcePackUrl","note"})
    public String item3;


    public String getTime(){
        return item1;
    }

    public String getTitle(){
        return item2;
    }

    public String getNote(){
        return item3;
    }

}
