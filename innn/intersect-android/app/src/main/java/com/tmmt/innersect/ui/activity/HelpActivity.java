package com.tmmt.innersect.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.AfterInfo;
import com.tmmt.innersect.ui.adapter.AdvancedAdapter;
import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;
import com.tmmt.innersect.utils.Util;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by flame on 2017/4/19.
 */

public class HelpActivity extends BaseActivity {

    AdvancedAdapter<AfterInfo.Content> mAdapter;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    AfterInfo mAfterInfo;

    @Override
    protected String getTitleString() {
        return getString(R.string.buy_hint);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help;
    }

    public void initView(){
        mAdapter=new AdvancedAdapter<>(R.layout.viewholder_help,(holder, position, data) -> {
            TextView titleView=holder.get(R.id.title_view);
            titleView.setText(data.title);
            holder.itemView.setOnClickListener(v -> {
                Intent intent=new Intent(HelpActivity.this,HelpDetailActivity.class);
                intent.putExtra(Constants.TITLE,data.title);
                intent.putParcelableArrayListExtra(Constants.PROBLEM_INFO,data.contents);
                startActivity(intent);
            });
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getSaleAfterInfo("v1").enqueue(new NetCallback<AfterInfo>() {
            @Override
            public void onSucceed(AfterInfo data) {
                mAfterInfo=data;
                mAdapter.addItems(data.contents);
            }
        });
    }

    @OnClick(R.id.contact_view)
    void contact(){
        Util.startMoor(this);
//        if(mAfterInfo!=null){
//            Intent intent=new Intent(Intent.ACTION_DIAL);
//            intent.setData(Uri.parse("tel:"+mAfterInfo.phone));
//            startActivity(intent);
//        }
    }
}
