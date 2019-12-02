package com.tmmt.innersect.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.DialCode;
import com.tmmt.innersect.ui.adapter.DataBindingAdapter;
import com.tmmt.innersect.utils.Util;
import com.tmmt.innersect.widget.WaveSideBarView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by flame on 2017/4/19.
 */

public class DialCodeActivity extends AppCompatActivity{

    DataBindingAdapter<DialCode> mAdapter;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.side_view)
    WaveSideBarView mSideBarView;
    private List<DialCode> mContent;
    public static final String CODE_KEY="code_key";

    @OnClick(R.id.close_view)
    void close(){
        onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialcode);
        ButterKnife.bind(this);
        init();
    }
    public void init(){
        mAdapter=new DataBindingAdapter<>(R.layout.viewholder_dialcode,
            (holder,position, data)-> {
                Intent intent =new Intent();
                intent.putExtra(CODE_KEY,data.dial_code);
                setResult(RESULT_OK,intent);
                finish();
            });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mSideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener(){
            @Override
            public void onLetterChange (String letter){
                int pos = getLetterPosition(letter);
                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });
        getSource();
    }

    private int getLetterPosition(String letter){
        for(int i=0;i<mContent.size();i++){
            if(letter.equalsIgnoreCase(mContent.get(i).getLetter())){
                return i;
            }
        }
        return -1;
    }

    private void getSource(){
        Observable.fromCallable(new Callable<List<DialCode>>() {
            @Override
            public List<DialCode> call() throws Exception {
                try {
                    InputStream in=getResources().getAssets().open("diallingcode.json");
                    InputStreamReader json=new InputStreamReader(in);
                    List<DialCode> results= Util.jsonToArrayList(json,DialCode.class);
                    Collections.sort(results, new Comparator<DialCode>() {
                        @Override
                        public int compare(DialCode o1, DialCode o2) {
                            return o1.getLetter().compareTo(o2.getLetter());
                        }
                    });
                    return results;
                }catch (IOException e){
                }
                return null;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<DialCode>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onNext(List<DialCode> result) {
                        mContent=result;
                        mAdapter.addItems(mContent);
                    }
                });
    }
}
