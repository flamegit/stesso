package com.stesso.android.lib

import android.view.ViewGroup
import android.widget.TextView

class HeaderDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        return CommonViewHolder(TextView(parent.context))
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data :Any?){
        if(data is String){
            (holder.itemView as TextView).text=data
        }

    }
}