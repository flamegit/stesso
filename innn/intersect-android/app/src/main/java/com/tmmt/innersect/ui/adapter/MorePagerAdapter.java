//package com.tmmt.innersect.ui.adapter;
//
//
//
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.mvp.model.MoreInfo;
//import com.tmmt.innersect.utils.Util;
//
///**
// * Created by Administrator on 2016/8/13.
// */
//public class MorePagerAdapter extends CommonPagerAdapter<MoreInfo> {
//
//    public MorePagerAdapter(boolean loop){
//        super(loop);
//    }
//
//    @Override
//    public Object instantiateItem(final ViewGroup container, int position) {
//        ImageView imageView;
//        int size=mContent.size();
//        final int p=position%size;
//        imageView=new ImageView(container.getContext());
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Util.openTarget(container.getContext(),mContent.get(p).schema,"");
//            }
//        });
//        Glide.with(container.getContext()).load(mContent.get(p).img)
//                .error(R.mipmap.ticket_share)
//                .into(imageView);
//
//        container.addView(imageView);
//        return imageView;
//    }
//}