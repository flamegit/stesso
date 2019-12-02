package com.tmmt.innersect.mvp.model;


import java.util.List;

/**
 * Created by flame on 2018/1/10.
 */

public class RefundProgress {

    public RefundItem order;
    Tel stel;

    public List<Step> steps;

    public int getStepProgress(){
        for(int i=0;i<steps.size();i++){
            if(steps.get(i).isEmpty()){
                return i;
            }
        }
        return 3;
    }

    public String getTel(){
        if(stel!=null){
            return stel.tel;
        }else {
            return "400-8765-0000";
        }
    }

    public boolean isPass(){
        if(steps.size()>2){
            int id= steps.get(2).id;
            if(id==51||id==61){
                return true;
            }
        }
        return false;
    }

    public List<Progress> getProgress(int i){
        return steps.get(i).ops;
    }

    public String getTitle(int i){
        return steps.get(i).name;
    }

    static class Step {
        int id;
        String name;
        List<Progress> ops;

        public boolean isEmpty(){
            if(ops==null ||ops.isEmpty()){
                return true;
            }
            return false;
        }
    }

    public static class Progress{
        int action;
        public String adesc;
        public String aname;
        public Transport exp;
        long id;
        public String info;
        public String remark;
        public int issb;
        int step;
        public int sc;
        public int se;
        public int so;
        public String orderNo;


        public String getDesc(){
            StringBuilder sb=new StringBuilder();
            if(adesc!=null&& !adesc.isEmpty()){
                sb.append(adesc);
            }
            if(info!=null&& !info.isEmpty()){
                sb.append("\n"+info);
            }
            if(remark!=null&& !remark.isEmpty()){
                sb.append("\n"+remark);
            }
            return sb.toString();
        }

    }

    public static class Transport{
        public String code;
        public String name;
        public String expn;
    }

    static class Tel {
        String msg;
        String tel;
    }
}
