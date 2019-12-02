package com.tmmt.innersect.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.ShopCart;
import com.tmmt.innersect.mvp.model.ShopItem;
import com.tmmt.innersect.mvp.presenter.ShopCartPresenter;
import com.tmmt.innersect.mvp.view.ExtraView;
import com.tmmt.innersect.ui.activity.SettlementActivity;
import com.tmmt.innersect.ui.activity.ShopCartActivity;
import com.tmmt.innersect.ui.adapter.Report;
import com.tmmt.innersect.ui.adapter.ShopCartAdapter;
import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;
import com.tmmt.innersect.utils.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by flame on 2017/4/12.
 */

public class ShopCartFragment extends BaseFragment implements Report,ExtraView {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    ShopCartAdapter mAdapter;
    @BindView(R.id.action_view)
    TextView mActionView;
    @BindView(R.id.price_view)
    TextView mPriceView;
    @BindView(R.id.select_all)
    CheckBox mAllSelectView;
    ShopCartPresenter mPresenter;
    @BindView(R.id.bottom_bar_container)
    View mViewContainer;
    @BindView(R.id.head_desc)
    TextView mHeadDesc;
    @BindView(R.id.head_action)
    TextView mHeadAction;
    @BindView(R.id.head_layout)
    View mHeadLayout;
    @BindView(R.id.loading_view)
    View mLoadingView;

    PopupWindow mPopWindow;

    private List<ShopCartActivity.SkuCount> mChangeList;

    @Override
    int getLayout() {
        return R.layout.fragment_shop_cart;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mChangeList=new ArrayList<>();
        mPresenter=new ShopCartPresenter();
        mPresenter.attachView(this);
        ButterKnife.bind(this,view);
        mAdapter=new ShopCartAdapter(false);
        mAdapter.setReport(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        mLoadingView.setVisibility(View.VISIBLE);
        mPresenter.loadShopCart();
    }

    @OnClick(R.id.select_all)
    public void selectAll(){
        ShopCart.getInstance().selectAll(mAllSelectView.isChecked());
        refreshUI();
        updateStatus();
    }

    private void updateStatus(){
        ShopCart shopCart=ShopCart.getInstance();
        int count=shopCart.getAvailabeCount();

        if(count!=0 && shopCart.getSelectCount()==count){
            mAllSelectView.setChecked(true);
        }else {
            mAllSelectView.setChecked(false);
        }
        BigDecimal price=ShopCart.getInstance().getSelectPrice();
        mPriceView.setText("￥"+price);
        if(ShopCart.getInstance().getSelectCount()==0){
            mActionView.setEnabled(false);
            mAllSelectView.setText(getString(R.string.select_all));
            mActionView.setBackgroundResource(R.drawable.solid_gray_bg);
        }else {
            mActionView.setEnabled(true);
            mActionView.setBackgroundResource(R.drawable.common_yellow_bg);
            mAllSelectView.setText(String.format(getString(R.string.select_count),
                    ""+ShopCart.getInstance().getSelectCount()));
        }
    }

    public void refreshUI(){
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.action_view)
    public void openPaymentActivity()
    {
        ShopCart shopCart=ShopCart.getInstance();
        showChoseWindow(shopCart.getStatus());
        //Util.startActivity(getContext(),SettlementActivity.class);
        //SettlementActivity.isFromCart=true;
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
        //mAdapter.notifyItemRemoved(position);
        mAdapter.notifyDataSetChanged();
        mPresenter.deleteShopItem(Arrays.asList(item));
        if(ShopCart.getInstance().isEmpty()){
            showEmptyView();
        }
        updateStatus();
    }

    @Override
    public void checkedChange(boolean isChecked) {
        updateStatus();
    }

    @Override
    public void sizeChange(int position, int newSize) {
        updateStatus();
        ShopCartActivity.SkuCount skuCount=new ShopCartActivity.SkuCount();
        skuCount.id=ShopCart.getInstance().getItem(position).id;
        skuCount.skuCount=newSize;
        mChangeList.add(skuCount);
    }

    @Override
    public void onSuccess(Object data) {

        mLoadingView.setVisibility(View.INVISIBLE);
        ShopCart shopCart=ShopCart.getInstance();
        mHeadDesc.setText(shopCart.headDesc);
        mHeadAction.setText(shopCart.headText);
        mHeadLayout.setOnClickListener(v-> {
            Util.openTarget(getContext(),shopCart.headSchema,"");
        });

        if(shopCart.isEmpty()){
            showEmptyView();
            mHeadLayout.setVisibility(View.GONE);
        }else {
            mAdapter.setContent(shopCart.getContent());
            updateStatus();
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(int code) {
        mLoadingView.setVisibility(View.INVISIBLE);
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


    private void showChoseWindow(int style){

        String shop=ShopCart.getInstance().getShop();
        if(style==1){
            SettlementActivity.start(getContext(),shop);
            return;
        }else if(style==2){
            Util.startActivity(getContext(),SettlementActivity.class);
            return;
        }

        View view;
        if(style==0){
            view= LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_separate, null);
            View close=view.findViewById(R.id.close_view);

            view.findViewById(R.id.transport_view).setOnClickListener(v -> mPopWindow.dismiss());

            close.setOnClickListener(v -> mPopWindow.dismiss());
        }else {
            view= LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_cart_chose, null);
            View close=view.findViewById(R.id.close_view);
            close.setOnClickListener(v -> mPopWindow.dismiss());
            TextView style1=view.findViewById(R.id.style_view1);
            TextView style2=view.findViewById(R.id.style_view2);
            TextView hintView1=view.findViewById(R.id.hint_view1);
            TextView hintView2=view.findViewById(R.id.hint_view2);
            ImageView imageView1=view.findViewById(R.id.shop_view);
            ImageView imageView2=view.findViewById(R.id.transport_view);
            String str1,str2,hint1,hint2;
            int res1,res2;
            switch (style){
                case 3:
                    str1="门店自提";
                    str2="邮寄给我";
                    hint1="立即提货免邮费";
                    hint2="一个工作日内发货";
                    res1=R.mipmap.house;
                    res2=R.mipmap.car;

                    style1.setOnClickListener(v -> {
                        SettlementActivity.start(getContext(),shop);
                        mPopWindow.dismiss();
                    });

                    style2.setOnClickListener(v -> {
                        Util.startActivity(getContext(),SettlementActivity.class);
                        mPopWindow.dismiss();
                    });

                    imageView1.setOnClickListener(v -> {
                        SettlementActivity.start(getContext(),shop);
                        mPopWindow.dismiss();
                    });
                    imageView2.setOnClickListener(v -> {
                        Util.startActivity(getContext(),SettlementActivity.class);
                        mPopWindow.dismiss();
                    });

                    break;
                case 5:
                    str1="分开结算";
                    str2="全部自提";
                    hint1="如需部分邮寄请分开结算";
                    hint2="立即提货免邮费";
                    res1=R.mipmap.separate;
                    res2=R.mipmap.shop_bag;

                    imageView1.setOnClickListener(v -> mPopWindow.dismiss());
                    imageView2.setOnClickListener(v -> {
                        SettlementActivity.start(getContext(),shop);
                        mPopWindow.dismiss();
                    });

                    style1.setOnClickListener(v -> mPopWindow.dismiss());
                    style2.setOnClickListener(v -> {
                        SettlementActivity.start(getContext(),shop);
                        mPopWindow.dismiss();
                    });

                    break;
                case 6:
                    str1="分开结算";
                    str2="全部邮寄";
                    hint1="如需部分自提请分开结算";
                    hint2="一个工作日内发货";
                    res1=R.mipmap.separate;
                    res2=R.mipmap.car;

                    style1.setOnClickListener(v -> mPopWindow.dismiss());
                    style2.setOnClickListener(v -> {
                                Util.startActivity(getContext(), SettlementActivity.class);
                                mPopWindow.dismiss();
                            });

                    imageView1.setOnClickListener(v -> mPopWindow.dismiss());
                    imageView2.setOnClickListener(v -> {
                        Util.startActivity(getContext(),SettlementActivity.class);
                        mPopWindow.dismiss();
                    });

                    break;
                default:
                    str1="门店自提";
                    str2="邮寄给我";
                    hint1="立即提货免邮费";
                    hint2="一个工作日内发货";
                    res1=R.mipmap.house;
                    res2=R.mipmap.car;
            }
            style1.setText(str1);
            style2.setText(str2);
            hintView1.setText(hint1);
            hintView2.setText(hint2);
            imageView1.setImageResource(res1);
            imageView2.setImageResource(res2);
        }

        mPopWindow= new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setFocusable(true);// 取得焦点
        mPopWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        backgroundAlpha(0.4f);
        mPopWindow.setOnDismissListener(() -> backgroundAlpha(1));
        mPopWindow.setAnimationStyle(R.style.pop_window_anim);
        mPopWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);

    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
    }

}
