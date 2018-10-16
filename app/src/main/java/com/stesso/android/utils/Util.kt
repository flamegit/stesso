//package com.stesso.android.utils
//
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.graphics.drawable.Drawable
//import android.graphics.drawable.GradientDrawable
//import android.net.ConnectivityManager
//import android.net.NetworkInfo
//import android.net.Uri
//import android.support.v7.app.AppCompatActivity
//import android.text.TextUtils
//import android.view.WindowManager
//import android.widget.ImageView
//
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.engine.DiskCacheStrategy
//import com.google.gson.Gson
//import com.google.gson.JsonObject
//import com.google.gson.reflect.TypeToken
//
//
//import org.json.JSONException
//import org.json.JSONStringer
//
//import java.io.File
//import java.io.InputStreamReader
//import java.lang.reflect.Type
//import java.math.RoundingMode
//import java.net.URLDecoder
//import java.security.MessageDigest
//import java.text.DecimalFormat
//import java.text.NumberFormat
//import java.text.SimpleDateFormat
//import java.util.ArrayList
//import java.util.Calendar
//import java.util.Date
//
//import javax.crypto.Cipher
//import javax.crypto.SecretKeyFactory
//import javax.crypto.spec.DESKeySpec
//import javax.crypto.spec.IvParameterSpec
//
//
//import okhttp3.MediaType
//import okhttp3.RequestBody
//
//
///**
// *
// */
//object Util {
//    var source: String? = null
//    internal var medias = arrayOf<SHARE_MEDIA>(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.FACEBOOK)
//    val isNetworkAvailable: Boolean
//        get() {
//            val connectivityManager = App.getAppContext()
//                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            if (connectivityManager != null) {
//                val info = connectivityManager.activeNetworkInfo
//                if (info != null && info.isConnected) {
//                    if (info.state == NetworkInfo.State.CONNECTED) {
//                        return true
//                    }
//                }
//            }
//            return false
//        }
//
//    val windowWidth: Int
//        get() {
//            val wm = App.getAppContext()
//                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager
//
//            return wm.defaultDisplay.width
//        }
//
//    private val DES_IV = byteArrayOf(0x12.toByte(), 0x34.toByte(), 0x56.toByte(), 0x78.toByte(), 0x90.toByte(), 0xAB.toByte(), 0xCD.toByte(), 0xEF.toByte())// 设置向量，略去
//
//    val dayOfMonth: Int
//        get() {
//            val cal = Calendar.getInstance()
//            cal.time = Date()
//            return cal.get(Calendar.DAY_OF_MONTH)
//        }
//
//    fun isUrl(url: String?): Boolean {
//        if (url != null && !url.isEmpty()) {
//            if (url.toLowerCase().startsWith("http")) {
//                return true
//            }
//        }
//        return false
//    }
//
//    fun getSign(method: String, path: String, body: String?): String? {
//
//        if (body == null) {
//            KLog.i(String.format("method %s path %s", method, path))
//        } else {
//            KLog.i(String.format("method %s path %s body %s", method, path, body))
//        }
//        val account = AccountInfo.getInstance()
//        if (!account.isLogin()) {
//            return "nologin"
//        }
//        KLog.i(String.format("secretKey %s ", account.getSecretKey()))
//        val stringBuilder = StringBuilder()
//        if (method == "GET") {
//            stringBuilder.append(account.getToken()).append(account.getSecretKey()).append(path)
//            val md5 = MD5(stringBuilder.toString())
//            return MD5(md5)
//        }
//        if (method == "POST") {
//            stringBuilder.append(account.getToken()).append(account.getSecretKey()).append(path).append(body)
//            val md5 = MD5(stringBuilder.toString())
//            return MD5(md5)
//        }
//        return null
//    }
//
//    /**
//     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
//     */
//    fun dip2px(dpValue: Float): Int {
//        val scale = App.getAppContext().getResources().getDisplayMetrics().density
//        return (dpValue * scale + 0.5f).toInt()
//    }
//
//    fun sp2px(spValue: Float): Int {
//        val fontScale = App.getAppContext().getResources().getDisplayMetrics().scaledDensity
//        return (spValue * fontScale + 0.5f).toInt()
//    }
//
//    /**
//     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
//     */
//    fun px2dip(pxValue: Float): Int {
//        val scale = App.getAppContext().getResources().getDisplayMetrics().density
//        return (pxValue / scale + 0.5f).toInt()
//    }
//
//    fun startActivity(context: Context, clazz: Class<*>) {
//        val intent = Intent(context, clazz)
//        context.startActivity(intent)
//    }
//
//    fun startActivity(context: Context, clazz: Class<*>, key: String, `val`: String) {
//        val intent = Intent(context, clazz)
//        intent.putExtra(key, `val`)
//        context.startActivity(intent)
//    }
//
//    fun convertTime(time: Long): String {
//        if (time <= 0) {
//            return "00:00"
//        }
//        val m = time / 60000
//        val s = (time - m * 60000) / 1000
//        val minute = String.format("%02d", m)
//        val second = String.format("%02d", s)
//        return "$minute:$second"
//    }
//
//    fun getSize(file: File): Double {
//        //判断文件是否存在
//        if (file.exists()) {
//            //如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
//            if (!file.isFile) {
//                //获取文件大小
//                val fl = file.listFiles()
//                var ss = 0.0
//                for (f in fl)
//                    ss += getSize(f)
//                return ss
//            } else {
//                val ss = file.length().toDouble() / 1024.0 / 1024.0
//                println(file.name + " : " + ss + "MB")
//                return ss
//            }
//        } else {
//            println("文件或者文件夹不存在，请检查路径是否正确！")
//            return 0.0
//        }
//    }
//
//    fun getCacheSize(context: Context): Double {
//        val file = context.applicationContext.cacheDir
//        return getSize(file)
//    }
//
//    fun clearCache(context: Context) {
//        val root = context.applicationContext.cacheDir
//        clearCache(root)
//    }
//
//    fun clearCache(file: File) {
//        if (file.isDirectory) {
//            for (f in file.listFiles()) {
//                if (f.isFile) {
//                    f.delete()
//                } else {
//                    clearCache(f)
//                }
//            }
//        } else {
//            file.delete()
//        }
//    }
//
//    fun <T> jsonToArrayList(inputStreamReader: InputStreamReader, clazz: Class<T>): ArrayList<T> {
//        val type = object : TypeToken<ArrayList<JsonObject>>() {
//
//        }.type
//        val jsonObjects = Gson().fromJson<ArrayList<JsonObject>>(inputStreamReader, type)
//        val arrayList = ArrayList<T>()
//        for (jsonObject in jsonObjects) {
//            arrayList.add(Gson().fromJson(jsonObject, clazz))
//        }
//        return arrayList
//    }
//
//    fun jsonToList(json: String): List<String> {
//        val gson = Gson()
//        return gson.fromJson<ArrayList<*>>(json, ArrayList<*>::class.java)
//        //return new ArrayList<>(array)
//    }
//
//    fun isMobileNO(code: String, mobiles: String): Boolean {
//        if (TextUtils.isEmpty(mobiles)) {
//            return false
//        }
//        if (code != "+86") {
//            return true
//        }
//        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//        val telRegex = "[1][3456789]\\d{9}"
//        return mobiles.matches(telRegex.toRegex())
//    }
//
//    fun getformatTime(time: Long): String {
//        return getFormatDate(time, "MM月dd日 HH:mm")
//
//    }
//
//    @JvmOverloads
//    fun getFormatDate(time: Long, field: String = "yyyy-MM-dd HH:mm:ss"): String {
//        val date = Date(time)
//        val format = SimpleDateFormat(field)
//        return format.format(date)
//    }
//
//    /**
//     * 获取汉字字符串的第一个字母
//     */
//    fun getPinYinFirstLetter(str: String): String {
//        val sb = StringBuffer()
//        sb.setLength(0)
//        val c = str[0]
//        val pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c)
//        if (pinyinArray != null) {
//            sb.append(pinyinArray!![0].get(0))
//        } else {
//            sb.append(c)
//        }
//        return sb.toString()
//    }
//
//    fun MD5(pwd: String?): String? {
//        //用于加密的字符
//        val md5String = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
//        try {
//            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
//            val btInput = pwd!!.toByteArray()
//
//            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
//            val mdInst = MessageDigest.getInstance("MD5")
//            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
//            mdInst.update(btInput)
//
//            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
//            val md = mdInst.digest()
//
//            // 把密文转换成十六进制的字符串形式
//            val j = md.size
//            val str = CharArray(j * 2)
//            var k = 0
//            for (i in 0 until j) {   //  i = 0
//                val byte0 = md[i]  //95
//                str[k++] = md5String[byte0.ushr(4) and 0xf]    //    5
//                str[k++] = md5String[byte0 and 0xf]   //   F
//            }
//            //返回经过加密后的字符串
//            return String(str)
//
//        } catch (e: Exception) {
//            return null
//        }
//
//    }
//
//    fun getString(rid: Int): String {
//        return App.getAppContext().getString(rid)
//    }
//
//    fun checkLogin(context: Context, intent: Intent) {
//        if (!AccountInfo.getInstance().isLogin()) {
//            startActivity(context, LoginActivity::class.java)
//        } else {
//            context.startActivity(intent)
//        }
//    }
//
//    @JvmOverloads
//    fun openTarget(context: Context, schema: String?, title: String, createTask: Boolean = false) {
//        if (schema == null) {
//            return
//        }
//        val uri = Uri.parse(URLDecoder.decode(schema))
//        val target = uri.getQueryParameter("target")
//
//        val url = uri.getQueryParameter("url")
//
//        val src = uri.getQueryParameter("source")
//        var id = 0
//        try {
//            id = Integer.valueOf(uri.getQueryParameter("id")!!)
//        } catch (e: NumberFormatException) {
//            //return 0l;
//        }
//
//        if (src != null && !src.isEmpty()) {
//            source = src
//        }
//        var intent: Intent? = null
//        if (target == null) {
//            return
//        }
//        when (target) {
//            "activity" -> if (id == 12) {
//                return
//            } else if (id == 14) {
//                if (!AccountInfo.getInstance().isLogin()) {
//                    intent = Intent(context, LoginActivity::class.java)
//                    if (createTask) {
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    }
//                    context.startActivity(intent)
//                    return
//                }
//                return
//            } else if (id == 13) {
//            } else {
//                intent = Intent(context, HomeActivity::class.java)
//            }
//            "news" -> {
//                intent = Intent(context, InfoDetailActivity::class.java)
//                intent.putExtra(Constants.INFO_ID, id)
//                intent.putExtra(Constants.IS_INFO, true)
//            }
//            "url" -> if (url != null) {
//                intent = Intent(context, WebViewActivity::class.java)
//                intent.putExtra(Constants.TARGET_URL, url)
//                intent.putExtra(Constants.TITLE, title)
//            }
//            "product" -> {
//                intent = Intent(context, CommodityDetailActivity::class.java)
//                intent.putExtra(Constants.INFO_ID, java.lang.Long.valueOf(uri.getQueryParameter("id")!!))
//                intent.putExtra(Constants.SHOP, uri.getQueryParameter("shop"))
//            }
//            "brands" -> intent = Intent(context, BrandListActivity::class.java)
//            "asale" -> {
//                intent = Intent(context, ExchangeDetailActivity::class.java)
//                intent.putExtra(Constants.ORDER_NO, uri.getQueryParameter("asno"))
//            }
//            "orderDetail" -> {
//                intent = Intent(context, OrdersDetailActivity::class.java)
//                intent.putExtra("orderNo", uri.getQueryParameter("orderNo"))
//            }
//
//            "order" -> {
//                intent = Intent(context, OrdersDetailActivity::class.java)
//                intent.putExtra("orderNo", uri.getQueryParameter("no"))
//            }
//
//            "chatMessages" -> startMoor(context)
//
//            "popup" -> {
//                intent = Intent(context, PopupActivity::class.java)
//                intent.putExtra(Constants.INFO_ID, uri.getQueryParameter("id"))
//            }
//            "coupon" -> if (AccountInfo.getInstance().isLogin()) {
//                val code = uri.getQueryParameter("code")
//                val jsonStringer = JSONStringer()
//                try {
//                    jsonStringer.`object`()
//                            .key("cdkey").value(code)
//                            .endObject()
//                } catch (e: JSONException) {
//                    KLog.i("JsonException")
//                }
//
//                if (context is AppCompatActivity) {
//                    val body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString())
//                    ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).bindCoupon(body).enqueue(object : Callback<HttpBean<Problem>>() {
//                        fun onResponse(call: Call<HttpBean<Problem>>, response: Response<HttpBean<Problem>>) {
//                            if (response.isSuccessful()) {
//                                val data = response.body().msg
//                                if (response.body().code === 200) {
//                                    val dialogFragment = CommonDialogFragment.newInstance(R.layout.dialog_coupon_success, "领取成功", data)
//                                    dialogFragment.show(context.supportFragmentManager, "lottery")
//                                    dialogFragment.setActionListener(object : CommonDialogFragment.ActionListener() {
//                                        fun doAction() {
//                                            startActivity(context, CouponActivity::class.java)
//                                        }
//
//                                        fun cancel() {}
//                                    })
//                                } else if (response.body().code === 3805) {
//                                    CommonDialogFragment.newInstance(R.layout.dialog_coupon_fail, "你已经领取过了", data)
//                                            .show(context.supportFragmentManager, "lottery")
//                                } else {
//                                    SystemUtil.toast(data)
//                                }
//                            }
//                        }
//
//                        fun onFailure(call: Call<HttpBean<Problem>>, t: Throwable) {
//                            SystemUtil.toast("优惠券领取失败")
//                            KLog.d(t.toString())
//                        }
//                    })
//                }
//            }
//
//            "plist" -> {
//                val activityId = uri.getQueryParameter("aid")
//                val bid = uri.getQueryParameter("brandId")
//                if (bid != null) {
//                    intent = Intent(context, CommodityListActivity::class.java)
//                    intent.putExtra(Constants.BRAND_ID, java.lang.Long.valueOf(bid))
//                    intent.putExtra(Constants.ACTIVITY_ID, activityId)
//                } else if (activityId != null) {
//                    intent = Intent(context, DiscountListActivity::class.java)
//                    intent.putExtra(Constants.ACTIVITY_ID, activityId)
//                }
//            }
//
//            "productlist" -> {
//                val productId = uri.getQueryParameter("pid")
//                val brandId = uri.getQueryParameter("brandId")
//                if (productId != null) {
//                    if (brandId != null) {
//                        intent = Intent(context, CommodityListActivity::class.java)
//                        intent.putExtra(Constants.BRAND_ID, java.lang.Long.valueOf(brandId))
//                        intent.putExtra(Constants.ACTIVITY_ID, productId)
//                        intent.putExtra(Constants.IS_POPUP, false)
//                    } else {
//                        intent = Intent(context, DiscountListActivity::class.java)
//                        intent.putExtra(Constants.ACTIVITY_ID, productId)
//                        intent.putExtra(Constants.IS_POPUP, false)
//                    }
//                }
//            }
//
//            "popuplist" -> {
//                intent = Intent(context, WebViewActivity::class.java)
//                intent.putExtra(Constants.TARGET_URL, BuildConfig.POPUPURL)
//                intent.putExtra(Constants.TITLE, "POP UP")
//                intent.putExtra(Constants.TYPE, 1)
//            }
//            "category" -> intent = Intent(context, CategoryListActivity::class.java)
//            "lottery" -> {
//                intent = Intent(context, DrawActivity::class.java)
//                intent.putExtra(Constants.ACTIVITY_ID, java.lang.Long.valueOf(uri.getQueryParameter("id")!!))
//            }
//            "discounts" -> {
//                intent = Intent(context, DiscountListActivity::class.java)
//                intent.putExtra(Constants.ACTIVITY_ID, uri.getQueryParameter("id"))
//                intent.putExtra(Constants.IS_POPUP, false)
//            }
//            "reservation" -> if (AccountInfo.getInstance().isLogin()) {
//                val aid = java.lang.Long.valueOf(uri.getQueryParameter("id")!!)
//                intent = Intent(context, ReservationActivity::class.java)
//                intent.putExtra(Constants.INFO_ID, aid)
//            } else {
//                intent = Intent(context, LoginActivity::class.java)
//            }
//            "toast" -> {
//                val type = uri.getQueryParameter("type")
//                if ("notOpen" == type) {
//                    SystemUtil.reportServerError(getString(R.string.not_open))
//                }
//                if ("closed" == type) {
//                    SystemUtil.reportServerError(getString(R.string.close))
//                }
//            }
//        }
//        if (intent != null) {
//            if (createTask) {
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            }
//            context.startActivity(intent)
//        }
//    }
//
//    fun goToTarget(context: Context, schema: String) {
//
//        openTarget(context, schema, " ", true)
//    }
//
//    fun getDisplayList(activity: Activity): Array<SHARE_MEDIA> {
//        val result = ArrayList<SHARE_MEDIA>()
//        for (media in medias) {
//            if (UMShareAPI.get(activity).isInstall(activity, media)) {
//                result.add(media)
//            }
//        }
//        return result.toTypedArray<SHARE_MEDIA>()
//    }
//
//    fun loadImage(context: Context, picUrl: String, imageView: ImageView) {
//        if (picUrl.endsWith(".gif")) {
//            Glide.with(context)
//                    .load(picUrl)
//                    .asGif()
//                    .dontAnimate()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)// DiskCacheStrategy.NONE
//                    .placeholder(R.mipmap.white_logo)
//                    .into(imageView)
//        } else {
//            Glide.with(context)
//                    .load(picUrl)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .placeholder(R.mipmap.white_logo)
//                    .into(imageView)
//        }
//    }
//
//    fun fillImage(context: Context, url: String, target: ImageView) {
//        Glide.with(context).load(url)
//                .dontAnimate()
//                .into(target)
//    }
//
//    @Throws(Exception::class)
//    fun decode(data: String, key: String): String {
//        val keySpec = DESKeySpec(key.toByteArray())
//        val keyFactory = SecretKeyFactory.getInstance("DES")
//        val deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
//        deCipher.init(Cipher.DECRYPT_MODE, keyFactory.generateSecret(keySpec), IvParameterSpec(DES_IV))
//        val base64Decoder = BASE64Decoder()
//        val pasByte = deCipher.doFinal(base64Decoder.decodeBuffer(data))
//        return String(pasByte, "UTF-8")
//    }
//
//    fun getTimeStamp(y: Int, m: Int, d: Int): Long {
//        val cal = Calendar.getInstance()
//        cal.set(Calendar.HOUR_OF_DAY, 0)
//        cal.set(Calendar.MINUTE, 0)
//        cal.set(Calendar.SECOND, 0)
//        cal.set(y, m, d)
//        return cal.timeInMillis
//    }
//
//    fun createDrawable(fillColor: Int, roundRadius: Int, strokeWidth: Int, strokeColor: Int): Drawable {
//        val gd = GradientDrawable()
//        gd.setColor(fillColor)
//        gd.cornerRadius = roundRadius.toFloat()
//        gd.setStroke(strokeWidth, strokeColor)
//        return gd
//    }
//
//    fun getFomatNum(num: Float): String {
//
//        //        if(num==(int)num){
//        //            return String.format("%.0f",num);
//        //        }else {
//        //            return String.format("%.1f",num);
//        //        }
//        val nf = NumberFormat.getNumberInstance()
//        nf.maximumFractionDigits = 2
//        nf.roundingMode = RoundingMode.HALF_UP
//        nf.isGroupingUsed = false
//        return nf.format(num.toDouble())
//    }
//
//    fun getPrice(num: Float): String {
//        return DecimalFormat("0.00").format(num.toDouble())
//    }
//
//    fun startMoor(context: Context) {
//        val accountInfo = AccountInfo.getInstance()
//        if (accountInfo.isLogin()) {
//            val user = accountInfo.getUser()
//            val name = user.name + user.mobile
//            MoorActivity.start(context, name, user.userId)
//        } else {
//            startActivity(context, LoginActivity::class.java)
//        }
//    }
//
//    fun isNotEmpty(str: String?): Boolean {
//        return if (str == null || str.isEmpty()) {
//            false
//        } else true
//    }
//
//}
