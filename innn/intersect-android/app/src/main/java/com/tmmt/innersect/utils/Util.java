package com.tmmt.innersect.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.m7.imkfsdk.MoorActivity;
import com.socks.library.KLog;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tmmt.innersect.App;
import com.tmmt.innersect.BuildConfig;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.mvp.model.Problem;
import com.tmmt.innersect.mvp.model.User;
import com.tmmt.innersect.ui.activity.AwardActivity;
import com.tmmt.innersect.ui.activity.BrandListActivity;
import com.tmmt.innersect.ui.activity.CategoryListActivity;
import com.tmmt.innersect.ui.activity.CommodityDetailActivity;
import com.tmmt.innersect.ui.activity.CommodityListActivity;
import com.tmmt.innersect.ui.activity.CouponActivity;
import com.tmmt.innersect.ui.activity.DiscountListActivity;
import com.tmmt.innersect.ui.activity.DrawActivity;
import com.tmmt.innersect.ui.activity.ExchangeDetailActivity;
import com.tmmt.innersect.ui.activity.HomeActivity;
import com.tmmt.innersect.ui.activity.InfoDetailActivity;
import com.tmmt.innersect.ui.activity.LoginActivity;
import com.tmmt.innersect.ui.activity.OrdersDetailActivity;
import com.tmmt.innersect.ui.activity.PopupActivity;
import com.tmmt.innersect.ui.activity.ReservationActivity;
import com.tmmt.innersect.ui.activity.WebViewActivity;
import com.tmmt.innersect.ui.fragment.CommonDialogFragment;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import Decoder.BASE64Decoder;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 */
public class Util {
    public static String source=null;
    static SHARE_MEDIA[] medias={
            SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA,SHARE_MEDIA.FACEBOOK
    };
    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isUrl(String url){
        if(url!=null && !url.isEmpty()){
            if(url.toLowerCase().startsWith("http")){
                return  true;
            }
        }
        return false;
    }

    public static String getSign(String method,String path,String body){

        if(body==null){
            KLog.i(String.format("method %s path %s", method,path));
        }else {
            KLog.i(String.format("method %s path %s body %s", method,path,body));
        }
        AccountInfo account= AccountInfo.getInstance();
        if(!account.isLogin()){
            return "nologin";
        }
        KLog.i(String.format("secretKey %s ",account.getSecretKey()));
        StringBuilder stringBuilder=new StringBuilder();
        if(method.equals("GET")){
            stringBuilder.append(account.getToken()).append(account.getSecretKey()).append(path);
            String md5=MD5(stringBuilder.toString());
            return MD5(md5);
        }
        if(method.equals("POST")){
            stringBuilder.append(account.getToken()).append(account.getSecretKey()).append(path).append(body);
            String md5=MD5(stringBuilder.toString());
            return MD5(md5);
        }
        return null;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = App.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(float spValue) {
        final float fontScale = App.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int getWindowWidth(){
        WindowManager wm = (WindowManager) App.getAppContext()
                .getSystemService(Context.WINDOW_SERVICE);

        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = App.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void startActivity(Context context,Class clazz){
        Intent intent=new Intent(context,clazz);
        context.startActivity(intent);
    }

    public static void startActivity(Context context,Class clazz,String key,String val){
        Intent intent=new Intent(context,clazz);
        intent.putExtra(key,val);
        context.startActivity(intent);
    }

    public static String convertTime(long time){
        if(time<=0){
            return "00:00";
        }
        long m=time/60000;
        long s=(time-m*60000)/1000;
        String minute=String.format("%02d",m);
        String second=String.format("%02d",s);
        return minute+":"+second;
    }

    public static double getSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
            if (!file.isFile()) {
                //获取文件大小
                File[] fl = file.listFiles();
                double ss = 0;
                for (File f : fl)
                    ss += getSize(f);
                return ss;
            } else {
                double ss = (double) file.length() / 1024 / 1024;
                System.out.println(file.getName() + " : " + ss + "MB");
                return ss;
            }
        } else {
            System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0.0;
        }
    }

    public static double getCacheSize(Context context){
        File file= context.getApplicationContext().getCacheDir();
        return getSize(file);
    }

    public static void clearCache(Context context){
        File root= context.getApplicationContext().getCacheDir();
        clearCache(root);
    }
    public static void clearCache(File file){
        if(file.isDirectory()){
            for(File f:file.listFiles()){
                if(f.isFile()){
                    f.delete();
                }else {
                    clearCache(f);
                }
            }
        }else {
            file.delete();
        }
    }

    public static <T> ArrayList<T> jsonToArrayList(InputStreamReader inputStreamReader, Class<T> clazz)
    {
        Type type = new TypeToken<ArrayList<JsonObject>>() {}.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(inputStreamReader, type);
        ArrayList<T> arrayList = new ArrayList<>();
        for (JsonObject jsonObject : jsonObjects)
        {
            arrayList.add(new Gson().fromJson(jsonObject, clazz));
        }
        return arrayList;
    }

    public static  List<String> jsonToList(String json)
    {
        Gson gson = new Gson();
        return gson.fromJson(json, ArrayList.class);
        //return new ArrayList<>(array)
    }

    public static boolean isMobileNO(String code,String mobiles) {
        if (TextUtils.isEmpty(mobiles)){
            return false ;
        }
        if(!code.equals("+86")){
            return true;
        }
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "[1][3456789]\\d{9}" ;
        return mobiles.matches( telRegex ) ;
    }

    public static String getformatTime(long time){
        return getFormatDate(time,"MM月dd日 HH:mm");

    }

    public static String getFormatDate(long time) {
        return getFormatDate(time,"yyyy-MM-dd HH:mm:ss");
    }
    public static String getFormatDate(long time,String field) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(field);
        return format.format(date);
    }

    /**
     * 获取汉字字符串的第一个字母
     */
    public static String getPinYinFirstLetter(String str) {
        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        char c = str.charAt(0);
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c);
        if (pinyinArray != null) {
            sb.append(pinyinArray[0].charAt(0));
        } else {
            sb.append(c);
        }
        return sb.toString();
    }
    public final static String MD5(String pwd) {
        //用于加密的字符
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = pwd.getBytes();

            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);

            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {   //  i = 0
                byte byte0 = md[i];  //95
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }
            //返回经过加密后的字符串
            return new String(str);

        } catch (Exception e) {
            return null;
        }
    }

    public static String getString(int rid){
        return App.getAppContext().getString(rid);
    }

    public static void checkLogin(Context context,Intent intent){
        if(!AccountInfo.getInstance().isLogin()){
            startActivity(context,LoginActivity.class);
        }else {
            context.startActivity(intent);
        }
    }

    public static void openTarget(Context context,String schema,String title,boolean createTask){
        if(schema==null){
            return;
        }
        Uri uri=Uri.parse(URLDecoder.decode(schema));
        final String target=uri.getQueryParameter("target");
        final String url=uri.getQueryParameter("url");
        String src=uri.getQueryParameter("source");
        int id=0;
        try{
            id=Integer.valueOf(uri.getQueryParameter("id"));
        }catch (NumberFormatException e){
            //return 0l;
        }
        if(src!=null && !src.isEmpty()){
            source=src;
        }
        Intent intent=null;
        if(target==null ){
            return;
        }
        switch(target){
            case "activity":
                if (id == 12) {
                    return;
                } else if(id==14){
                    if(!AccountInfo.getInstance().isLogin()){
                        intent=new Intent(context,LoginActivity.class);
                        if(createTask){
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        }
                        context.startActivity(intent);
                        return;
                    }
                    return;
                }else if(id==13){
                }else {
                    intent=new Intent(context,HomeActivity.class);
                }
                break;
            case "news":
                intent = new Intent(context, InfoDetailActivity.class);
                intent.putExtra(Constants.INFO_ID, id);
                intent.putExtra(Constants.IS_INFO,true);
                break;
            case "url":
                if (url != null) {
                    intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(Constants.TARGET_URL, url);
                    intent.putExtra(Constants.TITLE, title);
                }
                break;
            case "product":
                intent=new Intent(context,CommodityDetailActivity.class);
                intent.putExtra(Constants.INFO_ID,Long.valueOf(uri.getQueryParameter("id")));
                intent.putExtra(Constants.SHOP,uri.getQueryParameter("shop"));
                break;
            case "brands":
                intent=new Intent(context, BrandListActivity.class);
                break;
            case "asale":
                intent=new Intent(context, ExchangeDetailActivity.class);
                intent.putExtra(Constants.ORDER_NO,uri.getQueryParameter("asno"));
                break;
            case "orderDetail":
                intent=new Intent(context, OrdersDetailActivity.class);
                intent.putExtra("orderNo",uri.getQueryParameter("orderNo"));
                break;

            case "order":
                intent=new Intent(context, OrdersDetailActivity.class);
                intent.putExtra("orderNo",uri.getQueryParameter("no"));
                break;

            case "chatMessages":
                startMoor(context);
                break;

            case "popup":
                intent=new Intent(context, PopupActivity.class);
                intent.putExtra(Constants.INFO_ID,uri.getQueryParameter("id"));
                break;
            case "coupon":
                if(AccountInfo.getInstance().isLogin()){
                    String code=uri.getQueryParameter("code");
                    JSONStringer jsonStringer = new JSONStringer();
                    try {
                        jsonStringer.object()
                                .key("cdkey").value(code)
                                .endObject();
                    } catch (JSONException e) {
                        KLog.i("JsonException");
                    }
                    if(context instanceof  AppCompatActivity){
                        final AppCompatActivity activity=(AppCompatActivity)context;
                        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
                        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).bindCoupon(body).enqueue(new Callback<HttpBean<Problem>>() {
                            @Override
                            public void onResponse(Call<HttpBean<Problem>> call, Response<HttpBean<Problem>> response) {
                                if(response.isSuccessful()){
                                    String data=response.body().msg;
                                    if(response.body().code==200){
                                        CommonDialogFragment dialogFragment= CommonDialogFragment.newInstance(R.layout.dialog_coupon_success,"领取成功",data);
                                        dialogFragment.show(activity.getSupportFragmentManager(), "lottery");
                                        dialogFragment.setActionListener(new CommonDialogFragment.ActionListener() {
                                            @Override
                                            public void doAction() {
                                                startActivity(activity, CouponActivity.class);
                                            }
                                            @Override
                                            public void cancel() {}
                                        });
                                    }else if(response.body().code==3805){
                                        CommonDialogFragment.newInstance(R.layout.dialog_coupon_fail,"你已经领取过了",data)
                                                .show(activity.getSupportFragmentManager(), "lottery");
                                    }else {
                                        SystemUtil.toast(data);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<HttpBean<Problem>> call, Throwable t) {
                                SystemUtil.toast("优惠券领取失败");
                                KLog.d(t.toString());
                            }
                        });
                    }
                }
                break;

            case "plist":
                String activityId=uri.getQueryParameter("aid");
                String bid=uri.getQueryParameter("brandId");
                if(bid!=null) {
                    intent=new Intent(context, CommodityListActivity.class);
                    intent.putExtra(Constants.BRAND_ID,Long.valueOf(bid));
                    intent.putExtra(Constants.ACTIVITY_ID,activityId);
                }else if(activityId!=null){
                    intent = new Intent(context, DiscountListActivity.class);
                    intent.putExtra(Constants.ACTIVITY_ID, activityId);
                }
                break;

            case "productlist":
                String productId=uri.getQueryParameter("pid");
                String brandId=uri.getQueryParameter("brandId");
                if(productId!=null){
                    if(brandId!=null){
                        intent=new Intent(context, CommodityListActivity.class);
                        intent.putExtra(Constants.BRAND_ID,Long.valueOf(brandId));
                        intent.putExtra(Constants.ACTIVITY_ID,productId);
                        intent.putExtra(Constants.IS_POPUP,false);
                    }else {
                        intent=new Intent(context, DiscountListActivity.class);
                        intent.putExtra(Constants.ACTIVITY_ID,productId);
                        intent.putExtra(Constants.IS_POPUP,false);
                    }
                }
                break;

            case "popuplist":
                intent=new Intent(context, WebViewActivity.class);
                intent.putExtra(Constants.TARGET_URL, BuildConfig.POPUPURL);
                intent.putExtra(Constants.TITLE, "POP UP");
                intent.putExtra(Constants.TYPE,1);
                break;
            case "category":
                intent=new Intent(context, CategoryListActivity.class);
                break;
            case "lottery":
                intent=new Intent(context, DrawActivity.class);
                intent.putExtra(Constants.ACTIVITY_ID,Long.valueOf(uri.getQueryParameter("id")));
                break;
            case "discounts":
                intent=new Intent(context, DiscountListActivity.class);
                intent.putExtra(Constants.ACTIVITY_ID,uri.getQueryParameter("id"));
                intent.putExtra(Constants.IS_POPUP,false);
                break;
            case "reservation":
                if(AccountInfo.getInstance().isLogin()){
                    long aid=Long.valueOf(uri.getQueryParameter("id"));
                    intent=new Intent(context, ReservationActivity.class);
                    intent.putExtra(Constants.INFO_ID,aid);
                }else {
                    intent=new Intent(context,LoginActivity.class);
                }
                break;
            case "toast":
                String type=uri.getQueryParameter("type");
                if("notOpen".equals(type)){
                    SystemUtil.reportServerError(getString(R.string.not_open));
                }
                if("closed".equals(type)){
                    SystemUtil.reportServerError(getString(R.string.close));
                }
                break;
        }
        if(intent!=null){
            if(createTask){
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }
    public static void openTarget(Context context,String schema,String title){
        openTarget(context,schema,title,false);
    }

    public static void goToTarget(Context context,String schema) {

        openTarget(context,schema," ",true);
    }

    public static SHARE_MEDIA[] getDisplayList(Activity activity){
        List<SHARE_MEDIA> result=new ArrayList<>();
        for(SHARE_MEDIA media:medias){
            if(UMShareAPI.get(activity).isInstall(activity, media)){
                result.add(media);
            }
        }
        return result.toArray(new SHARE_MEDIA[result.size()]);
    }

    public static void loadImage(Context context,String picUrl,ImageView imageView){
        if(picUrl.endsWith(".gif")){
            Glide.with(context)
                    .load(picUrl)
                    .asGif()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)// DiskCacheStrategy.NONE
                    .placeholder(R.mipmap.white_logo)
                    .into(imageView);
        }else{
            Glide.with(context)
                    .load(picUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.mipmap.white_logo)
                    .into(imageView);
        }
    }

    public static void fillImage(Context context,String url,ImageView target){
        Glide.with(context).load(url)
                .dontAnimate()
                .into(target);
    }

    private static final byte[] DES_IV = { (byte) 0x12, (byte) 0x34, (byte) 0x56,
            (byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };// 设置向量，略去

    public static String decode(String data, String key) throws Exception {
        DESKeySpec keySpec = new DESKeySpec(key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Cipher deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        deCipher.init(Cipher.DECRYPT_MODE, keyFactory.generateSecret(keySpec), new IvParameterSpec(DES_IV));
        BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] pasByte = deCipher.doFinal(base64Decoder.decodeBuffer(data));
        return new String(pasByte, "UTF-8");
    }

    public static long getTimeStamp(int y,int m,int d){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(y,m,d);
        return cal.getTimeInMillis();
    }

    public static Drawable createDrawable(int fillColor,int roundRadius,int strokeWidth,int strokeColor){
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(fillColor);
        gd.setCornerRadius(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);
        return gd;
    }

    public static String getFomatNum(float num){

//        if(num==(int)num){
//            return String.format("%.0f",num);
//        }else {
//            return String.format("%.1f",num);
//        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setGroupingUsed(false);
        return nf.format(num);
    }

    public static int getDayOfMonth(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static  String getPrice(float num){
        return new DecimalFormat("0.00").format(num);
    }

    public static void startMoor(Context context){
        AccountInfo accountInfo=AccountInfo.getInstance();
        if(accountInfo.isLogin()){
            User user=accountInfo.getUser();
            String name=user.name+user.mobile;
            MoorActivity.start(context,name,user.userId);
        }else {
            startActivity(context,LoginActivity.class);
        }
    }

    public static boolean isNotEmpty(String str){
        if(str==null ||str.isEmpty()){
            return false;
        }
        return true;
    }

}
