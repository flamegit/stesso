package com.stesso.android.model

class User {

    var name: String? = null
    var mobile: String? = null
    var gender: Int = 0
    var token: String? = null
        get() {
            if (field == null) {

            }
            return field
        }


}



