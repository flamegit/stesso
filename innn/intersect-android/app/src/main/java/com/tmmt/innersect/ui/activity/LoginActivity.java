package com.tmmt.innersect.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.ui.fragment.BindingFragment;
import com.tmmt.innersect.ui.fragment.IdentifyFragment;
import com.tmmt.innersect.ui.fragment.LoginFragment;
import com.tmmt.innersect.ui.fragment.PasswordFragment;
import com.tmmt.innersect.ui.fragment.PhoneFragment;
import com.tmmt.innersect.ui.fragment.ResetPwdFragment;
import com.tmmt.innersect.ui.fragment.SetPasswordFragment;
import com.tmmt.innersect.ui.fragment.VerifyPasswordFragment;
import com.tmmt.innersect.ui.fragment.VideoLoginFragment;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;


/**
 * Created by flame on 2017/4/19.
 */

public class LoginActivity extends AppCompatActivity implements Communication{

    public static final String ACTION_LOGIN_CODE="com.tmmt.innersect.ACTION_LOGIN_CODE";
    public static final String ACTION_LOGIN_PASSWORD="com.tmmt.innersect.ACTION_LOGIN_PASSWORD";
    public static final String ACTION_LOGIN_SET_PASSWORD="com.tmmt.innersect.ACTION_SET_PASSWORD";
    public static final String ACTION_SHOW_TEL="com.tmmt.innersect.ACTION_SHOW_TEL";
    public static final String ACTION_VERIFY_PWD="com.tmmt.innersect.ACTION_VERIFY_PWD";
    public static final int VERIFY_CODE=1;
    public static final int PASSWORD=2;
    public static final int BINDING=3;
    public static final int RESET_PWD=4;
    public static final int SET_PWD=5;
    public static final int PHONE_LOGIN=6;

    Fragment mTarget;
    private String mMobile;
    private int mPrevious;
    private SHARE_MEDIA mPlatform;
    private Map<String,String> mData;

    public void setMobile(String mobile){
        mMobile=mobile;
    }
    public String getMobile(){
       return mMobile;
    }
    public void setThirdParty(SHARE_MEDIA platform, Map<String, String> data){
        mPlatform=platform;
        mData=data;
    }
    public SHARE_MEDIA getPlatform(){
        return mPlatform;
    }
    public Map<String,String> getDate(){
        return mData;
    }
    public void setPrevious(int pre){
        mPrevious=pre;
    }
    public int getPrevious(){
        return mPrevious;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initView(intent);
    }

    private void initView(Intent intent){
        String action=intent.getAction();
        if(action==null || action.equals(ACTION_SHOW_TEL)){
            if(AccountInfo.showVideo()){
                mTarget=new VideoLoginFragment();
                AccountInfo.setShowVideo();
            }else {
                mTarget=new LoginFragment();
            }
        }else if(action.equals(ACTION_LOGIN_CODE)){
            mTarget=new IdentifyFragment();
        }else if(action.equals(ACTION_LOGIN_PASSWORD)){
            mTarget=new PasswordFragment();
        }else if(action.equals(ACTION_LOGIN_SET_PASSWORD)){
            mTarget=new SetPasswordFragment();
        }else if(ACTION_VERIFY_PWD.equals(action)){
            mTarget=new VerifyPasswordFragment();
        }
        Fragment fragment=getSupportFragmentManager().findFragmentById(android.R.id.content);
        if(fragment==null){
            getSupportFragmentManager().beginTransaction().add(android.R.id.content,mTarget).commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content,mTarget)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();
        }
    }

    public void goToTarget(int target){
        Fragment fragment;
        String tag="tag";
        switch (target){
            case VERIFY_CODE:
                fragment=new IdentifyFragment();
                break;
            case PASSWORD:
                fragment=new PasswordFragment();
                break;
            case BINDING:
                fragment=new BindingFragment();
                break;
            case RESET_PWD:
                fragment =new ResetPwdFragment();
                tag="reset_pwd";
                break;
            case SET_PWD:
                fragment=new SetPasswordFragment();
                break;
            case PHONE_LOGIN:
                fragment=new PhoneFragment();
                break;
            default:
                fragment=new IdentifyFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content,fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(tag)
                .commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onBackPressed() {
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return;
        }
        if(mPrevious==RESET_PWD){
            getSupportFragmentManager().popBackStack("reset_pwd",0);
            mPrevious=0;
        }else {
            super.onBackPressed();
        }
    }
}
