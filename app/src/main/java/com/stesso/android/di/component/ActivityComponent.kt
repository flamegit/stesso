package com.stesso.android.di.component

import com.stesso.android.di.module.ActivityModule
import com.stesso.android.di.scope.ActivityScope
import com.stesso.android.*
import com.stesso.android.account.LoginActivity
import com.stesso.android.account.ModifyPasswordActivity
import com.stesso.android.account.RegisterActivity
import com.stesso.android.account.SettingActivity
import com.stesso.android.address.AddAddressActivity
import com.stesso.android.address.AddressListActivity
import com.stesso.android.shopcart.ShopCartActivity
import dagger.Subcomponent

/**
 * Created by flame on 2018/2/1.
 */

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(activity: BaseActivity)

}

