//package com.tmmt.innersect.ui.fragment;
//
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.Contacts;
//import android.support.v4.app.Fragment;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.View;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.socks.library.KLog;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.AccountInfo;
//import com.tmmt.innersect.mvp.model.Address;
//import com.tmmt.innersect.mvp.presenter.AddressPresenter;
//import com.tmmt.innersect.mvp.view.AddressView;
//import com.tmmt.innersect.mvp.view.BaseView;
//import com.tmmt.innersect.ui.activity.AddressActivity;
//import com.tmmt.innersect.utils.AddressApi;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//import chihane.jdaddressselector.AddressProvider;
//import chihane.jdaddressselector.AddressSelector;
//import chihane.jdaddressselector.BottomDialog;
//import chihane.jdaddressselector.OnAddressSelectedListener;
//import chihane.jdaddressselector.model.City;
//import chihane.jdaddressselector.model.County;
//import chihane.jdaddressselector.model.Province;
//import chihane.jdaddressselector.model.Street;
//
//import static android.app.Activity.RESULT_OK;
//
//
///**
// * Created by flame on 2017/4/12.
// */
//
//public class EditAddressFragment2 extends BaseFragment implements BaseView<List<Address>> {
//
//    @BindView(R.id.name_view)
//    EditText mNameView;
//    @BindView(R.id.tel_view)
//    EditText mTelView;
//    @BindView(R.id.city_view)
//    TextView mCityView;
//    @BindView(R.id.detail_view)
//    EditText mDetailView;
//    @BindView(R.id.default_view)
//    CheckBox mDefaultView;
//    AddressPresenter mPresenter;
//    String mAction=AddressActivity.ACTION_ADD;
//    Address mAddress;
//    boolean isFirst;
//    //AddressSelector mAddressSelector;
//
//    @Override
//    int getLayout() {
//        return R.layout.fragment_add_address;
//    }
//
//    public static Fragment getInstance(Address address){
//        Fragment fragment=new EditAddressFragment2();
//        Bundle bundle=new Bundle();
//        bundle.putParcelable(AddressActivity.ADDRESS_KEY,address);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Override
//    protected void initView(View view) {
//        super.initView(view);
//        setHasOptionsMenu(true);
//
//        mPresenter=new AddressPresenter();
//        mPresenter.attachView(this);
//        Intent intent=getActivity().getIntent();
//        mAddress= intent.getParcelableExtra(AddressActivity.ADDRESS_KEY);
//        mAction= intent.getAction();
//        isFirst=intent.getBooleanExtra(AddressActivity.IS_FIRST,false);
//        if(isFirst){
//            mDefaultView.setChecked(true);
//            mDefaultView.setEnabled(false);
//        }
//        if(mAddress!=null){
//            mNameView.setText(mAddress.getName());
//            mCityView.setText(mAddress.getCity());
//            mTelView.setText(mAddress.getTel());
//            mDetailView.setText(mAddress.getDetail());
//            mDefaultView.setChecked(mAddress.isDefault());
//        }
//
//        AddressSelector selector = new AddressSelector(getContext());
//        final AddressApi addressApi=new AddressApi(getContext());
//        selector.setAddressProvider(new AddressProvider() {
//            @Override
//            public void provideProvinces(AddressReceiver<Province> addressReceiver) {
//                List<Province> provinces = addressApi.getProvinces();
//                addressReceiver.send(provinces);
//            }
//
//            @Override
//            public void provideCitiesWith(int provinceId, AddressReceiver<City> addressReceiver) {
//                List<City> cities = addressApi.getCities(provinceId);
//                addressReceiver.send(cities);
//
//            }
//
//            @Override
//            public void provideCountiesWith(int cityId, AddressReceiver<County> addressReceiver) {
//                KLog.i(cityId);
//                List<County> counties=addressApi.getCountries(cityId);
//                addressReceiver.send(counties);
//            }
//
//            @Override
//            public void provideStreetsWith(int countyId, AddressReceiver<Street> addressReceiver) {
//                // blahblahblah
//            }
//        });
//
//
//
//    }
//
//    @Override
//    public void success(List<Address> list) {
//        getActivity().onBackPressed();
//    }
//
//    @Override
//    public void failed() {}
//
//
//
//    @OnClick(R.id.select_tel)
//    public void selectTel(){
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("vnd.android.cursor.dir/phone");
//        startActivityForResult(intent, 1);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.main,menu);
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case 1:
//                if (resultCode == RESULT_OK) {
//                    if (data == null) {
//                        return;
//                    }
//                    Uri uri = data.getData();
//                    Cursor cursor =getContext().getContentResolver().query(uri, null, null, null, null);
//                    cursor.moveToFirst();
//                    KLog.d(cursor.toString());
//                    String number = cursor.getString(cursor.getColumnIndexOrThrow(Contacts.Phones.NUMBER));
//                    mTelView.setText(number);
//                }
//                break;
//        }
//    }
//
//    @OnClick({R.id.select_address})
//    public void showSelect(){
//        final BottomDialog dialog = new BottomDialog(getContext());
//        dialog.setOnAddressSelectedListener(new OnAddressSelectedListener() {
//            @Override
//            public void onAddressSelected(Province province, City city, County county, Street street) {
//                if(street==null){
//                    mCityView.setText(province.name+","+city.name+",");
//                }else {
//                    mCityView.setText(province.name+","+city.name+","+street.name);
//                }
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }
//
//    public void saveAddress(){
//        Address address;
//        if (mAddress==null||AddressActivity.ACTION_ADD.equals(mAction)
//                ||AddressActivity.ACTION_PICK_ADD.equals(mAction)){
//            address=new Address();
//        }else {
//            address=mAddress;
//        }
//        if(!check()){
//            return;
//        }
//        address.setName(mNameView.getText().toString().trim());
//        address.setTel(mTelView.getText().toString().trim());
//        address.setCity(mCityView.getText().toString().trim());
//        address.setDetail(mDetailView.getText().toString().trim());
//        address.setDefault(mDefaultView.isChecked()?1:0);
//        address.setUserId(AccountInfo.getInstance().getUserId());
//
//        if (AddressActivity.ACTION_EDIT.equals(mAction)){
//            mPresenter.updateAddress(address);
//        }else {
//            mPresenter.saveAddress(address);
//        }
//
//    }
//
//    private boolean check(){
//        String name=mNameView.getText().toString().trim();
//        if(name.isEmpty()){
//            Toast.makeText(getContext(),"收货人不能为空",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        String tel=mTelView.getText().toString().trim();
//        if(tel.isEmpty()){
//            Toast.makeText(getContext(),"联系方式不能为空",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        String city=mCityView.getText().toString().trim();
//        if(city.isEmpty()){
//            Toast.makeText(getContext(),"所在地区不能为空",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        String detail=mDetailView.getText().toString().trim();
//        if(detail.isEmpty()){
//            Toast.makeText(getContext(),"地址详情不能为空",Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//
//    private void showEmptyView() {}
//}
