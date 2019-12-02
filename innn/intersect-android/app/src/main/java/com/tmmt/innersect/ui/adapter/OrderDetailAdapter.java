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
import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Commodity;
import com.tmmt.innersect.mvp.model.CommonAdapterItem;
import com.tmmt.innersect.mvp.model.DeliverInfo;
import com.tmmt.innersect.mvp.model.OrderDetailInfo;
import com.tmmt.innersect.ui.activity.ApplyRefundActivity;
import com.tmmt.innersect.ui.activity.CancelOrderActivity;
import com.tmmt.innersect.ui.activity.CancelProgressActivity;
import com.tmmt.innersect.ui.activity.CommodityDetailActivity;
import com.tmmt.innersect.ui.activity.LogisticsActivity;
import com.tmmt.innersect.ui.activity.QRCodeActivity;
import com.tmmt.innersect.ui.activity.RefundHistoryActivity;
import com.tmmt.innersect.ui.activity.SelectPaymentActivity;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;
import com.tmmt.innersect.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/4.
 */
public class OrderDetailAdapter extends RecyclerView.Adapter<CommonViewHolder> {

    static final int ORDER_STATE=0;
    static final int ORDER_SHOP=1;
    static final int COMMODITY_LIST=2;
    static final int PRICE_INFO=3;
    static final int ORDER_INFO=4;
    static final int CANCEL_ORDER=5;
    static final int REFUND_INFO=6;

    public interface OnItemClickListener{
        void onItemClick(View view,int position,Commodity data);
    }

    private OrderDetailInfo mOrder;
    private OnItemClickListener mListener;
    List<CommonAdapterItem<?>> mContent;
    CountDownTimer mTimer;

    @Override
    public int getItemViewType(int position) {
        return  mContent.get(position).mType;
    }

    public void setOrders(OrderDetailInfo order){
        mOrder=order;
        mContent=getItemList(order);
        notifyDataSetChanged();

    }
    public List<CommonAdapterItem<?>> getItemList(OrderDetailInfo order){
        List<CommonAdapterItem<?>> list=new ArrayList<>();
        CommonAdapterItem<Integer> stateItem=new CommonAdapterItem<>();

        if(order.shop==null){
            stateItem.mType=ORDER_STATE;
        }else {
            stateItem.mType=ORDER_SHOP;
        }
        list.add(stateItem);

        if(order.isShowCancel){
            CommonAdapterItem<Integer> cancelItem=new CommonAdapterItem<>();
            cancelItem.mType=CANCEL_ORDER;
            list.add(cancelItem);
        }
        if(order.aas==1){
            CommonAdapterItem<Integer> refundItem=new CommonAdapterItem<>();
            refundItem.mType=REFUND_INFO;
            list.add(refundItem);
        }
        if (mOrder.product != null) {
            for (Commodity commodity:order.product) {
                CommonAdapterItem<Commodity> commodityItem=new CommonAdapterItem();
                commodityItem.mType=COMMODITY_LIST;
                commodityItem.data=commodity;
                list.add(commodityItem);
            }
        }
        CommonAdapterItem<Integer> priceItem=new CommonAdapterItem<>();
        priceItem.mType=PRICE_INFO;
        list.add(priceItem);
        CommonAdapterItem<Integer> orderItem=new CommonAdapterItem<>();
        orderItem.mType=ORDER_INFO;
        list.add(orderItem);
        return list;
    }

    public void setListener(OnItemClickListener listener){
        mListener=listener;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        final int orderCode=mOrder.getOrderCode();
        final Context context=holder.itemView.getContext();
        int type=getItemViewType(position);

        switch (type){
            case ORDER_STATE:
            case ORDER_SHOP:
                TextView stateView=holder.get(R.id.order_state);
                final String orderStatus=mOrder.getOrderStatus();
                final TextView desView = holder.get(R.id.order_des);
                final TextView actionView =holder.get(R.id.action_view);
                stateView.setText(orderStatus);
                if(orderCode== OrderDetailInfo.UNPAID) {
                    actionView.setVisibility(View.VISIBLE);
                    actionView.setText(Util.getString(R.string.pay_immediately));
                    long left = mOrder.getOrderLeftTime();
                    mTimer = new CountDownTimer(left, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            String content = Util.getString(R.string.reserve_time) + Util.convertTime(millisUntilFinished);
                            desView.setText(content);
                        }
                        @Override
                        public void onFinish() {
                            KLog.d("订单已过期");
                        }
                    };
                    mTimer.start();
                    actionView.setOnClickListener(v ->
                            SelectPaymentActivity.start(context, mOrder.orderNo, Constants.ORDER_DETAIL, mOrder.payableAmount)
                    );
                }
                desView.setText(mOrder.getOrderDesc());
                holder.<TextView>get(R.id.address_view).setText(mOrder.getAddress());
                //
                if(type==ORDER_STATE){
                    if(mOrder.shippingMethod.equals("11") && (orderCode==OrderDetailInfo.UNRECEIVED || orderCode==OrderDetailInfo.RECEIVED)){
                        actionView.setText(Util.getString(R.string.track));
                        actionView.setVisibility(View.VISIBLE);
                        if(orderCode==OrderDetailInfo.UNRECEIVED){
                            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getDeliverInfo(mOrder.orderNo).enqueue(new NetCallback<DeliverInfo>() {
                                @Override
                                public void onSucceed(DeliverInfo data) {
                                    if(data!=null){
                                        if(data.shippingInfo!=null && !data.shippingInfo.isEmpty()){
                                            desView.setText(data.shippingInfo.get(0).context);
                                        }
                                    }
                                }
                            });
                        }
                        actionView.setOnClickListener(v-> Util.startActivity(context,LogisticsActivity.class,Constants.ORDER_NO,mOrder.orderNo));
                    }
                    holder.<TextView>get(R.id.contact_view).setText(mOrder.getContactInfo());
                } else {
                    if(orderCode==OrderDetailInfo.TRANSPORT){
                        actionView.setVisibility(View.VISIBLE);
                        actionView.setText("提货码");
                        actionView.setOnClickListener(v -> {
                            QRCodeActivity.start(context,mOrder.orderNo);
                        });
                    }
                    holder.<TextView>get(R.id.shop_view).setText("门店自提");
                }
                break;

            case COMMODITY_LIST:

                final Commodity commodity=(Commodity)mContent.get(position).data;
                holder.itemView.setOnClickListener(view-> CommodityDetailActivity.start(context,commodity.productId,mOrder.shop));
                holder.<TextView>get(R.id.price_view).setText("￥"+commodity.salePrice);
                ImageView iconView=holder.get(R.id.icon_view);

                Glide.with(context).load(commodity.skuThumbnail)
                        .placeholder(new ColorDrawable(Color.BLACK))
                        .crossFade()
                        .into(iconView);
                if(commodity.isPreSale()){
                    holder.get(R.id.pre_sale).setVisibility(View.VISIBLE);
                }

                if(commodity.skuColor!=null){
                    try{
                        holder.<TextView>get(R.id.color_view).setBackgroundColor(Color.parseColor(commodity.skuColor));
                    }catch (IllegalArgumentException e){
                        holder.<TextView>get(R.id.color_view).setText(commodity.skuColor);
                    }
                }

                holder.<TextView>get(R.id.name_view).setText(commodity.getName());
                holder.<TextView>get(R.id.quantity_view).setText("x"+commodity.quantity);
                TextView sizeView=holder.get(R.id.size_view);
                if(commodity.skuSize!=null){
                    sizeView.setText(commodity.skuSize);
                }else {//sizeView.setText(commodity.getSkuDate());
                }

                if((commodity.asdesc!=null && !commodity.asdesc.isEmpty()) || commodity.isas==1){
                    holder.get(R.id.refund_layout).setVisibility(View.VISIBLE);
                    if(commodity.isas==0) {
                        holder.get(R.id.refund_btn).setVisibility(View.GONE);
                        holder.<TextView>get(R.id.refund_tips).setText("" + commodity.asdesc);
                    }
                } else {
                    holder.get(R.id.refund_layout).setVisibility(View.GONE);
                }
                //holder.get(R.id.refund_layout).setVisibility(commodity.isas==1?View.VISIBLE:View.GONE);
                holder.get(R.id.refund_layout).setOnClickListener(v -> {
                    if(commodity.ascount>0 &&commodity.isas==1){
                        ApplyRefundActivity.start(context,commodity,mOrder.address);
                    }else if(mOrder.aas==1){
                        if(mListener!=null){
                            mListener.onItemClick(null,position,commodity);
                        }
                    }
                });
                break;

            case PRICE_INFO:
                holder.<TextView>get(R.id.transport_price).setText("￥"+mOrder.deliveryAmount);
                holder.<TextView>get(R.id.commodity_price).setText("￥"+mOrder.totalAmount);
                holder.<TextView>get(R.id.discount_view).setText("-￥"+mOrder.discountAmount);
                holder.get(R.id.method_layout).setVisibility(View.VISIBLE);
                holder.<TextView>get(R.id.method_view).setText(mOrder.getShip());
                holder.get(R.id.total_layout).setVisibility(View.VISIBLE);
                holder.<TextView>get(R.id.total_price).setText("￥"+mOrder.payableAmount);
                break;

            case ORDER_INFO:
                TextView codeView=holder.get(R.id.orders_code);
                TextView orderTimeView=holder.get(R.id.orders_time);
                TextView checkView=holder.get(R.id.check_time);
                TextView methodView=holder.get(R.id.check_method);
                TextView action=holder.get(R.id.action_view);
                codeView.setText(String.format(context.getString(R.string.order_code),mOrder.orderNo));
                orderTimeView.setText(Util.getString(R.string.order_time)+Util.getFormatDate(mOrder.createTime));

                if(orderCode== OrderDetailInfo.UNPAID){
                    action.setVisibility(View.VISIBLE);
                    action.setOnClickListener(v -> {
                        mListener.onItemClick(v,position,null);
                    });
                }else if(orderCode!= OrderDetailInfo.CANCEL){
                    checkView.setText(Util.getString(R.string.check_time)+ Util.getFormatDate(mOrder.paidTime));
                    methodView.setVisibility(View.VISIBLE);
                    methodView.setText(Util.getString(R.string.pay_style)+mOrder.getPayStyle());
                    if(mOrder.getPayStyle().equals("no")){
                        methodView.setVisibility(View.GONE);
                    }
                    checkView.setVisibility(View.VISIBLE);
                }
//                if(orderCode== OrderDetailInfo.RECEIVED){
//                    action.setVisibility(View.VISIBLE);
//                    action.setText(Util.getString(R.string.apply));
//                    action.setOnClickListener(v->Util.startActivity(v.getContext(), HelpActivity.class));
//                }
                break;
            case CANCEL_ORDER:
                holder.<TextView>get(R.id.title_view).setText("申请退款");
                holder.<ImageView>get(R.id.icon_view).setImageResource(R.mipmap.refund_money);
                holder.<TextView>get(R.id.action_view).setText(mOrder.getApplyResult());
                holder.itemView.setOnClickListener(v -> {
                           if(mOrder.refundStatus==0){
                               CancelOrderActivity.start(context,mOrder.orderNo,mOrder.address);
                           }else {
                               CancelProgressActivity.start(context,mOrder.refundNo);
                           }
                        });
                break;
            case REFUND_INFO:
                holder.<TextView>get(R.id.title_view).setText("查看售后");
                holder.<ImageView>get(R.id.icon_view).setImageResource(R.mipmap.refund_icon);
                holder.itemView.setOnClickListener(v -> {
                    RefundHistoryActivity.start(context,mOrder.orderNo);
                });
                break;
        }
    }

    public void release(){
        if(mTimer!=null){
            mTimer.cancel();
        }
    }

    @Override
    public int getItemCount() {
        if(mOrder==null){
            return 0;
        }
        return mContent.size();
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        int layoutId;
        switch (viewType){
            case ORDER_SHOP:
                layoutId=R.layout.viewholder_shop_order;
                break;
            case ORDER_STATE:
                layoutId=R.layout.viewholder_order_state;
                break;
            case COMMODITY_LIST:
                layoutId=R.layout.viewholder_settlement;
                break;
            case PRICE_INFO:
                layoutId=R.layout.viewholder_order_price;
                break;
            case ORDER_INFO:
                layoutId=R.layout.viewholder_order_info;
                break;
            case CANCEL_ORDER:
            case REFUND_INFO:
                layoutId=R.layout.viewholder_order_action;
                break;
            default:
                layoutId=R.layout.viewholder_order_info;

        }
        view=LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        return new CommonViewHolder(view);
    }
}
