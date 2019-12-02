package com.tmmt.innersect.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.presenter.LoginPresenter;
import com.tmmt.innersect.mvp.view.LoginView;
import com.tmmt.innersect.ui.activity.AgreementActivity;
import com.tmmt.innersect.ui.activity.Communication;
import com.tmmt.innersect.ui.activity.DialCodeActivity;
import com.tmmt.innersect.ui.activity.LoginActivity;
import com.tmmt.innersect.utils.Util;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by flame on 2017/4/12.
 */

public class LoginFragment extends BaseLoginFragment implements LoginView {

    private LoginPresenter mPresenter;
    Communication mCommunication;
    @BindView(R.id.tel_view)
    EditText mTelView;
    @BindView(R.id.root_view)
    RelativeLayout mRootView;
    @BindView(R.id.container)
    View mContainer;
    @BindView(R.id.select_code)
    TextView mCodeView;
    private View mActionView;
    @BindView(R.id.contract_view)
    TextView mContractView;
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
            //Toast.makeText(getContext(), "Authorize start", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            //Toast.makeText(getContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            KLog.d(data);
            if(mCommunication!=null){
                mCommunication.setThirdParty(platform,data);
            }
            mPresenter.exitMobile(platform,data);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            if(getActivity()!=null){
                if(!UMShareAPI.get(getContext()).isInstall(getActivity(), platform)){
                    Toast.makeText(getContext(), "没安装应用", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "授权失败", Toast.LENGTH_SHORT).show();
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            //Toast.makeText(getContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onAttach(Context context) {
        if (context instanceof Communication) {
            mCommunication = (Communication) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Communication");
        }
        super.onAttach(context);
    }

    @OnClick(R.id.close_view)
    public void closeView(){
        getActivity().finish();
    }

    @Override
    int getLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initView(View view) {

        super.initView(view);
        ButterKnife.bind(this, view);
        mPresenter=new LoginPresenter();
        mPresenter.attachView(this);
        mTelView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mTelView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        //mTelView.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        mTelView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                onNext();
                return true;
            }
        });
        mActionView = View.inflate(getContext(), R.layout.yellow_action_view, null);
        SpannableString spannableString=new SpannableString(getString(R.string.contract));
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
        spannableString.setSpan(foregroundColorSpan, 12, 20, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mContractView.setText(spannableString.toString());
        mActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onNext();
            }
        });
    }

    private void onNext(){
        String mobile=mTelView.getText().toString().trim();
        String code=mCodeView.getText().toString();
        if(!Util.isMobileNO(code,mobile)){
            Toast.makeText(getContext(),getString(R.string.phone_format_error),Toast.LENGTH_SHORT).show();
            return;
        }
        if(mCommunication==null){
            return;
        }
        mCommunication.setMobile(code+"/"+mobile);
        mPresenter.isSetPassword(code,mobile);
    }

    @OnClick(R.id.select_code)
    public void selectDialCode() {
        Intent intent=new Intent(getContext(),DialCodeActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            String dialCode=data.getStringExtra(DialCodeActivity.CODE_KEY);
            if(dialCode!=null){
                mCodeView.setText(dialCode);
            }
        }
    }

    @OnClick({R.id.qq_login, R.id.wechat_login, R.id.sina_login, R.id.fb_login})
    public void thirdLogin(View view) {
        SHARE_MEDIA platform;
        switch (view.getId()) {
            case R.id.qq_login:
                platform = SHARE_MEDIA.QQ;
                break;
            case R.id.fb_login:
                platform = SHARE_MEDIA.FACEBOOK;
                break;
            case R.id.sina_login:
                platform = SHARE_MEDIA.SINA;
                break;
            case R.id.wechat_login:
                platform = SHARE_MEDIA.WEIXIN;
                break;
            default:
                platform = SHARE_MEDIA.FACEBOOK;
                break;
        }

        UMShareAPI.get(getContext()).getPlatformInfo(getActivity(),platform,umAuthListener);

    }

    @Override
    public void onSoftKeyboardChange(int height) {
        if (height >0) {
            if (mActionView.getParent()==null) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.bottomMargin = height;
                mRootView.addView(mActionView, layoutParams);
            }
            mActionView.setVisibility(View.VISIBLE);
        } else {
            if (mActionView != null) {
                mActionView.setVisibility(View.GONE);
            }
        }
    }

    @OnClick(R.id.contract_view)
    void openContract(){
        Util.startActivity(getContext(),AgreementActivity.class);
    }

    @Override
    public void unBindMobile() {
        mCommunication.goToTarget(LoginActivity.BINDING);//unbind phone number;
    }

    @Override
    public void setPassword(boolean setPwd) {
        int target;
        if(setPwd){
            target= LoginActivity.PASSWORD;
        }else {
            target=LoginActivity.VERIFY_CODE;
        }
        mCommunication.goToTarget(target);

    }

    @Override
    public void success(int code) {
        if(code==200){
            getActivity().finish();
        }else {
            //Toast.makeText(getContext(),"登录失败",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }
}
