package com.tmmt.innersect.mvp.presenter;


import com.google.gson.Gson;
import com.socks.library.KLog;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.mvp.model.Address;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.mvp.model.Status;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.utils.SystemUtil;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by flame on 2017/5/3.
 */

public class AddressPresenter extends BasePresenter<CommonView> {

    private String mUserId;

    private Callback<Status> mCallback;

    public AddressPresenter() {
        super();
        mUserId = AccountInfo.getInstance().getUserId();
        mCallback=new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful()){
                    if(response.body().code==200){
                    }else {
                        SystemUtil.reportServerError(response.body().msg);
                        KLog.d("服务器错误"+response.body().code);
                    }
                }
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                KLog.d(t);
                SystemUtil.reportNetError(t);
            }
        };
    }

    public void loadAddresses() {
        KLog.i(mUserId);
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getAddressList(mUserId)
                .map(new Func1<HttpBean<List<Address>>, List<Address>>() {
                    @Override
                    public List<Address> call(HttpBean<List<Address>> listHttpBean) {
                        return listHttpBean.data;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Address>>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e);
                        SystemUtil.reportNetError(e);
                    }
                    @Override
                    public void onNext(List<Address> addressList) {
                        List<Address> list = addressList;
                        if(mView!=null){
                            mView.success(list);
                        }
                    }
                });

    }

    public void deleteAddress(int id) {
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("id").value(id)
                    .endObject();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).removeAddress(body).enqueue(mCallback);
    }


    public void setDefaultAddress(int id) {
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("id").value(id)
                    .endObject();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).setDefaultAddress(body).enqueue(mCallback);
    }


    public void saveAddress(final Address address) {
        String json = new Gson().toJson(address);
        KLog.json(json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).addAddress(body).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful()){
                    if(response.body().code!=200){
                        SystemUtil.reportServerError(response.body().msg);
                        KLog.d("服务器错误"+response.body().code);

                    }
                    if(mView!=null){
                        mView.success(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                KLog.d(t);
                SystemUtil.reportNetError(t);
            }
        });
    }


    public void updateAddress(Address address) {
        String json = new Gson().toJson(address);
        KLog.json(json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).updateAddress(body).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful()){
                    if(response.body().code!=200){
                        SystemUtil.reportServerError(response.body().msg);
                    }
                    if(mView!=null){
                        mView.success(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                KLog.d(t);
                SystemUtil.reportNetError(t);
            }
        });
    }
}
