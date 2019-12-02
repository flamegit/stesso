package com.tmmt.innersect.ui.adapter.viewholder;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.Address;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by flame on 2017/4/12.
 */

public class AddressViewHolder extends BaseViewHolder<Address> {

    @Nullable
    @BindView(R.id.delete_view)
    public TextView deleteView;

    @BindView(R.id.default_view)
    public CheckBox checkBox;

    @BindView(R.id.name_view)
    TextView nameView;

    @BindView(R.id.tel_view)
    TextView telView;

    @BindView(R.id.address_view)
    TextView addressView;
    @BindView(R.id.edit_view)
    public View editView;

    public SwipeHorizontalMenuLayout swipeLayout;

    public AddressViewHolder(View view){
        super(view);
        ButterKnife.bind(this,view);
        swipeLayout=(SwipeHorizontalMenuLayout)view;
    }

    @Override
    public void bindViewHolder(final Address address, int position) {
        checkBox.setChecked(address.isDefault());
        nameView.setText(address.getName());
        telView.setText(address.getTel());
        addressView.setText(address.getDetail());
    }
}
