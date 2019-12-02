package com.tmmt.innersect.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.IHeaderView;
import com.lcodecore.tkrefreshlayout.OnAnimEndListener;
import com.tmmt.innersect.R;
import com.tmmt.innersect.utils.Util;

/**
 * Created by flame on 2017/8/4.
 */

public class RefreshHeader extends LinearLayout implements IHeaderView {

    private ImageView mLogo;//刷新动画视图
    private TextView mTextView;

    public RefreshHeader(Context context) {
        super(context);
        mLogo=new ImageView(context);
        setBackgroundColor(Color.WHITE);
        mLogo.setImageResource(R.mipmap.logo_refresh);
        int padding = Util.dip2px(5);
        mLogo.setPadding(padding,padding,padding,padding);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        mTextView=new TextView(context);
        mTextView.setText(context.getString(R.string.pull_down_refresh));
        mTextView.setTextSize(12);
        mTextView.setPadding(0,0,0,padding);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0);
        layoutParams.weight=1;
        addView(mLogo,layoutParams);
        LinearLayout.LayoutParams textParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(mTextView,textParams);
    }

    @Override
    public void onFinish(OnAnimEndListener animEndListener) {
        animEndListener.onAnimEnd();

    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        mTextView.setText(Util.getString(R.string.loading));
        mLogo.setScaleX(1);
        mLogo.setScaleY(1);
    }

    @Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        mLogo.setScaleX(fraction);
        mLogo.setScaleY(fraction);
        if(fraction>=1f){
            mTextView.setText(Util.getString(R.string.release_refresh));
        }

    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {

    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void reset() {
        mTextView.setText(Util.getString(R.string.pull_down_refresh));
    }
}
