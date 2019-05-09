package com.stesso.android.lib

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by flame on 2018/2/1.
 */

open class CommonAdapter<T>(private val mLayoutId: Int, val bind: (CommonViewHolder, Int, T) -> Unit) : RecyclerView.Adapter<CommonViewHolder>() {

    private var mContent: MutableList<T> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val view = createView(parent, viewType)
        return CommonViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        bind(holder, position, mContent[position])
    }
    override fun getItemCount(): Int = mContent.size
    protected fun createView(parent: ViewGroup, viewType: Int): View {
        return LayoutInflater.from(parent.context).inflate(mLayoutId, parent, false)
    }

    fun removeItem(position:Int){
        mContent.removeAt(position)
        notifyItemRemoved(position)
    }
    fun addItems(items: Collection<T>?, append: Boolean) {
        items?.let {
            if (!append) {
                mContent.clear()
            }
            mContent.addAll(it)
            notifyDataSetChanged()
        }
    }
}
