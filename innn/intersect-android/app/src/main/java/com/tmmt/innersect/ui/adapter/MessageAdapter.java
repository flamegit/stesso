package com.tmmt.innersect.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moor.imkf.ormlite.stmt.query.Not;
import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Commodity;
import com.tmmt.innersect.mvp.model.CommonAdapterItem;
import com.tmmt.innersect.mvp.model.DeliverInfo;
import com.tmmt.innersect.mvp.model.Message;
import com.tmmt.innersect.mvp.model.MessageStatus;
import com.tmmt.innersect.mvp.model.Notification;
import com.tmmt.innersect.mvp.model.OrderDetailInfo;
import com.tmmt.innersect.ui.activity.ApplyRefundActivity;
import com.tmmt.innersect.ui.activity.CancelOrderActivity;
import com.tmmt.innersect.ui.activity.CancelProgressActivity;
import com.tmmt.innersect.ui.activity.CommodityDetailActivity;
import com.tmmt.innersect.ui.activity.LogisticsActivity;
import com.tmmt.innersect.ui.activity.NotificationActivity;
import com.tmmt.innersect.ui.activity.RefundHistoryActivity;
import com.tmmt.innersect.ui.activity.SelectPaymentActivity;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;
import com.tmmt.innersect.utils.RedPointDrawable;
import com.tmmt.innersect.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/4.
 */
public class MessageAdapter extends RecyclerView.Adapter<CommonViewHolder> {

    static final int EMPTY_SERVICE_MESSAGE=0;
    static final int SERVICE_MESSAGE=2;
    static final int EMPTY_CUSTOM_MESSAGE=3;
    static final int CUSTOM_MESSAGE=4;
    static final int EMPTY_ACTIVITY_MESSAGE=5;
    static final int ACTIVITY_MESSAGE=6;

//    public interface OnItemClickListener{
//        void onItemClick(View view, int position, Commodity data);
//    }

//    private OnItemClickListener mListener;

    List<CommonAdapterItem<?>> mContent;
    MessageStatus mStatus;

    public MessageAdapter(){
        mContent=new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return  mContent.get(position).mType;
    }

 //   public void setListener(OnItemClickListener listener){
 //       mListener=listener;
//    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        final Context context=holder.itemView.getContext();
        switch (getItemViewType(position)){
            case EMPTY_CUSTOM_MESSAGE:

                holder.<ImageView>get(R.id.icon_view).setImageResource(R.mipmap.custom_message);
                holder.<TextView>get(R.id.title_view).setText("客服消息");
                holder.itemView.setOnClickListener(v -> Util.startMoor(context));

                break;

            case EMPTY_SERVICE_MESSAGE:
                holder.<ImageView>get(R.id.icon_view).setImageResource(R.mipmap.service_message_black);
                holder.<TextView>get(R.id.title_view).setText("服务通知");

                holder.itemView.setOnClickListener(v -> Util.startActivity(context, NotificationActivity.class));

                break;
            case EMPTY_ACTIVITY_MESSAGE:
                holder.<ImageView>get(R.id.icon_view).setImageResource(R.mipmap.activity_message);
                holder.<TextView>get(R.id.title_view).setText("活动通知");
                break;

            case ACTIVITY_MESSAGE:

                Message.ActivityInfo data=(Message.ActivityInfo)mContent.get(position).data;
                holder.<TextView>get(R.id.title_view).setText(data.ntitle);
                holder.<TextView>get(R.id.time_view).setText(data.getTime());
                Util.fillImage(context,data.nimg,holder.get(R.id.image_view));
                holder.itemView.setOnClickListener(v -> {
                    Util.openTarget(context,data.schema,"");
                });
                holder.get(R.id.head_view).setVisibility(position==2?View.VISIBLE:View.GONE);
                break;

            case CUSTOM_MESSAGE:

                Notification custom=(Notification)mContent.get(position).data;
                if(mStatus.cn!=0){
                    holder.<ImageView>get(R.id.indicator).setBackground(new RedPointDrawable(context));
                }
                holder.itemView.setOnClickListener(v -> {
                    Util.startMoor(context);
                    holder.<ImageView>get(R.id.indicator).setBackground(null);
                    if(mStatus.cn!=0){
                        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).setMessageRead("flagcn").enqueue(new NetCallback<String>() {
                            @Override
                            public void onSucceed(String data) {}
                        });
                    }

                });
                holder.<TextView>get(R.id.title_view).setText("客服消息");
                holder.<TextView>get(R.id.time_view).setText(custom.getTime());
                holder.<TextView>get(R.id.message_view).setText(custom.msg);
                break;

            case SERVICE_MESSAGE:
                Notification service=(Notification)mContent.get(position).data;

                if(mStatus.sn!=0){
                    holder.<ImageView>get(R.id.indicator).setBackground(new RedPointDrawable(context));
                }
                holder.itemView.setOnClickListener(v -> {
                    Util.startActivity(context, NotificationActivity.class);
                    holder.<ImageView>get(R.id.indicator).setBackground(null);
                    if(mStatus.sn!=0){
                        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).setMessageRead("flagsn").enqueue(new NetCallback<String>() {
                            @Override
                            public void onSucceed(String data) {}
                        });
                    }
                });
                holder.<TextView>get(R.id.title_view).setText("服务通知");
                holder.<TextView>get(R.id.time_view).setText(service.getTime());
                holder.<TextView>get(R.id.count_view).setText(service.getCount());

                holder.<TextView>get(R.id.message_view).setText(service.title+" | "+service.msg);
                Util.loadImage(context,service.getThumbnail(),holder.get(R.id.icon_view));
                break;
            default:

        }
    }

    public void fillContent(Message message){
        mStatus=message.digest;
        CommonAdapterItem cnItem=new CommonAdapterItem();
        if(message.cn==null){
            cnItem.mType=EMPTY_CUSTOM_MESSAGE;
        }else {
            cnItem.mType=CUSTOM_MESSAGE;
            cnItem.data=message.cn;
        }
        CommonAdapterItem snItem=new CommonAdapterItem();
        if(message.sn==null){
            snItem.mType=EMPTY_SERVICE_MESSAGE;
        }else {
            snItem.mType=SERVICE_MESSAGE;
            snItem.data=message.sn;
        }
        mContent.add(cnItem);
        mContent.add(snItem);

        CommonAdapterItem anItem=new CommonAdapterItem();
        if(message.ans==null){
           anItem.mType=EMPTY_ACTIVITY_MESSAGE;
           mContent.add(anItem);
        }else {
            for(int i=0;i<message.ans.size();i++){
                CommonAdapterItem tmp=new CommonAdapterItem();
                tmp.mType=ACTIVITY_MESSAGE;
                tmp.data=message.ans.get(i);
                mContent.add(tmp);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        int layoutId;
        switch (viewType){
            case EMPTY_CUSTOM_MESSAGE:
            case EMPTY_SERVICE_MESSAGE:
                layoutId=R.layout.viewholder_empty_message;
                break;
            case EMPTY_ACTIVITY_MESSAGE:
                layoutId=R.layout.viewholder_empty_activity;
                break;

            case ACTIVITY_MESSAGE:
                layoutId=R.layout.viewholder_activity_message;
                break;

            case CUSTOM_MESSAGE:
                layoutId=R.layout.viewholder_custom_message;

                break;
            case SERVICE_MESSAGE:
                layoutId=R.layout.viewholder_service_message;
                break;

            default:
                layoutId=R.layout.viewholder_order_info;

        }
        view=LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new CommonViewHolder(view);
    }
}
