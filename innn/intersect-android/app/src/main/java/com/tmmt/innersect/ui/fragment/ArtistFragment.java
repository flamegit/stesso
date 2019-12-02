//package com.tmmt.innersect.ui.fragment;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.google.android.flexbox.FlexboxLayout;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.Constants;
//import com.tmmt.innersect.mvp.model.CommonData;
//import com.tmmt.innersect.ui.activity.InfoDetailActivity;
//import com.tmmt.innersect.utils.Util;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class ArtistFragment extends Fragment {
//
//    public static final String ARTIST_LIST="artist_list";
//
//    public static Fragment getInstance(List<CommonData> dataList){
//        Fragment fragment=new ArtistFragment();
//        Bundle bundle=new Bundle();
//        bundle.putParcelableArrayList(ARTIST_LIST,new ArrayList(dataList));
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    public ArtistFragment() {}
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View itemView = inflater.inflate(R.layout.viewpager_flexlayout, null);
//        FlexboxLayout layout = (FlexboxLayout) itemView.findViewById(R.id.flex_layout);
//        List<CommonData> dataList=getArguments().getParcelableArrayList(ARTIST_LIST);
//        if(dataList!=null){
//            int end=dataList.size();
//            int size=Math.min(end,2);
//            fillView(dataList,layout,size,end);
//        }
//        return itemView;
//    }
//
//    private void fillView(List<CommonData> dataList, ViewGroup parent, int count, int end) {
//        int padding = Util.dip2px(16);
//        int w = Util.getWindowWidth() -  padding;
//        int size = w / count;
//        for (int i = 0; i < end; i++) {
//            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(size, size);
//            View itemView;
//            final CommonData data=dataList.get(i);
//            itemView = LayoutInflater.from(getContext()).inflate(R.layout.grid_view_item2, null);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(data.url!=null &&data.url.length()>4){
//                        Intent intent=new Intent(getContext(),InfoDetailActivity.class);
//                        intent.putExtra(Constants.INFO_ID,data.id);
//                        getContext().startActivity(intent);
//                    }
//
//                }
//            });
//            ImageView imageView = (ImageView) itemView.findViewById(R.id.image_view);
//            loadImage(getContext(),data.url,imageView);
//            parent.addView(itemView, layoutParams);
//        }
//    }
//
//    private void loadImage(Context context,String picUrl,ImageView imageView){
//        if(picUrl.endsWith(".gif")){
//            Glide.with(context)
//                    .load(picUrl)
//                    .asGif()
//                    .dontAnimate()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)// DiskCacheStrategy.NONE
//                    .placeholder(R.mipmap.white_logo)
//                    .into(imageView);
//        }else{
//            Glide.with(context)
//                    .load(picUrl)
//                    .crossFade()
//                    .skipMemoryCache(true)
//                    .placeholder(R.mipmap.white_logo)
//                    .into(imageView);
//        }
//    }
//
//}
