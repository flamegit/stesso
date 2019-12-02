//package com.tmmt.innersect.ui.activity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.DecodeFormat;
//import com.bumptech.glide.request.animation.GlideAnimation;
//import com.bumptech.glide.request.target.SimpleTarget;
//import com.socks.library.KLog;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.AccountInfo;
//import com.tmmt.innersect.common.Constants;
//import com.tmmt.innersect.datasource.net.ApiManager;
//import com.tmmt.innersect.datasource.net.NetCallback;
//import com.tmmt.innersect.mvp.model.Agenda;
//import com.tmmt.innersect.mvp.presenter.CommonPresenter;
//import com.tmmt.innersect.mvp.view.BaseView;
//import com.tmmt.innersect.utils.Util;
//import com.tmmt.innersect.widget.RingsView;
//import org.json.JSONException;
//import org.json.JSONStringer;
//import butterknife.BindView;
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//
//public class ReservationActivity2 extends BaseActivity implements BaseView<Agenda>{
//
//    @BindView(R.id.ring_view)
//    RingsView ringsView;
//    @BindView(R.id.top_image)
//    ImageView topImage;
//    @BindView(R.id.title_view)
//    TextView titleView;
//    @BindView(R.id.time_view)
//    TextView timeView;
//    @BindView(R.id.area_view)
//    TextView areaView;
//    @BindView(R.id.reserve_layout)
//    View reserveLayout;
//    @BindView(R.id.reserve_state)
//    TextView stateView;
//    @BindView(R.id.reserve_time)
//    TextView appointView;
//    @BindView(R.id.success_view)
//    View successView;
//    @BindView(R.id.reserve_result)
//    TextView resultView;
//    @BindView(R.id.blur_view)
//    ImageView blurView;
//    CommonPresenter mPresenter;
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_reservation2;
//    }
//
//    @Override
//    protected void initView() {
//        super.initView();
//        int id=getIntent().getIntExtra(Constants.INFO_ID,1);
//        mPresenter=new CommonPresenter();
//        mPresenter.attachView(this);
//        mPresenter.loadAgendaDetail(id);
//    }
//
//    private void fillView(final Agenda agenda){
//        if(agenda==null){
//            return;
//        }
//        titleView.setText(agenda.title);
//        areaView.setText(agenda.activityLocation+"  查看地图");
//        areaView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Util.startActivity(ReservationActivity2.this,MapActivity.class);
//            }
//        });
//        String time=Util.getFormatDate(agenda.startTime,"HH:mm")+"-"+Util.getFormatDate(agenda.endTime,"HH:mm");
//        timeView.setText(time);
//        switch (agenda.status){
//            case 0:
//                reserveLayout.setVisibility(View.VISIBLE);
//                appointView.setText(Util.getFormatDate(agenda.appointTime,"HH:mm"));
//                stateView.setText(getString(R.string.start_reverse));
//                break;
//            case 1:
//                ringsView.setVisibility(View.VISIBLE);
//                ringsView.spread();
//                ringsView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        reserve(agenda.id);
//                    }
//                });
//                break;
//            case 2:
//                reserveLayout.setVisibility(View.VISIBLE);
//                successView.setVisibility(View.VISIBLE);
//                appointView.setVisibility(View.GONE);
//                stateView.setText(getString(R.string.reversed));
//                break;
//            case 3:
//                resultView.setVisibility(View.VISIBLE);
//                resultView.setText(getString(R.string.reverse_full));
//                break;
//            case 4:
//                resultView.setVisibility(View.VISIBLE);
//                resultView.setText(getString(R.string.reverse_finish));
//                break;
//        }
//
//        Glide.with(this).load(agenda.imgUrl).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                KLog.d("blur");
//                blurView.setImageBitmap(Util.blur(ReservationActivity2.this,resource));
//            }
//        });
//
//        Glide.with(this).load(agenda.imgUrl).into(topImage);
//    }
//
//    private void reserve(int id){
//        JSONStringer jsonStringer = new JSONStringer();
//        try {
//            jsonStringer.object()
//                    .key("id").value(id)
//                    .key("userId").value(AccountInfo.getInstance().getUserId())
//                    .endObject();
//            KLog.json(jsonStringer.toString());
//        } catch (JSONException e) {
//            KLog.i("JsonException");
//        }
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
//        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).reserveActivity(body).enqueue(new NetCallback<Boolean>() {
//            @Override
//            public void onSucceed(Boolean data) {
//                reserveLayout.setVisibility(View.VISIBLE);
//                successView.setVisibility(View.VISIBLE);
//                appointView.setVisibility(View.GONE);
//                stateView.setText(getString(R.string.reversed));
//                ringsView.setVisibility(View.GONE);
//            }
//        });
//    }
//
//    @Override
//    public void success(Agenda data) {
//        fillView(data);
//    }
//
//    @Override
//    public void failed() {}
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mPresenter.onDestory();
//        ringsView.stop();
//    }
//
//    public static void start(Context context, int id){
//        Intent intent=new Intent(context,ReservationActivity2.class);
//        intent.putExtra(Constants.INFO_ID,id);
//        context.startActivity(intent);
//    }
//
//}
