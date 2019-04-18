package com.stesso.android.model

import com.google.gson.Gson
import com.stesso.android.App
import com.stesso.android.utils.*

object Account {
    var user: User? = null
        get() {
            if (field == null) {
                field = Gson().fromJson(App.instance().get(USER_INFO), User::class.java)
            }
            return field
        }
        set(value) {
            field = value
            if (value == null) {
                App.instance().clear()
            } else {
                App.instance().put(USER_INFO, Gson().toJson(value))
            }

        }
    var shopCart: ShopcartDTO? = null
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
            } else {
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



