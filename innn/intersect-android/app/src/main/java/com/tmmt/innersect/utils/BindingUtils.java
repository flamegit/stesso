package com.tmmt.innersect.utils;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class BindingUtils {

   @BindingAdapter ({"imageUrl"})
   public static void loadImage(ImageView view, String url) {
      Glide.with(view.getContext()).load(url).into(view);
   }

   @BindingAdapter({"android:src"})
   public static void setImageViewResource(ImageView imageView, int resource) {
      imageView.setImageResource(resource);
   }
}
