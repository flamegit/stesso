package com.tmmt.innersect.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.ShopCart;
import com.tmmt.innersect.ui.fragment.ShopCartEditFragment;
import com.tmmt.innersect.ui.fragment.ShopCartFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by flame on 2017/4/19.
 */

public class ShopCartActivity extends AppCompatActivity{

    private boolean isEditMod;
    @BindView(R.id.action_view)
    TextView mActionView;

    @BindView(R.id.empty_view)
    View mEmptyView;
    ShopCartFragment mShopCartFragment;
    ShopCartEditFragment mShopCartEditFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);
        ButterKnife.bind(this);
        isEditMod=false;
        mActionView.setText(getString(R.string.edit));
        initView();
    }

    private void  initView(){
        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.content);
        if(fragment==null){
            mShopCartFragment=new ShopCartFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content,mShopCartFragment)
                    .commit();
        }
    }

    @OnClick(R.id.back_view)
    public void back(){
        onBackPressed();
        //overridePendingTransition(0,R.anim.activity_close);
    }

    @OnClick(R.id.action_view)
    public void changeMod(){
        if(isEditMod){
            isEditMod=false;
            mActionView.setText(getString(R.string.edit));
            if(mShopCartEditFragment!=null){
                mShopCartEditFragment.changeQuantity();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content,mShopCartFragment)
                    .commit();
        }else{
            mActionView.setText(getString(R.string.complete));
            //ShopCart.getInstance().removeAll(false);
            isEditMod=true;
            if(mShopCartEditFragment==null) {
                mShopCartEditFragment = new ShopCartEditFragment();
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content,mShopCartEditFragment)
                    .commit();
        }
    }

    @OnClick(R.id.go_shopping)
    public void goShopping(){
        Intent intent =new Intent(this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    //@Override
    public void showEmptyView(){
        mActionView.setVisibility(View.INVISIBLE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mShopCartFragment!=null){
            mShopCartFragment.changeQuantity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static class SkuCount{
        public int id;
        public int skuCount;
    }
}
