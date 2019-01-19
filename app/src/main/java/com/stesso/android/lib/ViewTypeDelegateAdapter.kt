package com.stesso.android.lib

import android.view.ViewGroup

interface ViewTypeDelegateAdapter {

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder

    fun onBindViewHolder(holder: CommonViewHolder, position: Int, data :Any?)


}