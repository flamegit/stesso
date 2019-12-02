package com.tmmt.innersect.ui.activity;

import android.animation.ValueAnimator;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.mvp.model.ShopCart;
import com.tmmt.innersect.mvp.presenter.AccountPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.fragment.CommonDialogFragment;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.utils.Util;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by flame on 2017/4/19.
 */

public class SettingActivity extends BaseActivity implements CommonView<Integer> {

    AccountPresenter mPresenter;
    @BindView(R.id.cache_size)
    TextView mCacheView;
    @BindView(R.id.progress_view)
    View mProgressView;
    @BindView(R.id.push_switch)
    Switch mSwitchView;
    @BindView(R.id.version_view)
    TextView mVersionView;

    @Override
    protected void initView() {
        super.initView();
        mSwitchView.setChecked(!JPushInterface.isPushStopped(this));
        mVersionView.setText(SystemUtil.getPackageInfo().versionName);
        mPresenter=new AccountPresenter();
        mPresenter.attachView(this);
        mCacheView.setText(String.format("%.2f MB",Util.getCacheSize(this)));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected String getTitleString() {
        return getString(R.string.setting);
    }

    @OnClick(R.id.cache_view)
    void clearCache(){
        AnalyticsUtil.reportEvent(AnalyticsUtil.SETTING_CLEARCACHE);

        float start=(float)Util.getCacheSize(this);
        Util.clearCache(this);
        KLog.d("clear cache");
        ValueAnimator animator=ValueAnimator.ofFloat(start,0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCacheView.setText(String.format("%.2f MB",animation.getAnimatedValue()));
            }
        });
        animator.setDuration(500).start();

    }

    @OnClick(R.id.about_view)
    void aboutApp(){
        Util.startActivity(this,AboutActivity.class);
    }

    @OnClick(R.id.logout)
    public void logout() {
        //
        CommonDialogFragment fragment = CommonDialogFragment.newInstance(R.layout.dialog_order,getString(R.string.logout_title));
        fragment.setActionListener(new CommonDialogFragment.ActionListener() {
            @Override
            public void doAction() {
                AnalyticsUtil.reportEvent(AnalyticsUtil.ACCOUNT_LOGOUT_CONFIRM);
                mPresenter.logout();
            }
            @Override
            public void cancel() {
                AnalyticsUtil.reportEvent(AnalyticsUtil.ACCOUNT_LOGOUT_CANCEL);
            }
        });
        fragment.show(getSupportFragmentManager(), "logout");

    }

    @OnClick(R.id.push_switch)
    public void stopPush(Switch switchView){
        if(!switchView.isChecked()){
            JPushInterface.stopPush(this);
            AnalyticsUtil.reportEvent(AnalyticsUtil.SETTING_CLOSEPUSH);

        }else {
            AnalyticsUtil.reportEvent(AnalyticsUtil.SETTING_OPENPUSH);
            JPushInterface.resumePush(this);
        }
        KLog.d(JPushInterface.isPushStopped(this));
    }

    @OnClick(R.id.suggestion_view)
    public void openSuggestion(){
        Util.startActivity(this,FeedbackActivity.class);
    }

    @Override
    public void success(Integer code) {
        if(code==200){
            AccountInfo.getInstance().logout();
            ShopCart.getInstance().setRefresh(true);
            Toast.makeText(this,"退出成功",Toast.LENGTH_SHORT).show();
//            Intent intent=new Intent(this, ExhibitionActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            finish();
            Util.startActivity(this,LoginActivity.class);
        }else {
            Toast.makeText(this,"退出失败",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void failed() {}

    private void showProcess() {
        mProgressView.setVisibility(View.VISIBLE);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }
}
