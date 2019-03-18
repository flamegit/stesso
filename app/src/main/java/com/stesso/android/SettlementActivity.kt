package com.stesso.android

import android.content.Intent
import android.os.Bundle
import com.stesso.android.address.AddressListActivity
import com.stesso.android.lib.*
import com.stesso.android.model.Account
import com.stesso.android.model.Address
import com.stesso.android.model.EmptyItem
import kotlinx.android.synthetic.main.activity_settlement.*

class SettlementActivity : PayActivity() {

    private val adapter = MultiTypeAdapter()
    private var address: Address? = null
    private val shopCart = Account.shopCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        setContentView(R.layout.activity_settlement)
        recycler_view.adapter = adapter
        adapter.addItems(shopCart?.getSelectItems(), SETTLEMENT_ITEM, true)
        adapter.addItem(shopCart, SETTLEMENT_INFO, true)
        adapter.addItem("pay", SETTLEMENT_PAY, true)
        adapter.setOnItemClick { _, _, _ ->
            val intent = Intent(this@SettlementActivity, AddressListActivity::class.java)
            this@SettlementActivity.startActivityForResult(intent, SELECT_ADDRESS)
        }
        doHttpRequest(apiService.getAddressList()) {
            if (it?.isEmpty() == true) {
                adapter.addTopItem(EmptyItem(), EMPTY_ADDRESS)
            } else {
                address = it?.get(0)
                adapter.addTopItem(address, SETTLEMENT_ADDRESS)
            }
        }
        settlement_view.setOnClickListener {
            val body = mapOf(Pair("addressId", address?.id), Pair("cartId", shopCart?.getIdList()), Pair("message", "dd"))
            doHttpRequest(apiService.submitOrder(body)) {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_ADDRESS) {
            adapter.changeItem(0, data?.getParcelableExtra(KEY_ADDRESS), SETTLEMENT_ADDRESS)
        }
    }
}
