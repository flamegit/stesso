package com.tmmt.innersect.ui.activity;


import android.animation.Animator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.AwardInfo;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.mvp.model.LinkInfo;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.utils.Util;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SuccessActivity extends AppCompatActivity implements CommonView<LinkInfo>{

    @BindView(R.id.image_view)
    ImageView imageView;

    @BindView(R.id.empty_view)
    View emptyView;

    @BindView(R.id.share_button)
    View shareButton;

    @BindView(R.id.lottery_hint)
    View mLotteryHint;

    @BindView(R.id.thanks_view)
    View mThanksView;

    @BindView(R.id.lottery_text)
    TextView mLotteryText;

    @BindView(R.id.animation_view)
    LottieAnimationView mAnimationView;

    @BindView(R.id.layout_view)
    View mLayout;

    CommonPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        ButterKnife.bind(this);
        String orderNO=getIntent().getStringExtra(Constants.ORDER_NO);
        mPresenter=new CommonPresenter();
        mPresenter.attachView(this);
        mPresenter.getLinkInfo(orderNO);
        mAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                mLayout.setVisibility(View.VISIBLE);

                ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getPayedInfo().enqueue(new Callback<HttpBean<AwardInfo>>() {
                    @Override
                    public void onResponse(Call<HttpBean<AwardInfo>> call, Response<HttpBean<AwardInfo>> response) {
                        if(response.isSuccessful()){
                            if(response.body().code==200){
                                if(response.body().data==null){
                                    mThanksView.setVisibility(View.VISIBLE);
                                    return;
                                }
                                AwardInfo data=response.body().data;
                                if(data.isActive){
                                    mLotteryText.setText(data.getHint());
                                    mLotteryHint.setOnClickListener(v -> {
                                        Util.openTarget(SuccessActivity.this,data.schema,"");
                                    });
                                    mLotteryHint.setVisibility(View.VISIBLE);
                                }else {
                                    mThanksView.setVisibility(View.VISIBLE);
                                }
                            }else {
                                //failed(response.body().code);
                                //SystemUtil.reportServerError(response.body().msg);
                            }
                        }else {
                            KLog.i("response failed"+response.code()+response.message());
                            //failed(response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<HttpBean<AwardInfo>> call, Throwable t) {
                        KLog.i(t);
                        //failed(-1);
                        //SystemUtil.reportNetError(t);
                    }
                });
            }
            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

    }

    @Override
    public void success(LinkInfo data) {
        if(data==null){
            emptyView.setVisibility(View.VISIBLE);
        }else {
            shareButton.setVisibility(View.VISIBLE);
            Glide.with(SuccessActivity.this).load(data.bgUrl).into(imageView);
            imageView.setOnClickListener(v -> doShare(data));
        }
    }

    @Override
    public void failed() {
        emptyView.setVisibility(View.VISIBLE);

    }

    @OnClick(R.id.close_view)
    void close(){
        onBackPressed();
    }

    private static UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }
        @Override
        public void onResult(SHARE_MEDIA platform) {
            KLog.d("plat", "platform" + platform);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t != null) {
                KLog.d("throw", "throw:" + t.getMessage());
            }
        }
        @Override
        public void onCancel(SHARE_MEDIA platform) {
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
        mPresenter.onDestory();
    }
}
