package com.tmmt.innersect.ui.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by flame on 2017/9/4.
 */

public class CommonViewHolder extends RecyclerView.ViewHolder {

    SparseArray<View> views;

    public CommonViewHolder(View view){
        super(view);
        views=new SparseArray<>();
    }
    public  <T extends View> T get(int id) {
        T t=(T)views.get(id);
        if(t==null){
            t=itemView.findViewById(id);
            views.put(id,t);
        }
        return t;
    }
}
