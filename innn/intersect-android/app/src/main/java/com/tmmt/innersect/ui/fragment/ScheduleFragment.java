//package com.tmmt.innersect.ui.fragment;
//
//
//import android.content.DialogInterface;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.TextView;
//
//import com.tmmt.innersect.R;
//import com.tmmt.innersect.mvp.model.AgendaList;
//import com.tmmt.innersect.mvp.presenter.CommonPresenter;
//import com.tmmt.innersect.mvp.view.CommonView;
//import com.tmmt.innersect.ui.adapter.ScheduleAdapter;
//import com.tmmt.innersect.utils.Util;
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * Created by flame on 2017/4/12.
// */
//
//public class ScheduleFragment extends BaseFragment implements CommonView<AgendaList> {
//
//    private static final String[] sDates={
//            "2017-10-05",
//            "2017-10-06",
//            "2017-10-07",
//    };
//
//    final String[] items = { "10月5日","10月6日","10月7日" };
//
//    @BindView(R.id.recycler_view)
//    RecyclerView mRecyclerView;
//    @BindView(R.id.date_view)
//    TextView mDateView;
//    ScheduleAdapter mAdapter;
//    CommonPresenter mPresenter;
//    private String mCurrDate;
//    private int mIndex;
//    @BindView(R.id.bind_view)
//    TextView mBindView;
//
//    @Override
//    int getLayout() {
//        return R.layout.fragment_schedule;
//    }
//    @Override
//    protected void initView(View view) {
//        super.initView(view);
//        setIndex();
//        mCurrDate=sDates[mIndex];
//        mAdapter=new ScheduleAdapter();
//        mPresenter=new CommonPresenter();
//        mPresenter.attachView(this);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mPresenter.loadAgendaList(mCurrDate);
//    }
//
//    private void setIndex(){
//        long six=Util.getTimeStamp(2017,9,6);
//        long seven=Util.getTimeStamp(2017,9,7);
//        long curr=System.currentTimeMillis();
//
//        if(curr<six){
//            mIndex=0;
//        }else if(curr<seven){
//            mIndex=1;
//        }else {
//            mIndex=2;
//        }
//        mDateView.setText(items[mIndex]);
//    }
//
//    @OnClick({R.id.date_view,R.id.change_view})
//    void changeDate(){
//        showSingleChoiceDialog();
//    }
//
//    @Override
//    public void success(AgendaList data) {
//        if(data.isBindRing){
//            mBindView.setText(getString(R.string.bind_));
//        }else {
//            mBindView.setText(getString(R.string.bind_ring));
//        }
////        mBindView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent=new Intent(getContext(),BindRingActivity.class);
////                Util.checkLogin(getContext(),intent);
////            }
////        });
//        mAdapter.addItems(data.agendaList);
//    }
//
//    @Override
//    public void failed() {}
//
//    private void showSingleChoiceDialog(){
//
//        final AlertDialog.Builder singleChoiceDialog =
//                new AlertDialog.Builder(getContext());
//        singleChoiceDialog.setTitle(getString(R.string.chose_date));
//        // 第二个参数是默认选项，此处设置为0
//        singleChoiceDialog.setSingleChoiceItems(items,mIndex,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                       mIndex=which;
//                    }
//                });
//        singleChoiceDialog.setPositiveButton(getString(android.R.string.ok),
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mDateView.setText(items[mIndex]);
//                        mCurrDate=sDates[mIndex];
//                        mPresenter.loadAgendaList(mCurrDate);
//                    }
//                });
//        singleChoiceDialog.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                      dialog.dismiss();
//            }
//        });
//        singleChoiceDialog.show();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mPresenter.onDestory();
//    }
//}
