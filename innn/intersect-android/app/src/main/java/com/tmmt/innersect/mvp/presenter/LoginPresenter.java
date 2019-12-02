package com.tmmt.innersect.mvp.presenter;

import com.socks.library.KLog;
import com.tmmt.innersect.App;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.mvp.model.Status;
import com.tmmt.innersect.mvp.model.User;
import com.tmmt.innersect.mvp.view.LoginView;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.SystemUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by flame on 2017/5/3.
 */

public class LoginPresenter extends BasePresenter<LoginView> {

    String appId;

    public LoginPresenter() {
        appId = SystemUtil.getIMEI();
    }
    public void getVerifyCode(String countryCode, String mobile) {
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("countryCode").value(countryCode)
                    .key("mobile").value(mobile)
                    .endObject();
            KLog.json(jsonStringer.toString());

        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getVerifyCode(body).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful()){
                    if(response.body().code!=200){
                        KLog.d("服务器错误"+response.body().code);
                        SystemUtil.reportServerError(response.body().msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                KLog.i(t);
                SystemUtil.reportNetError(t);
            }
        });
    }

    public void verifyCodeLogin(String code, String countryCode, String mobile) {
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("sourceType").value(5)
                    .key("validCode").value(code)
                    .key("countryCode").value(countryCode)
                    .key("mobile").value(mobile)
                    .key("uid").value(appId)
                    .endObject();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        login(jsonStringer.toString(), 5);

    }

    public void thirdPartyLogin(SHARE_MEDIA platform, Map<String, String> data, String verifyCode, String mobile, String countryCode) {
        String unionid = data.get("unionid");//weixin
        String uid = data.get("uid");        //qq
        String openid = data.get("openid");
        String accessToken = data.get("access_token");
        if(accessToken==null){
            accessToken=uid;
        }
        try {
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("uid").value(appId)
                    .key("validCode").value(verifyCode)
                    .key("countryCode").value(countryCode)
                    .key("mobile").value(mobile)
                    .key("accessToken").value(accessToken);

            int type = 1;
            switch (platform) {
                case WEIXIN:
                    type = 1;
                    jsonStringer.key("wxUnid").value(unionid)
                            .key("sourceType").value(type)
                            .key("wxOpenid").value(openid);
                    break;
                case QQ:
                    type = 6;
                    jsonStringer.key("qqUnid").value(uid)
                            .key("sourceType").value(type)
                            .key("qqOpenid").value(openid);
                    break;
                case SINA:
                    type = 2;
                    jsonStringer.key("weiboUnid").value(uid)
                            .key("sourceType").value(type);
                    break;
                case FACEBOOK:
                    type = 3;
                    String name=data.get("name");
                    String gender=data.get("gender");
                    if(gender==null){
                        gender="girl";
                    }
                    String url=data.get("iconurl");
                    jsonStringer.key("facebookUnid").value(uid)
                            .key("facebookThirdPartyId").value(uid)
                            .key("sourceType").value(type)
                            .key("facebookNick").value(name)
                            .key("facebookSex").value(gender)
                            .key("facebookUserIcon").value(url);
                    break;
            }
            jsonStringer.endObject();
            KLog.json(jsonStringer.toString());
            login(jsonStringer.toString(), type);
        } catch (JSONException e) {
            KLog.d(e);
        }
    }


    public void thirdPartyLogin(SHARE_MEDIA platform, Map<String, String> data) {
        String unionid = data.get("unionid");
        String openid = data.get("openid");
        String uid = data.get("uid");        //qq
        String accessToken = data.get("access_token");
        if(accessToken==null){
            accessToken=uid;
        }
        try {
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("uid").value(appId)
                    .key("accessToken").value(accessToken);
            int type = 1;
            switch (platform) {
                case WEIXIN:
                    type = 1;
                    jsonStringer.key("wxUnid").value(unionid)
                            .key("sourceType").value(type)
                            .key("wxOpenid").value(openid);
                    break;
                case QQ:
                    type = 6;
                    jsonStringer.key("qqUnid").value(uid)
                            .key("sourceType").value(type)
                            .key("qqOpenid").value(openid);
                    break;
                case SINA:
                    type = 2;
                    jsonStringer.key("weiboUnid").value(uid)
                            .key("sourceType").value(type);
                    break;
                case FACEBOOK:
                    type = 3;
                    String name=data.get("name");
                    String gender=data.get("gender");

                    if(gender==null){
                        gender="girl";
                    }

                    String url=data.get("iconurl");
                    jsonStringer.key("facebookUnid").value(uid)
                            .key("facebookThirdPartyId").value(uid)
                            .key("sourceType").value(type)
                            .key("facebookNick").value(name)
                            .key("facebookSex").value(gender)
                            .key("facebookUserIcon").value(url);
                    break;
            }
            jsonStringer.endObject();
            KLog.json(jsonStringer.toString());
            login(jsonStringer.toString(), type);
        } catch (JSONException e) {
            KLog.d(e);
        }
    }

    private void login(String json, final int platform) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Subscription subscription=ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).login(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpBean<User>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e);
                        SystemUtil.reportNetError(e);
                    }

                    @Override
                    public void onNext(HttpBean<User> userInfoViewModel) {
                        User user = userInfoViewModel.data;
                        if (userInfoViewModel.code == 200) {
                            AccountInfo.getInstance().saveUserInfo(user);
                            setAlias(user.userId);
                            report();
                        }else {
                            SystemUtil.reportServerError(userInfoViewModel.msg);
                        }
                        mView.success(userInfoViewModel.code);
                        loginReport(platform,userInfoViewModel.code);
                    }
                });
        addSubscription(subscription);
    }

    private void setAlias(String alias) {
        JPushInterface.setAlias(App.getAppContext(), alias, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                KLog.i(i);
                if (i == 6002) {}
            }
        });
    }

    private void report(){
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).reportAction(1).enqueue(new NetCallback<String>() {
            @Override
            public void onSucceed(String data) {}
        });
    }

    public void isSetPassword(String countryCode,String mobile) {
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("countryCode").value(countryCode)
                    .key("mobile").value(mobile)
                    .endObject();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonStringer.toString());
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).isPassWordExit(body).enqueue(new Callback<HttpBean<Boolean>>() {
            @Override
            public void onResponse(Call<HttpBean<Boolean>> call, Response<HttpBean<Boolean>> response) {
                if(response.isSuccessful()){
                    if(mView!=null){
                        mView.setPassword(response.body().data);
                    }
                }
            }
            @Override
            public void onFailure(Call<HttpBean<Boolean>> call, Throwable t) {
                KLog.d(t);
                SystemUtil.reportNetError(t);
            }
        });
    }

    private void loginReport(int platform, int code) {
        int result = code == 200 ? 1 : -1;
        switch (platform) {
            case 1:
            case 2:
            case 3:
            case 6:
                AnalyticsUtil.reportEvent(AnalyticsUtil.LOGIN_QUICKLOG_AUTHORIZATION, "result", String.valueOf(result));
            case 4:
                AnalyticsUtil.reportEvent(AnalyticsUtil.LOGIN_MOBILE_PASSWORD, "result", String.valueOf(result));
                break;
            case 5:
                AnalyticsUtil.reportEvent(AnalyticsUtil.LOGIN_MOBILE_VERIFICATIONCODE, "result", String.valueOf(result));
                break;
        }

    }

    public void passwordLogin(String password, String mobile, String countryCode) {
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("sourceType").value(4)
                    .key("password").value(password)
                    .key("countryCode").value(countryCode)
                    .key("mobile").value(mobile)
                    .key("uid").value(appId)
                    .endObject();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        login(jsonStringer.toString(), 4);
    }

    public void exitMobile(final SHARE_MEDIA platform, final Map<String, String> data) {
        String unionid = data.get("unionid");
        String uid = data.get("uid");

        try {
            JSONStringer jsonStringer = new JSONStringer().object();
            int type = 1;
            switch (platform) {
                case WEIXIN:
                    type = 1;
                    jsonStringer.key("sourceType").value(type)
                            .key("unid").value(unionid);
                    break;
                case QQ:
                    type = 6;
                    jsonStringer.key("sourceType").value(type)
                            .key("unid").value(uid);
                    break;
                case SINA:
                    type = 2;
                    jsonStringer.key("sourceType").value(type)
                            .key("unid").value(uid);
                    break;
                case FACEBOOK:
                    type = 3;
                    jsonStringer.key("sourceType").value(type)
                            .key("unid").value(uid);
                    break;
            }
            jsonStringer.endObject();
            KLog.json(jsonStringer.toString());

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());

            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).existMobile(body)
                    .doOnNext(new Action1<Status>() {
                        @Override
                        public void call(Status status) {
                            if (status.data != null && !status.data.isEmpty()) {
                                KLog.i(status.data);
                                thirdPartyLogin(platform, data);
                            }
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Status>() {
                        @Override
                        public void onCompleted() {}

                        @Override
                        public void onError(Throwable e) {
                            KLog.d(e);
                            //SystemUtil.reportNetError(e);
                        }

                        @Override
                        public void onNext(Status status) {
                            if (status.data == null || status.data.isEmpty()) {
                                if(mView!=null){
                                    mView.unBindMobile();
                                }
                            }
                        }
                    });
        } catch (JSONException e) {
            KLog.d(e);
        }
    }

}
