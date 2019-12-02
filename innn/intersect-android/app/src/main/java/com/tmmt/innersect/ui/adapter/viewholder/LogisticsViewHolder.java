//package com.tmmt.innersect.ui.adapter.viewholder;
//
//import android.view.View;
//import android.widget.TextView;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.mvp.model.DeliverInfo;
//
///**
// * Created by flame on 2017/4/12.
// */
//
//public class LogisticsViewHolder extends BaseViewHolder<DeliverInfo.ShopingInfo> {
//
//    public TextView contentView;
//    public TextView timeView;
//    public LogisticsViewHolder(View view){
//        super(view);
//        contentView=itemView.findViewById(R.id.logistics_view);
//        timeView=itemView.findViewById(R.id.time_view);
//    }
//
//    @Override
//    public void bindViewHolder(DeliverInfo.ShopingInfo shopinfo, int position) {
//        contentView.setText(shopinfo.context);
//        timeView.setText(shopinfo.time);
//    }
//}
