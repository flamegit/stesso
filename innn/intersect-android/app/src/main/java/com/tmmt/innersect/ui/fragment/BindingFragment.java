package com.tmmt.innersect.ui.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tmmt.innersect.R;
import com.tmmt.innersect.ui.activity.DialCodeActivity;
import com.tmmt.innersect.ui.activity.LoginActivity;
import com.tmmt.innersect.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by flame on 2017/4/12.
 */

public class BindingFragment extends BaseLoginFragment{

    @BindView(R.id.tel_view)
    EditText mTelView;
    @BindView(R.id.root_view)
    RelativeLayout mRootView;
    @BindView(R.id.select_code)
    TextView mCodeView;
    private View mActionView;


    @Override
    int getLayout() {
        return R.layout.fragment_binding;
    }

    @Override
    protected void initView(View view) {

        super.initView(view);
        ButterKnife.bind(this, view);
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
                LoginActivity activity=(LoginActivity)getActivity();
                activity.setMobile(code+"/"+mobile);
                if(activity!=null){
                    activity.setPrevious(LoginActivity.BINDING);
                    activity.goToTarget(LoginActivity.VERIFY_CODE);
                }

            }
        });
    }

    @OnClick(R.id.back_view)
    void back(){
        getActivity().onBackPressed();
    }

    @OnClick(R.id.close_view)
    void close(){
        getActivity().finish();
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
