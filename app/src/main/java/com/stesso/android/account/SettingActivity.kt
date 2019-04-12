package com.stesso.android.account

import android.os.Bundle
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.SuggestionActivity
import com.stesso.android.address.AddressListActivity
import com.stesso.android.model.Account
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {

    val user = Account.user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        configTitleView(title_view)
        row5.setOnClickListener {
            openActivity(AddressListActivity::class.java)
        }

        row1_text.text = user?.nicknamne
        row2_text.text = user?.addTime
        row3_text.text = user?.getGender()
        row4_text.text = user?.mobile?.replaceRange(1,4,"*")

        row8.setOnClickListener {
            openActivity(SuggestionActivity::class.java)
        }

        logout.setOnClickListener { Account.logout() }
    }
}
