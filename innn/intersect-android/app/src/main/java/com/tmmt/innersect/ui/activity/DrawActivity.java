package com.tmmt.innersect.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.DrawResult;
import com.tmmt.innersect.mvp.model.LotteryDetail;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.adapter.CommonPagerAdapter;
import com.tmmt.innersect.ui.fragment.CommonDialogFragment;
import com.tmmt.innersect.utils.FileUtil;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.utils.Util;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DrawActivity extends BaseActivity implements CommonView<LotteryDetail> {

    public static final int INIT = 1;
    public static final int DRAWING = 2;
    public static final int LOADING = 3;
    public static final int HIT = 4;
    public static final int MISS = 5;
    public static final int STOPPING = 6;
    public static final int OVER = 7;

    @BindView(R.id.no_network)
    View mNoNetWork;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.num_view)
    TextView mNumView;
    @BindView(R.id.container)
    ViewGroup mContainer;
    @BindView(R.id.image_view)
    ImageView mImageView;
    @BindView(R.id.draw_btn)
    ImageView mDrawView;
    @BindView(R.id.cover_view)
    View mCoverView;
    @BindView(R.id.award_view)
    View mAwardView;
    @BindView(R.id.top_image)
    ImageView mTopImage;
    @BindView(R.id.rule_view)
    TextView mRuleView;
    @BindView(R.id.rule_title)
    TextView mRuleTitle;
    @BindView(R.id.action_text)
    TextView mActionView;
    @BindView(R.id.count_view)
    TextSwitcher mCountView;
    @BindView(R.id.again_view)
    View mAgainView;
    @BindView(R.id.count_layout)
    View mCountLayout;

    @BindView(R.id.result_view)
    ImageView mResultView;

    @BindView(R.id.change_text)
    View mChanceText;

    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;

    @BindView(R.id.top_bar)
    View mTopBar;

    DrawResult mResult;
    int mChanceCount;
    String mTarget;
    int mStartNum;
    int mState;
    Animation mAnimation;
    ValueAnimator mValueAnimator;
    CommonPagerAdapter<Bitmap> mAdapter;

    Map<String, Bitmap> mBitmapMap;
    Bitmap[] mBitmaps;
    Subscription mSubscription;
    CommonPresenter mPresenter;
    LotteryDetail mLotteryData;
    long mActivityId;
    int mSize;
    boolean isLoad;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_draw;
    }

    @Override
    protected void initView() {
        super.initView();
        mTarget = "nothing";
        if (AccountInfo.showDialog(Constants.SHOW_DIALOG)) {
            CommonDialogFragment.newInstance(R.layout.dialog_hint, "")
                    .show(getSupportFragmentManager(), "lottery");
            AccountInfo.setShowDialog(Constants.SHOW_DIALOG);
        }

        isLoad = false;
        mStartNum = 3;
        mState = LOADING;
        mCountView.setFactory(
                () -> {
                    TextView textView = new TextView(DrawActivity.this);
                    textView.setTextSize(28);//字号
                    TextPaint paint = textView.getPaint();
                    paint.setFakeBoldText(true);
                    return textView;
                });

        mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                float h = Util.dip2px(50);
                float alpha = scrollY / h;
                mTopBar.setAlpha(alpha);
            }
        });

        mActivityId = getIntent().getLongExtra(Constants.ACTIVITY_ID, 1);
        mValueAnimator = ValueAnimator.ofInt(0, 100);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int index = (Integer) animation.getAnimatedValue();
                mImageView.setImageBitmap(mBitmaps[index % mSize]);
            }
        });
        mValueAnimator.setDuration(10000);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mImageView.setImageBitmap(mBitmapMap.get(mTarget));
                //mImageView.setScaleX(1.1f);
                //mImageView.setScaleY(1.1f);
                mTopImage.setVisibility(View.GONE);

            }
        });

        mAnimation = AnimationUtils.loadAnimation(this, R.anim.text_anim);
        mAnimation.setRepeatCount(2);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mStartNum = 3;
                mNumView.setText(String.valueOf(mStartNum));
                mDrawView.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mNumView.setVisibility(View.GONE);
                mCoverView.setVisibility(View.GONE);
                mValueAnimator.start();
                mDrawView.setEnabled(true);
                mImageView.setVisibility(View.VISIBLE);
                mImageView.setImageBitmap(mBitmaps[mViewPager.getCurrentItem() % mSize]);
                mViewPager.setVisibility(View.GONE);
                mDrawView.setImageResource(R.mipmap.draw_stop);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                mStartNum--;
                mNumView.setText(String.valueOf(mStartNum));
            }
        });
        mAdapter = new CommonPagerAdapter<>(true,
                (container, content) -> {
                    ImageView imageView = new ImageView(container.getContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageBitmap(content);
                    return imageView;
                });
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                if (position > -1 && position < 1) {
                    float factor = Math.max(0.75f, 1 - Math.abs(position) * 0.8f);
                    page.setScaleX(factor);
                    page.setScaleY(factor);
                } else {
                    page.setScaleX(0.75f);
                    page.setScaleY(0.75f);
                }
            }
        });
        mPresenter = new CommonPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getLotteryDetail(mActivityId);
    }

    @Override
    public void success(LotteryDetail data) {
        if (data != null) {
            mLotteryData = data;
            mCountView.setText(String.valueOf(data.drawChance));
            if (data.isLogin) {
                mActionView.setText("获取更多机会");
                mChanceText.setVisibility(View.VISIBLE);
                mAwardView.setVisibility(View.VISIBLE);
                mCountView.setVisibility(View.VISIBLE);
                mAwardView.setOnClickListener(
                        v -> {
                            if (mState != DRAWING) {
                                Util.startActivity(DrawActivity.this, AwardActivity.class);
                            }
                        });
            } else {
                mChanceText.setVisibility(View.INVISIBLE);
                mAwardView.setVisibility(View.INVISIBLE);
                mCountView.setVisibility(View.INVISIBLE);
                mActionView.setText("登录参与抽奖");
            }
            mActionView.setOnClickListener(v ->
                    {
                        if (mState != DRAWING) {
                            Util.checkLogin(DrawActivity.this, new Intent(DrawActivity.this, CommonActivity.class));
                        }
                    }
            );
            Glide.with(this).load(data.bgUrl).into(mTopImage);
            mRuleView.setText(data.rule);
            mChanceCount = data.drawChance;
            String fileName = String.valueOf(data.id) + data.sourcePackVersion;

            if (isLoad) {
                return;
            }

            if (!data.isActive()) {
                mDrawView.setImageResource(R.mipmap.draw_over);
            } else {
                mDrawView.setImageResource(R.mipmap.draw_play);
            }

            if (FileUtil.isFileExist(fileName)) {
                fillViewPager(fileName);
            } else {
                ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).downloadFile(data.sourcePackUrl)
                        .map(body -> {
                            if (FileUtil.writeResponseBodyToDisk(body)) {
                                try {
                                    FileUtil.unZipFiles(fileName);
                                    return true;
                                } catch (IOException e) {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(b -> {
                            if(b){
                                fillViewPager(fileName);
                            }
                        });
            }
        }
    }

    private void fillViewPager(String fileName) {
        mBitmapMap = FileUtil.getBitmaps(fileName);
        mBitmaps = mBitmapMap.values().toArray(new Bitmap[0]);
        mSize = mBitmapMap.size();
        Bitmap bitmap = mBitmapMap.remove("nothing");
        mAdapter.addItems(mBitmapMap.values());
        mBitmapMap.put("nothing", bitmap);
        mViewPager.arrowScroll(View.FOCUS_RIGHT);
        mSubscription = Observable.interval(1, 2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(t -> mViewPager.arrowScroll(View.FOCUS_RIGHT));
        isLoad = true;
        if (mLotteryData.isActive()) {
            mState = INIT;
        }
    }

    private void resetView() {
        mCountLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        mResultView.setVisibility(View.GONE);
        mTopImage.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.INVISIBLE);
        mSubscription = Observable.interval(1, 2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(t -> mViewPager.arrowScroll(View.FOCUS_RIGHT));
    }

    @Override
    public void failed() {
        mNoNetWork.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.draw_btn)
    void doAction() {
        if (!AccountInfo.getInstance().isLogin()) {
            Util.startActivity(this, LoginActivity.class);
            return;
        }
        switch (mState) {
            case INIT:
                if (mChanceCount > 0) {
                    mState = DRAWING;
                    if (mSubscription != null) {
                        mSubscription.unsubscribe();
                    }
                    mCoverView.setVisibility(View.VISIBLE);
                    mNumView.startAnimation(mAnimation);
                } else {
                    SystemUtil.toast("没有抽奖机会");
                }
                break;

            case DRAWING:
                mState = STOPPING;
                ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).draw(mActivityId).enqueue(new NetCallback<DrawResult>() {
                    @Override
                    public void onSucceed(DrawResult data) {
                        mResult = data;
                        mCountLayout.setVisibility(View.GONE);
                        mResultView.setVisibility(View.VISIBLE);
                        mChanceCount--;
                        mCountView.setText(String.valueOf(mChanceCount));
                        if (data != null) {
                            mTarget = String.valueOf(data.picId);
                            KLog.d(mTarget);
                            mState = HIT;
                            mDrawView.setImageResource(R.mipmap.draw_use);
                            mResultView.setImageResource(R.mipmap.lucky_icon);
                            mAgainView.setVisibility(View.VISIBLE);
                            mAgainView.setOnClickListener(v -> {
                                mState = MISS;
                                doAction();
                                mAgainView.setVisibility(View.GONE);
                            });
                        } else {
                            mTarget = "nothing";
                            mState = MISS;
                            mDrawView.setImageResource(R.mipmap.draw_again);
                            mResultView.setImageResource(R.mipmap.sorry);
                        }
                        mValueAnimator.cancel();
                    }

                    @Override
                    protected void failed(int code) {
                        super.failed(code);
                        //SystemUtil.toast("当前网络状况不佳，请稍后重试");
                    }
                });
                break;
            case HIT:
                if (mResult.type == 11) {
                    CouponActivity.start(this, false);
                } else if (mResult.type == 12) {
                    AwardSettlementActivity.start(this, mResult.id);
                } else if (mResult.type == 13) {
                    QualificationActivity.start(this, mResult.id);
                }
                break;
            case MISS:
                mState = INIT;
                mDrawView.setImageResource(R.mipmap.draw_play);
                resetView();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mValueAnimator.cancel();
        mAnimation.cancel();
    }
}
