package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.ReserveResult;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.view.CommonView;

import butterknife.BindView;
import butterknife.OnClick;

public class ReserveSuccessActivity extends BaseActivity implements CommonView<ReserveResult>{

    @BindView(R.id.order_view)
    TextView orderView;

    @BindView(R.id.name_view)
    TextView nameView;

    @BindView(R.id.image_view)
    ImageView imageView;

    @BindView(R.id.hint_view)
    TextView hintView;

    CommonPresenter mPresenter;

    long activityId;

    public static void start(Context context, long id){
        Intent intent = new Intent(context, ReserveSuccessActivity.class);
        intent.putExtra(Constants.INFO_ID, id);
        context.startActivity(intent);
    }


    @Override
    protected String getTitleString() {
        return getString(R.string.reserve_qualification);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reserve_success;
    }

    @Override
    protected void initView() {
        super.initView();
        mPresenter=new CommonPresenter();
        mPresenter.attachView(this);
        activityId=getIntent().getLongExtra(Constants.INFO_ID,1);
        mPresenter.getReserveResult(activityId);
    }

    @Override
    public void success(ReserveResult data) {
        if(data==null){
            return;
        }
        nameView.setText(data.winningInfo.title);
        hintView.setText(data.winningInfo.desc);
        orderView.setText(String.format(getString(R.string.order_text),data.order));
        Glide.with(this)
                .load(data.winningInfo.imgUrl)
                .into(imageView);
    }

    @Override
    public void failed() {}

    @OnClick(R.id.check_view)
    void checkInfo(){
        CollectionInfoActivity.start(this,activityId,false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }
}
