package com.tmmt.innersect.ui.fragment;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.mvp.presenter.AccountPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.activity.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by flame on 2017/4/12.
 */

public class SetPasswordFragment extends BaseLoginFragment implements CommonView<Integer> {

    @BindView(R.id.set_password)
    EditText mPasswordView;
    @BindView(R.id.verify_password)
    EditText mVerifyView;

    @BindView(R.id.root_view)
    RelativeLayout mRootView;
    @BindView(R.id.first_indicator)
    View mFirstIndicator;

    @BindView(R.id.close_view)
    View mCloseView;

    @OnClick(R.id.back_view)
    void back(){
        getActivity().onBackPressed();
    }

    @BindView(R.id.second_indicator)
    View mSecondIndicator;
    AccountPresenter mPresenter;

    private TextView mActionView;
    @Override
    int getLayout() {
        return R.layout.fragment_set_password;
    }
    @Override
    protected void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this,view);
        if(LoginActivity.ACTION_VERIFY_PWD.equals(getActivity().getIntent().getAction())){
            mCloseView.setVisibility(View.GONE);
        }
        mPresenter=new AccountPresenter();
        mPresenter.attachView(this);
        mActionView = (TextView)View.inflate(getContext(), R.layout.yellow_action_view, null);
        mActionView.setText(getString(android.R.string.ok));
        mActionView.setEnabled(false);
        mActionView.setBackgroundResource(R.drawable.solid_gray_bg);
        mActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.setPassword(mPasswordView.getText().toString().trim());
            }
        });
        mPasswordView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(checkFormat(s.toString())){
                    mFirstIndicator.setVisibility(View.VISIBLE);
                }else {
                    mFirstIndicator.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mVerifyView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String pwd=mPasswordView.getText().toString().trim();
                if(checkFormat(pwd) && pwd.equals(s.toString())){
                        mSecondIndicator.setVisibility(View.VISIBLE);
                        mActionView.setBackgroundResource(R.drawable.common_yellow_bg);
                        mActionView.setEnabled(true);
                }else {
                    mSecondIndicator.setVisibility(View.GONE);
                    mActionView.setBackgroundResource(R.drawable.solid_gray_bg);
                    mActionView.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private boolean checkFormat(String pwd){
        String pwdRegex="[0-9a-zA-Z]{8,16}";
        return pwd.matches(pwdRegex);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSoftKeyboardChange(int height) {
        if (height > 0) {
            if (mActionView.getParent() == null) {
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

    @OnClick(R.id.close_view)
    public void closeView(){
        getActivity().finish();
    }

    @OnClick(R.id.back_view)
    public void goBack(){
        getActivity().onBackPressed();
    }

    @Override
    public void success(Integer code) {
        if(code==200){
            AccountInfo.getInstance().setPassword();
            if(getActivity()!=null){
                getActivity().finish();
            }
        }else {
            Toast.makeText(getContext(),"设置密码失败",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void failed() {}

}
