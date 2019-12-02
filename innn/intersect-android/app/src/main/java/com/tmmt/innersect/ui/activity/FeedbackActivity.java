package com.tmmt.innersect.ui.activity;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.mvp.model.Status;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.utils.Util;

import org.json.JSONException;
import org.json.JSONStringer;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by flame on 2017/4/19.
 */

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.action_view)
    TextView mActionView;
    @BindView(R.id.feedback_view)
    EditText mFeedbackView;

    @BindView(R.id.contact_view)
    EditText mContactView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected String getTitleString() {
        return getString(R.string.suggestion);
    }
    @Override
    protected void initView() {
        super.initView();
        AnalyticsUtil.reportEvent(AnalyticsUtil.SETTING_FEEDBACK);
        mActionView.setText(getString(R.string.post));
    }
    @OnClick(R.id.action_view)
    void feedback(){
        AnalyticsUtil.reportEvent(AnalyticsUtil.SETTING_FEEDBACK_SUBMIT);
        String feedback=mFeedbackView.getText().toString();

        if(feedback.isEmpty()){
            Toast.makeText(this,getString(R.string.feed_warm),Toast.LENGTH_SHORT).show();
            return;
        }
        String contact=mContactView.getText().toString();
        postFeedback(feedback,contact);

    }

    public void postFeedback(String content,String contact) {
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("userId").value(AccountInfo.getInstance().getUserId())
                    .key("feedbackContent").value(content)
                    .key("userContact").value(contact)
                    .endObject();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).feedback(body).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                KLog.d(response.body().code);
                SystemUtil.reportServerError(Util.getString(R.string.commit_success));
                finish();
            }
            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                SystemUtil.reportServerError(Util.getString(R.string.commit_failed));
                KLog.d(t);
            }
        });
    }
}


