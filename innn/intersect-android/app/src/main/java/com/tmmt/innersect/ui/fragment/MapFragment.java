//package com.tmmt.innersect.ui.fragment;
//
//import android.view.View;
//import android.widget.ImageView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.Constants;
//
//import com.tmmt.innersect.utils.Util;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
//
///**
// * Created by flame on 2017/4/12.
// */
//
//public class MapFragment extends BaseFragment {
//
//    @BindView(R.id.map_view)
//    ImageView mapView;
//
//    @Override
//    int getLayout() {
//        return R.layout.fragment_map;
//    }
//
//    @Override
//    protected void initView(View view) {
//        super.initView(view);
//        Glide.with(getContext()).load(Constants.MAP_URL).diskCacheStrategy(DiskCacheStrategy.NONE).into(mapView);
//    }
//
//    @OnClick(R.id.map_view)
//    void open(){
//        Util.startActivity(getContext(), MapActivity.class);
//    }
//
//}
