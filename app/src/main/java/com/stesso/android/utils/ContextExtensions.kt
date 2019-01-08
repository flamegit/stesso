package com.stesso.android.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import android.widget.Toast
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONStringer

/**
 * Created by flame on 2018/2/17.
 */
fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun <T : Activity> Context.openActivity(activity: Class<T>) {
    val intent = Intent(this, activity)
    startActivity(intent)
}

fun <T : Activity> Context.openActivity(activity: Class<T>, key: String, value: String) {
    val intent = Intent(this, activity)
    intent.putExtra(key, value)
    startActivity(intent)
}

fun Context.setRobotoFont(textView: TextView) {
    val mgr = assets
    val tf = Typeface.createFromAsset(mgr, "fonts/RobotoCondensed-Bold.ttf")
    textView.typeface = tf
}

fun convertToJson(pairs: List<Pair<String, String>>): String {
    val jsonStringer = JSONStringer().`object`()
    pairs.forEach {
        jsonStringer.key(it.first).value(it.second)
    }
    jsonStringer.endObject()
    return jsonStringer.toString()
}

fun createShape(fillColor: Int, strokeColor: Int = Color.BLACK, strokeWidth: Int = 0, radius: Int = 4): GradientDrawable {
    val drawable = GradientDrawable()
    drawable.cornerRadius = radius.toFloat()
    if (strokeWidth > 0) {
        drawable.setStroke(strokeWidth, strokeColor)
    }
    drawable.setColor(fillColor)
    return drawable
}

fun <T> applySchedulers(): ObservableTransformer<T, T> {
    return ObservableTransformer {
        it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

const val TOKEN ="token"
const val CONFIG ="config"

fun Context.put(key:String,value:String?){
    getSharedPreferences(CONFIG,Context.MODE_PRIVATE).edit().putString(key,value).apply()
}

fun Context.clear(){
    getSharedPreferences(CONFIG,Context.MODE_PRIVATE).edit().clear().apply()
}

fun Context.get(key:String):String?{
    return getSharedPreferences(CONFIG,Context.MODE_PRIVATE).getString(key,null)
}





