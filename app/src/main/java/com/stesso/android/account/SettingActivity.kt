package com.stesso.android.account

import android.os.Bundle
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.address.AddressListActivity
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        configTitleView(title_view)
        row6.setOnClickListener{
            openActivity(AddressListActivity::class.java)
        }
    }
}
