package com.stesso.android

import KEY_ADDRESS
import SELECT_ADDRESS
import android.content.Intent
import android.os.Bundle
import com.stesso.android.address.AddressListActivity
import com.stesso.android.lib.EMPTY_ADDRESS
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.lib.SETTLEMENT_ADDRESS
import com.stesso.android.model.EmptyItem
import kotlinx.android.synthetic.main.activity_address_list.*

class SettlementActivity : BaseActivity() {

    private val adapter = MultiTypeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getActivityComponent().inject(this)
        setContentView(R.layout.activity_settlement)
        recycler_view.adapter = adapter

        adapter.setOnItemClick { _, _ ->
            val intent = Intent(this@SettlementActivity, AddressListActivity::class.java)
            this@SettlementActivity.startActivityForResult(intent, SELECT_ADDRESS)
        }

        doHttpRequest(apiService.getAddressList()) {
            if (it?.isEmpty() == true) {
                adapter.addItem(EmptyItem(), EMPTY_ADDRESS)
            } else {
                adapter.addItem(it?.get(0), SETTLEMENT_ADDRESS)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_ADDRESS) {
            adapter.changeItem(0,data?.getParcelableExtra(KEY_ADDRESS),SELECT_ADDRESS)
        }
    }
}
