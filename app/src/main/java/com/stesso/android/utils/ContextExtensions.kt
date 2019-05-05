package com.stesso.android.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.stesso.android.App
import com.stesso.android.R
import com.stesso.android.account.LoginActivity
import com.stesso.android.model.Account
import com.stesso.android.model.RootNode
import io.reactivex.CompletableTransformer
import io.reactivex.Single
import io.reactivex.SingleTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.json.JSONStringer
import java.text.SimpleDateFormat
import java.text.ParseException
import java.util.*


/**
 * Created by flame on 2018/2/17.
 */
fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun <T : Activity> Context.openActivity(activity: Class<T>) {
    val intent = Intent(this, activity)
    startActivity(intent)
}

fun <T : Activity, D> Context.openActivity(activity: Class<T>, key: String, value: D) {
    val intent = Intent(this, activity)
    if (value is String) {
        intent.putExtra(key, value)
    } else if (value is Int) {
        intent.putExtra(key, value)
    }
    startActivity(intent)
}


fun Context.getAppVersion(): String {
    var pi: PackageInfo? = null
    try {
        val pm = packageManager
        pi = pm.getPackageInfo(packageName,
                PackageManager.GET_CONFIGURATIONS)

        return pi.versionName
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""

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

fun <T> applySingleSchedulers(): SingleTransformer<T, T> {
    return SingleTransformer {
        it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun Context.checkLogin(action: () -> Unit) {
    if (Account.isLogin()) {
        action()
    } else {
        openActivity(LoginActivity::class.java)
    }
}


fun <T> doHttpRequest(single: Single<RootNode<T>>, onSuccess: (T?) -> Unit) {
    val disposable = single.compose(applySingleSchedulers())
            .subscribe({ rootNode ->
                if (rootNode.errno != 0) {
                } else {
                    onSuccess(rootNode.data)
                }
            }, {
                it.printStackTrace()
            })
}

fun applyCompletableSchedulers(): CompletableTransformer {
    return CompletableTransformer {
        it.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun Context.checkLoginInfo(phoneNum: String, password: String, verifyCode: String = "1234"): Boolean {
    if (phoneNum.length < 4) {
        toast("手机号格式错误")
        return false
    }
    if (password.length < 6) {
        toast("密码至少六位")
        return false
    }
    if (verifyCode.length < 4) {
        toast("验证码至少四位")
    }
    return true
}

fun Context.createProgressDialog(): AlertDialog {
    return AlertDialog.Builder(this, R.style.ProgressDialog).setView(R.layout.dialog_progress)
            .create()
}

fun Context.getWindowWidth(): Int {
    val metrics = DisplayMetrics()
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    wm.defaultDisplay.getMetrics(metrics)
    return metrics.widthPixels
}


const val TOKEN = "token"
const val USER_INFO = "user_info"
const val CONFIG = "config"

fun Context.put(key: String, value: String?) {
    getSharedPreferences(CONFIG, Context.MODE_PRIVATE).edit().putString(key, value).apply()
}

fun Context.clear() {
    getSharedPreferences(CONFIG, Context.MODE_PRIVATE).edit().clear().apply()
}

fun Context.get(key: String): String? {
    return getSharedPreferences(CONFIG, Context.MODE_PRIVATE).getString(key, null)
}

fun dip2px(dpValue: Int): Int {
    val scale = App.instance().resources.displayMetrics.density
    return (dpValue * scale + 0.5f).toInt()
}

fun parseTime(strDate: String): String {
    return parseTime(strDate, "yyyy年MM月dd日")
}

fun parseTimeMillis(strDate: String): Long {
    if (strDate.isEmpty()) {
        return DateTime.now().millis
    }
    return DateTime.parse(strDate, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).millis
}


fun parseTime(strDate: String, format: String): String {
    if (strDate.isEmpty()) {
        return strDate
    }
    val dateTime = DateTime.parse(strDate, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
    return dateTime.toString(format)
}

fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}


