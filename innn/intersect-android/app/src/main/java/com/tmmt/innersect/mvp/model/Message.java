package com.tmmt.innersect.mvp.model;


import com.tmmt.innersect.utils.Util;

import java.util.List;

/**
 * Created by flame on 2017/7/24.
 */

public class Message {

    public MessageStatus digest;
    public Notification cn;
    public Notification sn;
    public List<ActivityInfo> ans;


    public static class ActivityInfo{
        int id;
        public String nimg;
        public String ntitle;// (string): 通知标题 ,
        public long pushTime; //(string): 指定发送时间 ,
        public String schema; //(string): 跳转schema

        public String getTime(){
            return Util.getFormatDate(pushTime,"yyyy.MM.dd HH:mm");
        }
    }

}



