//package com.tmmt.innersect.ui.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.util.SparseArray;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.Constants;
//import com.tmmt.innersect.mvp.model.Information;
//import com.tmmt.innersect.ui.activity.InfoDetailActivity;
//import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;
//import com.tmmt.innersect.utils.Util;
//
///**
// * Created by flame on 2017/7/24.
// */
//
//public class NewsAdapter2 extends MultiTypeAdapter<Information> {
//
//
//
//    public NewsAdapter2(SparseArray<Integer> array){
//
//        super(array,((holder, position, data) -> {
//            if(data.mType==Constants.FOOTER){
//
//            }else {
//                Information infor=data.data;
//                holder.<TextView>get(R.id.title_view).setText(infor.title);
//                holder.<TextView>get(R.id.time_view).setText(infor.getTime());
//                final Context context = holder.itemView.getContext();
//                ImageView imageView = holder.get(R.id.info_image);
//                Util.fillImage(context,infor.imgsrc,imageView);
//                holder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent(context,InfoDetailActivity.class);
//                        intent.putExtra(Constants.INFO_ID,infor.id);
//                        intent.putExtra(Constants.IS_INFO,true);
//                        context.startActivity(intent);
//                    }
//                });
//            }
//
//        }));
//    }
//
//
//    @Override
//    public void onBindViewHolder(CommonViewHolder holder, final int position) {
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mListener!=null){
//                    mListener.onItemClick(position,mContent.get(position));
//                }
//            }
//        });
//
//        if(getItemViewType(position)==3){
//            return;
//        }
//        fillViewHolder(holder,position,mContent.get(position));
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if(showFooter && position==getItemCount()-1){
//            return 3;
//        }
//        if((position+1)%4==0 && mType==0){
//            return 2;
//        }
//        return 1;
//    }
//
//    public long getLastTimestamp() {
//        if (mContent == null || mContent.isEmpty()) {
//            return System.currentTimeMillis();
//        }
//        int size = mContent.size();
//        return mContent.get(size - 1).ptime;
//    }
//}
