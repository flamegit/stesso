//package com.stesso.android.lib;
//
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v4.view.ViewCompat;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.animation.AccelerateInterpolator;
//import android.widget.TextView;
//
//
///**
// * Created by Administrator on 2016/10/18.
// */
//
//public class ScrollBehavior extends CoordinatorLayout.Behavior<TextView> {
//    private boolean isShow;
//
//    private int scrollY=0;
//
//    public ScrollBehavior(Context context, AttributeSet attrs){
//        super();
//    } //
//
//
//    @Override
//    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
//        return axes == ViewCompat.SCROLL_AXIS_VERTICAL||super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
//    }
//
//    @Override
//    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull TextView child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
//        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
//
////        if(dyConsumed<0){
////            show(child);
////        }
////        else if(dyConsumed>0){
////            hide(child);
////        }
//        scrollY+=dyConsumed;
//
//        child.setAlpha(scrollY/200f);
//
//    }
//
//    private void hide(View view){
//        if(!isShow) return;
//        isShow=false;
//        ViewCompat.animate(view)
//                .translationY(view.getHeight())
//                .alpha(0f)
//                .setInterpolator(new AccelerateInterpolator())
//                .setListener(null);
//    }
//
//    private void show(View view){
//        if(isShow) return;
//        isShow=true;
//        ViewCompat.animate(view)
//                .translationY(0)
//                .alpha(1f)
//
//                .setInterpolator(new AccelerateInterpolator())
//                .setListener(null);
//    }
//}