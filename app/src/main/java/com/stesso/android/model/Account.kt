package com.stesso.android.model

import com.stesso.android.utils.TOKEN
import com.stesso.android.utils.clear
import com.stesso.android.utils.get
import com.stesso.android.utils.put
import com.stesso.android.App

object Account {

    var user: User? = null

    var token: String? = null
        get() {
            if (field == null) {
                field = App.instance().get(TOKEN)
            }
            return field
        }
        set(value) {
            field = value
            if (value == null) {
                App.instance().clear()
            }else{
                App.instance().put(TOKEN, value)
            }

        }

    fun isLogin(): Boolean {
        return !token.isNullOrEmpty()
    }

    fun logout() {
        token = null
    }


}



