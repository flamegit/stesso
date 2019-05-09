package com.stesso.android.address

import com.stesso.android.KEY_ADDRESS
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.stesso.android.BaseActivity
import com.stesso.android.R
import com.stesso.android.TYPE
import com.stesso.android.lib.ADDRESS_TYPE
import com.stesso.android.lib.DividerItemDecoration
import com.stesso.android.lib.MultiTypeAdapter
import com.stesso.android.model.Address
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.activity_address_list.*

class AddressListActivity : BaseActivity() {

    private val adapter = MultiTypeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        val type = intent.getIntExtra(TYPE, 0)
        getActivityComponent().inject(this)
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL, false))

        if (type == 1) {
            adapter.setOnItemClick { _, data, _ ->
                if (data is Address) {
                    setResult(Activity.RESULT_OK, Intent().putExtra(KEY_ADDRESS, data))
                    finish()
                }
            }
        }

        configTitleView(title_view) {
            openActivity(AddAddressActivity::class.java)
        }
        loadData()
    }

    override fun onResume() {
        super.onResume()
        if (reload) {
            loadData()
        }
    }

    fun loadData() {
        doHttpRequest(apiService.getAddressList()) {
            reload = false
            adapter.addItems(it, ADDRESS_TYPE)
        }
    }

    companion object {
        var reload = false
    }
}
