package com.tmmt.innersect.ui.fragment;

import android.app.Service;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.presenter.LoginPresenter;
import com.tmmt.innersect.mvp.view.LoginView;
import com.tmmt.innersect.ui.activity.AgreementActivity;
import com.tmmt.innersect.ui.activity.Communication;
import com.tmmt.innersect.ui.activity.LoginActivity;
import com.tmmt.innersect.utils.Util;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoLoginFragment extends BaseFragment implements LoginView,MediaPlayer.OnPreparedListener {

    private LoginPresenter mPresenter;
    Communication mCommunication;

    MediaPlayer mMediaPlayer;

    @BindView(R.id.contract_view)
    TextView mContractView;

    @BindView(R.id.surface_view)
    SurfaceView mSurfaceView;

    @BindView(R.id.video_container)
    ViewGroup mContainer;

    @BindView(R.id.silent_view)
    ImageView mSilentView;

    @BindView(R.id.fullscreen)
    ImageView mFullScreen;

    boolean isSilent;

    AssetFileDescriptor mAssetFileDescriptor;

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
            //Toast.makeText(getContext(), "Authorize start", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            //Toast.makeText(getContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
            KLog.d(data);
            if(mCommunication!=null){
                mCommunication.setThirdParty(platform,data);
            }
            mPresenter.exitMobile(platform,data);
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            //Toast.makeText(getContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            //Toast.makeText(getContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onAttach(Context context) {
        if (context instanceof Communication) {
            mCommunication = (Communication) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Communication");
        }
        super.onAttach(context);
    }

    @OnClick(R.id.close_view)
    public void closeView(){
        getActivity().finish();
    }

    @Override
    int getLayout() {
        return R.layout.fragment_video_login;
    }

    @Override
    protected void initView(View view) {

        super.initView(view);
        ButterKnife.bind(this, view);
        mPresenter=new LoginPresenter();
        mPresenter.attachView(this);
        isSilent=false;
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                play();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        SpannableString spannableString=new SpannableString(getString(R.string.contract));
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.BLACK);
        spannableString.setSpan(foregroundColorSpan, 12, 20, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mContractView.setText(spannableString.toString());
        //play();

    }

    @OnClick(R.id.phone_login)
    void  phoneLogin(){
        if(mCommunication!=null){
            mCommunication.goToTarget(LoginActivity.PHONE_LOGIN);
        }
    }

    @OnClick({R.id.qq_login, R.id.wechat_login, R.id.sina_login, R.id.fb_login})
    public void thirdLogin(View view) {
        SHARE_MEDIA platform;
        switch (view.getId()) {
            case R.id.qq_login:
                platform = SHARE_MEDIA.QQ;
                break;
            case R.id.fb_login:
                platform = SHARE_MEDIA.FACEBOOK;
                break;
            case R.id.sina_login:
                platform = SHARE_MEDIA.SINA;
                break;
            case R.id.wechat_login:
                platform = SHARE_MEDIA.WEIXIN;
                break;
            default:
                platform = SHARE_MEDIA.FACEBOOK;
                break;
        }

        UMShareAPI.get(getContext()).getPlatformInfo(getActivity(),platform,umAuthListener);

    }

    @OnClick(R.id.contract_view)
    void openContract(){
        Util.startActivity(getContext(),AgreementActivity.class);
    }

    @Override
    public void unBindMobile() {
        mCommunication.goToTarget(LoginActivity.BINDING);//unbind phone number;
    }

    @Override
    public void setPassword(boolean setPwd) {
        int target;
        if(setPwd){
            target= LoginActivity.PASSWORD;
        }else {
            target=LoginActivity.VERIFY_CODE;
        }
        mCommunication.goToTarget(target);
    }

    @Override
    public void success(int code) {
        if(code==200){
            getActivity().finish();
        }else {
            Toast.makeText(getContext(),"登录失败",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        //设置一个播放错误的监听
        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        mp.setDisplay(mSurfaceView.getHolder());
        mp.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //变成横屏了
            setVideoParams(mMediaPlayer, true);
            mFullScreen.setImageResource(R.mipmap.small_screen);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //变成竖屏了
            setVideoParams(mMediaPlayer, false);
            mFullScreen.setImageResource(R.mipmap.full_screen);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void openVolume(){
        AudioManager audioManager=(AudioManager)getContext().getSystemService(Service.AUDIO_SERVICE);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_SYSTEM);
        mMediaPlayer.setVolume(audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM), audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM));

    }

    @OnClick(R.id.silent_view)
    void silent(){
        if(mMediaPlayer==null){
            return;
        }
        if(isSilent){
            openVolume();
            isSilent=false;
            mSilentView.setImageResource(R.mipmap.sound_open);

        }else {
            mMediaPlayer.setVolume(0,0);
            isSilent=true;
            mSilentView.setImageResource(R.mipmap.sound_close);
        }
    }

    @OnClick(R.id.fullscreen)
    void changeOrientation(){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //变成竖屏
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //变成横屏了
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    void play() {
        mMediaPlayer = new MediaPlayer();
        mAssetFileDescriptor = getResources().openRawResourceFd(R.raw.login);
        try {
            silent();
            mMediaPlayer.setDataSource(mAssetFileDescriptor.getFileDescriptor(), mAssetFileDescriptor.getStartOffset(), mAssetFileDescriptor.getLength());
            //设置循环播放
            mMediaPlayer.setLooping(true);
            //设置播放区域
            //播放时屏幕保持唤醒
            mMediaPlayer.setScreenOnWhilePlaying(true);
            //异步准备播放视频
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setVideoParams(MediaPlayer mediaPlayer, boolean isLand) {
        ViewGroup.LayoutParams paramters = mContainer.getLayoutParams();
        if(isLand){
            paramters.height= ViewGroup.LayoutParams.MATCH_PARENT;
        }else {
            paramters.height= Util.dip2px(200);
        }
        mContainer.setLayoutParams(paramters);

    }

    @Override
    public void onPause() {
        super.onPause();
        if(mMediaPlayer!=null){
            mMediaPlayer.release();
        }
        try{
            if(mAssetFileDescriptor!=null){
                mAssetFileDescriptor.close();
            }
        }catch (IOException e){

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
    }

}
