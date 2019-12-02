package com.tmmt.innersect.mvp.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by flame on 2017/9/12.
 */

public class ExhibitionInfo {

    public Content brands;
    public Content artists;
    public Content stars;

    public static class Content{
        public ArrayList<CommonData> contents;
        public String title;
    }

    public List<Content> getList(){
        List<Content> list=new ArrayList<>();
        list.add(brands);
        list.add(artists);
        list.add(stars);
        return list;
    }
}
