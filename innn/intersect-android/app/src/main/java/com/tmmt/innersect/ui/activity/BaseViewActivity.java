package com.tmmt.innersect.ui.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.fragment.EmptyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BaseViewActivity<T> extends AppCompatActivity implements CommonView<T> {

    @Nullable
    @BindView(R.id.title_view)
    TextView mTitleView;

    protected CommonPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mPresenter=new CommonPresenter();
        mPresenter.attachView(this);
        initView();
        if(mTitleView!=null){
            mTitleView.setText(getTitleString());
        }
    }

    @Override
    public void failed() {
        showEmptyView();
    }

    @Override
    public void success(T data) {}

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

    protected void showEmptyView(){
        EmptyFragment emptyFragment=new EmptyFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_view,emptyFragment)
                .commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }
}
