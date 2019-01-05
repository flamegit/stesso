package com.stesso.android.account

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.flame.kotlinstudy.utils.convertToJson
import com.example.flame.kotlinstudy.utils.createShape
import com.stesso.android.App
import com.stesso.android.R
import com.stesso.android.datasource.net.ApiService
import com.stesso.android.di.module.ActivityModule
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import org.json.JSONStringer
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var api: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as App
        app.component.plus(ActivityModule(this)).inject(this)
        setContentView(R.layout.activity_login)
        login_view.background = createShape(Color.BLUE,30)
        login_view.setOnClickListener {

        }
        val json = convertToJson(arrayListOf(Pair("moblie","dd"), Pair("",""), Pair("","")))
        val body =RequestBody.create(MediaType.parse("application/json"),json)
        api.register(body).subscribe {

        }

    }
}
