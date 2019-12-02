package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Address;
import com.tmmt.innersect.mvp.model.AwardCommodity;
import com.tmmt.innersect.mvp.model.DiscountInfo;
import com.tmmt.innersect.mvp.model.OrderItem;
import com.tmmt.innersect.mvp.model.OrderViewModel;
import com.tmmt.innersect.mvp.model.SettlementInfo;
import com.tmmt.innersect.mvp.model.ShopItem;
import com.tmmt.innersect.mvp.model.Status;
import com.tmmt.innersect.ui.adapter.AwardSettlementAdapter;
import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class  AwardSettlementActivity extends BaseActivity {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.name_view)
    TextView mNameView;
    @BindView(R.id.tel_view)
    TextView mTelView;
    @BindView(R.id.address_view)
    TextView mAddressView;
    @BindView(R.id.add_address)
    View mAddAddressView;
    @BindView(R.id.address_container)
    View mAddressContainer;
    @BindView(R.id.info_view)
    TextView mPriceView;
    @BindView(R.id.action_view)
    View mActionView;
    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    private Address mDefaultAddress;
    private SettlementInfo mInfo;
    List<ShopItem> mCommodityList;
    AwardSettlementAdapter mAdapter;
    long mPrizeId;
    BigDecimal mTotalPrice;
    //AwardCommodity mAwardCommodity;

    ShopItem mShopItem;

    float mDiscount;

    public static void start(Context context, long id){
        Intent intent=new Intent(context,AwardSettlementActivity.class);
        intent.putExtra(Constants.ACTIVITY_ID,id);
        context.startActivity(intent);
    }


    public static void start(Context context,ShopItem item,long prizeId,float price){
        Intent intent=new Intent(context,AwardSettlementActivity.class);
        intent.putExtra(Constants.LIST,item);
        intent.putExtra(Constants.ACTIVITY_ID,prizeId);
        intent.putExtra(Constants.TOTAL_PRICE,price);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settlement;
    }

    @Override
    protected String getTitleString() {
        return getString(R.string.payment);
    }

    @Override
    protected void initView() {
        super.initView();

        mDiscount=getIntent().getFloatExtra(Constants.TOTAL_PRICE,0);
        mShopItem=getIntent().getParcelableExtra(Constants.LIST);
        mPrizeId=getIntent().getLongExtra(Constants.ACTIVITY_ID,0);
        mTotalPrice=new BigDecimal(String.valueOf(mShopItem.price));

        mInfo = new SettlementInfo(mTotalPrice.toString(), String.valueOf(mDiscount), "0");
        mAdapter = new AwardSettlementAdapter(mInfo);
        mCommodityList=Arrays.asList(mShopItem);
        mAdapter.addItems(mCommodityList);
        mPriceView.setText(getString(R.string.sub_total) + "￥"+mInfo.getTotalPrice(mTotalPrice));
        mAdapter.changeSettlementInfo(mInfo);

//        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getAwardProduct(mPrizeId).enqueue(new NetCallback<AwardCommodity>() {
//            @Override
//            public void onSucceed(AwardCommodity data) {
//                mAwardCommodity=data;
//                mCommodityList=data.skus;
//                mAdapter.addItems(mCommodityList);
//                mTotalPrice=data.getTotalPrice();
//                mInfo.discount=String.valueOf(data.discounts);
//                mInfo.totalPrice=String.valueOf(mTotalPrice);
//                mPriceView.setText(getString(R.string.sub_total) + "￥"+mInfo.getTotalPrice(mTotalPrice));
//                mAdapter.changeSettlementInfo(mInfo);
//
//            }
//        });

        loadDefaultAddress();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Address address = data.getParcelableExtra(AddressActivity.ADDRESS_KEY);
                fillAddress(address);
            } else {
                loadDefaultAddress();
            }
        }
    }

    private void fillAddress(Address address) {
        if (address != null) {
            mDefaultAddress = address;
            getDiscount();
            mAddressContainer.setVisibility(View.VISIBLE);
            mAddAddressView.setVisibility(View.INVISIBLE);
            mNameView.setText(address.getName());
            mTelView.setText(address.getTel());
            mAddressView.setText(address.getDetail());
        } else {
            mAddAddressView.setVisibility(View.VISIBLE);
            mDefaultAddress = null;
            mAddressContainer.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.address_container)
    public void selectAddress() {
        Intent intent = new Intent(AddressActivity.ACTION_PICK);
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.add_address)
    public void addAddress() {
        Intent intent = new Intent(AddressActivity.ACTION_PICK_ADD);
        intent.putExtra(AddressActivity.IS_FIRST, true);
        startActivityForResult(intent, 0);
    }

    private void getDiscount() {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).computeFee(getBody(false)).enqueue(new NetCallback<DiscountInfo>() {
            @Override
            public void onSucceed(DiscountInfo data) {
                mInfo.transport = String.valueOf(data.deliveryAmount);
                mAdapter.changeSettlementInfo(mInfo);
                mPriceView.setText(getString(R.string.sub_total) + "￥"+ mInfo.getTotalPrice(mTotalPrice));
            }
        });
    }

    private RequestBody getBody(boolean include) {
        OrderViewModel model = new OrderViewModel();
        ArrayList<OrderItem> items = new ArrayList<>();
        for (ShopItem shopItem : mCommodityList) {
            OrderItem item = new OrderItem();
            item.omsSkuId = shopItem.omsSkuId;
            item.productId = shopItem.productId;
            item.skuId = shopItem.skuId;
            item.quantity = shopItem.quantity;
            item.store = shopItem.store;
            items.add(item);
        }
        model.userId = AccountInfo.getInstance().getUserId();
        int addressId = 22;
        if (mDefaultAddress != null) {
            addressId = mDefaultAddress.getId();
        }
        model.setDeliveryAddressId(addressId);
        model.orderProductList = items;
        if(include){
            model.onprid=mPrizeId;
        }
        model.totalAmount = mTotalPrice;
        Gson gson = new Gson();
        String json = gson.toJson(model);
        KLog.json(json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        return body;
    }

    private void settlement() {
        mActionView.setEnabled(false);
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).addOrders(getBody(true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() {
                        mActionView.setEnabled(true);
                    }
                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e);
                        mActionView.setEnabled(true);
                    }
                    @Override
                    public void onNext(Status result) {
                        if (result.code == 200) {
                            checkOut(result.data);
                        } else {
                            Toast.makeText(AwardSettlementActivity.this, result.msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkOut(String orderNo) {
        BigDecimal total = mInfo.getTotalPrice(mTotalPrice);
        SelectPaymentActivity.start(AwardSettlementActivity.this,orderNo,Constants.AWARD,total.floatValue());
    }

    @OnClick(R.id.action_view)
    public void openPayment() {
        if (mDefaultAddress == null) {
            Toast.makeText(this, "请添加地址", Toast.LENGTH_SHORT).show();
            return;
        }
        settlement();
    }
    private void loadDefaultAddress() {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getDefaultAddress(AccountInfo.getInstance().getUserId())
                .enqueue(new NetCallback<Address>() {
                    @Override
                    public void onSucceed(Address data) {
                        mDefaultAddress = data;
                        fillAddress(data);
                    }
                    @Override
                    protected void failed(int code) {
                        mAddAddressView.setVisibility(View.VISIBLE);
                    }
                });
    }
}
