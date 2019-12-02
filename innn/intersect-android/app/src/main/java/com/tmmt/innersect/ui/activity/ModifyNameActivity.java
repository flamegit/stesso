package com.tmmt.innersect.ui.activity;


import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.mvp.model.User;
import com.tmmt.innersect.utils.AnalyticsUtil;

import org.json.JSONException;
import org.json.JSONStringer;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyNameActivity extends BaseActivity {

    @BindView(R.id.action_view)
    TextView mActionView;
    @BindView(R.id.user_name)
    EditText mUserView;
    private String mUserName;
    AccountInfo mAccount;

    @Override
    protected String getTitleString() {
        return getString(R.string.modify_name);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_modify_name;
    }

    @OnClick(R.id.clear_text)
    void clearText(){
        mUserView.setText("");
    }
    @Override
    protected void initView() {
        super.initView();
        mActionView.setText(getString(R.string.save));
        mAccount= AccountInfo.getInstance();
        mUserName=mAccount.getUser().name;
        mUserView.setText(mUserName);
        mUserView.setSelection(mUserName.length());
    }


    @OnClick(R.id.action_view)
    void modifyName() {
        String name=mUserView.getText().toString().trim();
        if(name.isEmpty()){
            Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if(name.equals(mUserName)){
            onBackPressed();
            return;
        }
        AnalyticsUtil.reportEvent(AnalyticsUtil.ACCOUNT_NICKNAME_SAVE);
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("icon").value("icon")
                    .key("name").value(name)
                    .key("token").value(mAccount.getToken())
                    .endObject();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).editUser(body).enqueue(new Callback<HttpBean<User>>() {
            @Override
            public void onResponse(Call<HttpBean<User>> call, Response<HttpBean<User>> response) {
                if(response.isSuccessful()){
                    mAccount.getUser().name=name;
                    mAccount.saveUserInfo();
                    finish();
                }else {
                    Toast.makeText(ModifyNameActivity.this,response.body().msg,Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<HttpBean<User>> call, Throwable t) {
                Toast.makeText(ModifyNameActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
