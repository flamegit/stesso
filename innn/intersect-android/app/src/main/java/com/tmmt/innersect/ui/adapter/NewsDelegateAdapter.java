package com.tmmt.innersect.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.CommonAdapterItem;
import com.tmmt.innersect.mvp.model.Information;
import com.tmmt.innersect.ui.activity.InfoDetailActivity;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;
import com.tmmt.innersect.utils.Util;

/**
 * Created by flame on 2017/7/24.
 */

public class NewsDelegateAdapter implements ViewTypeDelegateAdapter {

    @Override
    public void onBindViewHolder(CommonViewHolder holder, CommonAdapterItem item) {
        Information data=(Information)item.data;
        holder.<TextView>get(R.id.title_view).setText(data.title);
        holder.<TextView>get(R.id.time_view).setText(data.getTime());
        final Context context = holder.itemView.getContext();
        ImageView imageView = holder.get(R.id.info_image);
        Util.loadImage(context,data.imgsrc,imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,InfoDetailActivity.class);
                intent.putExtra(Constants.INFO_ID,data.id);
                intent.putExtra(Constants.IS_INFO,true);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent) {
        return null;
    }

    public NewsDelegateAdapter(int type){
        //mType=type;
    }

}
