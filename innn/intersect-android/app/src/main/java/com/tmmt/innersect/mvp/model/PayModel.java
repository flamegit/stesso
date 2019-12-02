package com.tmmt.innersect.mvp.model;

public class PayModel {

    public int code;
    public String msg;
    public Data data;

    public static class Data{
        public String channelParams;
        String httpEncoding;
        String httpMethod;
        String merid;
        String payurl;
    }

}