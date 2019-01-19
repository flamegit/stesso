package com.stesso.android.lib

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

class DefaultDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {

        val view= LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1,parent,false)
        return CommonViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data :Any?) {

        if(data is String){
            holder.get<TextView>(android.R.id.text1).text=data
            holder.get<TextView>(android.R.id.text2).text=data
        }
    }
}