//package com.tmmt.innersect.ui.activity;
//
//import android.animation.Animator;
//import android.animation.AnimatorSet;
//import android.animation.ObjectAnimator;
//import android.graphics.Bitmap;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.socks.library.KLog;
//import com.tmmt.innersect.BuildConfig;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.AccountInfo;
//import com.tmmt.innersect.common.Constants;
//import com.tmmt.innersect.utils.QRCodeUtil;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//
//public class VloneLotteryActivity extends AppCompatActivity {
//
//    @BindView(R.id.code_container)
//    View mCodeContainer;
//
//    @BindView(R.id.info_view)
//    View mInfoView;
//
//    @BindView(R.id.code_view)
//    View mCodeView;
//
//    @BindView(R.id.qr_view)
//    ImageView mQrView;
//
//    String mQrcode;
//
//    @OnClick(R.id.back_view)
//    void back(){
//        onBackPressed();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_vlone_lottery);
//        ButterKnife.bind(this);
//        mQrcode= getIntent().getStringExtra(Constants.LOTTERY_CODE);
//
//    }
//
//    private String getUrl(String qrcode){
//        if(BuildConfig.DEBUG){
//            return String.format("http://47.92.38.236:8081/shop/#/lottery?qrcode=%s",qrcode);
//        }else {
//            return String.format("https://innersect.net/shop/#/lottery?qrcode=%s",qrcode);
//        }
//    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            int size = mQrView.getWidth();
//            Bitmap bitmap = QRCodeUtil.createQRImage(getUrl(mQrcode), size, size);
//            mQrView.setImageBitmap(bitmap);
//        }
//    }
//
//    @OnClick(R.id.code_container)
//    public void rotation(){
//        ObjectAnimator animator=ObjectAnimator.ofFloat(mCodeContainer,"rotationY",0,90);
//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {}
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                if(mInfoView.getVisibility()==View.VISIBLE){
//                    mInfoView.setVisibility(View.INVISIBLE);
//                    mCodeView.setVisibility(View.VISIBLE);
//                    mCodeView.setRotationY(180);
//                }else {
//                    mInfoView.setVisibility(View.VISIBLE);
//                    mCodeView.setVisibility(View.INVISIBLE);
//                    mInfoView.setRotationY(180);
//                }
//            }
//            @Override
//            public void onAnimationCancel(Animator animation) {}
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {}
//        });
//
//        ObjectAnimator animator2=ObjectAnimator.ofFloat(mCodeContainer,"rotationY",90,180);
//        AnimatorSet set=new AnimatorSet();
//        set.playSequentially(animator,animator2);
//        set.start();
//    }
//
//}
