package com.tmmt.innersect.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.PayPal;
import com.braintreepayments.api.exceptions.BraintreeError;
import com.braintreepayments.api.exceptions.ErrorWithResponse;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PayPalRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.masapay.sdk.MasapayPaymentRequest;
import com.masapay.sdk.MasapayPaymentResponse;
import com.masapay.sdk.MasapayTask;
import com.socks.library.KLog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tmmt.innersect.App;
import com.tmmt.innersect.BuildConfig;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.mvp.model.PayModel;
import com.tmmt.innersect.ui.fragment.CommonDialogFragment;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.Util;
import com.tmmt.innersect.wxapi.WXPayEntryActivity;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
/**
 * Created by flame on 2017/4/19.
 */
public class SelectPaymentActivity extends BaseActivity implements
        PaymentMethodNonceCreatedListener,
        BraintreeCancelListener,
        BraintreeErrorListener {
    ProgressDialog mDialog;
    BraintreeFragment mBraintreeFragment;
    String[] mPayPalParam;
    private IWXAPI api;

    String mOrderNo;

    String mFrom;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_payment;
    }

    @Override
    protected String getTitleString() {
        return getString(R.string.select_pay_method);
    }

    public static void start(Context context,String orderNo,String from,float price){
        Intent intent=new Intent(context,SelectPaymentActivity.class);
        intent.putExtra(Constants.ORDER_NO,orderNo);
        intent.putExtra(Constants.FROM,from);
        intent.putExtra(Constants.TOTAL_PRICE,price);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        api = WXAPIFactory.createWXAPI(this, BuildConfig.WXAPPID, true);
        api.registerApp(BuildConfig.WXAPPID);
        mOrderNo = getIntent().getStringExtra(Constants.ORDER_NO);
        mFrom=getIntent().getStringExtra(Constants.FROM);
        float price = getIntent().getFloatExtra(Constants.TOTAL_PRICE, -1);
        if (price == -1) {
            //mPriceView.setText("总计：￥" + ShopCart.getInstance().getSelectPrice());
        } else {
            mPriceView.setText("总计：￥" + price);
        }
    }
    @BindView(R.id.price_view)
    TextView mPriceView;

    @OnClick(R.id.alipay)
    public void pay() {
        AnalyticsUtil.reportEvent("ticketdetail_buyticket_order_pay_alipay");
        getPayPara();
    }
    private RequestBody getPayBody(String gateway) {
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("orderNo").value(mOrderNo)
                    .key("gatewayCode").value(gateway)
                    .key("userId").value(AccountInfo.getInstance().getUserId())
                    .endObject();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
        return body;
    }

    @OnClick(R.id.paypal_pay)
    void payPalPay() {
        AnalyticsUtil.reportEvent("ticketdetail_buyticket_order_pay_paypal");
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).payOrder(getPayBody("paypalPay"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PayModel>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showDialog();
                    }
                    @Override
                    public void onCompleted() {
                        mDialog.dismiss();
                    }
                    @Override
                    public void onError(Throwable e) {
                        KLog.i(e);
                    }
                    @Override
                    public void onNext(PayModel model) {
                        if(model.code==200){
                            KLog.d(model.data.channelParams);
                            mPayPalParam = getPayPalParm(model.data.channelParams);
                            try {
                                mBraintreeFragment = BraintreeFragment.newInstance(SelectPaymentActivity.this, mPayPalParam[0]);
                                mBraintreeFragment.addListener(SelectPaymentActivity.this);
                                PayPalRequest request = new PayPalRequest(mPayPalParam[2])
                                        .currencyCode(mPayPalParam[1]);
                                PayPal.requestOneTimePayment(mBraintreeFragment, request);
                                // mBraintreeFragment is ready to use!
                            } catch (InvalidArgumentException e) {
                                // There was an issue with your authorization string.
                            }
                        }else {
                            Toast.makeText(SelectPaymentActivity.this,model.msg,Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        String nonce = paymentMethodNonce.getNonce();
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("amount").value(mPayPalParam[2])
                    .key("currency").value(mPayPalParam[1])
                    .key("tradeNo").value(mPayPalParam[3])
                    .key("nonce").value(nonce)
                    .endObject();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());

        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).reportServer(body).enqueue(new Callback<HttpBean<String>>() {
            @Override
            public void onResponse(Call<HttpBean<String>> call, Response<HttpBean<String>> response) {
            }

            @Override
            public void onFailure(Call<HttpBean<String>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onError(Exception error) {
        KLog.d("eeeorrrr");
        if (error instanceof ErrorWithResponse) {
            ErrorWithResponse errorWithResponse = (ErrorWithResponse) error;
            BraintreeError cardErrors = errorWithResponse.errorFor("creditCard");
            if (cardErrors != null) {
                // There is an issue with the credit card.
                BraintreeError expirationMonthError = cardErrors.errorFor("expirationMonth");
                if (expirationMonthError != null) {
                    // There is an issue with the expiration month.
                    KLog.d(expirationMonthError.getMessage());
                }
            }
        }
    }

    @Override
    public void onCancel(int requestCode) {
        KLog.d("cancel");
        // Use this to handle a canceled activity, if the given requestCode is important.
        // You may want to use this callback to hide loading indicators, and prepare your UI for input
    }

    @OnClick(R.id.wechat_pay)
    void wechatPay() {
        WXPayEntryActivity.orderNo=mOrderNo;
        AnalyticsUtil.reportEvent("ticketdetail_buyticket_order_pay_wechatpay");
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).payOrder(getPayBody("wxAppPay"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PayModel>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        KLog.i(e);
                    }
                    @Override
                    public void onNext(PayModel model) {
                        String values[] = getParm(model.data.channelParams);
                        PayReq req = new PayReq();
                        req.appId = values[0];
                        req.nonceStr = values[1];
                        req.packageValue = values[2];
                        req.partnerId = values[3];
                        req.prepayId = values[4];
                        req.sign = values[5];
                        req.timeStamp = values[6];
                        api.sendReq(req);
                    }
                });
    }
    /**************** masapay *********************/

    @OnClick(R.id.masa_pay)
    void masaPay(){
        AnalyticsUtil.reportEvent("ticketdetail_buyticket_order_pay_wechatpay");
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).payOrder(getMasapayBody())
                .subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PayModel>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        KLog.i(e);
                    }
                    @Override
                    public void onNext(PayModel model) {
                        // API，需传入支付完成处理Handler
                        MasapayPaymentRequest request=createRequest(model.data.channelParams);
                        MasapayTask masapayTask = new MasapayTask(mHandler);
                        MasapayTask.isSandbox = true;// 表示为测试环境
                        masapayTask.pay(request);
                    }
                });
    }

    private MasapayPaymentRequest createRequest(String channelParams){
        MasapayPaymentRequest request=getMasapayPaymentRequest();
        String values[] = getMasaParm(channelParams);

        // API的卡号、有效期、CVV2、持卡人邮箱邮箱必传
        request.setCardNumber("4111111111111111");
        request.setCardExpirationMonth(5);
        request.setCardExpirationYear(2020);
        request.setSecurityCode("123");
        request.setCardHolderEmail("aaa@bbb.com");

        request.setVersion(values[0]);
        request.setMerchantId(values[1]);
        request.setCharset(values[2]);
        request.setLanguage(values[3]);
        request.setSignType(values[4]);
        request.setMerchantOrderNo(values[5]);
        request.setGoodsName(values[6]);
        request.setGoodsDesc(values[7]);
        request.setOrderExchange(values[8]);
        request.setCurrencyCode(values[9]);
        request.setOrderAmount(Long.valueOf(values[10]));
        request.setSubmitTime(values[11]);
        request.setExpiryTime(values[12]);
        request.setBgUrl(values[13]);
        request.setOrderIp(values[14]);
        request.setSignMsg(values[15]);
        return request;
    }

    private RequestBody getMasapayBody() {
        String order = getIntent().getStringExtra(Constants.ORDER_NO);
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("creditcn").value("4111111111111111")
                    .key("creditcvv").value("123")
                    .key("creditp").value("5/2020")
                    .key("orderNo").value(order)
                    .key("gatewayCode").value("masaPay")
                    .key("userId").value(AccountInfo.getInstance().getUserId())
                    .endObject();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
        return body;
    }
    private String[] getMasaParm(String params){
        KLog.d(params);

        String[] values=new String [17];
        String[] maps = params.split("&");
        String[] key = {"version", "merchantId", "charset", "language", "signType", "merchantOrderNo", "goodsName",
        "goodsDesc","orderExchange","currencyCode","orderAmount","submitTime","expiryTime","bgUrl","orderIp","signMsg"};

        for (int i = 0; i < key.length; i++) {
            for (int j = 0; j < maps.length; j++) {
                int index = maps[j].indexOf(key[i]);
                if (index != -1) {
                    values[i] = maps[j].substring(index + key[i].length() + 1);
                    break;
                }
            }
        }
        return values;
    }
    /**
     * API方式： <li>账单信息、卡号、有效期、CVV2、账单邮箱必传</li>
     *
     * @return
     */
    private MasapayPaymentRequest getMasapayPaymentRequest() {
        MasapayPaymentRequest request = new MasapayPaymentRequest();

        // 异步通知地址
        request.setExt1("");
        request.setExt2("");
        request.setRemark("");
        // VISA   MASTER   JCB
        request.setOrgCode("VISA");
        //付款人姓名
        request.setCardHolderFirstName("aaa");
        request.setCardHolderLastName("bbb");
        //付款人手机
        request.setCardHolderPhoneNumber("");
        // 账单信息必传
        request.setBillFirstName("test1");
        request.setBillLastName("test2");
        request.setBillAddress("test3");
        request.setBillPostalCode("test4");
        request.setBillCountry("usa");
        request.setBillState("test5");
        request.setBillCity("test6");
        request.setBillEmail("8@123.com");
        request.setBillPhoneNumber("13100000000");
        //收获信息
        request.setShippingFirstName("first");
        request.setShippingLastName("last");
        request.setShippingAddress("浦东新区 浦电路");
        request.setShippingPostalCode("200000");
        request.setShippingCompany("上海乾汇测试客户端商户");
        request.setShippingCountry("China");
        request.setShippingState("Shanghai");
        request.setShippingCity("Shanghai");
        request.setShippingEmail("xxxx@masapay.com");
        request.setShippingPhoneNumber("13100000000");
        //风控信息
        request.setPayerName("15887456123");
        request.setPayerMobile("15887456123");
        request.setPayerEmail("xxxx@masapay.com");
        request.setRegisterUserId("123123123");
        request.setRegisterUserEmail("xxxx@masapay.com");
        request.setRegisterTime(DateFormat.format("yyyyMMddHHmmss",new Date()).toString());
        request.setRegisterIp("111.111.90.25");
        request.setRegisterTerminal("01");
        request.setOrderIp("111.111.90.25");
        request.setExt3("");
        request.setExt4("");
        request.setReferer("");
        return request;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MasapayTask.PAYMENT:
                    MasapayPaymentResponse paymentResponse = (MasapayPaymentResponse) msg.obj;
                    String resultCode = paymentResponse.getResultCode();
                    if (TextUtils.equals(resultCode, "10")) {
                        // TODO 商户支付成功处理
                        Toast.makeText(SelectPaymentActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.equals(resultCode, "11")) {
                        // TODO 商户支付失败处理
                        Toast.makeText(SelectPaymentActivity.this, "支付失败:" + paymentResponse.getErrCode() + "|" + paymentResponse.getErrMsg(), Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.equals(resultCode, "00")) {
                        // TODO 商户支付中处理
                        Toast.makeText(SelectPaymentActivity.this, "支付中", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    /*********************end masapay********************/
    private String[] getParm(String params) {
        String[] values = new String[7];
        String[] maps = params.split("&");
        String[] key = {"appid", "noncestr", "package", "partnerid", "prepayid", "sign", "imestamp"};

        for (int i = 0; i < key.length; i++) {
            for (int j = 0; j < maps.length; j++) {
                int index = maps[j].indexOf(key[i]);
                if (index != -1) {
                    values[i] = maps[j].substring(index + key[i].length() + 1);
                    break;
                }
            }
        }
        return values;
    }

    private String[] getPayPalParm(String param) {
        String[] values = new String[4];
        String[] maps = param.split("&");
        String[] key = {"client_token", "currency", "total_amount", "trade_no"};
        for (int i = 0; i < key.length; i++) {
            for (int j = 0; j < maps.length; j++) {
                int index = maps[j].indexOf(key[i]);
                if (index != -1) {
                    values[i] = maps[j].substring(index + key[i].length() + 1);
                    break;
                }
            }
        }
        return values;
    }

    public void getPayPara() {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).payOrder(getPayBody("aliAppPay"))
                .flatMap(new Func1<PayModel, Observable<Map<String, String>>>() {
                    @Override
                    public Observable<Map<String, String>> call(final PayModel model) {
                        return Observable.fromCallable(new Func0<Map<String, String>>() {
                            @Override
                            public Map<String, String> call() {
                                PayTask alipay = new PayTask(SelectPaymentActivity.this);
                                Map<String, String> result = alipay.payV2(model.data.channelParams, true);
                                KLog.i("msp", result.toString());
                                return result;
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Map<String, String>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        KLog.i(e);
                    }

                    @Override
                    public void onNext(Map<String, String> result) {
                        if (result.get("resultStatus").equals("9000")) {
                            Intent intent=new Intent(SelectPaymentActivity.this,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                            startActivity(intent);
                            Intent orderIntent = new Intent(SelectPaymentActivity.this, OrdersListActivity.class);
                            //orderIntent.putExtra(Constants.SHOW_DIALOG, Constants.SHOW_DIALOG);
                            startActivity(orderIntent);
                            Util.startActivity(SelectPaymentActivity.this,SuccessActivity.class,Constants.ORDER_NO,mOrderNo);
                        } else {
                            CommonDialogFragment dialogFragment = CommonDialogFragment.newInstance(R.layout.dialog_order_fail, "支付失败");
                            dialogFragment.setActionListener(new CommonDialogFragment.ActionListener() {
                                @Override
                                public void doAction() {
                                    //getPayPara();
                                }
                                @Override
                                public void cancel() {
                                    Intent intent=new Intent(SelectPaymentActivity.this,HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                                    startActivity(intent);
                                    Intent orderIntent = new Intent(SelectPaymentActivity.this, OrdersListActivity.class);
                                    startActivity(orderIntent);
                                    finish();
                                }
                            });
                            dialogFragment.show(getSupportFragmentManager(),"tag");
                            //Toast.makeText(SelectPaymentActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
        }
        // 设置进度条风格，风格为圆形，旋转的
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置ProgressDialog 提示信息
        //mDialog.setMessage("请稍等。。。");
        // 设置ProgressDialog 的进度条是否不明确
        mDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    public void onBackPressed() {
        CommonDialogFragment dialogFragment = CommonDialogFragment.newInstance(R.layout.dialog_subtitle, "确定要离开？",String.format("请在%d分钟内完成支付", App.sExpireTime));
        dialogFragment.setActionListener(new CommonDialogFragment.ActionListener() {
            @Override
            public void doAction() {}
            @Override
            public void cancel() {
                if(Constants.ORDER_DETAIL.equals(mFrom)){
                    SelectPaymentActivity.super.onBackPressed();
                    return;
                }
                Intent intent;
                if(Constants.CART.equals(mFrom)){
                    intent=new Intent(SelectPaymentActivity.this,ShopCartActivity.class);
                }else if(Constants.COMMODITY.equals(mFrom)){
                    intent=new Intent(SelectPaymentActivity.this,CommodityDetailActivity.class);
//                    SelectPaymentActivity.super.onBackPressed();
//                    Intent orderIntent = new Intent(SelectPaymentActivity.this, OrdersListActivity.class);
//                    startActivity(orderIntent);
//                    return;
                }
                else {
                    intent=new Intent(SelectPaymentActivity.this,AwardActivity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
                Intent orderIntent = new Intent(SelectPaymentActivity.this, OrdersListActivity.class);
                startActivity(orderIntent);
                //finish();
            }
        });
        dialogFragment.show(getSupportFragmentManager(),"tag");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBraintreeFragment != null) {
            mBraintreeFragment.removeListener(this);
        }
    }
}
