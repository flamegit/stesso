package com.tmmt.innersect.mvp.model;

/**
 * Created by flame on 2017/9/19.
 */

public class DefaultConfig {

    int code;
    public Data data;

    public static class Data{
        public String workingDay;
        public String orderExpireTime;

    }

    public static class WorkTime{
        public String desc;
        public String et;
        public String msg;
        public String st;
        public String tel;
    }

}
