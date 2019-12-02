//package com.tmmt.innersect.widget;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffXfermode;
//import android.graphics.drawable.Drawable;
//import android.util.AttributeSet;
//import android.view.View;
//
//import com.tmmt.innersect.R;
//
//
//public class CircleSpreadView extends View {
//
//    private Paint mPaint;
//    private Drawable mBackground;
//
//    private int radius;
//
//    public CircleSpreadView(Context context) {
//        this(context, null);
//    }
//
//    public CircleSpreadView(Context context, AttributeSet attrs) {
//        this(context, attrs, -1);
//    }
//
//    public CircleSpreadView(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init();
//        radius=0;
//    }
//
//    public void setRadius(int r){
//        radius=r;
//        invalidate();
//    }
//
//    public int getRadius(){
//        return radius;
//    }
//
//    private void init() {
//        mPaint = new Paint();
//        mPaint.setColor(Color.BLACK);
//        mPaint.setAntiAlias(true);
//        mBackground=getResources().getDrawable(R.mipmap.vlone_show_bg);
//
//
//    }
//
//    @Override
//    public void onDraw(Canvas canvas) {
//        int height=getHeight();
//        int width=getWidth();
//        int save = canvas.saveLayer(0,0,width,height,null,Canvas.ALL_SAVE_FLAG);
//        mBackground.setBounds(0,0,width,height);
//        mBackground.draw(canvas);
//
//        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
//        canvas.drawCircle(width/2,height/2,radius,mPaint);
//        canvas.restoreToCount(save);
//    }
//
//}
//
//
//
//
