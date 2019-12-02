//package com.tmmt.innersect.widget;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//
//import com.bumptech.glide.load.Transformation;
//import com.bumptech.glide.load.engine.Resource;
//
//import java.security.MessageDigest;
//
//
///**
// * Created by flame on 2017/6/13.
// */
//
//public class GlideTransformation implements Transformation {
//
//    int mTargetWidth;
//
//    public PicassoTransformation(int width){
//        mTargetWidth=width;
//    }
//
//    @Override
//    public Resource transform(Context context, Resource resource, int outWidth, int outHeight) {
//        return null;
//    }
//
//    @Override
//    public void updateDiskCacheKey(MessageDigest messageDigest) {
//
//    }
//
//
//    public Bitmap transform(Bitmap source) {
//
//        if (source.getWidth() == 0) {
//            return source;
//        }
//        double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
//
//        int targetHeight = (int) (mTargetWidth * aspectRatio);
//
//        if (targetHeight != 0 && mTargetWidth != 0) {
//            Bitmap result = Bitmap.createScaledBitmap(source, mTargetWidth, targetHeight, false);
//            if (result != source) {
//                // Same bitmap is returned if sizes are the same
//                source.recycle();
//            }
//            return result;
//        } else {
//            return source;
//        }
//
//    }
//
//    public String key() {
//        return "transformation" + " desiredWidth";
//    }
//
//}
