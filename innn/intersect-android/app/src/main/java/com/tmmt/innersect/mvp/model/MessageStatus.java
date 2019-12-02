package com.tmmt.innersect.mvp.model;

/**
 * Created by flame on 2017/7/24.
 */

public class MessageStatus {

    public int cn;
    public int sn;
    public int hn;

    public boolean isRead(){
        if(cn==0 && sn==0 ){
            return true;
        }
        return false;
    }
}



