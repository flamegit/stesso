package com.stesso.android.lib

import android.view.View
import android.view.ViewGroup

class CommonPagerAdapter<T>(val mFactory: (ViewGroup, T) -> View) : android.support.v4.view.PagerAdapter() {

    private val mContent: MutableList<T> = mutableListOf()

    override fun getCount(): Int {
        return mContent.size
    }

    fun addItems(lists: Collection<T>?) {
        if (lists == null || lists.isEmpty()) {
            return
        }
        mContent.clear()
        mContent.addAll(lists)
        notifyDataSetChanged()
    }
    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view === any
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(any as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = mFactory(container, mContent[position])
        container.addView(view)
        return view
    }
    // override fun getPageWidth(position: Int) =0.7f

}