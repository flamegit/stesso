package com.stesso.android.address

import com.stesso.android.ADDRESS_ID
import android.os.Bundle
import chihane.jdaddressselector.BottomDialog
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.lib.NetAddressProvider
import com.stesso.android.model.Address
import kotlinx.android.synthetic.main.activity_add_address.*
import org.json.JSONObject

class AddAddressActivity : BaseActivity() {
    private var address = Address()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        setContentView(R.layout.activity_add_address)
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
        delete_address.setOnClickListener {
            if (id == -1) {
                return@setOnClickListener
            }
            val body = JSONObject(mapOf(Pair("id", id)))
            doHttpRequest(apiService.deleteAddress(body)){
                onBackPressed()
            }
        }
        city_layout.setOnClickListener {
            showDialog()
        }
        configTitleView(title_view) {
            val name = name_view.text.toString()
            val mobile = tel_view.text.toString()
            val street = detail_view.text.toString()
            address.name = name
            address.mobile = mobile
            address.address = street
            doHttpRequest(apiService.saveAddress(address)) {
                onBackPressed()
            }
        }
    }

    private fun showDialog() {
        val dialog = BottomDialog(this)
        dialog.setAddressProvider(NetAddressProvider(apiService))
        dialog.setOnAddressSelectedListener { province, city, county, _ ->
            address.provinceId = province.id
            address.cityId = city.id
            address.areaId = county.id
            city_view.text = "${province.name} ${city.name} ${county.name}"
            dialog.dismiss()
        }
        dialog.show()
    }
}