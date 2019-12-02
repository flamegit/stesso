package com.tmmt.innersect.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.Coupon;
import com.tmmt.innersect.ui.activity.CouponCommodityActivity;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.Util;
import com.tmmt.innersect.widget.GapColorDrawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by flame on 2017/8/23.
 */

public class CouponAdapter extends BaseAdapter<Coupon> {

    int[] showStatus;
    boolean mSelected;
    int preMinCount;
    int currMinCount;

    List<Coupon> mSelectCoupons;
    List<Integer> mSelectList;
    boolean mAvailiable;

    public CouponAdapter(boolean selected ,boolean availiable){
        mSelected=selected;
        mSelectCoupons=new ArrayList<>();
        mAvailiable=availiable;
    }
    public void setSelectList(List<Integer> list){
        mSelectList=list;
    }
    @Override
    public void addItems(List<Coupon> items) {
        super.addItems(items);
        showStatus=new int[getItemCount()];
        preMinCount=currMinCount=items.size();
        Arrays.fill(showStatus,View.GONE);
        if(mSelectList!=null){
            for(Coupon coupon:mContent){
                if(mSelectList.contains(coupon.cid)){
                    mSelectCoupons.add(coupon);
                }
            }
        }
    }

    public List<Coupon> getSelectCoupons(){
        return mSelectCoupons;
    }
    @Override
    public int getItemCount() {
        if(mContent.isEmpty()){
            return 0;
        }else {
            return mContent.size()+1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position!=0){
            if(position==getItemCount()-1){
                return 2;
            }
        }
        return 1;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, final int position) {
        if(getItemViewType(position)==2){
            return;
        }
        fillViewHolder(holder,position,mContent.get(position));
    }

    @Override
    protected int getLayoutId(int viewType) {
        if(viewType==1){
            return R.layout.viewholder_coupon;
        }else {
            return R.layout.viewholder_coupon_end;
        }
    }

    private int getMin(boolean include){
        int min=mContent.size();
        for(Coupon coupon:mSelectCoupons){
            if(coupon.multiuse < min){
                if(include || coupon.multiuse!=0){
                    min=coupon.multiuse;
                }
            }
        }
        return min;
    }

    @Override
    protected void fillViewHolder(CommonViewHolder holder, final int position, Coupon data) {
        Context context=holder.itemView.getContext();
        CheckBox box=holder.get(R.id.check_box);
        if(mSelected && mAvailiable){
            if(mSelectCoupons.contains(data)){
                box.setChecked(true);
            }else {
                box.setChecked(false);
            }
            box.setVisibility(View.VISIBLE);
            if(data.multiuse==0){
                if(getMin(true)>0){
                    box.setEnabled(true);
                }else {
                    if(!box.isChecked()){
                        box.setEnabled(false);
                    }
                }
            }else {
                if(getMin(true)==0){
                    if(data.multiuse >=mSelectCoupons.size() && getMin(false)>=mSelectCoupons.size()){
                        box.setEnabled(true);
                    }else {
                        if(!box.isChecked()){
                            box.setEnabled(false);
                        }
                    }
                }else {
                    if(data.multiuse > mSelectCoupons.size() && getMin(false)>mSelectCoupons.size()){
                        box.setEnabled(true);
                    }else {
                        if(!box.isChecked()){
                            box.setEnabled(false);
                        }
                    }
                }
            }
            box.setOnClickListener(v -> {
                if(box.isChecked()){
                    mSelectCoupons.add(data);
                }else {
                    mSelectCoupons.remove(data);
                }
                notifyDataSetChanged();
            });
        }

        if(!mSelected && mAvailiable){
            holder.get(R.id.use_view).setVisibility(View.VISIBLE);

            holder.itemView.setOnClickListener(v -> {
                AnalyticsUtil.reportEvent("coupon_couponlist_UseNow");
                CouponCommodityActivity.start(context,data.title,data.cid);
            });
        }
        holder.get(R.id.right_layout).setBackground(new GapColorDrawable(Color.WHITE,2));
        holder.<TextView>get(R.id.value_view).setText(Util.getFomatNum(data.discounts));
        TextView ruleView=holder.get(R.id.rule_view);
        View letLayout=holder.get(R.id.left_layout);
        //TODO translate
        if(data.fullAmount==0){
            ruleView.setText("下单立减");
        }else {
            ruleView.setText("满"+Util.getFomatNum(data.fullAmount)+"元使用");
        }
        holder.<TextView>get(R.id.name_view).setText(data.title);
        holder.<TextView>get(R.id.time_view).setText(data.getTime());
        holder.<TextView>get(R.id.scope_view).setText(data.getScope());

        if(isUsable(data)){
            letLayout.setBackground(new GapColorDrawable(Color.parseColor("#f8e638"),1));
            ruleView.setBackgroundColor(Color.parseColor("#ead400"));
            if(data.multiuse>0){
                holder.get(R.id.over_lay).setVisibility(View.VISIBLE);
            }else {
                holder.get(R.id.over_lay).setVisibility(View.GONE);
            }
        }else {
            letLayout.setBackground(new GapColorDrawable(Color.parseColor("#D6DBE0"),1));
            ruleView.setBackgroundColor(Color.parseColor("#BEC6CC"));
            if(data.status==2||data.status==1){
                holder.<ImageView>get(R.id.status_view).setImageResource(R.mipmap.used_indicator);
            }else if(data.status==0){
                holder.<ImageView>get(R.id.status_view).setVisibility(View.GONE);
            }else {
                holder.<ImageView>get(R.id.status_view).setImageResource(R.mipmap.expire_icon);
            }
        }
        final TextView descView=holder.get(R.id.desc_view);
        final ImageView showDetail=holder.get(R.id.show_detail);
        descView.setVisibility(showStatus[position]);
        descView.setText(data.ruleDesc);
        showDetail.setImageResource(showStatus[position]==View.GONE?R.mipmap.down_indicator:R.mipmap.up_indicator);
        showDetail.setOnClickListener(
            v-> {
                if(descView.getVisibility()==View.VISIBLE){
                    showStatus[position]=View.GONE;
                }else {
                    showStatus[position]=View.VISIBLE;
                }
                notifyItemChanged(position);
            });
    }

    private boolean isUsable(Coupon data){
        if(mSelected){
            return data.usable==0;
        }else {
            return mAvailiable;
        }
    }
}
