package com.stesso.android.address

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.lib.ADDRESS_TYPE
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.activity_address_list.*

class AddressListActivity : BaseActivity() {

    private val adapter = MultiTypeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        getActivityComponent().inject(this)
        recycler_view.adapter =adapter
        recycler_view.layoutManager = LinearLayoutManager(this)
        doHttpRequest(apiService.getAddressList()){
            adapter.addItems(it, ADDRESS_TYPE)
        }

        action_view.setOnClickListener {
            openActivity(AddAddressActivity::class.java)
        }
        //doHttpRequest()
    }
}
