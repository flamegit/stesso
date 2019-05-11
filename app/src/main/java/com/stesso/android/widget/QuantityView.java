package com.stesso.android.widget;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
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
        void onQuantityChanged(int newQuantity, boolean programmatically,boolean minus);
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

    public void setQuantityChangeListener(OnQuantityChangeListener listener){
        onQuantityChangeListener=listener;
    }
    private void init() {
        maxQuantity=50;

        int dp8 = pxFromDp(8);
        int dp20 = pxFromDp(20);
        int dp36 = pxFromDp(36);

        mAddView = new ImageView(getContext());
        mAddView.setImageResource(R.drawable.ic_add_black_24dp);
        mAddView.setBackground(getResources().getDrawable(R.drawable.circl_gray_bg));
        mRemoveView = new ImageView(getContext());
        mRemoveView.setImageResource(R.drawable.ic_remove_black_24dp);
        mRemoveView.setBackground(getResources().getDrawable(R.drawable.circl_gray_bg));

        mTextViewQuantity = new TextView(getContext());
        mTextViewQuantity.setGravity(Gravity.CENTER);
        mTextViewQuantity.setTextSize(15);
        setQuantity(1);
        mTextViewQuantity.setBackgroundColor(getResources().getColor(R.color.space));

        LayoutParams params=new LayoutParams(dp36, dp20);
        params.setMargins(dp8,0,dp8,0);

        setOrientation(HORIZONTAL);

        addView(mRemoveView,new LayoutParams(dp20, dp20));
        addView(mTextViewQuantity, params);
        addView(mAddView, new LayoutParams(dp20, dp20));

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
                    onQuantityChangeListener.onQuantityChanged(quantity, false,false);
            }
        } else if (v == mRemoveView) {
            if (quantity <=1) {
                if (onQuantityChangeListener != null) onQuantityChangeListener.onMinReached();
            } else {
                quantity -= 1;
                mTextViewQuantity.setText(String.valueOf(quantity));
                if (onQuantityChangeListener != null)
                    onQuantityChangeListener.onQuantityChanged(quantity, false,true);
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