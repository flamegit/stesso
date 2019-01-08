package com.stesso.android.model

import com.example.flame.kotlinstudy.utils.TOKEN
import com.example.flame.kotlinstudy.utils.clear
import com.example.flame.kotlinstudy.utils.get
import com.example.flame.kotlinstudy.utils.put
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



