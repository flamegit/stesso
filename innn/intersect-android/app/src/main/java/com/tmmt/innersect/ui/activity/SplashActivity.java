package com.tmmt.innersect.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.mvp.model.AdInfo;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity implements CommonView<AdInfo> {

    private static final int REQUEST_READ_PHONE_STATE=1;
    @BindView(R.id.ad_image)
    ImageView mAdImage;
    @BindView(R.id.time_view)
    TextView mTimeView;
    CommonPresenter mPresenter;
    CountDownTimer mTimer;
    private AdInfo.Data mAdData;
    boolean skip=false;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.activity_ad);
        if(hasPermission()){
            AnalyticsUtil.reportEvent("advertising_showtimes");
        }
        ButterKnife.bind(this);
        mPresenter=new CommonPresenter();
        mPresenter.attachView(this);
        mPresenter.loadAd();
        mTimer =new CountDownTimer(7000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished/1000==4){
                    if(mAdData==null || mAdData.pic==null){
                        mTimer.onFinish();
                        return;
                    }
                    if(!isDestroyed()){
                        Glide.with(SplashActivity.this).load(mAdData.pic).crossFade().into(mAdImage);
                    }
                    skip=true;
                    mTimeView.setVisibility(View.VISIBLE);
                }
                if(millisUntilFinished/1000<=4){
                    mTimeView.setText(String.format(getString(R.string.skip),millisUntilFinished/1000));
                }
            }
            @Override
            public void onFinish() {
                if(hasPermission()){
                    openTarget(SplashActivity.this);
                }
                finish();
            }
        };
        mTimer.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }else {
                    Toast.makeText(this,"抱歉没有该权限，应用无法运行",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    public void success(AdInfo data) {
        if(data==null){
            return;
        }
        List<AdInfo.Data> infoList=data.list;
        if(infoList!=null && !infoList.isEmpty()){
            long curr=System.currentTimeMillis();
            for(int i=0;i<infoList.size();i++){
                AdInfo.Data adInfo=infoList.get(i);
                if(curr>adInfo.upTime && curr<adInfo.endTime){
                    mAdData=adInfo;
                    break;
                }
            }
        }
    }
    @Override
    public void failed() {}

    private void checkPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
        }
    }

    private boolean hasPermission(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        return permissionCheck==PackageManager.PERMISSION_GRANTED;
    }

    @OnClick(R.id.time_view)
    void open(){
        if(!hasPermission()){
            return;
        }
        AnalyticsUtil.reportEvent("advertising_skip");
        openTarget(this);
        finish();
    }

    public void openTarget(Context context){
        Util.startActivity(context,HomeActivity.class);
        if(AccountInfo.showVideo()){
            Util.startActivity(context,LoginActivity.class);
        }
    }

    @OnClick(R.id.ad_image)
    void goToTarget(){
        if(!hasPermission()){
            return;
        }
        AnalyticsUtil.reportEvent("advertising_detail");
        if(mAdData==null){
            return;
        }
        finish();
        openTarget(this);
        Util.openTarget(this,mAdData.content,mAdData.title);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mTimer.cancel();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
        mTimer.cancel();
        //RefWatcher refWatcher = App.getRefWatcher(this);
        //refWatcher.watch(this);
    }
}
