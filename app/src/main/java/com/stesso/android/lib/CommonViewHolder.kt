package com.example.flame.kotlinstudy.lib

import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View

/**
 * Created by flame on 2018/2/1.
 */

class CommonViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val views: SparseArray<View> = SparseArray()
    operator fun <T : View> get(id: Int): T {

        var t = views.get(id)
        if (t == null) {
            t = itemView.findViewById(id)
            views.put(id, t)
        }
        return t as T
    }
}
