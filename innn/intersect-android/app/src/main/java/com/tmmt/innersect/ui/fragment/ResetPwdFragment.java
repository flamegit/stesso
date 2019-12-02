package com.tmmt.innersect.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
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

public class ResetPwdFragment extends BaseLoginFragment {


    @BindView(R.id.tel_view)
    EditText mTelView;
    @BindView(R.id.root_view)
    RelativeLayout mRootView;
    @BindView(R.id.select_code)
    TextView mCodeView;
    private View mActionView;

    @BindView(R.id.welcome)
    TextView mTitleView;
    Communication mCommunication;

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

    @Override
    int getLayout() {
        return R.layout.fragment_binding;
    }

    @Override
    protected void initView(View view) {

        super.initView(view);
        ButterKnife.bind(this, view);
        String telNo = mCommunication.getMobile();
        String mobile;
        //TODO bug
        if(telNo!=null &&! telNo.isEmpty()){
            mobile = telNo.split("/")[1];
        }else {
            mobile= AccountInfo.getInstance().getUser().mobile;
        }
        mTitleView.setText(getString(R.string.reset_pwd));
        if(mobile!=null){
            mTelView.setText(mobile);
            mTelView.setSelection(mobile.length());
        }

        mTelView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mActionView = View.inflate(getContext(), R.layout.yellow_action_view, null);
        mActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile=mTelView.getText().toString().trim();
                String code=mCodeView.getText().toString();
                if(!Util.isMobileNO(code,mobile)){
                    Toast.makeText(getContext(),getString(R.string.phone_format_error),Toast.LENGTH_SHORT).show();
                    return;
                }
                mCommunication.setMobile(code+"/"+mobile);
                if(mCommunication!=null){
                    mCommunication.setPrevious(LoginActivity.RESET_PWD);
                    mCommunication.goToTarget(LoginActivity.VERIFY_CODE);
                }

            }
        });
        mTelView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTelView.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(mTelView,InputMethodManager.SHOW_FORCED );
            }
        }, 200);

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
        Util.startActivity(getContext(), DialCodeActivity.class);
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


}
