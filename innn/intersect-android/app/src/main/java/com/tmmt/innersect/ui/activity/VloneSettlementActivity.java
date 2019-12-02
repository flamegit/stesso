//package com.tmmt.innersect.ui.activity;
//
//import android.content.Intent;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.CheckBox;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.socks.library.KLog;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.AccountInfo;
//import com.tmmt.innersect.common.Constants;
//import com.tmmt.innersect.datasource.net.ApiManager;
//import com.tmmt.innersect.mvp.model.Address;
//
//import com.tmmt.innersect.mvp.model.HttpBean;
//import com.tmmt.innersect.mvp.model.ShopCart;
//import com.tmmt.innersect.mvp.model.ShopItem;
//import com.tmmt.innersect.mvp.presenter.VlonePresenter;
//import com.tmmt.innersect.ui.adapter.CommonAdapter;
//import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;
//import com.tmmt.innersect.ui.fragment.EmptyFragment;
//import com.tmmt.innersect.utils.AnalyticsUtil;
//import com.tmmt.innersect.utils.Util;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
//public class VloneSettlementActivity extends BaseActivity implements CommonAdapter.Report<String>{
//
//    @BindView(R.id.recycler_view)
//    RecyclerView mRecyclerView;
//    @BindView(R.id.name_view)
//    TextView mNameView;
//    @BindView(R.id.tel_view)
//    TextView mTelView;
//    @BindView(R.id.address_view)
//    TextView mAddressView;
//    @BindView(R.id.add_address)
//    View mAddAddressView;
//    @BindView(R.id.address_container)
//    View mAddressContainer;
//    @BindView(R.id.info_view)
//    TextView mPriceView;
//    @BindView(R.id.action_menu)
//    TextView mActionMenu;
//    @BindView(R.id.edit_action_bar)
//    View mEditBar;
//    @BindView(R.id.action_bar)
//    View mActionBar;
//    @BindView(R.id.delete_view)
//    TextView mDeleteView;
//    @BindView(R.id.select_all)
//    CheckBox mAllSelectView;
//
//    @BindView(R.id.code_view)
//    TextView mCodeView;
//
//    private Address mDefaultAddress;
//    CommonAdapter<ShopItem> mAdapter;
//    VlonePresenter mPresenter;
//    private boolean isEdit;
//    private List<SkuCount> mChangeList;
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_vlone_settlement;
//    }
//    @Override
//    protected String getTitleString() {
//        return "购物车";
//    }
//    @Override
//    protected void initView() {
//        super.initView();
//
//        AnalyticsUtil.reportEvent(AnalyticsUtil.VLONE_SHOP_SHOPPINGCAR);
//        isEdit=false;
//        mActionMenu.setText(R.string.edit);
//        mCodeView.setEnabled(false);
//        mCodeView.setBackgroundResource(R.drawable.solid_gray_bg);
//        mAdapter = new CommonAdapter<>(CommonAdapter.SHOP_BROWSE_TYPE, R.layout.viewholder_settlement);
//        mAdapter.addItems(ShopCart.getInstance().getSelect());
//        mAdapter.setReport(this);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
//        mRecyclerView.setNestedScrollingEnabled(false);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mPresenter=new VlonePresenter();
//        mPresenter.loadShopCart(new VlonePresenter.ViewCallback<List<ShopItem>>() {
//            @Override
//            public void success(List<ShopItem> data) {
//                fillView(data);
//                mPriceView.setText("实付： ￥"+ ShopCart.getInstance().getSelectPrice());
//            }
//        });
//        loadDefaultAddress();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            Address address = data.getParcelableExtra(AddressActivity.ADDRESS_KEY);
//            fillAddress(address);
//        }else {
//            loadDefaultAddress();
//        }
//    }
//    private void fillAddress(Address address){
//        if (address != null) {
//            mDefaultAddress=address;
//            mAddressContainer.setVisibility(View.VISIBLE);
//            mAddAddressView.setVisibility(View.INVISIBLE);
//            mNameView.setText(address.getName());
//            mTelView.setText(address.getTel());
//            mAddressView.setText(address.getDetail());
//            mCodeView.setEnabled(true);
//            mCodeView.setBackgroundResource(R.drawable.common_yellow_bg);
//        }else {
//            mDefaultAddress=null;
//            mAddAddressView.setVisibility(View.VISIBLE);
//            mAddressContainer.setVisibility(View.INVISIBLE);
//            mCodeView.setEnabled(false);
//            mCodeView.setBackgroundResource(R.drawable.solid_gray_bg);
//        }
//    }
//
//
//    @OnClick(R.id.address_container)
//    public void selectAddress() {
//        Intent intent = new Intent(AddressActivity.ACTION_PICK);
//        startActivityForResult(intent, 0);
//    }
//
//
//
//    @OnClick(R.id.add_address)
//    public void addAddress(){
//        Intent intent = new Intent(AddressActivity.ACTION_PICK_ADD);
//        intent.putExtra(AddressActivity.IS_FIRST,true);
//        startActivityForResult(intent, 0);
//    }
//
//    @OnClick(R.id.code_view)
//    public void showCodeView() {
//        if(mDefaultAddress!=null){
//            String id=String.valueOf(mDefaultAddress.getId());
//            Util.startActivity(this,VloneQRCodeActivity.class,Constants.ADDRESS_ID,id);
//        }
//    }
//
//    @OnClick(R.id.select_all)
//    void selectAll(){
//        boolean isChecked=mAllSelectView.isChecked();
//        ShopCart shopCart=ShopCart.getInstance();
//        shopCart.removeAll(isChecked);
//        checkedChange(isChecked);
//
//        mAdapter.notifyDataSetChanged();
//
//    }
//
//    @OnClick(R.id.action_menu)
//    public void changeMod(){
//        if(isEdit){
//            AnalyticsUtil.reportEvent(AnalyticsUtil.SHOPPINGCAR_EDIT_DONE);
//            isEdit=false;
//            mActionMenu.setText(getString(R.string.edit));
//            mActionBar.setVisibility(View.VISIBLE);
//            mEditBar.setVisibility(View.GONE);
//            changeQuantity();
//        }else{
//            AnalyticsUtil.reportEvent(AnalyticsUtil.SHOPPINGCAR_EDIT);
//            mActionMenu.setText(getString(R.string.complete));
//            isEdit=true;
//            mActionBar.setVisibility(View.GONE);
//            mEditBar.setVisibility(View.VISIBLE);
//
//        }
//        mAdapter.changeType(isEdit);
//        mRecyclerView.setAdapter(mAdapter);
//        mPriceView.setText("实付： ￥"+ ShopCart.getInstance().getSelectPrice());
//    }
//
//    @OnClick(R.id.delete_view)
//    public void delete(){
//        AnalyticsUtil.reportEvent(AnalyticsUtil.SHOPPINGCAR_EDIT_DELETE);
//        ShopCart shopCart=ShopCart.getInstance();
//        List<ShopItem> list=shopCart.getRemoveItems();
//        mPresenter.deleteShopItem(list);
//        shopCart.removeSelect();
//        mAdapter.notifyDataSetChanged();
//        if(shopCart.isEmpty()){
//            showEmptyView();
//        }
//    }
//
//    public void changeQuantity(){
//        if(mChangeList==null ||mChangeList.isEmpty()){
//            return;
//        }
//        String json=new Gson().toJson(mChangeList);
//        KLog.json(json);
//        mChangeList.clear();
//        mPresenter.updateQuantity(json);
//    }
//
//    private void fillView(List<ShopItem> result) {
//        ShopCart shopCart=ShopCart.getInstance();
//        if(shopCart.isRefresh()){
//            shopCart.addItems(result);
//
//            shopCart.setRefresh(false);
//        }
//        if(shopCart.isEmpty()){
//            showEmptyView();
//        }else {
//            mAdapter.setContent(shopCart.getContent());
//        }
//        //vlone only
//        shopCart.selectAll(true);
//        mAdapter.notifyDataSetChanged();
//
//    }
//
//    @Override
//    public void checkedChange(boolean isChecked) {
//        ShopCart shopCart =ShopCart.getInstance();
//        int count=shopCart.getCount();
//        if(count!=0&& shopCart.getRemoveCount()==count){
//            mAllSelectView.setChecked(true);
//        }else {
//            mAllSelectView.setChecked(false);
//        }
//        if(shopCart.getRemoveCount()==0){
//            mAllSelectView.setText(getString(R.string.select_all));
//            mDeleteView.setEnabled(false);
//            mDeleteView.setBackgroundResource(R.drawable.solid_gray_bg);
//        }else {
//            mAllSelectView.setText(String.format(getString(R.string.select_count),
//                    ""+ShopCart.getInstance().getRemoveCount()));
//            mDeleteView.setEnabled(true);
//            mDeleteView.setBackgroundResource(R.drawable.solid_black_bg);
//        }
//    }
//
//    @Override
//    public void callback(String data) {}
//
//    @Override
//    public void sizeChange(int position, int newSize) {
//        SkuCount skuCount=new SkuCount();
//        skuCount.id=ShopCart.getInstance().getItem(position).id;
//        skuCount.skuCount=newSize;
//        if(mChangeList==null){
//            mChangeList=new ArrayList<>();
//        }
//        mChangeList.add(skuCount);
//    }
//
//    private void loadDefaultAddress() {
//
//        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getDefaultAddress(AccountInfo.getInstance().getUserId())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<HttpBean<Address>>() {
//                    @Override
//                    public void onCompleted() {}
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mAddAddressView.setVisibility(View.VISIBLE);
//                        KLog.d(e);
//                    }
//                    @Override
//                    public void onNext(HttpBean<Address> result) {
//                        KLog.d(result.code);
//                        fillAddress(result.data);
//                    }
//                });
//    }
//    static class SkuCount{
//        public int id;
//        public int skuCount;
//    }
//
//    public void showEmptyView(){
//        Fragment emptyFragment=new EmptyFragment();
//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(android.R.id.content,emptyFragment)
//                .commit();
//
//    }
//
//}
