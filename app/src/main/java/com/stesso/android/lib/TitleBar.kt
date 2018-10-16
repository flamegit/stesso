package com.stesso.android.lib

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.stesso.android.R

class TitleBar(context: Context, attrs: AttributeSet, defStyle: Int) : FrameLayout(context, attrs, defStyle) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    var leftView: ImageView? = null
    var rightView: ImageView? = null

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
        val leftResourceId = array.getResourceId(R.styleable.TitleBar_left_icon, -1)
        val rightResourceId = array.getResourceId(R.styleable.TitleBar_right_icon, -1)
        val titleResourceId = array.getResourceId(R.styleable.TitleBar_title_resource, R.string.app_name)

        if (leftResourceId != -1) {
            val leftParams = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            leftView = ImageView(context)
            leftParams.gravity = Gravity.START
            addView(leftView, leftParams)
            leftView!!.setImageResource(leftResourceId)
        }

        if (rightResourceId != -1) {
            val rightParams = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            rightView = ImageView(context)
            rightParams.gravity = Gravity.END
            addView(rightView, rightParams)
            rightView!!.setImageResource(rightResourceId)
        }


        val title = TextView(context)
        val centerParams = FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

        centerParams.gravity = Gravity.CENTER
        addView(title, centerParams)
        title.setText(titleResourceId)
        array.recycle()

    }

}