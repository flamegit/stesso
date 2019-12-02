package com.tmmt.innersect.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.view.ViewCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.presenter.LoginPresenter;
import com.tmmt.innersect.mvp.view.LoginView;
import com.tmmt.innersect.ui.activity.Communication;
import com.tmmt.innersect.ui.activity.LoginActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by flame on 2017/4/12.
 */

public class  IdentifyFragment extends BaseLoginFragment implements LoginView {

    @BindView(R.id.identify_code_view)
    PinEntryEditText mIdentifyView;
    @BindView(R.id.root_view)
    RelativeLayout mRootView;
    private TextView mActionView;
    private int mDelay = 60 * 1000;
    @BindView(R.id.hint_view)
    TextView mHintView;

    @BindView(R.id.success_hint)
    View mSuccessView;

    LoginPresenter mPresenter;
    Communication mCommunication;
    CountDownTimer mTimer;
    private String mCode;
    private String mMobile;

    @Override
    int getLayout() {
        return R.layout.fragment_identify;
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

    @Override
    protected void initView(View view) {
        super.initView(view);
        ButterKnife.bind(this, view);
        String telNo = mCommunication.getMobile();
        if(telNo!=null){
            mMobile = telNo.split("/")[1];
            mCode = telNo.split("/")[0];
        }
        mHintView.setText(String.format(getString(R.string.code_send)+"%s", mMobile));
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
        mActionView = (TextView) View.inflate(getContext(), R.layout.black_action_view, null);
        mActionView.setBackgroundResource(R.drawable.solid_gray_bg);
        mActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getVerifyCode();
            }
        });
        getVerifyCode();

        mIdentifyView.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mIdentifyView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                getVerifyCode();
                return true;
            }
        });

        mIdentifyView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mIdentifyView.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                KLog.d(str.length());
                if (mCommunication != null) {
                    if (mCommunication.getPrevious() == LoginActivity.BINDING) {
                        SHARE_MEDIA platform = mCommunication.getPlatform();
                        Map<String, String> data = mCommunication.getDate();
                        mPresenter.thirdPartyLogin(platform, data, str.toString(), mMobile, mCode);
                        return;
                    }
                }
                mPresenter.verifyCodeLogin(str.toString(), mCode, mMobile);
            }
        });

        mIdentifyView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIdentifyView.focus();
            }
        }, 600);

    }

    private void getVerifyCode() {
        mPresenter.getVerifyCode(mCode, mMobile);
        mActionView.setEnabled(false);
        mTimer = new CountDownTimer(mDelay, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(!isDetached()){
                    mActionView.setText(String.format(getString(R.string.retry_code), "" + millisUntilFinished / 1000));
                }else {
                    KLog.d("test detache");
                }
            }
            @Override
            public void onFinish() {
                mActionView.setBackgroundResource(R.drawable.common_yellow_bg);
                mActionView.setText(getString(R.string.acquire_code));
                mActionView.setTextColor(Color.BLACK);
                mActionView.setEnabled(true);
            }
        };
        mTimer.start();
    }

    @OnClick(R.id.close_view)
    public void closeView() {
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void success(int code) {
        if(code==200){
            // TODO have bug
            if (mCommunication.getPrevious() == LoginActivity.RESET_PWD) {
                mCommunication.goToTarget(LoginActivity.SET_PWD);
                return;
            }
            mSuccessView.setVisibility(View.VISIBLE);
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getActivity().finish();
                }
            }, 200);
        }else if(code==1203){
            //Toast.makeText(getContext(),"验证码错误",Toast.LENGTH_SHORT).show();
            ViewCompat.animate(mIdentifyView).translationX(50).setInterpolator(new BounceInterpolator()).start();
            //verify code error
        }else {
            //Toast.makeText(getContext(),"登录失败",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setPassword(boolean setPwd) {}

    @Override
    public void unBindMobile() {}

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
            mActionView.setVisibility(View.GONE);
            //getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        //mTimer.cancel();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTimer.cancel();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();

    }
}
