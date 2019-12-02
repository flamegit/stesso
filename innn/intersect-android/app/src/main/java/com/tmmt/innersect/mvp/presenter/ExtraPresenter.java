package com.tmmt.innersect.mvp.presenter;

import com.socks.library.KLog;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.RegisterInfo;
import com.tmmt.innersect.mvp.view.ExtraView;

import org.json.JSONException;
import org.json.JSONStringer;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by flame on 2017/5/3.
 */

public class ExtraPresenter extends BasePresenter<ExtraView> {

    public void getRegisterInfo(long id) {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getRegisterInfo(id, AccountInfo.getInstance().getUserId())
                .enqueue(new NetCallback<RegisterInfo>() {
                    @Override
                    public void onSucceed(RegisterInfo data) {
                        fillView(data);
                    }
                });
    }

    public void reserve(long id,String location){
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("id").value(id)
                    .key("location").value(location)
                    .endObject();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).reserve(body).enqueue(new NetCallback<Boolean>() {
            @Override
            public void onSucceed(Boolean data) {
                fillView(data);
            }
        });
    }

    private <T> void fillView(T data) {
        if (mView != null) {
            mView.onSuccess(data);
        }
    }


}
