package com.tmmt.innersect.ui.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.tmmt.innersect.R;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Message;
import com.tmmt.innersect.ui.adapter.MessageAdapter;

import butterknife.BindView;

public class MessageActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    MessageAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected String getTitleString() {
        return "消息中心";
    }

    @Override
    protected void initView() {
        super.initView();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter=new MessageAdapter();

        mRecyclerView.setAdapter(mAdapter);

        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getMessage().enqueue(new NetCallback<Message>() {
            @Override
            public void onSucceed(Message data) {
                mAdapter.fillContent(data);
            }
        });
    }
}
