//package com.tmmt.innersect.widget;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.support.annotation.NonNull;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.scwang.smartrefresh.layout.api.RefreshHeader;
//import com.scwang.smartrefresh.layout.api.RefreshKernel;
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.constant.RefreshState;
//import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.utils.Util;
//
//
//public class CustomHeader extends LinearLayout implements RefreshHeader {
//
//    private ImageView mLogo;//刷新动画视图
//    private TextView mTextView;
//
//    public CustomHeader(Context context) {
//        super(context);
//        mLogo=new ImageView(context);
//        setBackgroundColor(Color.BLACK);
//        mLogo.setImageResource(R.mipmap.logo_refresh);
//        int padding = Util.dip2px(5);
//        mLogo.setPadding(padding,padding,padding,padding);
//        setOrientation(VERTICAL);
//        setGravity(Gravity.CENTER_HORIZONTAL);
//        mTextView=new TextView(context);
//        mTextView.setText("松开以刷新");
//        mTextView.setTextSize(12);
//        mTextView.setPadding(0,0,0,padding);
//        mTextView.setTextColor(Color.WHITE);
//        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0);
//        layoutParams.weight=1;
//        addView(mLogo,layoutParams);
//        LinearLayout.LayoutParams textParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//        addView(mTextView,textParams);
//    }
//
//    @NonNull
//    public View getView() {
//        return this;//真实的视图就是自己，不能返回null
//    }
//    @Override
//    public SpinnerStyle getSpinnerStyle() {
//        return SpinnerStyle.Scale;
//    }
//    @Override
//    public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {
//        //mProgressDrawable.start();//开始动画
//    }
//    @Override
//    public int onFinish(RefreshLayout layout, boolean success) {
//
//        return 500;//延迟500毫秒之后再弹回
//    }
//
//    @Override
//    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
//        switch (newState) {
//            case None:
//            case PullDownToRefresh:
//                mTextView.setText("松开以刷新");
//                break;
//            case Refreshing:
//                mTextView.setText("刷新中...");
//
//                break;
//            case ReleaseToRefresh:
//                break;
//        }
//    }
//    @Override
//    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {}
//    @Override
//    public void onPullingDown(float percent, int offset, int headHeight, int extendHeight) {
//    }
//    @Override
//    public void onReleasing(float percent, int offset, int headHeight, int extendHeight) {
//        mTextView.setText("刷新中...");
//    }
//    @Override
//    public void setPrimaryColors(int... colors){}
//}