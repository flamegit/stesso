//package com.tmmt.innersect.ui.activity;
//
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//
//import com.microquation.linkedme.android.LinkedME;
//import com.microquation.linkedme.android.indexing.LMUniversalObject;
//import com.microquation.linkedme.android.util.LinkProperties;
//import com.tmmt.innersect.common.Constants;
//import com.tmmt.innersect.utils.SystemUtil;
//import com.tmmt.innersect.utils.Util;
//
//import java.util.HashMap;
//
///**
// * <p>中转页面</p>
// *
// * Created by LinkedME06 on 16/11/17.
// */
//
//public class MiddleActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.i(LinkedME.TAG, "onCreate: MiddleActivity is called.");
//        //获取与深度链接相关的值
//        LinkProperties linkProperties = getIntent().getParcelableExtra(LinkedME.LM_LINKPROPERTIES);
//        LMUniversalObject lmUniversalObject = getIntent().getParcelableExtra(LinkedME.LM_UNIVERSALOBJECT);
//        //LinkedME SDK初始化成功，获取跳转参数，具体跳转参数在LinkProperties中，和创建深度链接时设置的参数相同；
//        if (linkProperties != null) {
//            Log.i("LinkedME-Demo", "Channel " + linkProperties.getChannel());
//            Log.i("LinkedME-Demo", "control params " + linkProperties.getControlParams());
//            Log.i("LinkedME-Demo", "link(深度链接) " + linkProperties.getLMLink());
//            //获取自定义参数封装成的HashMap对象
//            HashMap<String, String> hashMap = linkProperties.getControlParams();
//            //获取传入的参数
//            String url = hashMap.get("url");
//            if (url!= null) {
//                Util.openTarget(this,url," ");
//
////                } else if (view.equals(getString(R.string.str_h5_apps))) {
////                    title = getString(R.string.str_apps_name);
////                    shareContent = getString(R.string.str_share_content_apps);
////                    url_path = getString(R.string.str_path_apps);
////                } else if (view.equals(getString(R.string.str_h5_features))) {
////                    title = getString(R.string.str_features_name);
////                    shareContent = getString(R.string.str_share_content_features);
////                    url_path = getString(R.string.str_path_features);
////                } else if (view.equals(getString(R.string.str_h5_intro))) {
////                    title = getString(R.string.str_intro_name);
////                    shareContent = getString(R.string.str_share_content_intro);
////                    url_path = getString(R.string.str_path_intro);
////                }
////                openActivity(title, view, shareContent, url_path, ShareActivity.class);
//            }
//            //清除跳转数据，该方法理论上不需要调用，因Android集成方式各种这样，若出现重复跳转的情况，可在跳转成功后调用该方法清除参数
//            //LinkedME.getInstance().clearSessionParams();
//        }
//
//        //***
//        String schema=getIntent().getStringExtra(Constants.SCHEMA);
//        if(schema!=null){
//            Util.goToTarget(this,schema);
//        }
//        else {
//           // SystemUtil.reportServerError("schema is null");
//        }
//
//        if (lmUniversalObject != null) {
//            Log.i("LinkedME-Demo", "title " + lmUniversalObject.getTitle());
//            Log.i("LinkedME-Demo", "control " + linkProperties.getControlParams());
//            Log.i("ContentMetaData", "metadata " + lmUniversalObject.getMetadata());
//        }
//        finish();
//    }
//
////    public void openActivity(String title, String param_view, String shareContent, String url_path, Class clazz) {
////        Intent intent = new Intent(this, clazz);
////        if (!TextUtils.isEmpty(title)) {
////            intent.putExtra(ShareActivity.TITLE, title);
////        }
////        if (!TextUtils.isEmpty(param_view)) {
////            intent.putExtra(ShareActivity.PARAM_VIEW, param_view);
////        }
////        if (!TextUtils.isEmpty(shareContent)) {
////            intent.putExtra(ShareActivity.SHARE_CONTENT, shareContent);
////        }
////        if (!TextUtils.isEmpty(url_path)) {
////            intent.putExtra(ShareActivity.URL_PATH, url_path);
////        }
////        startActivity(intent);
////   }
//}
