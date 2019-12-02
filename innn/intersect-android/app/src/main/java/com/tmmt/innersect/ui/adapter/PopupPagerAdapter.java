package com.tmmt.innersect.ui.adapter;


import android.os.CountDownTimer;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.CommonItem;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.Util;

/**
 * Created by flame on 2017/11/24.
 */

public class PopupPagerAdapter extends CommonPagerAdapter<CommonItem> {

    public PopupPagerAdapter(){
        super(false);
        mTimers=new SparseArray<>();
    }
    SparseArray<ViewTimer> mTimers;

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        ImageView imageView;
        int size=mContent.size();
        final int p=position%size;
        CommonItem item=mContent.get(p);
        View view;

        //TODO translate
        long curr=System.currentTimeMillis();
        if(curr<item.startAt){
            view=LayoutInflater.from(container.getContext()).inflate(R.layout.viewholder_popup2,container,false);
            TextView timeView=view.findViewById(R.id.time_view);
            timeView.setText(Util.getFormatDate(item.startAt,"yyyy.MM.dd HH:mm"));
            TextView stateView=view.findViewById(R.id.state_text);
            stateView.setText("未开售");
        }
        else if(curr<item.endAt){
            view=LayoutInflater.from(container.getContext()).inflate(R.layout.viewholder_popup,container,false);
            ViewTimer timer=mTimers.get(position);
            if(timer==null ){
                timer=new ViewTimer(item.endAt-System.currentTimeMillis(),1000);
                mTimers.put(p,timer);
                timer.start();
            }
            timer.setTarget(view);
            view.setOnClickListener(v -> {
                Util.openTarget(container.getContext(),item.schema," ");
                AnalyticsUtil.reportEvent("home_popup_click");
            });
        }else {
            view=LayoutInflater.from(container.getContext()).inflate(R.layout.viewholder_popup2,container,false);
            TextView timeView=view.findViewById(R.id.time_view);
            String start=Util.getFormatDate(item.startAt,"yyyy.MM.dd");
            String end=Util.getFormatDate(item.endAt,"MM.dd");
            timeView.setText(start+"-"+end);
            TextView stateView=view.findViewById(R.id.state_text);
            stateView.setText("已结束");
        }

        imageView=view.findViewById(R.id.image_view);
        Glide.with(container.getContext()).load(mContent.get(p).bgUrl).into(imageView);
        container.addView(view);

        return view;
    }

    static class ViewTimer extends CountDownTimer{

        volatile View target;
        public void setTarget(View view){
            target=view;
        }

        public ViewTimer(long end,long interval){
            super(end,interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            if(target!=null){
                long total=millisUntilFinished/1000;
                long s=total%60;
                long min=total/60;
                long m=min%60;
                long hour=min/60;
                long h=hour%24;
                long d=hour/24;
                TextView dayView=target.findViewById(R.id.day_view);
                TextView timeView=target.findViewById(R.id.time_view);
                //TextView minuteView=target.findViewById(R.id.minute_view);
                //TextView secondView=target.findViewById(R.id.second_view);
                dayView.setText(String.format("%02d",d));
                timeView.setText(String.format("%02d : %02d : %02d",h,m,s));
                //minuteView.setText(String.format("%02d",m));
                //secondView.setText(String.format("%02d",s));
            }
        }

        @Override
        public void onFinish() {
            target=null;
        }
    }

}
