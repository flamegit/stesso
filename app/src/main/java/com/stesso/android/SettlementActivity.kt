package com.stesso.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.stesso.android.address.AddressListActivity
import com.stesso.android.lib.*
import com.stesso.android.model.Account
import com.stesso.android.model.Address
import com.stesso.android.model.EmptyAddress
import com.stesso.android.utils.toast
import kotlinx.android.synthetic.main.activity_settlement.*
import org.json.JSONObject

class SettlementActivity : PayActivity() {

    private val adapter = MultiTypeAdapter()
    private var address: Address? = null
    private val shopCart = Account.shopCart
    private var payType = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        setContentView(R.layout.activity_settlement)
        recycler_view.adapter = adapter
        adapter.addItem(EmptyAddress(), EMPTY_ADDRESS)
        adapter.addItems(shopCart?.getSelectItems(), SETTLEMENT_ITEM, true)
        adapter.addItem(shopCart, SETTLEMENT_INFO, true)
        adapter.addItem("pay", SETTLEMENT_PAY, true)
        adapter.setOnItemClick { _, _, action ->
            when (action) {
                0, 1 -> {
                    payType = action
                }
                else -> {
                    val intent = Intent(this@SettlementActivity, AddressListActivity::class.java)
                    this@SettlementActivity.startActivityForResult(intent, SELECT_ADDRESS)
                }
            }

        }
        doHttpRequest(apiService.getAddressList()) {
            if (it?.isEmpty() == true) {
                //adapter.addTopItem(EmptyAddress(), EMPTY_ADDRESS)
            } else {
                address = it?.get(0)
                adapter.changeItem(0,address, SETTLEMENT_ADDRESS)
            }
        }
        settlement_view.setOnClickListener {
            if (address == null) {
                toast("请选择地址")
                return@setOnClickListener
            }
            val body = mapOf(Pair("addressId", address?.id), Pair("cartId", shopCart?.getIdList()), Pair("message", "dd"))
            doHttpRequest(apiService.submitOrder(body)) {
                Log.d("STESSO",it)
                val jsonObject = JSONObject(it)
                val orderId = jsonObject.optInt("orderId")
                if (payType == 0) alipay(orderId) else wechatPay(orderId)
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
