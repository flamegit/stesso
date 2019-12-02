package com.tmmt.innersect.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.QualificationInfo;

import butterknife.BindView;

public class QualificationActivity extends BaseActivity {

    @BindView(R.id.name_view)
    TextView mNameView;

    @BindView(R.id.sub_title)
    TextView mSubTitle;

    @BindView(R.id.image_view)
    ImageView mImageView;

    @BindView(R.id.hint_view)
    TextView mHintView;

    long mPrizeId;

    public static void start(Context context, long id){
        Intent intent=new Intent(context,QualificationActivity.class);
        intent.putExtra(Constants.ACTIVITY_ID,id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_qualification;
    }

    @Override
    protected String getTitleString() {
        return "购买资格";
    }

    @Override
    protected void initView() {
        super.initView();
        mPrizeId=getIntent().getLongExtra(Constants.ACTIVITY_ID,0);
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getQualificationInfo(mPrizeId).enqueue(new NetCallback<QualificationInfo>() {
            @Override
            public void onSucceed(QualificationInfo data) {
                if(data!=null){
                    mNameView.setText(data.title);
                    mSubTitle.setText(data.subject);
                    Glide.with(QualificationActivity.this).load(data.imgUrl).into(mImageView);
                    mHintView.setText(data.ruleDesc);
                }
            }
        });
    }
}
