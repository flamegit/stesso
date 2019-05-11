package com.stesso.android

import android.os.Bundle
import com.stesso.android.model.Account
import com.stesso.android.model.SuggestionDTO
import com.stesso.android.utils.toast
import kotlinx.android.synthetic.main.activity_suggestion.*

class SuggestionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestion)
        getActivityComponent().inject(this)

        configTitleView(title_view) {

            if (suggestion_view.text.isNullOrEmpty()) {
                toast("反馈意见不能为空")
                return@configTitleView
            }
            val suggestion = SuggestionDTO(Account.user?.mobile
                    ?: "", 1, suggestion_view.text.toString(),0)
            doHttpRequest(apiService.submitSuggestion(suggestion)) {
                toast("提交成功")
                finish()
            }
        }
    }
}



