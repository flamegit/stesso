//package com.tmmt.innersect.ui.activity;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.tmmt.innersect.App;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.AccountInfo;
//
//import com.tmmt.innersect.common.Constants;
//import com.tmmt.innersect.datasource.net.ApiManager;
//import com.tmmt.innersect.datasource.net.NetCallback;
//import com.tmmt.innersect.mvp.model.LotteryInfo;
//import com.tmmt.innersect.mvp.model.TicketLotteryInfo;
//import com.tmmt.innersect.ui.fragment.CommonDialogFragment;
//import com.tmmt.innersect.ui.fragment.LotteryDialogFragment;
//import com.tmmt.innersect.utils.AnalyticsUtil;
//import com.tmmt.innersect.utils.Util;
//import com.tmmt.innersect.widget.GuaGuaKa;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//
//public class TicketDrawActivity extends AppCompatActivity {
//
//    @BindView(R.id.guaguaka)
//    GuaGuaKa mGuaView;
//    @BindView(R.id.action_view)
//    TextView mActionView;
//    @BindView(R.id.top_image)
//    ImageView mTopView;
//    @BindView(R.id.title1)
//    TextView mTitle1;
//    @BindView(R.id.title2)
//    TextView mTitle2;
//    @BindView(R.id.content1)
//    TextView mContent1;
//    @BindView(R.id.content2)
//    TextView mContent2;
//    @BindView(R.id.luck_switch)
//    View mSwitchView;
//    @BindView(R.id.chance_view)
//    TextView mChanceView;
//    @BindView(R.id.no_chance)
//    View mNoChanceView;
//    @BindView(R.id.progress_view)
//    ProgressBar mProgressView;
//    @BindView(R.id.bottom_btn)
//    View mBottomView;
//    @BindView(R.id.hint_view)
//    View mHintView;
//    int mChance;
//    boolean isHit;
//    LotteryInfo mLotteryInfo;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ticket_draw);
//        ButterKnife.bind(this);
//
//        mGuaView.setOnGuaGuaKaCompleteListener(new GuaGuaKa.OnGuaGuaKaCompleteListener() {
//            @Override
//            public void complete() {
//                if (isHit) {
//                    mActionView.setText("点击查看奖品");
//                    mBottomView.setVisibility(View.VISIBLE);
//                }
//                if (mChance > 0 || isHit) {
//                    mActionView.setVisibility(View.VISIBLE);
//                } else {
//                    mHintView.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getLotteryInfo("v1", AccountInfo.getInstance().getUserId()).enqueue(new NetCallback<TicketLotteryInfo>() {
//            @Override
//            public void onSucceed(TicketLotteryInfo data) {
//                if(!isDestroyed()){
//                    fillView(data);
//                }
//            }
//        });
//
//        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getDrawResult("v1", AccountInfo.getInstance().getUserId()).enqueue(new NetCallback<LotteryInfo>() {
//            @Override
//            public void onSucceed(LotteryInfo data) {
//                if (data.isWin) {
//                    mBottomView.setVisibility(View.VISIBLE);
//                    mLotteryInfo = data;
//                } else {}
//            }
//        });
//    }
//
//    private void fillView(TicketLotteryInfo info) {
//        if(info.isFirst){
//            CommonDialogFragment dialogFragment=CommonDialogFragment.newInstance(R.layout.dialog_general,"购票即可参与抽奖哦~");
//            dialogFragment.setCancelable(false);
//            dialogFragment.setActionListener(new CommonDialogFragment.ActionListener() {
//                @Override
//                public void doAction() {
//                    if(AccountInfo.getInstance().isLogin()){
//                        Util.startActivity(TicketDrawActivity.this,TicketListActivity.class);
//                    }else {
//                        Util.startActivity(TicketDrawActivity.this,LoginActivity.class);
//                    }
//                }
//                @Override
//                public void cancel() {
//
//                }
//            });
//            dialogFragment.show(getSupportFragmentManager(),"tag");
//        }
//        mChance = info.chance;
//        if (mChance > 0) {
//            mChanceView.setText(String.format("你还有%d次抽奖机会", info.chance));
//            mSwitchView.setVisibility(View.VISIBLE);
//        } else {
//            mNoChanceView.setVisibility(View.VISIBLE);
//        }
//        Glide.with(this).load(info.headImg.get(0)).into(mTopView);
//        mTitle1.setText(info.notice.subtitle);
//        mTitle2.setText(info.attention.subtitle);
//        mContent1.setText(info.notice.content);
//        mContent2.setText(info.attention.content);
//    }
//
//    @OnClick(R.id.action_view)
//    void action() {
//        if (isHit) {
//            AnalyticsUtil.reportEvent("ticket_activity_view_details");
//            isHit = false;
//            LotteryDialogFragment dialogFragment = LotteryDialogFragment.newInstance(mLotteryInfo);
//            dialogFragment.show(getSupportFragmentManager(), "tag");
//            if (mChance > 0) {
//                mChanceView.setText(String.format("你还有%d次抽奖机会", mChance));
//                mSwitchView.setVisibility(View.VISIBLE);
//            } else {
//                mNoChanceView.setVisibility(View.VISIBLE);
//            }
//        } else {
//            AnalyticsUtil.reportEvent("ticket_activity_again");
//            draw();
//        }
//        mActionView.setVisibility(View.GONE);
//        mGuaView.reset();
//    }
//
//    @OnClick(R.id.bottom_btn)
//    void openLottery() {
//        AnalyticsUtil.reportEvent("ticket_activity_exchange");
//        Intent intent = new Intent(this, TicketLotteryActivity.class);
//        intent.putExtra(Constants.LOTTERY_INFO, mLotteryInfo);
//        startActivity(intent);
//
//    }
//
//    @OnClick(R.id.close_view)
//    void close() {
//        onBackPressed();
//    }
//
//    @OnClick(R.id.luck_switch)
//    void draw() {
//        AnalyticsUtil.reportEvent("ticket_activity_start");
//        mProgressView.setVisibility(View.VISIBLE);
//        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).startDraw("v1", AccountInfo.getInstance().getUserId()).enqueue(new NetCallback<LotteryInfo>() {
//            @Override
//            public void onSucceed(LotteryInfo data) {
//                mProgressView.setVisibility(View.GONE);
//                mChance--;
//                mGuaView.turnOn();
//                if (data.isWin) {
//                    isHit = true;
//                    mGuaView.setText(data.name);
//                    mLotteryInfo = data;
//                }
//            }
//        });
//        mSwitchView.setVisibility(View.GONE);
//    }
//}
