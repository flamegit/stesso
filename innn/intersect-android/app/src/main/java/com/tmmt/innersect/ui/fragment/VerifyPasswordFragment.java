package com.tmmt.innersect.ui.fragment;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.presenter.AccountPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.activity.Communication;
import com.tmmt.innersect.ui.activity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by flame on 2017/4/12.
 */

public class VerifyPasswordFragment extends BaseLoginFragment implements CommonView<Integer> {
    @BindView(R.id.password_view)
    EditText mPasswordView;
    @BindView(R.id.root_view)
    RelativeLayout mRootView;

    @BindView(R.id.welcome)
    TextView mHintView;

    private TextView mActionView;
    Communication mCommunication;
    AccountPresenter mPresenter;

    @BindView(R.id.close_view)
    View mCloseView;


    @Override
    int getLayout() {
        return R.layout.fragment_password;
    }
    @Override
    protected void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this,view);
        mCloseView.setVisibility(View.INVISIBLE);
        mPresenter=new AccountPresenter();
        mPresenter.attachView(this);
        mHintView.setText("请输入原始密码");
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
                mPresenter.verifyPassword(pwd);
            }
        });

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


    @OnClick(R.id.forget_password)
    public void forgetPassword(){
        LoginActivity activity=(LoginActivity)getActivity();
        if(activity!=null){
            activity.goToTarget(LoginActivity.RESET_PWD);
        }
    }

    @OnClick(R.id.back_view)
    void back(){
        getActivity().onBackPressed();
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
            }
            mActionView.setVisibility(View.VISIBLE);
        } else {
            if (mActionView != null) {
                mActionView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void success(Integer code) {

        mCommunication.goToTarget(LoginActivity.SET_PWD);

    }

    @Override
    public void failed() {
        Toast.makeText(getContext(),"密码错误",Toast.LENGTH_SHORT).show();
        mPasswordView.setText("");
        ViewCompat.animate(mPasswordView).translationX(50).setInterpolator(new BounceInterpolator()).start();

    }
}
