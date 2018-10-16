package com.example.flame.kotlinstudy.lib

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by flame on 2018/2/1.
 */

class MultiTypeAdapter : RecyclerView.Adapter<CommonViewHolder>() {

    private val mContent: MutableList<CommonAdapterItem<*>> = mutableListOf()
    private val mFactory = DelegateAdapterFactory()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        return mFactory.getDelegateAdapter(viewType).onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        val type = mContent[position].type
        mFactory.getDelegateAdapter(type).onBindViewHolder(holder, position, mContent[position].data)
    }

    override fun getItemViewType(position: Int): Int = mContent[position].type

    override fun getItemCount(): Int = mContent.size

    fun addItems(items: Collection<*>?, type: Int = TYPE1, append: Boolean = false) {
        items?.let {
            if (!append) {
                mContent.clear()
            }
            mContent.addAll(transform(items, type))
            notifyDataSetChanged()
        }
    }
    fun addHeader(item: String?, type: Int = HEADER) {
        item?.let {
            mContent.add(0, CommonAdapterItem(it, type))
            notifyItemInserted(0)
        }
    }
    fun addFooter(item: String?, type: Int = FOOTER) {
        item?.let {
            val count = mContent.size
            mContent.add(count, CommonAdapterItem(it, type))
            notifyItemInserted(count)
        }
    }
    private fun transform(items: Collection<*>, type: Int): List<CommonAdapterItem<*>> {
        return items.map { CommonAdapterItem(it, type) }
    }

}
