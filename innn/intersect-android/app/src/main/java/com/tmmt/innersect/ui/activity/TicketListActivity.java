//package com.tmmt.innersect.ui.activity;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.CountDownTimer;
//import android.support.v4.view.ViewCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.socks.library.KLog;
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.common.Constants;
//import com.tmmt.innersect.datasource.net.ApiManager;
//import com.tmmt.innersect.datasource.net.NetCallback;
//
//import com.tmmt.innersect.mvp.model.ShopCart;
//import com.tmmt.innersect.mvp.model.ShopItem;
//import com.tmmt.innersect.mvp.model.SpuViewModel;
//import com.tmmt.innersect.mvp.model.TicketTime;
//import com.tmmt.innersect.ui.adapter.TicketAdapter;
//import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;
//import com.tmmt.innersect.ui.adapter.TicketListAdapter;
//import com.tmmt.innersect.utils.SystemUtil;
//import com.tmmt.innersect.utils.Util;
//import org.json.JSONException;
//import org.json.JSONStringer;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
//public class TicketListActivity extends AppCompatActivity implements TicketAdapter.OnSizeChangeListener{
//    @BindView(R.id.recycler_view)
//    RecyclerView mRecyclerView;
//    @BindView(R.id.season_view)
//    TextView mSeasonView;
//    @BindView(R.id.five_view)
//    TextView mFiveView;
//    @BindView(R.id.six_view)
//    TextView mSixView;
//    @BindView(R.id.seven_view)
//    TextView mSevenView;
//    @BindView(R.id.ticket_tab1)
//    View mTab1;
//    @BindView(R.id.ticket_tab2)
//    View mTab2;
//    @BindView(R.id.ticket_tab3)
//    View mTab3;
//    @BindView(R.id.ticket_tab4)
//    View mTab4;
//
//    @BindView(R.id.indicator_view)
//    View mIndicatorView;
//    @BindView(R.id.info_view)
//    TextView mInfoView;
//
//    @BindView(R.id.title_view)
//    TextView mTitleView;
//
//    TextView mPopInfoView;
//    int mCurrIndex;
//    TicketAdapter mAdapter;
//    TicketListAdapter mListAdapter;
//    TextView mSelectView;
//    View mPopupView;
//    PopupWindow mPopupWindow;
//    CountDownTimer mTimer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ticket_list);
//        ButterKnife.bind(this);
//
//        mAdapter=new TicketAdapter();
//        mAdapter.setListener(this);
//
//        mListAdapter=new TicketListAdapter();
//        mListAdapter.setListener(this);
//
//        mRecyclerView.setAdapter(mAdapter);
//
//        mCurrIndex=0;
//        mSelectView=mSeasonView;
//        mSeasonView.setTextColor(Color.BLACK);
//
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        createPopupWindow();
//
//        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getTicketTime().enqueue(new NetCallback<TicketTime>() {
//            @Override
//            public void onSucceed(TicketTime data) {
//                fillView(data);
//            }
//        });
//        onSizeChanged();
//    }
//
//    @OnClick(R.id.back_view)
//    void back(){
//        onBackPressed();
//    }
//
//    public void loadSpuInfo(final long productId) {
//        JSONStringer jsonStringer = new JSONStringer();
//        try {
//            jsonStringer.object()
//                    .key("deliveryAddress").value("APP")
//                    .key("productId").value(productId)
//                    .endObject();
//            KLog.json(jsonStringer.toString());
//        } catch (JSONException e) {
//            KLog.i("JsonException");
//        }
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
//        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getGroupInfo("v1",body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<SpuViewModel>() {
//                    @Override
//                    public void onCompleted() {}
//                    @Override
//                    public void onError(Throwable e) {
//                        KLog.d(e);
//                        SystemUtil.reportNetError(e);
//                    }
//                    @Override
//                    public void onNext(SpuViewModel viewModel) {
//                        mAdapter.addItems(viewModel.data);
//                        mAdapter.changeData(0);
//                    }
//                });
//    }
//
//    private void fillView(TicketTime ticketTime){
//        mTitleView.setText(ticketTime.title);
//        loadSpuInfo(ticketTime.productId);
//        long curr=System.currentTimeMillis();
//        long end=ticketTime.pet;
//        if(end>curr){
//            mTimer=new CountDownTimer(end-curr,1000) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//                    long total=millisUntilFinished/1000;
//                    long s=total%60;
//                    long min=total/60;
//                    long m=min%60;
//                    long hour=min/60;
//                    long h=hour%24;
//                    long d=hour/24;
////                    mDayView.setText(String.format("%02d",d));
////                    mHourView.setText(String.format("%02d",h));
////                    mMinuteView.setText(String.format("%02d",m));
////                    mSecondView.setText(String.format("%02d",s));
//                }
//                @Override
//                public void onFinish() {
//
//                }
//            };
//
//        }else {
//
//        }
//        if(mTimer!=null){
//            mTimer.start();
//        }
//    }
//
//    @Override
//    public void onSizeChanged() {
//        int count=mAdapter.getSelectCount();
//        float price=mAdapter.getSelectPrice();
//        mInfoView.setText(String.format("总计 （%d张）：￥%.2f",count,price));
//        if(mPopInfoView!=null){
//            mPopInfoView.setText(String.format("总计 （%d张）：￥%.2f",count,price));
//        }
//        if(count==0 && mPopupWindow!=null &&mPopupWindow.isShowing()){
//            mPopupWindow.dismiss();
//        }
//    }
//    @OnClick(R.id.arrow_view)
//    void showWindow(){
//        List<SpuViewModel.Group> list=mAdapter.getSelect();
//        if(list.size()>0){
//            mListAdapter.addItems(list);
//            mPopupWindow.showAtLocation(mPopupView, Gravity.BOTTOM, 0, 0);
//            backgroundAlpha(0.4f);
//        }
//    }
//
//    @OnClick(R.id.action_view)
//    void addOrder() {
//        ArrayList<SpuViewModel.Group> list=mAdapter.getSelect();
//        if(list.isEmpty()){
//            Toast.makeText(this,"您还没有选择购买的票",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        ArrayList<ShopItem> shopList=new ArrayList<>();
//        for(SpuViewModel.Group group:list){
//            ShopItem item=new ShopItem();
//            item.price=group.salePrice;
//            item.omsSkuId=group.omsSkuId;
//            item.name=group.skuName;
//            item.skuThumbnail=group.thumbnail;
//            item.quantity=group.quantity;
//            item.productId=group.productId;
//            item.skuId=group.skuId;
//            item.size=group.date;
//            shopList.add(item);
//        }
//        //ShopCart.getInstance().addTickets(shopList);
//        Intent intent=new Intent(this,TicketSettlementActivity.class);
//        intent.putParcelableArrayListExtra(Constants.LIST,shopList);
//        this.startActivity(intent);
//    }
//
//    private void backgroundAlpha(float bgAlpha) {
//        WindowManager.LayoutParams lp = getWindow().getAttributes();
//        lp.alpha = bgAlpha;
//        getWindow().setAttributes(lp);
//    }
//
//    @OnClick({R.id.ticket_tab1,R.id.ticket_tab2,R.id.ticket_tab3,R.id.ticket_tab4})
//    void selectItem(View view){
//        int distance=0;
//        int preIndex=mCurrIndex;
//        TextView preView=mSelectView;
//
//        switch (view.getId()){
//            case R.id.ticket_tab1:
//                mCurrIndex=0;
//                mSelectView=mSeasonView;
//                distance=mTab1.getLeft();
//                break;
//            case R.id.ticket_tab2:
//                mCurrIndex=1;
//                mSelectView=mFiveView;
//                distance=mTab2.getLeft();
//
//                break;
//            case R.id.ticket_tab3:
//                mCurrIndex=2;
//                mSelectView=mSixView;
//                distance=mTab3.getLeft();
//
//                break;
//            case R.id.ticket_tab4:
//                mCurrIndex=3;
//                mSelectView=mSevenView;
//                distance=mTab4.getLeft();
//                break;
//        }
//        if(preIndex==mCurrIndex){
//            return;
//        }
//
//        ViewCompat.animate(mIndicatorView).translationX(distance - Util.dip2px(16));
//        if(preView!=null){
//            preView.setTextColor(Color.parseColor("#9b9b9b"));
//        }
//        if(mSelectView!=null){
//            mSelectView.setTextColor(Color.BLACK);
//        }
//        mAdapter.changeData(mCurrIndex);
//    }
//    private boolean createPopupWindow() {
//
//        mPopupView= LayoutInflater.from(this).inflate(R.layout.popupwindow_custom,null);
//        RecyclerView recyclerView=(RecyclerView)mPopupView.findViewById(R.id.recycler_view);
//        recyclerView.setAdapter(mListAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
//        View down=mPopupView.findViewById(R.id.pop_arrow);
//        down.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPopupWindow.dismiss();
//                mAdapter.notifyDataSetChanged();
//            }
//        });
//        View action=mPopupView.findViewById(R.id.pop_action);
//        action.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addOrder();
//                mPopupWindow.dismiss();
//            }
//        });
//        mPopInfoView =(TextView)mPopupView.findViewById(R.id.pop_info);
//        mPopupWindow = new PopupWindow(mPopupView,
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        mPopupWindow.setFocusable(true);// 取得焦点
//        mPopupWindow.setAnimationStyle(R.style.pop_window_anim);
//        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroundAlpha(1f);
//            }
//        });
//        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
//        return true;
//    }
//}
//
//
