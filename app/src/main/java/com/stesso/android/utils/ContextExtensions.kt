package com.example.flame.kotlinstudy.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import android.widget.Toast

/**
 * Created by flame on 2018/2/17.
 */
fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun  <T: Activity>Context.openActivity(activity: Class<T>){
    val intent= Intent(this,activity)
    startActivity(intent)
}

fun  <T: Activity>Context.openActivity(activity: Class<T>,key:String,value:String){
    val intent= Intent(this,activity)
    intent.putExtra(key,value)
    startActivity(intent)
}

fun Context.setRobotoFont( textView: TextView){
    val mgr=assets
    val tf=Typeface.createFromAsset(mgr,"fonts/RobotoCondensed-Bold.ttf")
    textView.typeface=tf
}

fun createShape(color: Int, radius: Int): GradientDrawable {
    val drawable = GradientDrawable()
    drawable.cornerRadius = radius.toFloat()
    drawable.setStroke(5, Color.WHITE)
    drawable.setColor(color)
    return drawable
}