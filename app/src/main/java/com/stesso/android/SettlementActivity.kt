package com.stesso.android
import android.os.Bundle
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.lib.SETTLEMENT_ADDRESS
import kotlinx.android.synthetic.main.activity_address_list.*

class SettlementActivity : BaseActivity() {

    private val adapter = MultiTypeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getActivityComponent().inject(this)
        setContentView(R.layout.activity_settlement)
        recycler_view.adapter =adapter


        doHttpRequest(apiService.getAddressList()) {
            adapter.addItem(it?.get(0), SETTLEMENT_ADDRESS)

        }
    }
}
