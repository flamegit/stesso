package com.tmmt.innersect.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.mvp.model.User;
import com.tmmt.innersect.mvp.presenter.AccountPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.utils.Util;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by flame on 2017/4/19.
 */

public class AccountActivity extends BaseActivity implements CommonView<Integer> {

    @BindView(R.id.user_name)
    TextView mNameView;
    @BindView(R.id.tel_view)
    TextView mTelView;
    AccountInfo mAccount;
    AccountPresenter mPresenter;
    @BindView(R.id.weixin_switch)
    Switch mWeChatView;
    @BindView(R.id.sina_switch)
    Switch mSinaView;
    @BindView(R.id.fb_switch)
    Switch mFbView;
    @BindView(R.id.qq_switch)
    Switch mQqView;
    ProgressDialog mDialog;
    SHARE_MEDIA mPlatform;
    int mAction;

    @Override
    protected void initView() {
        super.initView();
        mAccount = AccountInfo.getInstance();
        mPresenter = new AccountPresenter();
        mPresenter.attachView(this);
        User user = mAccount.getUser();
        if(user!=null){
            StringBuilder stringBuilder = new StringBuilder(mAccount.getUser().mobile);
            stringBuilder.replace(3, 7, "****");
            mTelView.setText(stringBuilder.toString());
            mQqView.setChecked(user.isBindQQ);
            mWeChatView.setChecked(user.isBindWeixin);
            mSinaView.setChecked(user.isBindWeibo);
            mFbView.setChecked(user.isBindFacebook);
        }
    }
    @OnClick({R.id.weixin_switch, R.id.sina_switch, R.id.qq_switch, R.id.fb_switch})
    void bind3part(Switch switchView) {
        //AnalyticsUtil.reportEvent(AnalyticsUtil.ACCOUNT_THIRDPARTY_BIND);
        SHARE_MEDIA platform;
        switch (switchView.getId()) {
            case R.id.weixin_switch:
                platform = SHARE_MEDIA.WEIXIN;
                break;
            case R.id.sina_switch:
                platform = SHARE_MEDIA.SINA;
                break;
            case R.id.qq_switch:
                platform = SHARE_MEDIA.QQ;
                break;
            case R.id.fb_switch:
                platform = SHARE_MEDIA.FACEBOOK;
                break;
            default:
                platform = SHARE_MEDIA.FACEBOOK;
                break;
        }
        mPlatform=platform;
        if(switchView.isChecked()){
            mAction=1;
            UMShareAPI.get(this).getPlatformInfo(this, platform, umAuthListener);
        }else{
            mAction=2;
            mPresenter.unBind3Part(platform);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mAccount.getUser()!=null){
            mNameView.setText(mAccount.getUser().name);
        }
        SystemUtil.hideKey(this,mNameView);

    }

    @Override
    protected String getTitleString() {
        return getString(R.string.account_manager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account;
    }

    @OnClick(R.id.user_view)
    void modifyName() {
        Util.startActivity(this, ModifyNameActivity.class);
    }

    @OnClick(R.id.set_password)
    void setPassword() {
        isSetPassword();
    }

    @OnClick(R.id.my_address)
    void setAddress(){
        Util.startActivity(this,AddressActivity.class);
    }
    public void isSetPassword() {
        String countryCode=mAccount.getUser().countryCode;
        String mobile=mAccount.getUser().mobile;
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
                    Intent intent;
                    if(response.body().data){
                        intent=new Intent(LoginActivity.ACTION_VERIFY_PWD);
                    }else {
                        intent = new Intent(LoginActivity.ACTION_LOGIN_SET_PASSWORD);
                    }
                    startActivity(intent);
                }
            }
            @Override
            public void onFailure(Call<HttpBean<Boolean>> call, Throwable t) {
                KLog.d(t);
            }
        });
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
            //Toast.makeText(getApplicationContext(), "Authorize start", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            //Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            mPresenter.bind3Part(platform, data);
            showDialog();
            KLog.d(data);

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            //Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
            reset();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            reset();
            //Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

    private void reset(){
        switch (mPlatform) {
            case QQ:
                mQqView.setChecked(!mQqView.isChecked());
                break;
            case SINA:
                mSinaView.setChecked(!mSinaView.isChecked());
                break;
            case FACEBOOK:
                mFbView.setChecked(!mFbView.isChecked());
                break;
            case WEIXIN:
                mWeChatView.setChecked(!mWeChatView.isChecked());
                break;
        }
    }

    @Override
    public void success(Integer code) {
        if(mDialog!=null &&mDialog.isShowing()){
            mDialog.dismiss();
        }
        if(code!=200){
            Toast.makeText(this,"绑定/解绑失败",Toast.LENGTH_SHORT).show();
            reset();
            return;
        }

        User user=mAccount.getUser();
        boolean bind=mAction==1;
        switch (mPlatform){
            case QQ:
                user.isBindQQ=bind;
                break;
            case SINA:
                user.isBindWeibo=bind;
                break;
            case FACEBOOK:
                user.isBindFacebook=bind;
                break;
            case WEIXIN:
                user.isBindWeixin=bind;
                break;
        }

    }

    @Override
    public void failed() {}

    private void showDialog(){
        if(mDialog==null){
            mDialog = new ProgressDialog(this);

        }
        // 设置进度条风格，风格为圆形，旋转的
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置ProgressDialog 提示信息
        //mDialog.setMessage("请稍等。。。");
        // 设置ProgressDialog 的进度条是否不明确
        mDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mDialog.setCancelable(false);
        mDialog.show();
    }
}
