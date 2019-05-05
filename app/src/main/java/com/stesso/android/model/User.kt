package com.stesso.android.model

class User {

    var nickname: String? = null
    var username: String? = null
    var mobile: String? = null
    var gender: Int = 0
    var addTime: String? = null
    var avatar: String? = null
    var birthday:String? = null


    fun getGender(): String {
        return if (gender == 0) "男" else "女"
    }
}



