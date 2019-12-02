package com.tmmt.innersect.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.HotSpotItem;
import com.tmmt.innersect.ui.adapter.SearchAdapter;
import com.tmmt.innersect.ui.adapter.SearchFragmentAdapter;
import com.tmmt.innersect.ui.fragment.CommonDialogFragment;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.SystemUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.search_view)
    EditText mSearchView;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.result_view)
    View mResultView;

    @BindView(R.id.delete_view)
    View mDeleteView;

    SearchFragmentAdapter mAdapter;
    SearchAdapter mSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsUtil.reportEvent("home_search");
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        mSearchAdapter=new SearchAdapter();
        mSearchAdapter.setListener((p,t)->{
            CommonDialogFragment dialogFragment= CommonDialogFragment.newInstance(R.layout.dialog_order,"清除搜索记录");
            dialogFragment.show(getSupportFragmentManager(),"search");
            dialogFragment.setActionListener(new CommonDialogFragment.ActionListener() {
                @Override
                public void doAction() {
                    mSearchAdapter.deleteHistory(p);
                }
                @Override
                public void cancel() {
                }
            });
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mSearchAdapter);
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mAdapter=new SearchFragmentAdapter(getSupportFragmentManager());
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mAdapter);
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start==0){
                    if(count>0){
                        mDeleteView.setVisibility(View.VISIBLE);
                    }else {
                        mDeleteView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}

        });
        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String word=mSearchView.getText().toString().trim();
                return search(word);
            }
        });

        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getHotSpot().enqueue(new NetCallback<HotSpotItem>() {
            @Override
            public void onSucceed(HotSpotItem data) {
                mSearchAdapter.fillContent(data);
            }
        });
    }

    private boolean search(String word){
        AnalyticsUtil.reportEvent("search_inputbox_searchIcon");
        if(word.isEmpty()){
            SystemUtil.reportServerError("搜索关键字不能为空");
            return false;
        }
        AccountInfo.getInstance().saveSearchHistory(word);
        mRecyclerView.setVisibility(View.GONE);
        mTabLayout.setVisibility(View.VISIBLE);
        mAdapter.search(word);
        SystemUtil.hideKey(this,mSearchView);
        return true;
    }

    @OnClick(R.id.delete_view)
    void delete(){
        mSearchView.setText("");
    }

    @OnClick(R.id.close_view)
    void close(){
        onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String word=intent.getStringExtra(Constants.KEY_WORD);
        if(word!=null ){
            mSearchView.setText(word);
            mSearchView.setSelection(word.length());
            search(word);
        }
    }
}
