package com.tmmt.innersect.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tmmt.innersect.BuildConfig;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.ChanceDesc;
import com.tmmt.innersect.ui.adapter.AdvancedAdapter;
import com.tmmt.innersect.ui.adapter.decoration.LinearOffsetItemDecoration;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.utils.Util;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;

import butterknife.BindView;

public class CommonActivity extends  BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    AdvancedAdapter<ChanceDesc> mAdapter;

    private static final int [] ICONS={
            R.mipmap.open_app,
            R.mipmap.consume_icon,
            R.mipmap.broadcast,
            R.mipmap.evaluation_icon,
    };

    private static final int [] ITEMS={
            R.string.open_app,
            R.string.consume,
            R.string.broadcast,
            R.string.evaluation,
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_common;
    }

    @Override
    protected String getTitleString() {
        return "获取更多抽奖机会";
    }

    private void doShare() {
        AnalyticsUtil.reportEvent(AnalyticsUtil.MY_SHARE);
        UMWeb umWeb = new UMWeb(BuildConfig.ENDURL+"share");
        umWeb.setTitle("加入INNERSECT，顶尖正价潮货抢先GO");
        umWeb.setDescription("最值得期待潮流APP，汇集全球顶级潮流品牌");
        umWeb.setThumb(new UMImage(this, R.mipmap.app_icon));
        new ShareAction(this)
                .withText("VLONE亚洲首发，加入innersect顶尖正价潮货抢先GO！")
                .withMedia(umWeb)
                .setDisplayList(Util.getDisplayList(this))
                .setCallback(umShareListener)
                .open();
    }

    private static UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {}
        @Override
        public void onResult(SHARE_MEDIA platform) {
            KLog.d("plat", "platform" + platform);

            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).reportAction(3).enqueue(new NetCallback<String>() {
                @Override
                public void onSucceed(String data) {}
            });
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t != null) {
                KLog.d("throw", "throw:" + t.getMessage());
            }
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {}
    };

    @Override
    protected void onResume() {
        super.onResume();
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getChanceInfo(AccountInfo.getInstance().getUserId())
                .enqueue(new NetCallback<List<ChanceDesc>>() {
                    @Override
                    public void onSucceed(List<ChanceDesc> data) {
                        for(int i=0;i<data.size();i++){
                            data.get(i).resId=ICONS[i];
                            data.get(i).name=getString(ITEMS[i]);
                        }
                        mAdapter.addItems(data);
                    }
                });
    }

    @Override
    protected void initView() {
        super.initView();
        mAdapter=new AdvancedAdapter<>(R.layout.viewholder_lottery_chance,(holder,position,data)->{
            holder.<ImageView>get(R.id.icon_view).setImageResource(data.resId);
            holder.<TextView>get(R.id.title_view).setText(data.name);
            holder.<TextView>get(R.id.desc_view).setText(data.getDesc(position));
            holder.get(R.id.action_view).setVisibility(position==0? View.GONE:View.VISIBLE);
            holder.itemView.setOnClickListener(v -> {
                switch (position){
                    case 1:
                        Util.startActivity(CommonActivity.this,HomeActivity.class);
                        break;
                    case 2:
                        doShare();
                        break;
                    case 3:
                        SystemUtil.goToMarket(CommonActivity.this,"com.tmmt.innersect");
                        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).reportAction(4).enqueue(new NetCallback<String>() {
                            @Override
                            public void onSucceed(String data) {}
                        });
                        break;
                }
            });
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LinearOffsetItemDecoration(16));
        recyclerView.setAdapter(mAdapter);
    }
}
