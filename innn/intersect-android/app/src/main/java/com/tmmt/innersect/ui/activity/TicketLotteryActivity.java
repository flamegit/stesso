//package com.tmmt.innersect.ui.activity;
//
//import android.widget.ImageView;
//import android.widget.TextView;
//import com.bumptech.glide.Glide;
//import com.tmmt.innersect.App;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.Constants;
//import com.tmmt.innersect.mvp.model.LotteryInfo;
//
//import butterknife.BindView;
//
//
//public class TicketLotteryActivity extends BaseActivity {
//
//    @BindView(R.id.image_view)
//    ImageView mImageView;
//
//    @BindView(R.id.code_view)
//    TextView mCodeView;
//
//    @BindView(R.id.rule_view)
//    TextView mRuleView;
//
//    @Override
//    protected void initView() {
//        super.initView();
//        LotteryInfo info=getIntent().getParcelableExtra(Constants.LOTTERY_INFO);
//        if(info!=null){
//           fillView(info);
//        }
//    }
//
//    @Override
//    protected String getTitleString() {
//        return "我的兑奖券";
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_ticket_lottery;
//    }
//
//    private void fillView(LotteryInfo info){
//        Glide.with(App.getAppContext()).load(info.imgUrl).into(mImageView);
//        mCodeView.setText(info.qrCode);
//        mRuleView.setText(info.rule);
//    }
//}
