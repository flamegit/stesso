package com.tmmt.innersect.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tmmt.innersect.R;
import com.tmmt.innersect.ui.activity.AddressActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/8.
 */
public class LoginSuccessFragment extends Fragment  {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_login_success,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @OnClick(R.id.add_address)
    public void addAddress(){
        Intent intent=new Intent(AddressActivity.ACTION_ADD);
        intent.putExtra(AddressActivity.IS_FIRST,true);
        getContext().startActivity(intent);
    }

}
