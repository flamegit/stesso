package com.tmmt.innersect.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.socks.library.KLog;
import com.tmmt.innersect.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import okhttp3.ResponseBody;

/**
 * Created by flame on 2017/12/14.
 */

public class FileUtil {

//    /**
//     * 图片缩放比例
//     */
//    private static final float BITMAP_SCALE = 0.6f;
//    /**
//     * 最大模糊度(在0.0到25.0之间)
//     */
//    private static final float BLUR_RADIUS = 20f;
//    /**
//     * 模糊图片的具体方法
//     *
//     * @param context   上下文对象
//     * @param image     需要模糊的图片
//     * @return          模糊处理后的图片
//     */
//    public static Bitmap blur(Context context, Bitmap image) {
//        // 计算图片缩小后的长宽
//        int width = Math.round(image.getWidth() * BITMAP_SCALE);
//        int height = Math.round(image.getHeight() * BITMAP_SCALE);
//        // 将缩小后的图片做为预渲染的图片。
//        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
//        // 创建一张渲染后的输出图片。
//        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
//        // 创建RenderScript内核对象
//        RenderScript rs = RenderScript.create(context);
//        // 创建一个模糊效果的RenderScript的工具对象
//        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
//        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。
//        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
//        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
//        // 设置渲染的模糊程度, 25f是最大模糊度
//        blurScript.setRadius(BLUR_RADIUS);
//        // 设置blurScript对象的输入内存
//        blurScript.setInput(tmpIn);
//        // 将输出数据保存到输出内存中
//        blurScript.forEach(tmpOut);
//        // 将数据填充到Allocation中
//        tmpOut.copyTo(outputBitmap);
//        rs.destroy();
//        return outputBitmap;
//    }

//    public static Typeface getTypeface(Context context, String font){
//        try{
//            Typeface typeface = Typeface.createFromAsset(context.getAssets(),"font/"+font);
//            return typeface;
//        }catch(Exception e){}
//        return null;
//    }

     /* 解压到指定目录
     * @param zipPath
     * @param descDir
     * @author isea533
     */

    private static String BASE_PATH=App.getAppContext().getExternalFilesDir("LotteryRes") + File.separator;

    private static String ZIP_NAME="zip";

    public static  boolean isFileExist(String name){

        File pathFile = new File(BASE_PATH+name);
        return pathFile.exists();
    }

    public static Map<String,Bitmap> getBitmaps(String name){
        Map<String,Bitmap> list=new TreeMap<>();
        File dir=new File(BASE_PATH+name);
        try{
            for(File f:dir.listFiles()){
                if(f.isFile()){
                    String[] str=f.getName().split("\\.");
                    if(str!=null){
                        list.put(str[0],BitmapFactory.decodeFile(f.getAbsolutePath()));
                    }
                }
            }
        }catch (Exception e){}
        return list;
    }

    @SuppressWarnings("rawtypes")
    public static void unZipFiles(String name)throws IOException{

        String basePath=BASE_PATH+name+File.separator;
        ZipFile zip = new ZipFile(new File(BASE_PATH+ZIP_NAME));
        try{
            for(Enumeration entries = zip.entries(); entries.hasMoreElements();){
                ZipEntry entry = (ZipEntry)entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zip.getInputStream(entry);
                String outPath = (basePath+zipEntryName).replaceAll("\\*", "/");
                KLog.d(outPath);

                //判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if(!file.exists()){
                    file.mkdirs();
                }
                //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if(new File(outPath).isDirectory()){
                    continue;
                }
                //输出文件路径信息
                //System.out.println(outPath);
                OutputStream out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while((len=in.read(buf1))>0){
                    out.write(buf1,0,len);
                }
                in.close();
                out.close();
            }
        }catch (Exception e) {
        }

    }

    public static boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File out = new File(BASE_PATH+ZIP_NAME);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                //long fileSize = body.contentLength();
                //long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(out);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    //fileSizeDownloaded += read;
                    //Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
