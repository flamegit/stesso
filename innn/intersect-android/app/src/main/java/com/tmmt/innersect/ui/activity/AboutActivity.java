package com.tmmt.innersect.ui.activity;

import android.content.Intent;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.utils.SystemUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.version_view)
    TextView mVersionView;

    @BindView(R.id.code_view)
    TextView mCodeView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected String getTitleString() {
        return getString(R.string.about_innersect);
    }

    @Override
    protected void initView() {
        super.initView();
        mVersionView.setText("INNERSECT"+ SystemUtil.getPackageInfo().versionName);

        mCodeView.setText("Build"+ SystemUtil.getPackageInfo().versionCode);
    }

    @OnClick(R.id.purpose_view)
    void openPurpose(){
        Intent intent=new Intent(this,AgreementActivity.class);
        intent.putExtra(Constants.PRIVATE,true);
        startActivity(intent);
    }

//    @OnClick(R.id.cooperation_view)
//    void openCooperation(){
//        Util.startActivity(this,CooperationActivity.class);
//    }


}
