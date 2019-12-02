package com.tmmt.innersect.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.mvp.model.Address;
import com.tmmt.innersect.mvp.presenter.AddressPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.activity.AddressActivity;
import com.tmmt.innersect.utils.AnalyticsUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;

import static android.app.Activity.RESULT_OK;


/**
 * Created by flame on 2017/4/12.
 */

public class AddAddressFragment extends BaseFragment implements CommonView<List<Address>> {
    @BindView(R.id.name_view)
    EditText mNameView;
    @BindView(R.id.tel_view)
    EditText mTelView;
    @BindView(R.id.city_view)
    TextView mCityView;
    @BindView(R.id.detail_view)
    EditText mDetailView;
    @BindView(R.id.default_view)
    CheckBox mDefaultView;
    AddressPresenter mPresenter;
    boolean isFirst;
    Address mAddress;
    BottomDialog mBottomDialog;

    @Override
    int getLayout() {
        return R.layout.fragment_add_address;
    }

    public static Fragment getInstance(Address address){
        Fragment fragment=new AddAddressFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable(AddressActivity.ADDRESS_KEY,address);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        setHasOptionsMenu(true);
        mPresenter=new AddressPresenter();
        mAddress=new Address();
        mPresenter.attachView(this);
        Intent intent=getActivity().getIntent();
        isFirst=intent.getBooleanExtra(AddressActivity.IS_FIRST,false);
        mBottomDialog = new BottomDialog(getContext());
        mBottomDialog.setOnAddressSelectedListener(new OnAddressSelectedListener() {
            @Override
            public void onAddressSelected(Province province, City city, County county, Street street) {
                StringBuilder content=new StringBuilder();
                if(province!=null){
                    mAddress.setProvince(province.name,province.id);
                    content.append(province.name).append(" ");
                }
                if(city!=null){
                    mAddress.setCity(city.name,city.id);
                    content.append(city.name).append(" ");
                }
                if(county!=null){
                    mAddress.setCounty(county.name,county.id);
                    content.append(county.name);
                }
                mCityView.setText(content.toString());
                mBottomDialog.dismiss();
            }
        });

        if(isFirst){
            mDefaultView.setChecked(true);
            mDefaultView.setEnabled(false);
        }
    }

    @Override
    public void success(List<Address> addresses) {
        Intent intent=new Intent(AddressActivity.ACTION_SHOW);
        if(getContext()!=null){
            getContext().startActivity(intent);
        }
    }

    @Override
    public void failed() {}

    @OnClick(R.id.select_tel)
    void selectTel(){
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
        }else {
            openContacts();
        }
    }


    private void openContacts(){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_PICK);
        i.setData(ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(i, 1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    openContacts();
                } else {
                    // Permission Denied
                    Toast.makeText(getContext(), "没有该权限，无法获取联系人", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main,menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String phoneNumber = "";
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode==1) {
                        if (data == null) {
                            return;
                        }
                        Uri contactData = data.getData();
                        if (contactData == null) {
                            return;
                        }
                        Cursor cursor = getActivity().getContentResolver().query(contactData, null, null, null, null);
                        if (cursor.moveToFirst()) {
                            String hasPhone = cursor
                                    .getString(cursor
                                            .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                            String id = cursor.getString(cursor
                                    .getColumnIndex(ContactsContract.Contacts._ID));
                            if (hasPhone.equalsIgnoreCase("1")) {
                                hasPhone = "true";
                            } else {
                                hasPhone = "false";
                            }
                            if (Boolean.parseBoolean(hasPhone)) {
                                Cursor phones = getContext().getContentResolver()
                                        .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                                null,
                                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                        + " = " + id, null, null);
                                while (phones.moveToNext()) {
                                    phoneNumber = phones.getString(phones
                                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                }
                                phones.close();
                            }else {
                                return;
                            }
                            cursor.close();
                        }
                        mTelView.setText(phoneNumber);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getContactPhone(Cursor cursor) {
        // TODO Auto-generated method stub
        int phoneColumn = cursor
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        String result = "";
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人电话的cursor
            Cursor phone = getContext().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                            + contactId, null, null);
            if (phone.moveToFirst()) {
                for (; !phone.isAfterLast(); phone.moveToNext()) {
                    int index = phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phone
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    int phone_type = phone.getInt(typeindex);
                    String phoneNumber = phone.getString(index);
                    result = phoneNumber;
//                  switch (phone_type) {//此处请看下方注释
//                  case 2:
//                      result = phoneNumber;
//                      break;
//
//                  default:
//                      break;
//                  }
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            }
        }
        return result;
    }

    @OnClick({R.id.select_address})
    public void showSelect(){
        mCityView.setFocusable(true);
        mCityView.setFocusableInTouchMode(true);
        mCityView.requestFocus();
        mBottomDialog.show();
    }

    public void saveAddress(){
        AnalyticsUtil.reportEvent(AnalyticsUtil.ADDRESS_NEWADDRESS_SAVE);
        if(!check()){
            return;
        }
        mAddress.setName(mNameView.getText().toString().trim());
        mAddress.setTel(mTelView.getText().toString().trim());
        mAddress.setDetail(mDetailView.getText().toString().trim());
        mAddress.setDefault(mDefaultView.isChecked()?1:0);
        mAddress.setUserId(AccountInfo.getInstance().getUserId());
        mPresenter.saveAddress(mAddress);

    }

    protected boolean check(){
        String name=mNameView.getText().toString().trim();
        if(name.isEmpty()){
            Toast.makeText(getContext(),"收货人不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        String tel=mTelView.getText().toString().trim();
        if(tel.isEmpty()){
            Toast.makeText(getContext(),"联系方式不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        String city=mCityView.getText().toString().trim();
        if(city.isEmpty()){
            Toast.makeText(getContext(),"所在地区不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        String detail=mDetailView.getText().toString().trim();
        if(detail.isEmpty()){
            Toast.makeText(getContext(),"地址详情不能为空",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void showEmptyView() {}

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }
}
