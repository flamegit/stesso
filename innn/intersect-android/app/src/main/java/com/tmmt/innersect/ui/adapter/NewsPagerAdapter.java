//package com.tmmt.innersect.ui.adapter;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.mvp.model.TopNews;
//import com.tmmt.innersect.ui.adapter.CommonPagerAdapter;
//import com.tmmt.innersect.utils.Util;
//
///**
// * Created by Administrator on 2016/8/13.
// */
//public class NewsPagerAdapter extends CommonPagerAdapter<TopNews> {
//
//    public NewsPagerAdapter(){
//        super(false);
//    }
//
//    @Override
//    public Object instantiateItem(final ViewGroup container, int position) {
//        ImageView imageView;
//        int size=mContent.size();
//        final int p=position%size;
//        View view= LayoutInflater.from(container.getContext()).inflate(R.layout.included_card_news,container,false);
//        TextView textView=view.findViewById(R.id.title_view);
//        textView.setText(mContent.get(p).title);
//        imageView=view.findViewById(R.id.image_view);
//        Util.loadImage(container.getContext(),mContent.get(p).pic,imageView);
//        view.setOnClickListener(v -> Util.openTarget(container.getContext(),mContent.get(p).content," "));
//        container.addView(view);
//        return view;
//    }
//}