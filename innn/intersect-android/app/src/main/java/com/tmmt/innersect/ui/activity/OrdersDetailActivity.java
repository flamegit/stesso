package com.tmmt.innersect.ui.activity;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.Commodity;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.mvp.model.LinkInfo;
import com.tmmt.innersect.mvp.model.OrderDetailInfo;
import com.tmmt.innersect.ui.adapter.OrderDetailAdapter;
import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;
import com.tmmt.innersect.ui.adapter.decoration.OrderDecoration;
import com.tmmt.innersect.ui.fragment.CommonDialogFragment;
import com.tmmt.innersect.ui.fragment.OrderDialogFragment;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.RxBus;
import com.tmmt.innersect.utils.Util;
import com.tmmt.innersect.widget.CustomProgressDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;


public class OrdersDetailActivity extends BaseActivity implements OrderDetailAdapter.OnItemClickListener {

    RecyclerView mRecyclerView;
    OrderDetailAdapter mAdapter;

    @BindView(R.id.share_view)
    View mShareView;
    OrderDetailInfo mOrder;
    Subscription mSubscription;
    String mOrderNo;
    volatile boolean isReload;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected String getTitleString() {
        return getString(R.string.order_detail);
    }

    @Override
    protected void initView() {
        super.initView();
        isReload = false;
        AnalyticsUtil.reportEvent(AnalyticsUtil.ORDER_ORDERDETAILS_CLICK);
        mRecyclerView = findViewById(R.id.recycler_view);
        mAdapter = new OrderDetailAdapter();
        mAdapter.setListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new OrderDecoration());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        loadOrderDetail();
        mSubscription = RxBus.getDefault().toObservable(String.class).subscribe(s -> isReload = true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReload) {
            if (mOrderNo != null) {
                CustomProgressDialog progressDialog=new CustomProgressDialog(this);
                progressDialog.show();
                ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getOrderDetail(mOrderNo)
                        .enqueue(new NetCallback<OrderDetailInfo>() {
                            @Override
                            public void onSucceed(OrderDetailInfo data) {
                                progressDialog.dismiss();
                                mOrder = data;
                                mAdapter.setOrders(data);
                            }
                            @Override
                            protected void failed(int code) {
                                super.failed(code);
                                progressDialog.dismiss();
                            }
                        });
            }
        }
        isReload = false;
    }

    @Override
    public void onItemClick(View view, int position, Commodity data) {
        if (view != null) {
            DialogFragment fragment = OrderDialogFragment.newInstance(mOrder.orderNo);
            fragment.show(getSupportFragmentManager(), "");
        } else {
            CommonDialogFragment dialogFragment = new CommonDialogFragment.Builder()
                    .setLayout(R.layout.dialog_order)
                    .setTitle("您已申请过售后")
                    .setCancel("知道了")
                    .setOk("查看进度")
                    .build();
            dialogFragment.setActionListener(new CommonDialogFragment.ActionListener() {
                @Override
                public void doAction() {
                    RefundHistoryActivity.start(OrdersDetailActivity.this, mOrder.orderNo);
                }

                @Override
                public void cancel() {}
            });
            dialogFragment.show(getSupportFragmentManager(), "tag");
        }
    }

    private void loadOrderDetail() {
        mOrderNo = getIntent().getStringExtra("orderNo");
        if (mOrderNo == null) {
            return;
        }
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getOrderDetail(mOrderNo)
                .enqueue(new NetCallback<OrderDetailInfo>() {
                    @Override
                    public void onSucceed(OrderDetailInfo data) {
                        mOrder = data;
                        mAdapter.setOrders(data);
                        loadShare();
                    }
                });
    }

    private void loadShare() {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getLinkInfo(mOrder.orderNo).enqueue(new Callback<HttpBean<LinkInfo>>() {
            @Override
            public void onResponse(Call<HttpBean<LinkInfo>> call, Response<HttpBean<LinkInfo>> response) {
                if (response.isSuccessful()) {
                    LinkInfo data = response.body().data;
                    if (data != null && (mOrder.getOrderCode()!=OrderDetailInfo.UNPAID&&mOrder.getOrderCode()!=OrderDetailInfo.CANCEL)) {
                        mShareView.setVisibility(View.VISIBLE);
                        mShareView.setOnClickListener(v -> doShare(data));
                    }
                }
            }
            @Override
            public void onFailure(Call<HttpBean<LinkInfo>> call, Throwable t) {
            }
        });
    }

    @OnClick(R.id.action_view)
    void showHelp() {
        Util.startActivity(this, HelpActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    private static UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            KLog.d("plat", "platform" + platform);
            //Toast.makeText(getContext(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            //Toast.makeText(getContext(),platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                KLog.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            // Toast.makeText(getContext(),platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    void doShare(LinkInfo info) {
        UMWeb umWeb = new UMWeb(info.shareUrl);
        umWeb.setTitle(info.title);
        umWeb.setDescription(info.note);
        umWeb.setThumb(new UMImage(this, R.mipmap.app_icon));
        new ShareAction(this)
                .withMedia(umWeb)
                .setDisplayList(Util.getDisplayList(this))
                .setCallback(umShareListener)
                .open();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        mAdapter.release();
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
