package com.stesso.android

import android.content.Intent
import android.os.Bundle
import com.stesso.android.address.AddressListActivity
import com.stesso.android.lib.*
import com.stesso.android.model.Account
import com.stesso.android.model.Address
import com.stesso.android.model.EmptyItem
import com.stesso.android.shopcart.ShopCartActivity
import com.stesso.android.utils.openActivity
import com.stesso.android.utils.toast
import kotlinx.android.synthetic.main.activity_settlement.*
import kotlinx.android.synthetic.main.activity_settlement.title_view
import okhttp3.OkHttpClient

class SettlementActivity : PayActivity() {

    private val adapter = MultiTypeAdapter()
    private var address: Address? = null
    private val shopCart = Account.shopCart
    private var payType = 0
    private var orderId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getActivityComponent().inject(this)
        setContentView(R.layout.activity_settlement)
        configTitleView(title_view)
        recycler_view.adapter = adapter
        adapter.addItem(EmptyItem(), EMPTY_ADDRESS)
        adapter.addItems(shopCart?.getSelectItems(), SETTLEMENT_ITEM, true)
        adapter.addItem(shopCart, SETTLEMENT_INFO, true)
        adapter.addItem("pay", SETTLEMENT_PAY, true)
        adapter.addItem("footer", FOOTER, true)
        adapter.setOnItemClick { _, _, action ->
            when (action) {
                0, 1 -> {
                    payType = action
                }
                else -> {
                    val intent = Intent(this@SettlementActivity, AddressListActivity::class.java)
                    intent.putExtra(TYPE, 1)
                    this@SettlementActivity.startActivityForResult(intent, SELECT_ADDRESS)
                }
            }
        }
        doHttpRequest(apiService.getAddressList()) {
            if (it == null || it.isEmpty()) {
                //adapter.addTopItem(EmptyItem(), EMPTY_ADDRESS)
            } else {
                address = getDefaultAddress(it)
                adapter.changeItem(0, address, SETTLEMENT_ADDRESS)
            }
        }
        settlement_view.setOnClickListener {
            if (address == null) {
                toast("请选择地址")
                return@setOnClickListener
            }
            if (orderId != -1) {
                if (payType == 0) alipay(orderId) else wechatPay(orderId)
            } else {
                val body = mapOf(Pair("addressId", address?.id), Pair("cartId", shopCart?.getIdList()), Pair("message", "pay"))
                doHttpRequest(apiService.submitOrder(body)) { data ->
                    data?.orderId?.let {
                        ShopCartActivity.reload = true
                        orderId = it
                        if (payType == 0) alipay(it) else wechatPay(it)
                    }
                }
            }
        }

        disposableContainer.add(RxBus.subject.subscribe {
            if (it.code == 0) {
                toast("支付成功")
            } else {
                toast("支付失败")
            }
            ShopCartActivity.reload = true
            this.openActivity(OrderDetailActivity::class.java, ORDER_ID, orderId)
            finish()
        })
    }

    private fun getDefaultAddress(list: List<Address>): Address {

        OkHttpClient
        var default = list[0]
        list.forEach {
            if (it.isDefault) {
                default = it
                return@forEach
            }
        }
        return default
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_ADDRESS) {
            address = data?.getParcelableExtra(KEY_ADDRESS)
            adapter.changeItem(0, address, SETTLEMENT_ADDRESS)
        }
    }
}
