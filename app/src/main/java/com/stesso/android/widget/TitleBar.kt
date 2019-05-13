package com.stesso.android.widget

import android.content.Context
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.stesso.android.R
import com.stesso.android.utils.dip2px

class TitleBar(context: Context, attrs: AttributeSet, defStyle: Int) : FrameLayout(context, attrs, defStyle) {

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    var leftIconView: ImageView? = null
    var rightIconView: ImageView? = null
    var leftTextView: TextView? = null
    var rightTextView: TextView? = null
    var titleView: TextView? = null

    var countView: TextView? = null

    init {
        val array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
        val leftIconResource = array.getResourceId(R.styleable.TitleBar_left_icon, -1)
        val rightIconResource = array.getResourceId(R.styleable.TitleBar_right_icon, -1)
        val leftText = array.getString(R.styleable.TitleBar_left_text)
        val rightText = array.getString(R.styleable.TitleBar_right_text)
        val titleText = array.getString(R.styleable.TitleBar_title_text)
        val padding = dip2px(16)
        val small = dip2px(4)


        if (leftIconResource != -1) {
            leftIconView = ImageView(context).apply {
                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                params.gravity = Gravity.START
                setPadding(padding, padding, padding, padding)
                addView(this, params)
                setImageResource(leftIconResource)
            }

        }

        if (rightIconResource != -1) {
            rightIconView = ImageView(context).apply {
                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                params.gravity = Gravity.END
                addView(this, params)
                setPadding(padding, padding, padding, padding)
                this.setImageResource(rightIconResource)
            }
        }


        if (leftText != null) {
            leftTextView = TextView(context).apply {
                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                params.gravity = Gravity.START
                addView(this, params)
                this.text = leftText
                setPadding(padding, padding, padding, padding)
                this.setTextColor(ContextCompat.getColor(context, R.color.font_4A))
                this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            }

        }

        if (rightText != null) {
            rightTextView = TextView(context).apply {
                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                params.gravity = Gravity.END
                addView(this, params)
                this.text = rightText
                setPadding(padding, padding, padding, padding)
                this.setTextColor(ContextCompat.getColor(context, R.color.font_4A))
                this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            }
        }

        titleView = TextView(context).apply {
            val centerParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            centerParams.gravity = Gravity.CENTER
            addView(this, centerParams)
            this.text = titleText
            setPadding(padding, padding, padding, padding)
            this.setTextColor(ContextCompat.getColor(context, R.color.font_4A))
            this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        }

        countView = TextView(context).apply {
            visibility = View.INVISIBLE
            val centerParams = LayoutParams(padding, padding)
            centerParams.gravity = Gravity.TOP xor Gravity.END
            centerParams.setMargins(small, small, small, small)
            addView(this, centerParams)
            gravity = Gravity.CENTER
            background = ContextCompat.getDrawable(context, R.drawable.solid_big_red_bg)
            this.setTextColor(ContextCompat.getColor(context, R.color.white))
            this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8F)
        }

        array.recycle()
    }

    fun setCount(count: Int) {
        if (count == 0) {
            countView?.visibility = View.INVISIBLE
        } else {
            countView?.visibility = View.VISIBLE
            countView?.text = count.toString()
        }
    }

    fun setTitle(title: String) {
        titleView?.text = title
    }

    fun setLeftAction(action: (View) -> Unit) {
        leftIconView?.setOnClickListener(action)
        leftTextView?.setOnClickListener(action)
    }

    fun setRightAction(action: (View) -> Unit) {
        rightIconView?.setOnClickListener(action)
        rightTextView?.setOnClickListener(action)
    }

}