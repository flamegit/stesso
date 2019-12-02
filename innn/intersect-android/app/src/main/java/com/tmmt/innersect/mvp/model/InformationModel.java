package com.tmmt.innersect.mvp.model;


import java.util.List;

/**
 * Created by flame on 2017/4/13.
 */

public class InformationModel {

    public String msg;
    public int code;
    public Data data;

    public static class Data{
        public int isEnd;
        public List<Information> retvalList;
    }

}


