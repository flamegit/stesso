package com.stesso.android.address

import KEY_ADDRESS
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.lib.ADDRESS_TYPE
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.model.Address
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.activity_address_list.*

class AddressListActivity : BaseActivity() {

    private val adapter = MultiTypeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        getActivityComponent().inject(this)
        recycler_view.adapter = adapter
        recycler_view.layoutManager = LinearLayoutManager(this)

        recycler_view.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL))
        doHttpRequest(apiService.getAddressList()) {
            adapter.addItems(it, ADDRESS_TYPE)
        }

        adapter.setOnItemClick { _,data ->
            if(data is Address){
                setResult(Activity.RESULT_OK,Intent().putExtra(KEY_ADDRESS,data))
                finish()
            }
        }

        configTitleView(title_view) {
            openActivity(AddAddressActivity::class.java)
        }

        //doHttpRequest()
    }
}
