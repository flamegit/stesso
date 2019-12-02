package com.tmmt.innersect.mvp.model;

/**
 * Created by flame on 2017/12/20.
 */

public class AwardInfo {
    public boolean isActive;
    public String schema;
    public int step;

    public String getHint(){
        return String.format("获得%d次抽奖机会 立即查看",step);
    }

}
