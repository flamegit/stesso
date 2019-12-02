package com.tmmt.innersect.mvp.model;

import android.graphics.Color;

import com.tmmt.innersect.R;
import com.tmmt.innersect.utils.Util;

import java.util.List;

/**
 * Created by flame on 2017/9/20.
 */

public class ReserveInfo {

    public long id;
    public String title;
    public List<String> reservePic;
    long startTime;
    long endTime;
    int peopleLimit;
    public List<Location> address;
    public List<StringBundle> guide;
    public String sharePic;
    public String description;
    public List<MoreInfo> more;
    public int status;   // 0未开始 1可预约 2已预约 3已抢光 4已结束 ,

    public String getTime(){
        String start=Util.getFormatDate(startTime,"yyyy.MM.dd HH:mm");
        String end=Util.getFormatDate(endTime,"yyyy.MM.dd HH:mm");
        return start+"-"+end;
    }

    public String getAction(){
        if(status==2){
            return "查看";
        }else {
            return "去登记";
        }
    }


    public int getBgColor(){
        switch (status){
            case 1:
            case 2:
                return Color.parseColor("#f8e638");
            case 0:
            case 3:
            case 4:
                return Color.parseColor("#ff555556");

        }
        return Color.parseColor("#ff555556");

    }

    public int getTextColor(){
        switch (status){
            case 1:
            case 2:
                return Color.parseColor("#000000");
            case 0:
            case 3:
            case 4:
                return Color.parseColor("#ffffff");

        }
        return Color.parseColor("#000000");

    }


    public String getStatus(){
        switch (status){
            case 0:
                return String.format(Util.getString(R.string.start_reservation),Util.getformatTime(startTime));
            case 1:
                return Util.getString(R.string.order);
            case 2:
                return Util.getString(R.string.my_reservation);
            case 3:
                return Util.getString(R.string.no_left);
            case 4:
                return Util.getString(R.string.finished);

        }
        return Util.getString(R.string.finished);
    }
}
