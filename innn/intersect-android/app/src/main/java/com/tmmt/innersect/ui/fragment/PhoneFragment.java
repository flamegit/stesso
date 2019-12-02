package com.tmmt.innersect.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.presenter.LoginPresenter;
import com.tmmt.innersect.mvp.view.LoginView;
import com.tmmt.innersect.ui.activity.Communication;
import com.tmmt.innersect.ui.activity.DialCodeActivity;
import com.tmmt.innersect.ui.activity.LoginActivity;
import com.tmmt.innersect.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


/**
 * Created by flame on 2017/4/12.
 */

public class PhoneFragment extends BaseLoginFragment implements LoginView {

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

    @Override
    int getLayout() {
        return R.layout.fragment_phone_login;
    }

    @Override
    protected void initView(View view) {

        super.initView(view);
        ButterKnife.bind(this, view);
        mPresenter=new LoginPresenter();
        mPresenter.attachView(this);
        mTelView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mActionView = View.inflate(getContext(), R.layout.yellow_action_view, null);
        mActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile=mTelView.getText().toString().trim();
                String code=mCodeView.getText().toString();
                if(!Util.isMobileNO(code,mobile)){
                    Toast.makeText(getContext(),"手机号码格式错误",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mCommunication==null){
                    return;
                }
                mCommunication.setMobile(code+"/"+mobile);
                mPresenter.isSetPassword(code,mobile);

            }
        });

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

    @OnClick(R.id.select_code)
    public void selectDialCode() {
        Intent intent=new Intent(getContext(),DialCodeActivity.class);
        startActivityForResult(intent,1);
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


    @Override
    public void unBindMobile() {
        //mCommunication.goToTarget(LoginActivity.BINDING);//unbind phone number;
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
//        if(code==200){
//            getActivity().finish();
//        }else {
//            Toast.makeText(getContext(),"登录失败",Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }
}
