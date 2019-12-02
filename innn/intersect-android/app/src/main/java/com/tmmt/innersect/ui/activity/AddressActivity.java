package com.tmmt.innersect.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.Address;
import com.tmmt.innersect.ui.fragment.AddAddressFragment;
import com.tmmt.innersect.ui.fragment.EditAddressFragment;
import com.tmmt.innersect.ui.fragment.ShowAddressFragment;
import com.tmmt.innersect.utils.SystemUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by flame on 2017/4/19.
 */

public class AddressActivity extends BaseActivity{
    public static final String ACTION_SHOW="com.tmmt.innersect.ACTION_SHOW";
    public static final String ACTION_EDIT="com.tmmt.innersect.ACTION_EDIT";
    public static final String ACTION_ADD="com.tmmt.innersect.ACTION_ADD";
    public static final String ACTION_PICK="com.tmmt.innersect.ACTION_PICK";
    public static final String ACTION_PICK_ADD="com.tmmt.innersect.ACTION_PICK_ADD";
    public static final String ADDRESS_KEY="address";
    public static final String IS_FIRST="isFirst";
    public static final String ONLY_ONE="only_one";

    @BindView(R.id.action_view)
    TextView mActionView;
    private Fragment mTarget;
    private String mAction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(getIntent());
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_address;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Address address=intent.getParcelableExtra(ADDRESS_KEY);
        if(address!=null){
            getIntent().putExtra(ADDRESS_KEY,address);
        }
        boolean isFirst=intent.getBooleanExtra(IS_FIRST,false);
        getIntent().putExtra(IS_FIRST,isFirst);

        boolean onlyOne=intent.getBooleanExtra(ONLY_ONE,false);
        getIntent().putExtra(ONLY_ONE,onlyOne);
        initView(intent);
    }

    private void initView(Intent intent){
        mAction=intent.getAction();
        if(mAction==null||mAction.equals(ACTION_SHOW)||mAction.equals(ACTION_PICK)){
            setTitle(getString(R.string.address_management));
            mActionView.setText("");
            mTarget=new ShowAddressFragment();
        }else if(mAction.equals(ACTION_ADD)||mAction.equals(ACTION_PICK_ADD)){
            setTitle(getString(R.string.add_address));
            mActionView.setText(getString(R.string.save));
            mTarget=new AddAddressFragment();

        }else if(mAction.equals(ACTION_EDIT)) {
            setTitle(getString(R.string.edit_address));
            mActionView.setText(getString(R.string.save));
            mTarget=new EditAddressFragment();
        }

        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.content);
        if(fragment==null){
            getSupportFragmentManager().beginTransaction().add(R.id.content,mTarget)
                    .commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.content,mTarget)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.content);
        if(fragment instanceof ShowAddressFragment||mAction.equals(ACTION_PICK_ADD)){
            super.onBackPressed();
        }else {
            SystemUtil.hideKey(this,mActionView);
            setTitle(getString(R.string.address_management));
            mActionView.setText("");
            fragment=new ShowAddressFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content,fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }

    @OnClick(R.id.action_view)
    public void saveAddress() {
        if(mTarget!=null && mTarget instanceof AddAddressFragment){
            ((AddAddressFragment)mTarget).saveAddress();
        }
    }
    private void setTitle(String title){
        mTitleView.setText(title);
    }

}
