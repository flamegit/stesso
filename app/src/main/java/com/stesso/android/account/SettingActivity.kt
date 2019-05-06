package com.stesso.android.account

import android.animation.ValueAnimator
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import com.stesso.android.*
import com.stesso.android.address.AddressListActivity
import com.stesso.android.model.Account
import com.stesso.android.utils.clearCache
import com.stesso.android.utils.getAppVersion
import com.stesso.android.utils.getCacheSize
import com.stesso.android.utils.openActivity
import kotlinx.android.synthetic.main.activity_setting.*
import java.util.*


class SettingActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        getActivityComponent().inject(this)
        configTitleView(title_view)
        row5.setOnClickListener {
            AddressListActivity.reload = true
            openActivity(AddressListActivity::class.java)
        }
        val user = Account.user
        row1_text.text = user?.nickname
        row2_text.text = user?.birthday
        row3_text.text = user?.getGender()
        row9_text.text = String.format("%.2f MB", getCacheSize(this))

        row9.setOnClickListener {
            val start = getCacheSize(this).toFloat()
            clearCache(this)
            val animator = ValueAnimator.ofFloat(start, 0F)
            animator.addUpdateListener { animation ->  row9_text.text =String.format("%.2f MB", animation.animatedValue) }
            animator.setDuration(500).start()

        }
        row4_text.text = user?.mobile?.replaceRange(3, 7, "****")
        row8.setOnClickListener {
            openActivity(SuggestionActivity::class.java)
        }

        row1.setOnClickListener {
            openActivity(ModifyNameActivity::class.java)
        }

        row2.setOnClickListener {
            //val dialog = BottomSheetDialog()
            val ca = Calendar.getInstance()
            val y = ca.get(Calendar.YEAR)
            val m = ca.get(Calendar.MONTH)
            val d = ca.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(this,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val birthday = "$year-$month-$dayOfMonth"
                        row2_text.text = birthday
                        val body = mapOf(Pair("nickName", user?.nickname
                                ?: ""), Pair("gender", user?.gender), Pair("birthday", birthday))
                        doHttpRequest(apiService.updateUserInfo(body)) {
                            Account.user = it
                        }
                    },
                    y, m, d)
            datePickerDialog.show()
        }

        row3.setOnClickListener {
            val items = arrayOf("男", "女")
            AlertDialog.Builder(this).setTitle("性别")
                    .setItems(items) { _, which ->
                        row3_text.text = items[which]
                        val body = mapOf(Pair("nickName", user?.nickname
                                ?: ""), Pair("gender", which), Pair("birthday", user?.birthday
                                ?: ""))
                        doHttpRequest(apiService.updateUserInfo(body)) {
                            Account.user = it
                        }
                    }.setNegativeButton("取消") { _, _ ->
                    }.create().show()
        }

        row10_text.text = "V${this.getAppVersion()}"

        logout.setOnClickListener {
            AlertDialog.Builder(this).setTitle("退出当前账号")
                    .setMessage("确定退出当前账号？")
                    .setNegativeButton("取消") { dialog, _ ->
                        dialog.dismiss()
                    }.setPositiveButton("确定") { dialog, _ ->
                        dialog.dismiss()
                        Account.logout()
                        openActivity(MainActivity::class.java)
                    }.show()

        }
    }

    override fun onResume() {
        super.onResume()
        row1_text.text = Account.user?.nickname
    }
}
