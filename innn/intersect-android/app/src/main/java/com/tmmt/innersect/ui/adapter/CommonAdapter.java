package com.tmmt.innersect.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tmmt.innersect.mvp.model.Address;
import com.tmmt.innersect.ui.activity.AddressActivity;
import com.tmmt.innersect.ui.adapter.viewholder.AddressViewHolder;
import com.tmmt.innersect.ui.adapter.viewholder.BaseViewHolder;
import com.tmmt.innersect.utils.AnalyticsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/4.
 */
public class CommonAdapter<T> extends RecyclerView.Adapter<BaseViewHolder<T>> {

    public static final int ADDRESS_TYPE=3;
    //public static final int LOGISTICS_TYPE=6;

    public interface Report<T>{
        void callback(T data);
    }

    public interface AddressCallback{
        void removeAddress(int id, boolean isLast);
        void setDefault(int id);
    }

    private List<T> mContent;
    protected int mLayoutId;
    protected int mViewHolderType;
    private Report mReport;

    private AddressCallback mAddressCallback;

    public void setReport(Report report){
        mReport=report;
    }

    public void setAddressCallback(AddressCallback callback){
        mAddressCallback=callback;
    }

    public CommonAdapter(int viewHolderType,int layoutId){
        mViewHolderType=viewHolderType;
        mLayoutId=layoutId;
        mContent=new ArrayList<>();
    }
    public void addItems( List<T> items){
        if(mContent.size()>0){
            mContent.clear();
        }
        if(items==null ||items.isEmpty()){
            notifyDataSetChanged();
            return;
        }
        mContent.addAll(items);
        notifyItemRangeInserted(0,items.size());
    }

    public void setContent(List<T> items){
        mContent=items;
    }

    @Override
    public int getItemCount() {
        if(mContent==null){
            return 0;
        }
        return mContent.size();
    }
    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {

        holder.bindViewHolder(mContent.get(position),position);
        final Context context=holder.itemView.getContext();
        //Addresss
        if(mViewHolderType==ADDRESS_TYPE){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mReport!=null){
                        mReport.callback(mContent.get(position));
                    }
                }
            });
            final AddressViewHolder viewHolder=(AddressViewHolder) holder;
            viewHolder.deleteView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Address address=(Address)mContent.get(position);
                    viewHolder.swipeLayout.smoothCloseMenu();
                    mContent.remove(position);
                    if(address.isDefault()){
                        if(mContent.size()>0){
                            Address first=(Address)mContent.get(0);
                            first.setDefault(1);
                        }
                    }
                    if(mAddressCallback!=null){
                        mAddressCallback.removeAddress(address.getId(),mContent.isEmpty());
                    }
                    //TODO
                    notifyItemRemoved(position);
                    notifyDataSetChanged();

                }
            });
            if(viewHolder.checkBox.isChecked()){
                viewHolder.checkBox.setEnabled(false);
            }else {
                viewHolder.checkBox.setEnabled(true);
            }
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(viewHolder.checkBox.isChecked()){
                        for(int i=0;i<mContent.size();i++){
                            Address address=(Address)mContent.get(i);
                            address.setDefault(0);
                        }
                        Address address=(Address)mContent.get(position);
                        address.setDefault(1);
                        if(mAddressCallback!=null){
                            mAddressCallback.setDefault(address.getId());
                        }
                        notifyDataSetChanged();
                    }
                }
            });

            viewHolder.editView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Address address=(Address)mContent.get(position);
                    Intent intent=new Intent(AddressActivity.ACTION_EDIT);
                    intent.putExtra(AddressActivity.ADDRESS_KEY,address);
                    intent.putExtra(AddressActivity.ONLY_ONE,mContent.size()==1);
                    AnalyticsUtil.reportEvent(AnalyticsUtil.ADDRESS_EDIT);
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(mLayoutId,parent,false);
        switch (mViewHolderType){
            case ADDRESS_TYPE:
                return new AddressViewHolder(view);
            //case LOGISTICS_TYPE:
                //return new LogisticsViewHolder(view);
        }
        return new AddressViewHolder(view);
    }
}
