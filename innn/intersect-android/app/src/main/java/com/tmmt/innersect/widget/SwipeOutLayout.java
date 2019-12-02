/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.tmmt.innersect.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class SwipeOutLayout extends FrameLayout {

    private final ViewDragHelper dragger;
    private final float minimumFlingVelocity;

    private float mSwipeLimit;

    private float mSwipeBack;

    public SwipeOutLayout(Context context) {
        this(context, null);
    }

    public SwipeOutLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeOutLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dragger = ViewDragHelper.create(this, 1f / 8f, new ViewDragCallback()); // 1f / 8f是敏感度参数参数越大越敏感
        minimumFlingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSwipeLimit = w / 4;
        mSwipeBack = mSwipeLimit / 4;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return dragger.shouldInterceptTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        try {
            dragger.processTouchEvent(event);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void computeScroll() {
        if (dragger.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return Math.min(0, left);
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            //return Math.max(0, top);
            return 0;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return (int) (mSwipeLimit * 1.2);
        }




        @Override
        public int getViewVerticalDragRange(View child) {
            return 0;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {

        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            float fling=Math.abs(xvel);
            int direction=0;
            if(xvel<0 && fling>minimumFlingVelocity){
                direction=1;
            }
            if(xvel>0 && fling>minimumFlingVelocity){
                direction=2;
            }
            if (Math.abs(releasedChild.getLeft()) <= mSwipeBack || direction==2) {
                dragger.settleCapturedViewAt(0, 0);
            }
            if(Math.abs(releasedChild.getLeft()) > mSwipeBack || direction==1){
                dragger.settleCapturedViewAt(-(int) mSwipeLimit, 0);
            }
            postInvalidate();
        }
    }


}
