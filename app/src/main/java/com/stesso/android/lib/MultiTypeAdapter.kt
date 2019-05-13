package com.stesso.android.lib

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by flame on 2018/2/1.
 */

class MultiTypeAdapter : RecyclerView.Adapter<CommonViewHolder>() {

    private val mContent: MutableList<CommonAdapterItem> = mutableListOf()

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

    fun addItems(items: Collection<Any>?, type: Int = TYPE1, append: Boolean = false, spanSize: Int = 1) {
        items?.let {
            if (!append) {
                mContent.clear()
            }
            mContent.addAll(transform(items, type, spanSize))
            notifyDataSetChanged()
        }
    }

    fun clear() {
        mContent.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClick(callback: (position: Int, data: Any?, action: Int, extra: Any?) -> Unit) {
        mFactory.onItemClick = callback
    }

    fun setOnItemClick(callback: (position: Int, data: Any?, action: Int) -> Unit) {
        mFactory.onItemClick = { position, data, action, _ ->
            callback(position, data, action)
        }
    }

    fun getItemType(position: Int): Int {
        return mContent[position].type
    }

    fun removeItem(position: Int) {
        mContent.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItem(item: Collection<Any>?, type: Int = TYPE1, append: Boolean = false) {
        item?.let {
            if (!append) {
                mContent.clear()
            }
            mContent.add(CommonAdapterItem(item.toMutableList(), type))
            notifyDataSetChanged()
        }
    }

    fun addItem(item: Any?, type: Int = TYPE1, append: Boolean = false, spanSize: Int = 1) {
        item?.let {
            if (!append) {
                mContent.clear()
            }
            mContent.add(transform(item, type, spanSize))
            notifyDataSetChanged()
        }
    }

    fun changeItem(position: Int, item: Any?, type: Int,spanSize: Int=1) {
        item?.let {
            if (position < mContent.size) {
                mContent[position] = transform(item, type,spanSize)
                notifyItemChanged(position)
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView?.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return mContent[position].spanSize
                }
            }
        }
    }

    private fun transform(item: Any, type: Int, spanSize: Int = 1): CommonAdapterItem {
        return CommonAdapterItem(item, type, spanSize)
    }

    private fun transform(items: Collection<Any>, type: Int, spanSize: Int = 1): List<CommonAdapterItem> {
        return items.map { CommonAdapterItem(it, type, spanSize) }
    }

    data class CommonAdapterItem(val data: Any, val type: Int, var spanSize: Int = 1)
}
