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
import com.google.gson.GsonBuilder;
import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Address;
import com.tmmt.innersect.mvp.model.Coupon;
import com.tmmt.innersect.mvp.model.DiscountInfo;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.mvp.model.Location;
import com.tmmt.innersect.mvp.model.OrderItem;
import com.tmmt.innersect.mvp.model.OrderViewModel;
import com.tmmt.innersect.mvp.model.ProductList;
import com.tmmt.innersect.mvp.model.SettlementInfo;
import com.tmmt.innersect.mvp.model.ShopAddress;
import com.tmmt.innersect.mvp.model.ShopCart;
import com.tmmt.innersect.mvp.model.ShopItem;
import com.tmmt.innersect.mvp.model.Status;
import com.tmmt.innersect.ui.adapter.SettlementAdapter;
import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;

import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SettlementActivity extends BaseActivity {
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
    //@BindView(R.id.hint_view)
    //View mHintView;
    @BindView(R.id.action_view)
    View mActionView;

    @BindView(R.id.shop_view)
    TextView mShopView;

    @BindView(R.id.location_view)
    TextView mLoacationView;

    @BindView(R.id.shop_layout)
    View mShopLayout;

    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;
    private Address mDefaultAddress;
    private List<Integer> mPidList;
    private ArrayList<Integer> mCidList;
    private SettlementInfo mInfo;
    BigDecimal mCommodityPrice;
    List<ShopItem> mCommodityList;
    String mOrderNo;
    SettlementAdapter mAdapter;
    int mAmount = 0;
    int mCount = 0;

    String mJson;
    String mFrom;
    String mShop;

    //public static boolean isFromCart;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settlement;
    }

    @Override
    protected String getTitleString() {
        return getString(R.string.payment);
    }

    public static void start(Context context,ShopItem item,String shop){
        Intent intent=new Intent(context,SettlementActivity.class);
        intent.putExtra(Constants.LIST,item);
        intent.putExtra(Constants.SHOP,shop);
        context.startActivity(intent);
    }

    public static void start(Context context,String shop){
        Intent intent=new Intent(context,SettlementActivity.class);
        intent.putExtra(Constants.SHOP,shop);
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        super.initView();
        mOrderNo = null;
        mShop=getIntent().getStringExtra(Constants.SHOP);
        ShopItem item=getIntent().getParcelableExtra(Constants.LIST);
        if(item==null){
            mFrom=Constants.CART;
            ShopCart shopCart = ShopCart.getInstance();
            mCommodityList = shopCart.getSelect();
            mCommodityPrice = shopCart.getSelectPrice();
        }else {
            mFrom=Constants.COMMODITY;
            mCommodityList= Arrays.asList(item);
            mCommodityPrice=new BigDecimal(String.valueOf(item.price));
        }

        mPriceView.setText(getString(R.string.sub_total) + "￥" + mCommodityPrice);
        mInfo = new SettlementInfo(mCommodityPrice.toString(), "0", "0");
        mAdapter = new SettlementAdapter(mInfo);
        mAdapter.setCallback((int position, Object data) -> {
            Intent intent = new Intent(this, CouponActivity.class);
            intent.putExtra(Constants.IS_EDIT, true);
            if (mJson == null) {
                mJson = getCouponString();
            }
            if (mCidList != null) {
                intent.putIntegerArrayListExtra(Constants.ID_LIST, mCidList);
            }
            intent.putExtra(Constants.BODY, mJson);
            startActivityForResult(intent, 1);
        });
        mAdapter.addItems(mCommodityList);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadDefaultAddress();
        loadCouponInfo();
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

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int use = data.getIntExtra(Constants.COUNT, 0);
                mAmount = data.getIntExtra(Constants.AMOUNT, 0);
                mCidList = data.getIntegerArrayListExtra(Constants.ID_LIST);
                mAdapter.updateCouponInfo(mCount, use, mAmount, true);
                int discount;
                try {
                    discount = Integer.valueOf(mInfo.discount) + mAmount;
                } catch (NumberFormatException e) {
                    discount = mAmount;
                }

                mInfo.discount = String.valueOf(discount) + ".0";
                mAdapter.changeSettlementInfo(mInfo);
                BigDecimal total = mInfo.getTotalPrice(mCommodityPrice);

                mPriceView.setText(getString(R.string.sub_total) + "￥" + total);
            } else {

            }
        }
    }
    private void loadCouponInfo() {
        mJson = getCouponString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), mJson);
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getCoupons(body).enqueue(new Callback<HttpBean<List<Coupon>>>() {
            @Override
            public void onResponse(Call<HttpBean<List<Coupon>>> call, Response<HttpBean<List<Coupon>>> response) {
                if (response.isSuccessful()) {
                    List<Coupon> data = response.body().data;
                    if (data != null && !data.isEmpty()) {
                        int c = 0;
                        for (Coupon coupon : data) {
                            if (coupon.usable == 0) {
                                c++;
                            }
                        }
                        mCount = c;
                    } else {
                        mCount = 0;
                    }
                    mAdapter.updateCouponInfo(mCount, 0, 0, false);
                }
            }

            @Override
            public void onFailure(Call<HttpBean<List<Coupon>>> call, Throwable t) {

            }
        });
    }
    private void fillAddress(Address address) {
        if(isDestroyed()){
            return;
        }
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

    private void fillAddress(Location location) {
        if(isDestroyed()){
            return;
        }

        if (location != null) {
            mShopLayout.setVisibility(View.VISIBLE);
            mShopView.setText("门店自提");
            mLoacationView.setText(location.location);
        } else {}
    }

    @OnClick(R.id.address_container)
    public void selectAddress() {
        mOrderNo = null;
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
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).computeFee(getBody()).enqueue(new NetCallback<DiscountInfo>() {
            @Override
            public void onSucceed(DiscountInfo data) {
                mInfo.transport = String.valueOf(data.deliveryAmount);
                mInfo.hint = data.ticketOrderTips;
                List<DiscountInfo.Discount> list = data.discountList;
                if (list != null && !list.isEmpty()) {
                    if (mPidList == null) {
                        mPidList = new ArrayList<>();
                    }
                    mPidList.clear();
                    int discount = 0;
                    for (int i = 0; i < list.size(); i++) {
                        discount += list.get(i).discounts;
                        mPidList.add(list.get(i).relateId);
                    }
                    discount += mAmount;
                    mInfo.discount = String.valueOf(discount);
                }
                BigDecimal total = mInfo.getTotalPrice(mCommodityPrice);
                mPriceView.setText(getString(R.string.sub_total) + "￥" + total);
                mAdapter.changeSettlementInfo(mInfo);
            }
        });
    }

    private RequestBody getBody() {
        OrderViewModel model = new OrderViewModel();
        ArrayList<OrderItem> items = new ArrayList<>();
        for (ShopItem shopItem : mCommodityList) {
            OrderItem item = new OrderItem();
            item.omsSkuId = shopItem.omsSkuId;
            item.productId = shopItem.productId;
            item.skuId = shopItem.skuId;
            item.quantity = shopItem.quantity;

            if(shopItem.shop==null){
                item.store=shopItem.store;
            }else {
                item.store = shopItem.shop;
            }
            items.add(item);
        }
        model.userId = AccountInfo.getInstance().getUserId();

        int addressId=-1;
        if (mDefaultAddress != null) {
            addressId = mDefaultAddress.getId();
        }
        if (mPidList != null) {
            model.pidList = mPidList;
        }
        if (mCidList != null) {
            model.cidList = mCidList;
        }

        if(addressId!=-1){
            model.deliveryAddressId=addressId;
        }

        model.orderProductList = items;
        model.totalAmount = mCommodityPrice;

        //TODO
        //model.shop=mShop;
        //Gson gson= new GsonBuilder().serializeNulls().create();

        Gson gson=new Gson();
        String json = gson.toJson(model);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        return body;
    }

    private String getCouponString() {
        ProductList list = new ProductList();
        ArrayList<OrderItem> items = new ArrayList<>();
        for (ShopItem shopItem : mCommodityList) {
            OrderItem item = new OrderItem();
            item.omsSkuId = shopItem.omsSkuId;
            item.productId = shopItem.productId;
            item.skuId = shopItem.skuId;
            item.quantity = shopItem.quantity;
            item.store = "APP";
            items.add(item);
        }
        list.products = items;
        Gson gson = new Gson();
        String json = gson.toJson(list);
        KLog.json(json);
        return json;
    }

    private void settlement() {
        if (mOrderNo != null) {
            checkOut();
            return;
        }
        mActionView.setEnabled(false);
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).addOrders(getBody())
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
                            mOrderNo = result.data;
                            checkOut();
                            ShopCart.getInstance().setRefresh(true);
                            ShopCart.getInstance().clear();
                        } else {
                            Toast.makeText(SettlementActivity.this, result.msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void checkOut() {
        BigDecimal total = mInfo.getTotalPrice(mCommodityPrice);
        SelectPaymentActivity.start(SettlementActivity.this,mOrderNo,mFrom,total.floatValue());
    }

    @OnClick(R.id.action_view)
    public void openPayment() {
        if (mDefaultAddress == null && mShop==null) {
            Toast.makeText(this, "请添加地址", Toast.LENGTH_SHORT).show();
            return;
        }
        settlement();
    }

    private void loadDefaultAddress() {

        if(mShop!=null){
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getShopAddress(mShop).enqueue(new NetCallback<Location>() {
                @Override
                public void onSucceed(Location data) {
                    fillAddress(data);
                }
            });

        }else {
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
                        }});
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
