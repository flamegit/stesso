package com.stesso.android.account

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.flame.kotlinstudy.utils.createShape
import com.stesso.android.R
import kotlinx.android.synthetic.main.activity_login.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login_view.background = createShape(Color.BLUE,30)


    }
}
