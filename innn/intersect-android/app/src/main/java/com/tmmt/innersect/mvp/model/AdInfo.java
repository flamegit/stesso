package com.tmmt.innersect.mvp.model;

import java.util.List;

/**
 * Created by flame on 2017/8/1.
 */

public class AdInfo {

    boolean isOn;
    public List<Data> list;
    public static class Data{
        public String content;
        int lastFor;
        public String pic;
        public long upTime;
        public long endTime;
        public String title;
        int type;
    }
}
