package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.LogisticsInfo;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.widget.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONStringer;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PostLogisticsActivity extends BaseActivity {

    @BindView(R.id.action_view)
    TextView mActionView;

    @BindView(R.id.code_view)
    EditText mCodeView;

    @BindView(R.id.company_view)
    EditText mCompanyView;

    String mAsno;

    LogisticsInfo mInfo;

    int mSelect;

    private String[] getReason(LogisticsInfo info){
        if(info.exps==null||info.exps.isEmpty()){
            return null;
        }
        String[] reasonList=new String[info.exps.size()];
        for (int i = 0; i <info.exps.size() ; i++) {
            reasonList[i]=info.exps.get(i).name;
        }
        return reasonList;
    }

    public static void start(Context context, String asno){
        Intent intent=new Intent(context,PostLogisticsActivity.class);
        intent.putExtra(Constants.ORDER_NO,asno);
        context.startActivity(intent);
    }

    @Override
    protected String getTitleString() {
        return "物流信息";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_post_logistics;
    }

    @OnClick(R.id.company_layout)
    void choseReason() {
        if(mInfo!=null){
            String [] list =getReason(mInfo);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setSingleChoiceItems(list, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCompanyView.setText(list[which]);
                            mSelect=which;
                            dialog.dismiss();
                        }
                    }).create();

            dialog.show();
        }else {
            CustomProgressDialog progressDialog=new CustomProgressDialog(this);
            progressDialog.show();

            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getCommanyList(mAsno).enqueue(new NetCallback<LogisticsInfo>() {
                @Override
                public void onSucceed(LogisticsInfo data) {
                    mInfo=data;
                    progressDialog.dismiss();
                    choseReason();
                }

                @Override
                protected void failed(int code) {
                    super.failed(code);
                    progressDialog.dismiss();
                }
            });
        }
    }

//    asno (string, optional): 售后单号 ,
//    expc (string, optional): 快递公司 ,
//    expn (string, optional): 快递号

    @OnClick(R.id.action_view)
    void post(){
        String company=mCompanyView.getText().toString();
        String code=mCodeView.getText().toString();
        if(company.isEmpty()){
            SystemUtil.toast("请选择物流公司");
            return;
        }
        if(code.isEmpty()){
            SystemUtil.toast("请填写物流单号");
            return;
        }

        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("asno").value(mAsno)
                    .key("expc").value(mInfo.exps.get(mSelect).code)
                    .key("expn").value(code)
                    .endObject();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).postLogistics(body).enqueue(new NetCallback<String>() {
            @Override
            public void onSucceed(String data) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
        mActionView.setText(R.string.post);
        mSelect=-1;
        mAsno=getIntent().getStringExtra(Constants.ORDER_NO);

        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getCommanyList(mAsno).enqueue(new NetCallback<LogisticsInfo>() {
            @Override
            public void onSucceed(LogisticsInfo data) {
                mInfo=data;
            }
        });
    }
}
