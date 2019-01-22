package com.stesso.android.widget;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stesso.android.R;


/**
 * Quantity view to add and remove quantities
 */
public class QuantityView extends LinearLayout implements View.OnClickListener {
    private ImageView mAddView, mRemoveView;
    private TextView mTextViewQuantity;
    int quantity,maxQuantity,hintCount;


    public interface OnQuantityChangeListener {
        void onQuantityChanged(int newQuantity, boolean programmatically);
        void onLimitReached();
        void onMinReached();
    }
    private OnQuantityChangeListener onQuantityChangeListener;
    public QuantityView(Context context) {
        super(context);
        init();
    }
    public QuantityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public int getHintCount(){
        return hintCount;
    }

    public void setHintCount(int count){
        hintCount=count;
    }

    public void setMaxQuantity(int max){
        maxQuantity=max;
    }

    public void setQuantityChangeListener(OnQuantityChangeListener listener){
        onQuantityChangeListener=listener;
    }
    private void init() {
        maxQuantity=50;
        hintCount=0;
        quantity=1;
        int dp5 = pxFromDp(5);
        int dp20 = pxFromDp(20);
        mAddView = new ImageView(getContext());
        mAddView.setImageResource(R.drawable.ic_add_black_24dp);
        mAddView.setPadding(dp5, dp5, dp5, dp5);
        mAddView.setBackgroundColor(Color.WHITE);
        mRemoveView = new ImageView(getContext());
        mRemoveView.setPadding(dp5, dp5, dp5, dp5);
        mRemoveView.setImageResource(R.drawable.ic_remove_black_24dp);
        mRemoveView.setBackgroundColor(Color.WHITE);
        mTextViewQuantity = new TextView(getContext());
        mTextViewQuantity.setPadding(dp20,dp5,dp20,dp5);
        mTextViewQuantity.setGravity(Gravity.CENTER);
        mTextViewQuantity.setTextSize(15);
        mTextViewQuantity.setText(quantity+"");
        mTextViewQuantity.setBackgroundColor(Color.WHITE);

        LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(2,2,2,2);
        LayoutParams params2=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params2.setMargins(0,2,0,2);
        setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setOrientation(HORIZONTAL);
        addView(mRemoveView,params);
        addView(mTextViewQuantity, params2);
        addView(mAddView, params);
        mAddView.setOnClickListener(this);
        mRemoveView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(!isEnabled()){
            return;
        }
        if (v == mAddView) {
            if (quantity + 1 > maxQuantity) {
                if (onQuantityChangeListener != null) onQuantityChangeListener.onLimitReached();
            } else {
                quantity += 1;
                mTextViewQuantity.setText(String.valueOf(quantity));
                if (onQuantityChangeListener != null)
                    onQuantityChangeListener.onQuantityChanged(quantity, false);
            }
        } else if (v == mRemoveView) {
            if (quantity <=1) {
                if (onQuantityChangeListener != null) onQuantityChangeListener.onMinReached();
            } else {
                quantity -= 1;
                mTextViewQuantity.setText(String.valueOf(quantity));
                if (onQuantityChangeListener != null)
                    onQuantityChangeListener.onQuantityChanged(quantity, false);
            }
        }
    }
    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        mTextViewQuantity.setText(String.valueOf(this.quantity));
    }

    private int pxFromDp(final float dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

}