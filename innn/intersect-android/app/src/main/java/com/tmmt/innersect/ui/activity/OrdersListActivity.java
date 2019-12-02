package com.tmmt.innersect.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.mvp.model.OrderDetailInfo;
import com.tmmt.innersect.ui.adapter.OrderListAdapter;
import com.tmmt.innersect.ui.adapter.decoration.SectionDecoration;
import com.tmmt.innersect.ui.fragment.CommonDialogFragment;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.Util;
import com.tmmt.innersect.widget.RefreshHeader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrdersListActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    OrderListAdapter mAdapter;
    @BindView(R.id.action_view)
    TextView mActionView;
    @BindView(R.id.refresh_layout)
    TwinklingRefreshLayout mRefreshLayout;
    @BindView(R.id.loading_view)
    AVLoadingIndicatorView mLoadingView;
    private int mPage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_orders_list;
    }

    @Override
    protected String getTitleString() {
        return getString(R.string.order_list);
    }

    @Override
    protected void initView() {
        mPage=1;
        super.initView();
        AnalyticsUtil.reportEvent(AnalyticsUtil.ORDER_ORDERDETAILS_CLICK);
        mActionView.setText(getString(R.string.service));
        mRecyclerView = findViewById(R.id.recycler_view);
        SectionDecoration sectionDecoration = new SectionDecoration(this, new SectionDecoration.DecorationCallback() {
            @Override
            public boolean isFirstInGroup(int pos) {
                if (pos == 0) {
                    return true;
                }
                String pre = mAdapter.getItem(pos - 1).getDate();
                String curr = mAdapter.getItem(pos).getDate();
                return !pre.equals(curr);
            }
            @Override
            public String getSectionHeader(int pos) {
                return mAdapter.getItem(pos).getDate();
            }
        });

        mAdapter = new OrderListAdapter((int) sectionDecoration.getLeftGap());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(sectionDecoration);
        loadOrderList(false);
        mRefreshLayout.setHeaderView(new RefreshHeader(this));
        mRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                mPage=1;
                loadOrderList(false);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                mPage++;
                loadOrderList(true);
            }
        });

        String dialog=getIntent().getStringExtra(Constants.SHOW_DIALOG);
        if(dialog!=null){
            showDialog();
        }
    }

    @OnClick(R.id.action_view)
    void open(){
        Util.startActivity(this,RefundHistoryActivity.class);
    }

    private void showDialog(){

        CommonDialogFragment dialogFragment=new CommonDialogFragment.Builder()
                .setLayout(R.layout.dialog_pay_success)
                .setTitle("支付成功")
                .setContent("可在订单详情中查询如何取货哦")
                .build();

        dialogFragment.show(getSupportFragmentManager(),"tag");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode,resultCode,data);
    }

    private void loadOrderList(final boolean append) {
        if(!Util.isNetworkAvailable()){
            showNetUnavailable();
            return;
        }
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getOrderList(AccountInfo.getInstance().getUserId(), mPage, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpBean<List<OrderDetailInfo>>>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        if(!append){
                            mLoadingView.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onCompleted() {
                        mLoadingView.setVisibility(View.INVISIBLE);
                        if(append){
                            mRefreshLayout.finishLoadmore();
                        }else {
                            mRefreshLayout.finishRefreshing();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        if(append){
                            mRefreshLayout.finishLoadmore();
                        }else {
                            mRefreshLayout.finishRefreshing();
                            mLoadingView.setVisibility(View.INVISIBLE);
                            showNetUnavailable();
                        }
                        KLog.d(e);

                    }
                    @Override
                    public void onNext(HttpBean<List<OrderDetailInfo>> result) {
                        if(append){
                            if (result.data.size()> 0) {
                                mAdapter.appendItems(result.data);
                            } else {
                                mRefreshLayout.setEnableLoadmore(false);
                            }
                        }else {
                            mRefreshLayout.setEnableLoadmore(true);
                            if (result.data.size()> 0) {
                                mAdapter.addItems(result.data);
                            } else {
                                showEmptyView();
                            }
                        }
                    }
                });
    }

    private  UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
            //Util.startActivity(OrdersListActivity.this,TicketDrawActivity.class);

        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
            KLog.d("plat","platform"+platform);
            //Util.startActivity(OrdersListActivity.this,TicketDrawActivity.class);

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if(t!=null){
                KLog.d("throw","throw:"+t.getMessage());
            }
            //Util.startActivity(OrdersListActivity.this,TicketDrawActivity.class);
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {
           //Util.startActivity(OrdersListActivity.this,TicketDrawActivity.class);
        }
    };

    void share() {
        UMImage umImage=new UMImage(this,R.mipmap.innersect_icon);
        new ShareAction(this)
                .withMedia(umImage)
                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN, SHARE_MEDIA.SINA)
                .setCallback(umShareListener)
                .open();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.release();
    }
}
