package com.tmmt.innersect.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.tmmt.innersect.App;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.DefaultConfig;
import com.tmmt.innersect.mvp.model.User;
import com.tmmt.innersect.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flame on 2017/6/12.
 */

public class AccountInfo {
    public static final String ACCOUNT_KEY="accountKey";
    public static final String ACCOUNT_INFO="accountInfo";
    private static final String OPEN_COUNT="open_count";
    private static final String SEARCH_HISTORY="search_history";
    private static final String DEFAULT="no_login";
    private static final String LOGIN_INFO="login_info";
    private static final String SHOW_VIDEO="show_video";
    private static final String SHOW_DIALOG="show_dialog";


    public static AccountInfo mInstance;
    private User mUser;

    private DefaultConfig mConfig;

    private AccountInfo(){}
    public static AccountInfo getInstance() {
        if (mInstance == null) {
            synchronized (AccountInfo.class) {
                if (mInstance == null) {
                    mInstance = new AccountInfo();
                }
            }
        }
        return mInstance;
    }

    public DefaultConfig getConfig(){
        String str="{\"desc\":\"客服工作时间：10:00-18:00\",\"et\":\"18:00\",\"msg\":\"如有疑问，请拨打客服电话\",\"st\":\"10:00\",\"tel\":\"400-168-6368\"}";
        if(mConfig==null){
            mConfig=new Gson().fromJson(str, DefaultConfig.class);
        }
        return mConfig;
    }

    public String getToken(){
        User user=getUser();
        return user==null?DEFAULT:user.token;
    }
    public String getSecretKey(){
        User user=getUser();
        return user==null?DEFAULT:user.secretkey;
    }

    public String getUserId(){
        User user=getUser();
        return user==null?DEFAULT:user.userId;
    }
    public void saveUserInfo(User user){
        mUser=user;
        SharedPreferences sp= App.getAppContext().getSharedPreferences(ACCOUNT_INFO, Context.MODE_PRIVATE);
        sp.edit().putString(ACCOUNT_KEY,user.toString())
                .apply();
        KLog.json(user.toString());
    }

    public void saveUserInfo(){
        if(mUser==null){
            return;
        }
        SharedPreferences sp= App.getAppContext().getSharedPreferences(ACCOUNT_INFO, Context.MODE_PRIVATE);
        sp.edit().putString(ACCOUNT_KEY,mUser.toString())
                .apply();
        KLog.json(mUser.toString());
    }

    public List<String> getSearchHistory(){
        SharedPreferences sp= App.getAppContext().getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE);
        String json= sp.getString(getUserId(),null);
        if(json==null){
            return new ArrayList<>();
        }
        return Util.jsonToList(json);
    }

    public void deleteHistory(){
        SharedPreferences sp= App.getAppContext().getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE);
        sp.edit().remove(getUserId()).apply();
    }

    public void saveSearchHistory(String word){
        List<String> history=getSearchHistory();
        if(!history.contains(word)) {
            if(history.size()>=6){
                history.remove(0);
            }
            history.add(word);
        }else {
            history.remove(word);
            history.add(word);
        }
        SharedPreferences sp= App.getAppContext().getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE);
        sp.edit().putString(getUserId(),new Gson().toJson(history)).apply();
    }

    public static boolean showVideo(){
        SharedPreferences sp= App.getAppContext().getSharedPreferences(LOGIN_INFO,Context.MODE_PRIVATE);
        return sp.getBoolean(SHOW_VIDEO,true);
    }

    public static void setShowVideo(){
        SharedPreferences sp= App.getAppContext().getSharedPreferences(LOGIN_INFO,Context.MODE_PRIVATE);
        sp.edit().putBoolean(SHOW_VIDEO,false).apply();
    }

    public static boolean showDialog(String key){
        SharedPreferences sp= App.getAppContext().getSharedPreferences(LOGIN_INFO,Context.MODE_PRIVATE);
        return sp.getBoolean(key,true);
    }

    public static void setShowDialog(String key){
        SharedPreferences sp= App.getAppContext().getSharedPreferences(LOGIN_INFO,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,false).apply();
    }


    public  void setPassword(){
        SharedPreferences sp= App.getAppContext().getSharedPreferences("PassWordInfo",Context.MODE_PRIVATE);
        sp.edit().putBoolean(getUser().mobile,true).apply();
    }

    public  boolean isSetPassword(String mobile){
        SharedPreferences sp= App.getAppContext().getSharedPreferences("PassWordInfo",Context.MODE_PRIVATE);
        return sp.getBoolean(mobile,false);
    }

    public  boolean isLogin(){
        return getUser()!=null;
    }

    public void logout(){
        mUser=null;
        SharedPreferences sp= App.getAppContext().getSharedPreferences(ACCOUNT_INFO, Context.MODE_PRIVATE);
        boolean success=sp.edit().clear().commit();
        if(success){
            KLog.i("logout success");
        }
    }

    public static int getOpenCount(){
        SharedPreferences sp= App.getAppContext().getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
        return sp.getInt(OPEN_COUNT,0);

    }
    public static void saveOpenCount(int count){
        SharedPreferences sp= App.getAppContext().getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
        sp.edit().putInt(OPEN_COUNT,count).apply();
    }

    public User getUser(){
        if(mUser!=null){
            return mUser;
        }
        SharedPreferences sp= App.getAppContext().getSharedPreferences(ACCOUNT_INFO,Context.MODE_PRIVATE);
        String json=sp.getString(ACCOUNT_KEY,null);
        if(json!=null){
            mUser= new Gson().fromJson(json,User.class);
            return mUser;
        }
        return null;
    }
}
