//package com.tmmt.innersect.ui.activity;
//
//import android.animation.ValueAnimator;
//import android.media.AudioManager;
//import android.media.SoundPool;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.animation.DecelerateInterpolator;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.Constants;
//import com.tmmt.innersect.mvp.presenter.VlonePresenter;
//import com.tmmt.innersect.ui.fragment.CommonDialogFragment;
//import com.tmmt.innersect.utils.AnalyticsUtil;
//import com.tmmt.innersect.utils.Util;
//import com.tmmt.innersect.widget.WheelView;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//public class VloneLuckActivity extends AppCompatActivity {
//
//    @BindView(R.id.wheel_first)
//    WheelView firstView;
//    @BindView(R.id.wheel_second)
//    WheelView secondView;
//    @BindView(R.id.wheel_third)
//    WheelView thirdView;
//    @BindView(R.id.left_light)
//    ImageView leftLight;
//    @BindView(R.id.right_light)
//    ImageView rightLight;
//    @BindView(R.id.start_view)
//    TextView startView;
//    @BindView(R.id.luck_result)
//    TextView resultView;
//
//    @BindView(R.id.fail_result)
//    TextView failView;
//
//    @BindView(R.id.lottery_view)
//    TextView lotteryView;
//
//    @BindView(R.id.lottery_count)
//    TextView countView;
//
//    int changeCount;
//    VlonePresenter mPresenter;
//    String mCurrCode;
//
//    String mLotteryCode;
//
//    //MediaPlayer mMediaPlayer;
//    SoundPool mSoundPool;
//
//    int mStopId;
//    int mFailedId;
//    int mBingoId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        AnalyticsUtil.reportEvent(AnalyticsUtil.VLONE_SHOP_GODRAW);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_vlone_luck);
//        ButterKnife.bind(this);
//
////        Typeface typeface=Util.getTypeface(this,"luck.TTF");
////        resultView.setTypeface(typeface);
////        failView.setTypeface(typeface);
//
//        mPresenter=new VlonePresenter();
//        mPresenter.getDrawCount(new VlonePresenter.ViewCallback<String>() {
//            @Override
//            public void success(String data) {
//                changeCount= Integer.valueOf(data);
//                resultView.setText("剩余机会："+changeCount);
//            }
//        });
//
//        mSoundPool=new SoundPool(3, AudioManager.STREAM_MUSIC,0);
//        mStopId=mSoundPool.load(this,R.raw.stop,0);
//        mFailedId=mSoundPool.load(this,R.raw.failed,0);
//        mBingoId=mSoundPool.load(this,R.raw.bingo,0);
//
//        mPresenter.getDrawResult(new VlonePresenter.ViewCallback<String>() {
//            @Override
//            public void success(String data) {
//                if(data!=null){
//                    mLotteryCode=data;
//                    lotteryView.setVisibility(View.VISIBLE);
//                    countView.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//    }
//
//    @OnClick(R.id.back_view)
//    void back(){
//        onBackPressed();
//    }
//
//    @OnClick(R.id.lottery_view)
//    public void goToLottery(){
//        if(mLotteryCode!=null){
//            AnalyticsUtil.reportEvent(AnalyticsUtil.DRAW_MYTICKET);
//            Util.startActivity(this,VloneLotteryActivity.class, Constants.LOTTERY_CODE,mLotteryCode);
//        }
//    }
//
//    public void playLightAnimation(){
//        ValueAnimator animator=ValueAnimator.ofInt(0,50);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int value=(Integer) animation.getAnimatedValue();
//                if(value%2==0){
//                    leftLight.setImageResource(R.mipmap.light_1);
//                    rightLight.setImageResource(R.mipmap.light_1);
//                }else {
//                    leftLight.setImageResource(R.mipmap.light_2);
//                    rightLight.setImageResource(R.mipmap.light_2);
//                }
//            }
//        });
//        animator.setInterpolator(new DecelerateInterpolator(1f));
//        animator.setDuration(15000);
//        animator.start();
//    }
//
//    @OnClick(R.id.start_view)
//    public void drawLottery(){
//        AnalyticsUtil.reportEvent(AnalyticsUtil.DRAW_CLICK);
//        startView.setEnabled(false);
//        if(changeCount==0){
//            CommonDialogFragment fragment=CommonDialogFragment.newInstance(R.layout.dialog_luck,"");
//            fragment.show(getSupportFragmentManager(),"luck");
//        }else {
//            resultView.setVisibility(View.VISIBLE);
//            failView.setVisibility(View.INVISIBLE);
//
//            if(changeCount>0){
//                mPresenter.draw(new VlonePresenter.ViewCallback<String>() {
//                    @Override
//                    public void success(String data) {
//                        mCurrCode=data;
//                        drawLottery(mCurrCode!=null);
//                    }
//                });
//            }
//            resultView.setText("剩余机会："+ --changeCount);
//
//
//        }
//    }
//
//    private void drawLottery(boolean hit){
//        startView.setText("抽奖中...");
//        playLightAnimation();
//        playMusic();
//        startView.setEnabled(false);
//        int first=30-firstView.getIndex();
//        int second=34-secondView.getIndex();
//        int third=40-thirdView.getIndex();
//        int remainder;
//
//        if(hit){
//            if((remainder=first%3)!=0){
//                first-=remainder;
//            }
//            if((remainder=second%3)!=0){
//                second-=remainder;
//            }
//            if((remainder=third%3)!=0){
//                third-=remainder;
//            }
//
//        }else {
//            remainder=third%3;
//            if(remainder==0){
//                third++;
//            }
//        }
//        firstView.startScroll(first,5000);
//        secondView.startScroll(second,10000);
//        thirdView.startScroll(third,15000);
//        firstView.setScrollListener(new WheelView.ScrollListener() {
//            @Override
//            public void onScrollFinish() {
//                mSoundPool.play(mStopId,1,1,0,0,1);
//                firstView.setScrollListener(null);
//            }
//        });
//        secondView.setScrollListener(new WheelView.ScrollListener() {
//            @Override
//            public void onScrollFinish() {
//                mSoundPool.play(mStopId,1,1,0,0,1);
//                secondView.setScrollListener(null);
//            }
//        });
//
//        thirdView.setScrollListener(new WheelView.ScrollListener() {
//            @Override
//            public void onScrollFinish() {
//                if(mCurrCode!=null){
//                    resultView.setText("BINGO");
//                    resultView.setVisibility(View.VISIBLE);
//                    failView.setVisibility(View.INVISIBLE);
//
//                    mLotteryCode=mCurrCode;
//                    lotteryView.setVisibility(View.VISIBLE);
//                    countView.setVisibility(View.VISIBLE);
//
//                    final Runnable action=new Runnable() {
//                        @Override
//                        public void run() {
//                            Util.startActivity(VloneLuckActivity.this,VloneLotteryActivity.class);
//                        }
//                    };
//                    resultView.postDelayed(action,500);
//                    mSoundPool.play(mBingoId,1,1,0,0,1);
//
//                }else {
//                    mSoundPool.play(mFailedId,1,1,0,0,1);
//                    resultView.setVisibility(View.INVISIBLE);
//                    failView.setVisibility(View.VISIBLE);
//                    resultView.setText("再接再厉，谁还没有个手背的时候");
//                }
//                startView.setText("点击开始");
//                startView.setEnabled(true);
//                thirdView.setScrollListener(null);
//                //mMediaPlayer.pause();
//
//            }
//        });
//    }
//
//    private void playMusic(){
////        if(mMediaPlayer==null){
////            mMediaPlayer=MediaPlayer.create(this,R.raw.rolling);
////            mMediaPlayer.setLooping(true);
////        }
////        //mMediaPlayer.start();
//
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
////        if(mMediaPlayer!=null){
////            mMediaPlayer.stop();
////        }
//        mSoundPool.release();
//        thirdView.setScrollListener(null);
//    }
//}

//ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.Callback() {
//@Override
//public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        //return 0;
//        if(viewHolder.getItemViewType()==2){
//        return makeMovementFlags(0,ItemTouchHelper.LEFT);
//        }
//        return 0;
//
//        }
//@Override
//public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//        return false;
//        }
//
//@Override
//public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {}
//@Override
//public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        if(viewHolder instanceof TextViewHolder){
//        TextViewHolder textViewHolder=(TextViewHolder)viewHolder;
//        getDefaultUIUtil().onDraw(c,recyclerView,textViewHolder.textView,dX,dY,actionState,isCurrentlyActive);
//        }else {
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        }
//        }
//        });
