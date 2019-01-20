package com.stesso.android.account

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.stesso.android.R
import com.stesso.android.address.AddAddressActivity
import com.stesso.android.address.AddressListActivity
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        row6.setOnClickListener{
            openActivity(AddressListActivity::class.java)
        }
    }
}
