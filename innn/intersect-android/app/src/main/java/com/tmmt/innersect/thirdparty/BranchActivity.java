package com.tmmt.innersect.thirdparty;

/**
 * Created by flame on 2017/11/21.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.socks.library.KLog;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Common;
import com.tmmt.innersect.ui.activity.HomeActivity;
import com.tmmt.innersect.ui.activity.SplashActivity;
import com.tmmt.innersect.utils.FileUtil;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.utils.Util;

import org.json.JSONObject;

import java.util.List;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class BranchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    KLog.d(referringParams.toString());
                    //Log.i("BRANCH SDK", referringParams.toString());
                } else {
                    Log.i("BRANCH SDK", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);

        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getResInfo().enqueue(new NetCallback<List<Common>>() {
            @Override
            public void onSucceed(List<Common> list) {
                if (list == null) {
                    return;
                }
                for (Common data : list) {
                    final String name = String.valueOf(data.id) + data.item2;
                    if (!FileUtil.isFileExist(name)) {
                        if(!Util.isUrl(data.item3)){
                            return;
                        }
                        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).downloadFile(data.item3)
                                .subscribeOn(Schedulers.io())
                                .subscribe(new Subscriber<ResponseBody>() {
                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        KLog.d(e.toString());
                                    }
                                    @Override
                                    public void onNext(ResponseBody body) {
                                        if (FileUtil.writeResponseBodyToDisk(body)) {
                                            try {
                                                FileUtil.unZipFiles(name);
                                            } catch (Exception e) {}
                                        }
                                    }
                                });
                    }
                }
            }
        });

        Uri uri=getIntent().getData();
        String url;
        JSONObject sessionParams = Branch.getInstance().getLatestReferringParams();
        url = sessionParams.optString("url");
        if (!url.isEmpty()) {
            Util.startActivity(this, HomeActivity.class);
            Util.openTarget(this, url, "");
        }else if(uri!=null){
            Util.startActivity(this, HomeActivity.class);
            Util.openTarget(this, uri.toString(), "");
        }
        else {
            Util.startActivity(BranchActivity.this, SplashActivity.class);
        }
        finish();
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }
}