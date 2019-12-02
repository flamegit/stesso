package com.tmmt.innersect.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.ShopCart;
import com.tmmt.innersect.mvp.model.ShopItem;
import com.tmmt.innersect.mvp.presenter.ShopCartPresenter;
import com.tmmt.innersect.ui.activity.ShopCartActivity;
import com.tmmt.innersect.ui.adapter.Report;
import com.tmmt.innersect.ui.adapter.ShopCartAdapter;
import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by flame on 2017/4/12.
 */

public class ShopCartEditFragment extends BaseFragment implements Report {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.select_all)
    CheckBox mAllSelectView;
    @BindView(R.id.bottom_bar_container)
    View mViewContainer;
    ShopCartAdapter mAdapter;
    private List<ShopCartActivity.SkuCount> mChangeList;
    @BindView(R.id.action_view)
    TextView mActionView;
    private ShopCartPresenter mPresenter;
    @Override
    int getLayout() {
        return R.layout.fragment_edit_shopcart;
    }

    @Override
    protected void initView(View view) {
        mPresenter=new ShopCartPresenter();
        super.initView(view);
        ButterKnife.bind(this,view);
        mChangeList=new ArrayList<>();
        mAdapter=new ShopCartAdapter(true);
        mAdapter.setReport(this);
        mAdapter.setContent(ShopCart.getInstance().getContent());
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration itemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //mRecyclerView.setRecyclerListener(null);
    }

    private void refreshUI(){
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.action_view)
    public void delete(){
        if(ShopCart.getInstance().getRemoveCount()==0){
            return;
        }
        CommonDialogFragment dialogFragment=CommonDialogFragment.newInstance(R.layout.dialog_vlone_login,"确定删除已选商品？");
        dialogFragment.setActionListener(new CommonDialogFragment.ActionListener() {
            @Override
            public void doAction() {
                ShopCart shopCart=ShopCart.getInstance();
                List<ShopItem> list=shopCart.getRemoveItems();
                mPresenter.deleteShopItem(list);
                shopCart.removeSelect();
                mAdapter.notifyDataSetChanged();
                if(shopCart.isEmpty()){
                    showEmptyView();
                }
                //ShopCart.getInstance().setRefresh(true);
            }

            @Override
            public void cancel() {}
        });
        dialogFragment.show(getFragmentManager(),"tag");
    }



    @OnClick(R.id.select_all)
    public void selectAll(){
        boolean select=mAllSelectView.isChecked();
        if(select){
            mActionView.setBackgroundResource(R.drawable.solid_black_bg);
            mActionView.setEnabled(true);

        }else {
            mActionView.setBackgroundResource(R.drawable.solid_gray_bg);
            mActionView.setEnabled(false);
        }
        ShopCart.getInstance().removeAll(select);
        refreshUI();
    }


    @Override
    public void checkedChange(boolean isChecked) {
        ShopCart shopCart =ShopCart.getInstance();
        int count=shopCart.getCount();
        if(count!=0&& shopCart.getRemoveCount()==count){
            mAllSelectView.setChecked(true);
        }else {
            mAllSelectView.setChecked(false);

        }
        if(shopCart.getRemoveCount()==0){
            mAllSelectView.setText(getString(R.string.select_all));
            mActionView.setEnabled(false);
            mActionView.setBackgroundResource(R.drawable.solid_gray_bg);
        }else {
            mActionView.setEnabled(true);
            mAllSelectView.setText(String.format(getString(R.string.select_count),
                    ""+ShopCart.getInstance().getRemoveCount()));
            mActionView.setBackgroundResource(R.drawable.solid_black_bg);
        }
    }

    @Override
    public void sizeChange(int position, int newSize) {
        ShopCartActivity.SkuCount skuCount=new ShopCartActivity.SkuCount();
        skuCount.id=ShopCart.getInstance().getItem(position).id;
        skuCount.skuCount=newSize;
        mChangeList.add(skuCount);
    }

    @Override
    public void itemDeleted(int position,boolean showDialog) {
        if(showDialog){
            CommonDialogFragment dialogFragment=CommonDialogFragment.newInstance(R.layout.dialog_vlone_login,"确定删除已选商品？");
            dialogFragment.setActionListener(new CommonDialogFragment.ActionListener() {
                @Override
                public void doAction() {
                    delete(position);
                }
                @Override
                public void cancel() {}
            });
            dialogFragment.show(getFragmentManager(),"tag");

        }else {
            delete(position);
        }
    }

    private void delete(int position){
        ShopCart shopCart=ShopCart.getInstance();
        ShopItem item=shopCart.getItem(position);
        shopCart.delete(position);
        mAdapter.notifyItemRemoved(position);
        mPresenter.deleteShopItem(Arrays.asList(item));
        if(ShopCart.getInstance().isEmpty()){
            showEmptyView();
        }
        //updateStatus();
    }

    public void changeQuantity(){
        if(!mChangeList.isEmpty()){
            String json=new Gson().toJson(mChangeList);
            KLog.json(json);
            mChangeList.clear();
            mPresenter.updateQuantity(json);
        }
    }

    //TODO
    private void showEmptyView(){
        ShopCartActivity cartActivity=(ShopCartActivity)getActivity();
        cartActivity.showEmptyView();
        mViewContainer.setVisibility(View.INVISIBLE);
    }
}
