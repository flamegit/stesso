package com.tmmt.innersect.ui.fragment;


import android.content.Intent;
import android.view.View;

import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.ui.activity.AddressActivity;
import com.tmmt.innersect.utils.AnalyticsUtil;


/**
 * Created by flame on 2017/4/12.
 */

public class EditAddressFragment extends AddAddressFragment{
    boolean onlyOne;

    @Override
    protected void initView(View view) {
        super.initView(view);
        AnalyticsUtil.reportEvent(AnalyticsUtil.ADDRESS_EDIT);
        Intent intent=getActivity().getIntent();
        mAddress= intent.getParcelableExtra(AddressActivity.ADDRESS_KEY);
        onlyOne=intent.getBooleanExtra(AddressActivity.ONLY_ONE,false);
        if(mAddress!=null){
            mNameView.setText(mAddress.getName());
            mCityView.setText(mAddress.getCity());
            mTelView.setText(mAddress.getTel());
            mDetailView.setText(mAddress.getStreet());
            mDefaultView.setChecked(mAddress.isDefault());
        }
        if(onlyOne){
            mDefaultView.setEnabled(false);
        }
    }

    public void saveAddress(){
        AnalyticsUtil.reportEvent(AnalyticsUtil.ADDRESS_EDIT);
        if(!check()||mAddress==null){
            return;
        }
        mAddress.setName(mNameView.getText().toString().trim());
        mAddress.setTel(mTelView.getText().toString().trim());
        mAddress.setDetail(mDetailView.getText().toString().trim());
        mAddress.setDefault(mDefaultView.isChecked()?1:0);
        mAddress.setUserId(AccountInfo.getInstance().getUserId());
        mPresenter.updateAddress(mAddress);
    }

}
