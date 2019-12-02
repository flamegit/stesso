package com.tmmt.innersect.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Coupon;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.adapter.CouponAdapter;
import com.tmmt.innersect.ui.adapter.decoration.LinearOffsetItemDecoration;
import com.tmmt.innersect.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by flame on 2017/4/12.
 */

public class CouponFragment extends BaseFragment implements CommonView<List<Coupon>>{

    @BindView(R.id.code_view)
    EditText mCodeView;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.empty_view)
    View mEmptyView;

    @BindView(R.id.code_layout)
    View codeLayout;

    @BindView(R.id.confirm_layout)
    View confirmLayout;

    CouponAdapter mAdapter;
    boolean mAvailable;
    CommonPresenter mPresenter;
    String mJson;
    boolean mSelect;

    @Override
    int getLayout() {
        return R.layout.fragment_coupon;
    }


    public static Fragment getInstance(boolean available){
        Fragment fragment=new CouponFragment();
        Bundle bundle=new Bundle();
        bundle.putBoolean(Constants.IS_EDIT,available);
        fragment.setArguments(bundle);
        return fragment;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            if(mSelect){
//                loadSelect();
//            }else {
//               // mPresenter.getCoupon(1);
//            }
//            //相当于Fragment的onResume
//        } else {
//            //相当于Fragment的onPause
//        }
//    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mSelect=getActivity().getIntent().getBooleanExtra(Constants.IS_EDIT,false);
        mJson=getActivity().getIntent().getStringExtra(Constants.BODY);
        mAvailable=getArguments().getBoolean(Constants.IS_EDIT);

        List<Integer> list=getActivity().getIntent().getIntegerArrayListExtra(Constants.ID_LIST);
        mAdapter=new CouponAdapter(mSelect,mAvailable);
        mAdapter.setSelectList(list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new LinearOffsetItemDecoration(32));
        mPresenter=new CommonPresenter();
        mPresenter.attachView(this);
        if(mSelect){
            loadSelect();
            if(mAvailable){
                confirmLayout.setVisibility(View.VISIBLE);
            }
        }else {
            if(mAvailable){
                mPresenter.getCoupon(1);
            }else {
                mPresenter.getCoupon(2);
            }
        }
        if(mAvailable){
            codeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void loadSelect() {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), mJson);
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getCoupons(body).enqueue(new Callback<HttpBean<List<Coupon>>>() {
            @Override
            public void onResponse(Call<HttpBean<List<Coupon>>> call, Response<HttpBean<List<Coupon>>> response) {
                if(response.isSuccessful()){
                   success(response.body().data);
                }
            }
            @Override
            public void onFailure(Call<HttpBean<List<Coupon>>> call, Throwable t) {}
        });
    }

    @Override
    public void success(List<Coupon> data) {
        if(data==null||data.isEmpty()){
            mEmptyView.setVisibility(View.VISIBLE);
            confirmLayout.setVisibility(View.INVISIBLE);
        }else {
            if(mSelect){
                List<Coupon> list=filter(data);
                mAdapter.addItems(list);
            }else {
                mAdapter.addItems(data);
            }
            mEmptyView.setVisibility(View.GONE);
        }
    }



    private List<Coupon> filter(List<Coupon> data){
        List<Coupon> list=new ArrayList<>();
        for(Coupon coupon:data){
            if(mAvailable){
                if(coupon.usable==0){
                    list.add(coupon);
                }
            }else {
                if(coupon.usable==1){
                    list.add(coupon);
                }
            }
        }
        return list;
    }

    @Override
    public void failed() {
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.exchange_view)
    void exchange(){
        String code=mCodeView.getText().toString().trim();
        if(!code.isEmpty()){
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).bindCoupon(code).enqueue(new NetCallback<String>() {
                @Override
                public void onSucceed(String data) {
                    SystemUtil.hideKey(getContext(),mCodeView);
                    SystemUtil.reportServerError("兑换成功");
                    if(mSelect){
                        loadSelect();
                    }else {
                        mPresenter.getCoupon(1);
                    }
                }
            });
        }
    }

    @OnClick(R.id.confirm_layout)
    void  confirm(){
        List<Coupon> coupons=mAdapter.getSelectCoupons();
        if(!coupons.isEmpty()){
            Intent intent=new Intent();
            intent.putExtra(Constants.COUNT,coupons.size());
            intent.putExtra(Constants.AMOUNT,getAmount(coupons));
            intent.putIntegerArrayListExtra(Constants.ID_LIST,getIdList(coupons));
            getActivity().setResult(Activity.RESULT_OK,intent);
        }else {
            Intent intent=new Intent();
            //intent.putExtra(Constants.COUNT,0);
            //intent.putExtra(Constants.AMOUNT,0);
            //intent.putIntegerArrayListExtra(Constants.ID_LIST,);
            getActivity().setResult(Activity.RESULT_OK,intent);
        }
        getActivity().finish();
    }

    private int getAmount(List<Coupon> coupons){
        int amount=0;
        for(Coupon coupon:coupons){
            amount+=coupon.discounts;
        }
        return amount;
    }

    private ArrayList<Integer> getIdList(List<Coupon> coupons){
       ArrayList<Integer> idList=new ArrayList<>();
       for(Coupon coupon:coupons){
           idList.add(coupon.cid);
       }
       return idList;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }
}
