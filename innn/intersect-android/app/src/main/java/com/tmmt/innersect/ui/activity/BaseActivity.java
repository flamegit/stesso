package com.tmmt.innersect.ui.activity;

import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tmmt.innersect.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaseActivity extends AppCompatActivity {

    @BindView(R.id.title_view)
    @Nullable
    TextView mTitleView;

    @BindView(R.id.empty_view)
    @Nullable
    View mEmptyView;

    @BindView(R.id.no_network)
    @Nullable
    View mNoNetworkView;

    @BindView(R.id.back_view)
    @Nullable
    View mBackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        if(mTitleView!=null){
            mTitleView.setText(getTitleString());
        }
    }

    protected void initView(){}

    protected int getLayoutId(){
        return R.layout.activity_about;
    }

    protected String getTitleString(){
        return  getString(R.string.app_name);
    }

    @OnClick(R.id.back_view)
    public void back(){
        onBackPressed();
    }

    @TargetApi(21)
    private void setStatusBarUpperAPI21(){
        Window window = getWindow();
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        //由于setStatusBarColor()这个API最低版本支持21, 本人的是15,所以如果要设置颜色,自行到style中通过配置文件设置
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
    }

    protected void showEmptyView(){
        mEmptyView.setVisibility(View.VISIBLE);
    }
    protected void showNetUnavailable(){
        mNoNetworkView.setVisibility(View.VISIBLE);
    }

}
