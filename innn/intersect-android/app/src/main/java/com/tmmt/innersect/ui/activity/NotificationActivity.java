package com.tmmt.innersect.ui.activity;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Notification;
import com.tmmt.innersect.mvp.model.NotificationInfo;
import com.tmmt.innersect.ui.adapter.AdvancedAdapter;
import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;
import com.tmmt.innersect.utils.Util;
import butterknife.BindView;

public class NotificationActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_view)
    View mEmptyView;

    AdvancedAdapter<Notification> mAdapter;

    @Override
    protected String getTitleString() {
        return "服务通知";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notification;
    }

    @Override
    protected void initView() {
        super.initView();
        mAdapter=new AdvancedAdapter<>(R.layout.viewholder_notification, new AdvancedAdapter.BindViewHolder<Notification>() {
            @Override
            public void onBind(CommonViewHolder holder, int position, Notification data) {
                Context context=holder.itemView.getContext();
                holder.<TextView>get(R.id.title_view).setText(data.title);
                holder.<TextView>get(R.id.message_view).setText(data.msg);
                holder.<TextView>get(R.id.count_view).setText(data.getCount());
                holder.<TextView>get(R.id.time_view).setText(data.getTime());
                Util.fillImage(context,data.getThumbnail(),holder.get(R.id.icon_view));
                holder.itemView.setOnClickListener(v -> data.jump(context));
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,true));
        mRecyclerView.setAdapter(mAdapter);
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getServiceMessage(0).enqueue(new NetCallback<NotificationInfo>() {
            @Override
            public void onSucceed(NotificationInfo data) {
                if(data.sns==null){
                    mEmptyView.setVisibility(View.VISIBLE);
                }
                mAdapter.addItems(data.sns);
            }
        });
    }
}
