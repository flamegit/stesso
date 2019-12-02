package com.tmmt.innersect.mvp.presenter;

import com.socks.library.KLog;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.mvp.model.Status;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.utils.SystemUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by flame on 2017/5/3.
 */

public class AccountPresenter extends BasePresenter<CommonView<Integer>> {

    public void bind3Part(SHARE_MEDIA platform, Map<String, String> data) {
        String userId = AccountInfo.getInstance().getUserId();
        String unionid = data.get("unionid");
        String uid = data.get("uid");        //qq

        String openid = data.get("openid");
        String accessToken = data.get("access_token");
        try {
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("userId").value(userId)
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
                    jsonStringer.key("facebookUnid").value(uid)
                            .key("facebookThirdPartyId").value(uid)
                            .key("sourceType").value(type);
                    KLog.d(data);
                    break;
            }
            jsonStringer.endObject();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());

            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).bind3part(body).enqueue(new Callback<HttpBean<Boolean>>() {
                @Override
                public void onResponse(Call<HttpBean<Boolean>> call, Response<HttpBean<Boolean>> response) {
                    if(response.isSuccessful()){
                        mView.success(response.body().code);
                    }else {
                        KLog.d("http error",response.code());
                    }
                }
                @Override
                public void onFailure(Call<HttpBean<Boolean>> call, Throwable t) {
                    KLog.d(t);
                    SystemUtil.reportNetError(t);

                }
            });
        } catch (JSONException e) {
            KLog.d(e);
        }
    }

    public void unBind3Part(SHARE_MEDIA platform) {
        String userId = AccountInfo.getInstance().getUserId();
        try {
            JSONStringer jsonStringer = new JSONStringer().object()
                    .key("userId").value(userId);
            int type = 1;
            switch (platform) {
                case WEIXIN:
                    type = 1;
                    break;
                case QQ:
                    type = 6;
                    break;
                case SINA:
                    type = 2;
                    break;
                case FACEBOOK:
                    type = 3;
                    break;
            }
            jsonStringer.key("sourceType").value(type)
                    .endObject();
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).unbind3part(body).enqueue(new Callback<HttpBean<Boolean>>() {
                @Override
                public void onResponse(Call<HttpBean<Boolean>> call, Response<HttpBean<Boolean>> response) {
                    if(response.isSuccessful()){
                        mView.success(response.body().code);
                    }else {
                        KLog.d("http error",response.code());
                    }
                }
                @Override
                public void onFailure(Call<HttpBean<Boolean>> call, Throwable t) {
                    SystemUtil.reportNetError(t);
                }
            });

        } catch (JSONException e) {}
    }

    public void logout() {
        JSONStringer jsonStringer = new JSONStringer();
        String token = AccountInfo.getInstance().getToken();
        try {
            jsonStringer.object()
                    .key("token").value(token)
                    .endObject();
            KLog.json(jsonStringer.toString());

        } catch (JSONException e) {
            KLog.i("JsonException");
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).logout(body).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    mView.success(response.body().code);
                }else {
                    KLog.d("http error",response.code());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                KLog.d(t);
                SystemUtil.reportNetError(t);
            }
        });
    }

    public void setPassword(String password) {
        JSONStringer jsonStringer = new JSONStringer();
        String token = AccountInfo.getInstance().getToken();
        try {
            jsonStringer.object()
                    .key("password").value(password)
                    .key("token").value(token)
                    .endObject();
            KLog.json(jsonStringer.toString());

        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).setPassWord(body).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    mView.success(response.body().code);
                }else {
                    KLog.d("http error",response.code());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                SystemUtil.reportNetError(t);
            }
        });
    }

    public void verifyPassword(String pwd){
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("password").value(pwd)
                    .key("token").value(AccountInfo.getInstance().getToken())
                    .endObject();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonStringer.toString());
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).verifyPassword(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpBean<Boolean>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e);
                        SystemUtil.reportNetError(e);
                    }

                    @Override
                    public void onNext(HttpBean<Boolean> result) {
                        if (result.data){
                            mView.success(result.code);
                        }else {
                            mView.failed();
                        }
                    }
                });
    }

}
