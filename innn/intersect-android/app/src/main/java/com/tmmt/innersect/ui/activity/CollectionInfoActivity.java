package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.RegisterInfo;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.presenter.ExtraPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.mvp.view.ExtraView;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.Util;

import butterknife.BindView;
import butterknife.OnClick;

public class CollectionInfoActivity extends BaseActivity implements CommonView<Boolean>,ExtraView<RegisterInfo> {

    @BindView(R.id.name_view)
    EditText nameView;
    @BindView(R.id.tel_view)
    EditText telView;
    @BindView(R.id.type_view)
    EditText typeView;
    @BindView(R.id.num_view)
    EditText numView;
    @BindView(R.id.gender_view)
    EditText genderView;
    @BindView(R.id.age_view)
    EditText ageView;
    @BindView(R.id.action_view)
    TextView actionView;

    @BindView(R.id.select_gender)
    View selectGender;

    @BindView(R.id.select_type)
    View selectType;

    RegisterInfo mInfo;
    String type;
    String age;
    String gender;
    CommonPresenter mPresenter;

    ExtraPresenter mExtraPresenter;

    int genderIndex;
    int cardIndex;
    boolean modify=false;
    long activityId;
    boolean isEdit;

    final  String[] credentials = {Util.getString(R.string.identity_card),Util.getString(R.string.passport) };
    final  String[] genders = { Util.getString(R.string.female),Util.getString(R.string.male) };

    public static void start(Context context, long id){
        Intent intent = new Intent(context, CollectionInfoActivity.class);
        intent.putExtra(Constants.INFO_ID, id);
        context.startActivity(intent);
    }

    public static void start(Context context, long id,boolean edit){
        Intent intent = new Intent(context, CollectionInfoActivity.class);
        intent.putExtra(Constants.INFO_ID, id);
        intent.putExtra(Constants.IS_EDIT,edit);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection_info;
    }

    @Override
    protected String getTitleString() {
        return getString(R.string.register_info);
    }

    @Override
    protected void initView() {
        super.initView();
        mInfo=new RegisterInfo();
        AnalyticsUtil.reportEvent("drawup_register");

        activityId=getIntent().getLongExtra(Constants.INFO_ID,1);
        isEdit=getIntent().getBooleanExtra(Constants.IS_EDIT,true);
        if(isEdit){
            actionView.setText(getString(R.string.complete));
        }else {
            nameView.setFocusable(false);
            telView.setFocusable(false);
            numView.setFocusable(false);
            ageView.setFocusable(false);
            selectGender.setEnabled(false);
            selectType.setEnabled(false);
        }
        mPresenter=new CommonPresenter();
        mPresenter.attachView(this);
        mExtraPresenter=new ExtraPresenter();
        mExtraPresenter.attachView(this);
        mExtraPresenter.getRegisterInfo(activityId);
    }

    @OnClick(R.id.select_type)
    void selectType(){
        showSingleChoiceDialog(credentials,1);
    }

    @OnClick(R.id.select_gender)
    void selectGender(){
        showSingleChoiceDialog(genders,2);
    }

    protected boolean check(){
        mInfo.name=nameView.getText().toString().trim();
        if(mInfo.name.isEmpty()){
            Toast.makeText(this,"姓名不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        mInfo.mobile=telView.getText().toString().trim();
        if(mInfo.mobile.isEmpty()){
            Toast.makeText(this,"联系电话不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }

        type=typeView.getText().toString().trim();
        if(type.isEmpty()){
            Toast.makeText(this,"证件类型不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        mInfo.idType=cardIndex;

        mInfo.idNo=numView.getText().toString().trim();
        if(mInfo.idNo.isEmpty()){
            Toast.makeText(this,"证件号码不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        gender=genderView.getText().toString().trim();
        if(gender.isEmpty()){
            Toast.makeText(this,"性别不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        mInfo.gender=genderIndex;

        age=ageView.getText().toString().trim();
        if(age.isEmpty()){
            Toast.makeText(this,"年龄不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        mInfo.age=Integer.valueOf(age);
        return true;
    }

    @OnClick(R.id.action_view)
    void register(){
        AnalyticsUtil.reportEvent("drawup_register_save");

        mInfo.reserveId=activityId;
        if(check() && isEdit){
            mPresenter.addRegisterInfo(mInfo,modify);
        }
    }

    private void showSingleChoiceDialog(final String[] item, final int type){
        final AlertDialog.Builder singleChoiceDialog =
                new AlertDialog.Builder(this);
        singleChoiceDialog.setTitle(getString(R.string.chose_date));
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(item,-1,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(type==1){
                            typeView.setText(item[which]);
                            cardIndex=which;
                        }else {
                            genderView.setText(item[which]);
                            genderIndex=which;
                        }
                        dialog.dismiss();

                    }
                });
//        singleChoiceDialog.setPositiveButton(getString(android.R.string.ok),
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mDateView.setText(items[mIndex]);
//                        mCurrDate=sDates[mIndex];
//                        mPresenter.loadAgendaList(mCurrDate);
//                    }
//                });
//        singleChoiceDialog.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//           }
//        });
        singleChoiceDialog.show();
    }

    private void fillView(RegisterInfo data){
        nameView.setText(data.name);
        telView.setText(data.mobile);
        ageView.setText(String.valueOf(data.age));
        typeView.setText(credentials[data.idType]);
        genderView.setText(genders[data.gender]);
        numView.setText(data.idNo);
    }

    @Override
    public void success(Boolean data) {
      if(data){
          finish();
      }
    }

    @Override
    public void onFailure(int code) {}

    @Override
    public void onSuccess(RegisterInfo data) {
        if(data==null){
            modify=false;
        }else {
            modify=true;
            mInfo.id=data.id;
            fillView(data);
        }
    }

    @Override
    public void failed() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
        mExtraPresenter.onDestory();
    }
}
