package com.tmmt.innersect.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.OrderDetailInfo;
import com.tmmt.innersect.ui.activity.OrdersDetailActivity;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;
import com.tmmt.innersect.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/4.
 */
public class OrderListAdapter extends RecyclerView.Adapter<CommonViewHolder> {
    private List<OrderDetailInfo> mContent;
    SparseArray<ViewTimer> mTimers;
    private static final int ONE_TYPE=1;
    private static final int TWO_TYPE=2;
    private static final int THREE_TYPE=3;
    private static final int FOUR_TYPE=4;
    private static final int FIVE_TYPE=5;
    private static final int SIX_TYPE=6;
    private static final int SEVEN_TYPE=7;
    private static final int EIGHT_TYPE=8;
    private static final int NINE_TYPE=9;
    private int mGap;

    public OrderListAdapter(int gap){
        mGap=gap;
        mContent=new ArrayList<>();
        mTimers=new SparseArray<>();
    }

    public void addItems(List<OrderDetailInfo> items){
        mContent.clear();
        mContent.addAll(items);
        notifyDataSetChanged();
    }

    public void appendItems(List<OrderDetailInfo> items){
        int count=mContent.size();
        mContent.addAll(items);
        notifyItemRangeInserted(count,items.size());
    }

    public OrderDetailInfo getItem(int position){
        return mContent.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        int size= mContent.get(position).product.size();
        return size>9?9:size;
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    @Override
    public void onBindViewHolder(final CommonViewHolder holder, final int position) {
        final Context context=holder.itemView.getContext();
        OrderDetailInfo order=mContent.get(position);
        ViewTimer timer;
        final int orderCode=order.getOrderCode();
        TextView quantityView =holder.get(R.id.quantity_view);
        TextView orderStatus=holder.get(R.id.order_status);
        FlexboxLayout container=holder.get(R.id.image_container);
        final TextView nameView =holder.get(R.id.name_view);
        final TextView timeView =holder.get(R.id.hint_view);
        orderStatus.setText(order.getOrderStatus());
        if(order.getTotalNum()>1){
            quantityView.setText(order.getTotalNum()+Util.getString(R.string.item_quantity));
            quantityView.setVisibility(View.VISIBLE);
        }else {
            quantityView.setVisibility(View.GONE);
        }
        for(int i=0;i<container.getChildCount();i++){
            Glide.with(context).load(order.product.get(i).skuThumbnail)
                    .placeholder(new ColorDrawable(Color.BLACK))
                    .crossFade()
                    .into((ImageView)container.getChildAt(i));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderNo=mContent.get(position).orderNo;
                Util.startActivity(context,OrdersDetailActivity.class,"orderNo",orderNo);
            }
        });
        if(orderCode== OrderDetailInfo.UNPAID) {
            long left = order.getOrderLeftTime();
            nameView.setVisibility(View.GONE);
            timeView.setVisibility(View.VISIBLE);
            timer=mTimers.get(position);
            if(timer==null){
                timer = new ViewTimer(left,1000);
                mTimers.put(position,timer);
                timer.start();
            }
            timer.setTarget(timeView);
        }else {
            nameView.setVisibility(View.VISIBLE);
            nameView.setText(order.getOrderTitle());
            timeView.setVisibility(View.GONE);
        }
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int width=parent.getWidth()-parent.getPaddingLeft()-parent.getPaddingRight();
        int size=(width-mGap)/3;
        View view=getView(parent,size,viewType);
        return new CommonViewHolder(view);
    }

    private View getView(ViewGroup parent,int w, int viewType){
        int size=w;
        Context context=parent.getContext();
        View view=LayoutInflater.from(context).inflate(R.layout.viewholder_order_detail,parent,false);
        FlexboxLayout container=(FlexboxLayout) view.findViewById(R.id.image_container);
        if(viewType==FOUR_TYPE){
            size+=Util.dip2px(2);
        }
        switch (viewType){
            case NINE_TYPE:
                addView(container,size);
            case EIGHT_TYPE:
                addView(container,size);
            case SEVEN_TYPE:
                addView(container,size);
            case SIX_TYPE:
                addView(container,size);
            case FIVE_TYPE:
                addView(container,size);
            case FOUR_TYPE:
                addView(container,size);
            case THREE_TYPE:
                addView(container,size);
            case TWO_TYPE:
                addView(container,size);
            case ONE_TYPE:
                addView(container,size);
        }
        return view;
    }

    public void release(){
        for(int i = 0; i < mTimers.size(); i++)
        {
            mTimers.valueAt(i).cancel();

        }
        mTimers.clear();
    }


    static class ViewTimer extends CountDownTimer{

        volatile TextView target;
        public void setTarget(TextView view){
            target=view;
        }
        public ViewTimer(long end,long interval){
            super(end,interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String content = Util.getString(R.string.reserve_time) + Util.convertTime(millisUntilFinished);
            if(target!=null){
                target.setText(content);
            }
        }
        @Override
        public void onFinish() {
            if(target!=null){
                target.setText("");
            }
            target=null;
        }
    }


    private void addView(FlexboxLayout parent,int size){
        int padding =Util.dip2px(2);
        ImageView imageView=new ImageView(parent.getContext());
        imageView.setBackgroundColor(Color.BLACK);
        int h=(int)(size*1.25);
        FlexboxLayout.LayoutParams params=new FlexboxLayout.LayoutParams(size-padding,h-padding);
        params.setMargins(padding,padding,0,0);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        parent.addView(imageView);
    }
}


