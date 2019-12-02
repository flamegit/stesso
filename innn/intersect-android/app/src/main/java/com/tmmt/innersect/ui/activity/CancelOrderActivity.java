package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Address;
import com.tmmt.innersect.mvp.model.Common;
import com.tmmt.innersect.utils.RxBus;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.widget.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CancelOrderActivity extends BaseActivity {

    @BindView(R.id.reason_view)
    TextView mReasonView;

    @BindView(R.id.name_view)
    EditText mNameView;

    @BindView(R.id.tel_view)
    EditText mTelView;

    @BindView(R.id.instruction_view)
    EditText mInstructionView;

    @BindView(R.id.action_view)
    TextView mActionView;

    @BindView(R.id.post_view)
    View mPostView;

    List<Common> mReasonList;
    int mSelectIndex;
    String mOrderNo;
    Address mAddress;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_cancel_order;
    }

    @Override
    protected String getTitleString() {
        return "申请取消订单";
    }


    @Override
    protected void initView() {
        mSelectIndex=-1;
        mOrderNo=getIntent().getStringExtra(Constants.ORDER_NO);
        mAddress=getIntent().getParcelableExtra(Constants.ADDRESS);
        mNameView.setText(mAddress.getName());
        mTelView.setText(mAddress.getTel());
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getCancelReason().enqueue(new NetCallback<List<Common>>() {
            @Override
            public void onSucceed(List<Common> data) {
                mReasonList=data;
            }
        });
    }

    public static void start(Context context, String orderNo,Address address){
        Intent intent=new Intent(context,CancelOrderActivity.class);
        intent.putExtra(Constants.ORDER_NO,orderNo);
        intent.putExtra(Constants.ADDRESS,address);
        context.startActivity(intent);
    }

    private boolean check(String name,String tel){
        if(mSelectIndex==-1){
            SystemUtil.toast("请选择退货原因");
            return false;
        }
        if(name.isEmpty()){
            SystemUtil.toast("请填写联系人");
            return false;
        }
        if(tel.isEmpty()){
            SystemUtil.toast("请填写联系电话");
            return false;
        }
        return true;
    }


    private String[] getReason(){

        if(mReasonList==null||mReasonList.isEmpty()){
            return null;
        }
        String[] reasonList=new String[mReasonList.size()];
        for (int i = 0; i <mReasonList.size() ; i++) {
            reasonList[i]=mReasonList.get(i).item2;
        }
        return reasonList;
    }

    @OnClick(R.id.post_view)
    void post(){

        String name=mNameView.getText().toString();
        String tel=mTelView.getText().toString();
        String instruction=mInstructionView.getText().toString();
        if(!check(name,tel)){
            return;
        }
        mPostView.setEnabled(false);
        int reasonId=mReasonList.get(mSelectIndex).id;
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("linkman").value(name)
                    .key("orderNo").value(mOrderNo)
                    .key("reasonId").value(reasonId)
                    .key("remark").value(instruction)
                    .key("tel").value(tel)
                    .endObject();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).applyCancelOrder(body).enqueue(new NetCallback<String>() {
            @Override
            public void onSucceed(String data) {
                RxBus.getDefault().post("reload");
                CancelProgressActivity.start(CancelOrderActivity.this,data,true);
            }

            @Override
            protected void failed(int code) {
                super.failed(code);
                mPostView.setEnabled(true);
            }
        });

    }


    @OnClick(R.id.reason_layout)
    void choseReason(){
        String[] reasons=getReason();
        if(reasons!=null){
            AlertDialog dialog = new AlertDialog.Builder(this).setTitle("取消订单原因")
                    .setSingleChoiceItems(reasons, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mSelectIndex=which;
                            mReasonView.setText(reasons[which]);
                            dialog.dismiss();
                        }
                    }).create();
            //dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.show();
        }else {
            CustomProgressDialog progressDialog=new CustomProgressDialog(this);
            progressDialog.show();
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getCancelReason().enqueue(new NetCallback<List<Common>>() {
                @Override
                public void onSucceed(List<Common> data) {
                    mReasonList=data;
                    choseReason();
                    progressDialog.dismiss();
                }

                @Override
                protected void failed(int code) {
                    super.failed(code);
                    progressDialog.dismiss();
                }
            });
        }
    }




//    linkman (string, optional): 联系人 ,
//    orderNo (string, optional): 订单号 ,
//    reasonId (integer, optional): 原因id ,
//    remark (string, optional): 备注 非必填 ,
//    tel (string, optional): 联系电话 ,
//    userId (string, optional): 用户id 非必填

}
