package com.stesso.android.address

import ADDRESS_ID
import android.os.Bundle
import chihane.jdaddressselector.BottomDialog
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.lib.NetAddressProvider
import com.stesso.android.model.Address
import kotlinx.android.synthetic.main.fragment_add_address.*


class AddAddressActivity : BaseActivity() {
    private var address = Address()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        setContentView(R.layout.fragment_add_address)
        val id = intent.getIntExtra(ADDRESS_ID, -1)
        if (id != -1) {
            doHttpRequest(apiService.getAddressDetail(id)) {
                if (it != null) {
                    address = it
                    name_view.setText(address.name)
                    tel_view.setText(address.mobile)
                    detail_view.setText(address.address)
                }
            }
        }

        select_address.setOnClickListener {
            showDialog()
        }

        configTitleView(title_view) {
            val name = name_view.text.toString()
            val mobile = tel_view.text.toString()
            val street = detail_view.text.toString()
            address.name = name
            address.mobile = mobile
            address.address = street
            doHttpRequest(apiService.saveAddress(address)) {}
        }

    }

    private fun showDialog() {
        val dialog = BottomDialog(this)
        dialog.setAddressProvider(NetAddressProvider(apiService))
        dialog.setOnAddressSelectedListener { province, city, county, _ ->
            address.provinceId = province.id
            address.cityId = city.id
            address.areaId = county.id
            dialog.dismiss()
        }
        dialog.show()
    }
}