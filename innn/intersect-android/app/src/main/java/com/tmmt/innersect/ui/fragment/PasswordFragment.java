package com.tmmt.innersect.ui.fragment;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.presenter.LoginPresenter;
import com.tmmt.innersect.mvp.view.LoginView;
import com.tmmt.innersect.ui.activity.Communication;
import com.tmmt.innersect.ui.activity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by flame on 2017/4/12.
 */

public class PasswordFragment extends BaseLoginFragment implements LoginView{
    @BindView(R.id.password_view)
    EditText mPasswordView;
    @BindView(R.id.root_view)
    RelativeLayout mRootView;
    private TextView mActionView;
    Communication mCommunication;
    LoginPresenter mPresenter;
    private String mCode;
    private String mMobile;

    private int mHeight;

    @Override
    int getLayout() {
        return R.layout.fragment_password;
    }
    @Override
    protected void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this,view);
        mPresenter=new LoginPresenter();
        mPresenter.attachView(this);
        String telNo=mCommunication.getMobile();
        if(telNo!=null){
            mMobile=telNo.split("/")[1];
            mCode=telNo.split("/")[0];
        }

        mPasswordView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        //mPasswordView.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String pwd=mPasswordView.getText().toString().trim();
                mPresenter.passwordLogin(pwd,mMobile,mCode);
                return true;
            }
        });
        mPasswordView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=8){
                    mActionView.setEnabled(true);
                    mActionView.setBackgroundResource(R.drawable.common_yellow_bg);
                }else {
                    mActionView.setEnabled(false);
                    mActionView.setBackgroundResource(R.drawable.solid_gray_bg);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        mActionView = (TextView)View.inflate(getContext(), R.layout.yellow_action_view, null);
        mActionView.setEnabled(false);
        mActionView.setBackgroundResource(R.drawable.solid_gray_bg);
        mActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd=mPasswordView.getText().toString().trim();
                mPresenter.passwordLogin(pwd,mMobile,mCode);
            }
        });

        mPasswordView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPasswordView.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(mPasswordView, 0);
            }
        }, 600);
    }

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

    @OnClick(R.id.back_view)
    void back(){
        getActivity().onBackPressed();
    }

    @OnClick(R.id.close_view)
    void close(){
        getActivity().finish();
    }

    @OnClick(R.id.forget_password)
    public void forgetPassword(){
        LoginActivity activity=(LoginActivity)getActivity();
        if(activity!=null){
            activity.goToTarget(LoginActivity.RESET_PWD);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSoftKeyboardChange(int height) {

        if (height > 0) {
            if (mActionView.getParent()==null) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.bottomMargin = height;
                mRootView.addView(mActionView, layoutParams);
            }else {
                if(height>mHeight){
                    mRootView.removeView(mActionView);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    layoutParams.bottomMargin = height;
                    mRootView.addView(mActionView, layoutParams);
                }
            }
            mHeight=height;
            mActionView.setVisibility(View.VISIBLE);
        } else {
            if (mActionView != null) {
                mActionView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void success(int code) {
        if(code==200){
            getActivity().finish();
        }else if(code==1101){
            //Toast.makeText(getContext(),"密码错误",Toast.LENGTH_SHORT).show();
            mPasswordView.setText("");
            ViewCompat.animate(mPasswordView).translationX(50).setInterpolator(new BounceInterpolator()).start();
        }
    }
    @Override
    public void setPassword(boolean setPwd) {}

    @Override
    public void unBindMobile() {}

}
