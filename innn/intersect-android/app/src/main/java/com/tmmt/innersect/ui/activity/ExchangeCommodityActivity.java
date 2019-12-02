package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Address;
import com.tmmt.innersect.mvp.model.Commodity;
import com.tmmt.innersect.mvp.model.Common;
import com.tmmt.innersect.mvp.model.RefundCommodity;
import com.tmmt.innersect.ui.fragment.CommonDialogFragment;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.RxBus;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.utils.Util;
import com.tmmt.innersect.widget.CustomProgressDialog;
import com.tmmt.innersect.widget.QuantityView;

import org.json.JSONException;
import org.json.JSONStringer;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ExchangeCommodityActivity extends BaseActivity {

    @BindView(R.id.icon_view)
    ImageView mIconView;
    @BindView(R.id.name_view)
    TextView mNameView;
    @BindView(R.id.color_view)
    TextView mColorView;
    @BindView(R.id.size_view)
    TextView mSizeView;
    @BindView(R.id.quantity_view)
    QuantityView mQuantityView;
    @BindView(R.id.reason_view)
    TextView mReasonView;

    @BindView(R.id.action_view)
    TextView mActionView;

    @BindView(R.id.contact_layout)
    View mContactLayout;

    @BindView(R.id.commodity_layout)
    View mCommodityLayout;

    @BindView(R.id.name_tel)
    TextView mNameTel;

    @BindView(R.id.address_view)
    TextView mAddressView;

    @BindView(R.id.type_view)
    TextView mTypeView;

    @BindView(R.id.origin_price)
    TextView mOriginPrice;

    @BindView(R.id.refund_price)
    TextView mRefundPrice;

    @BindView(R.id.tips_view)
    TextView mTipsView;

    @BindView(R.id.instruction_view)
    EditText mInstructionView;

    @BindView(R.id.post_view)
    View mPostView;

    RefundCommodity mRefundCommodity;

    List<Common> mReasonList;
    int mSelectIndex;

    Address mAddress;
    Commodity mCommodity;

    boolean isRefund;

    String mRefundMoney;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange_commodity;
    }

    public static void start(Context context, boolean isRefund, Commodity commodity,Address address){
        Intent intent=new Intent(context,ExchangeCommodityActivity.class);
        intent.putExtra(Constants.IS_REFUND,isRefund);
        intent.putExtra(Constants.ADDRESS,address);
        intent.putExtra(Constants.COMMODITY,commodity);
        context.startActivity(intent);
    }

    @Override
    protected String getTitleString() {
        if(!isRefund){
            return "申请换货";
        }else {
            return "申请退货退款";
        }
    }

    @Override
    protected void initView() {
        super.initView();

        mRefundMoney=null;
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getRefundReason().enqueue(new NetCallback<List<Common>>() {
            @Override
            public void onSucceed(List<Common> data) {
                mReasonList=data;
            }
        });

        mSelectIndex= -1;
        isRefund=getIntent().getBooleanExtra(Constants.IS_REFUND,false);
        mAddress=getIntent().getParcelableExtra(Constants.ADDRESS);
        mCommodity=getIntent().getParcelableExtra(Constants.COMMODITY);

        if(isRefund){
            AnalyticsUtil.reportEvent("applyAfterSale_refund_submit");
            mCommodityLayout.setVisibility(View.VISIBLE);
            mTypeView.setText("退货退款");
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getRefundCommodity(mCommodity.id,isRefund?1:2)
                    .enqueue(new NetCallback<RefundCommodity>() {
                        @Override
                        public void onSucceed(RefundCommodity data) {
                            mRefundCommodity=data;
                            mOriginPrice.setText(String.valueOf(data.salePrice));
                            mRefundMoney=data.refundAmount.toString();
                            mRefundPrice.setText(mRefundMoney);
                            mTipsView.setText(data.tips);
                        }
                    });

        }else {
            AnalyticsUtil.reportEvent("applyAfterSale_exchange_submit");
            mContactLayout.setVisibility(View.VISIBLE);
            if(mAddress.getName()!=null){
                mNameTel.setText(mAddress.getName()+" "+mAddress.getTel());
                mAddressView.setText(mAddress.getDetail());
            }
            mTypeView.setText("换货");
        }

        Util.fillImage(this,mCommodity.skuThumbnail,mIconView);
        mNameView.setText(mCommodity.getName());
        mColorView.setText(mCommodity.skuColor);
        mSizeView.setText(mCommodity.skuSize);
        mQuantityView.setMaxQuantity(mCommodity.ascount);
        mQuantityView.setQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged(int newQuantity, boolean programmatically) {
                if(mRefundCommodity!=null){
                    mRefundMoney=mRefundCommodity.refundAmount.multiply(new BigDecimal(newQuantity)).toString();
                    mRefundPrice.setText(mRefundMoney);
                }
            }
            @Override
            public void onLimitReached() {
                SystemUtil.toast("不能再多了");
            }

            @Override
            public void onMinReached() {

            }
        });
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

    @OnClick(R.id.action_view)
    void show(){
        Util.startActivity(this,ExchangeDetailActivity.class);
    }

    @OnClick(R.id.reason_layout)
    void choseReason(){

        String[] reasons=getReason();
        if(reasons!=null){
            AlertDialog dialog = new AlertDialog.Builder(this).setTitle("选择原因")
                    .setSingleChoiceItems(reasons, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mSelectIndex=which;
                            mReasonView.setText(reasons[which]);
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }else {
            CustomProgressDialog progressDialog=new CustomProgressDialog(this);
            progressDialog.show();
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getRefundReason().enqueue(new NetCallback<List<Common>>() {
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

    @OnClick(R.id.post_view)
    void post(){
        if(mSelectIndex==-1){
            SystemUtil.toast("请选择退货原因");
            return;
        }
        if(mRefundMoney==null && isRefund){
            return;
        }
        mPostView.setEnabled(false);
        int reasonId=mReasonList.get(mSelectIndex).id;
        String instruction=mInstructionView.getText().toString();

        try {
            JSONStringer jsonStringer=new JSONStringer().object()
                    .key("ast").value(isRefund?1:2)
                    .key("daid").value(mAddress.getId())
                    .key("quantity").value(mQuantityView.getQuantity())
                    .key("reasonId").value(reasonId)
                    .key("remark").value(instruction)
                    .key("topid").value(mCommodity.id);

            if(isRefund){
                jsonStringer.key("refundAmount").value(mRefundMoney)
                        .endObject();
            }else {
                jsonStringer.endObject();
            }
            KLog.json(jsonStringer.toString());
            RequestBody body = RequestBody.create(MediaType.parse("application/json"),jsonStringer.toString());
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).apply(body).enqueue(new NetCallback<String>() {
                @Override
                public void onSucceed(String data) {
                    RxBus.getDefault().post("reload");
                    CommonDialogFragment dialogFragment=CommonDialogFragment.newInstance(R.layout.dialog_pay_success,"已提交",
                            "我们尽快处理您的申请");
                    dialogFragment.show(getSupportFragmentManager(),"");
                    dialogFragment.setActionListener(new CommonDialogFragment.ActionListener() {
                        @Override
                        public void doAction() {
                            ExchangeDetailActivity.start(ExchangeCommodityActivity.this,isRefund,data,true);
                        }
                        @Override
                        public void cancel() {
                            onBackPressed();
                        }
                    });
                }

                @Override
                protected void failed(int code) {
                    super.failed(code);
                    mPostView.setEnabled(true);

                }
            });
        } catch (JSONException e) {
            KLog.i("JsonException");
        }


    }

    @OnClick(R.id.contact_layout)
    void selectAddress(){
        Intent intent = new Intent(AddressActivity.ACTION_PICK);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Address address = data.getParcelableExtra(AddressActivity.ADDRESS_KEY);
                //fillAddress(address);
                mNameTel.setText(address.getName()+" "+address.getTel());
                mAddressView.setText(address.getDetail());
                mAddress=address;
            }
        }
    }

//    ast (integer, optional): 售后类型：1：退款；2：换货 ,
//    daid (integer, optional): 地址ID ,
//    quantity (integer, optional): 数量 ,
//    reasonId (integer, optional): 原因ID ,
//    refundAmount (number, optional): 可退金额 ,
//    remark (string, optional): 备注描述 ,
//    topid (integer, optional): 订单商品ID

}
