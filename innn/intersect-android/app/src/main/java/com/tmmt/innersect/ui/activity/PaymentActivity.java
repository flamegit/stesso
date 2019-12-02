//package com.tmmt.innersect.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.mvp.model.Address;
//import com.tmmt.innersect.mvp.model.ShopCart;
//import com.tmmt.innersect.mvp.model.ShopItem;
//import com.tmmt.innersect.ui.adapter.CommonAdapter;
//import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;
//
//import com.tmmt.innersect.utils.Util;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class PaymentActivity extends AppCompatActivity {
//
//    @BindView(R.id.recycler_view)
//    RecyclerView mRecyclerView;
//
//    @BindView(R.id.name_view)
//    TextView mNameView;
//
//    @BindView(R.id.tel_view)
//    TextView mTelView;
//
//    @BindView(R.id.address_view)
//    TextView mAddressView;
//
//    ExtendAdapter<ShopItem> mAdapter;
//
//    @BindView(R.id.add_address)
//    View mAddAddressView;
//
//    @BindView(R.id.address_container)
//    View mAddressContainer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payment);
//        ButterKnife.bind(this);
//        mAdapter = new ExtendAdapter<>(CommonAdapter.SHOP_BROWSE_TYPE, R.layout.viewholder_commodity_order, R.layout.viewholder_order_price);
//        mAdapter.addItems(ShopCart.getInstance().getSelect());
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
//        mRecyclerView.setNestedScrollingEnabled(false);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            Address address = data.getParcelableExtra(AddressActivity.ADDRESS_KEY);
//            if (address != null) {
//                mAddressContainer.setVisibility(View.VISIBLE);
//                mAddAddressView.setVisibility(View.GONE);
//                mNameView.setText(address.getName());
//                mTelView.setText(address.getTel());
//                mAddressView.setText(address.getDetail());
//            } else {
//                Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    @OnClick(R.id.add_address)
//    public void selectAddress() {
//        Intent intent = new Intent(this, AddressActivity.class);
//        startActivityForResult(intent, 0);
//    }
//
//    @OnClick(R.id.action_view)
//    public void openPayment(){
//        Util.startActivity(this,SelectPaymentActivity.class);
//    }
//}
