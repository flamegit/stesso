package com.tmmt.innersect.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;

import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.Address;
import com.tmmt.innersect.mvp.presenter.AddressPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.activity.AddressActivity;
import com.tmmt.innersect.ui.adapter.CommonAdapter;
import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by flame on 2017/4/12.
 */

public class ShowAddressFragment extends BaseFragment implements CommonView<List<Address>>,
        CommonAdapter.AddressCallback,
        CommonAdapter.Report<Address>{

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.add_address_container)
    View mViewContainer;

    @BindView(R.id.progress_view)
    View mProgress;
    CommonAdapter<Address> mAdapter;
    AddressPresenter mPresenter;
    @BindView(R.id.empty_view_stub)
    ViewStub mEmptyView;

    @Override
    int getLayout() {
        return R.layout.fragment_show_address;
    }
    @Override
    public void callback(Address data) {
        Intent intent=new Intent();
        intent.putExtra(AddressActivity.ADDRESS_KEY,data);
        getActivity().setResult(Activity.RESULT_OK,intent);
        getActivity().finish();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAdapter=new CommonAdapter<>(CommonAdapter.ADDRESS_TYPE,R.layout.viewholder_swipe_address);
        String action=getActivity().getIntent().getAction();
        if(AddressActivity.ACTION_PICK.equals(action)||AddressActivity.ACTION_PICK_ADD.equals(action)){
            mAdapter.setReport(this);
        }
        mAdapter.setAddressCallback(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter=new AddressPresenter();
        mPresenter.attachView(this);

        showProcess();
        mPresenter.loadAddresses();
    }


    @Override
    public void success(List<Address> result) {
        if(result!=null && !result.isEmpty()){
            mAdapter.addItems(result);
        }else {
            showEmptyView();
        }
        hideProcess();
    }

    @Override
    public void failed() {
        hideProcess();
    }

    private void showProcess() {
        mProgress.setVisibility(View.VISIBLE);
    }

    private void hideProcess() {
        mProgress.setVisibility(View.GONE);
    }


    @OnClick(R.id.add_address)
    public void addAddress(){
        Intent intent=new Intent(AddressActivity.ACTION_ADD);
        getContext().startActivity(intent);
    }
    private void showEmptyView() {
        View views=mEmptyView.inflate();
        View addAddress=views.findViewById(R.id.add_address);
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddressActivity.ACTION_ADD);
                intent.putExtra(AddressActivity.IS_FIRST,true);
                getContext().startActivity(intent);
            }
        });
        mViewContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setDefault(int id) {
        mPresenter.setDefaultAddress(id);
    }

    @Override
    public void removeAddress(int id,boolean isLast) {
        if(isLast){
            showEmptyView();
        }
        mPresenter.deleteAddress(id);
    }
}
