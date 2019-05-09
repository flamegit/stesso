package com.stesso.android.lib

import android.view.ViewGroup
import android.widget.FrameLayout
import com.stesso.android.utils.dip2px

class FooterDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {

        val padding = dip2px(40)
        val view =FrameLayout(parent.context)
        view.setPadding(padding,padding,padding,padding)
        return CommonViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int, data :Any?) {

    }


}