package com.tmmt.innersect.ui.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.socks.library.KLog;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.MessageStatus;
import com.tmmt.innersect.ui.activity.AccountActivity;
import com.tmmt.innersect.ui.activity.AwardActivity;
import com.tmmt.innersect.ui.activity.CouponActivity;
import com.tmmt.innersect.ui.activity.FeedbackActivity;
import com.tmmt.innersect.ui.activity.HelpActivity;
import com.tmmt.innersect.ui.activity.IShare;
import com.tmmt.innersect.ui.activity.MessageActivity;
import com.tmmt.innersect.ui.activity.OrdersListActivity;
import com.tmmt.innersect.ui.activity.SettingActivity;
import com.tmmt.innersect.ui.adapter.AdvancedAdapter;
import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.RedPointDrawable;
import com.tmmt.innersect.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by flame on 2017/4/19.
 */

public class PersonInfoFragment extends BaseFragment {

    private static final int[] ICONS = {
            R.mipmap.help_icon,
            R.mipmap.connect_service,
            R.mipmap.feedback_icon,
            R.mipmap.share_icon
    };

    private static final int[] ITEMS = {
            R.string.sale_hint,
            R.string.connect_service,
            R.string.service,
            R.string.share,
    };

    @BindView(R.id.user_view)
    TextView mUserView;
    AdvancedAdapter<Pair<String, Integer>> mAdapter;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.message_view)
    ImageView mMessageView;

    IShare mIShare;

    MessageStatus mStatus;

    @Override
    public void onAttach(Context context) {
        if (context instanceof IShare) {
            mIShare = (IShare) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement IShare");
        }
        super.onAttach(context);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        KLog.d(AccountInfo.getInstance().getUserId());
        AnalyticsUtil.reportEvent("home_mytab");
        List<Pair<String, Integer>> info = new ArrayList<>();
        for (int i = 0; i < ITEMS.length; i++) {
            info.add(new Pair<>(getString(ITEMS[i]), ICONS[i]));
        }

        mMessageView.setOnClickListener(v -> Util.startActivity(getContext(), MessageActivity.class));
        mAdapter = new AdvancedAdapter<>(R.layout.viewholder_person_info, (holder, position, data) -> {
            holder.<TextView>get(R.id.title_view).setText(data.first);
            ImageView second=holder.get(R.id.icon_view);
            second.setImageResource(data.second);

            if(position==1 && mStatus!=null){
                second.setBackground(mStatus.cn==1?new RedPointDrawable(getContext()):null);
            }

            holder.itemView.setOnClickListener(v -> {
                switch (position) {
                    case 0:
                        Util.startActivity(getContext(), HelpActivity.class);
                        AnalyticsUtil.reportEvent(AnalyticsUtil.MY_SHOPPINGNOTICE);
                        break;
                    case 1:
                        //Util.startActivity(getContext(), MoorActivity.class);
                        Util.startMoor(getContext());
                        second.setBackground(null);
                        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).setMessageRead("flagsn").enqueue(new NetCallback<String>() {
                            @Override
                            public void onSucceed(String data) {}
                        });
                        break;
                    case 2:
                        Util.startActivity(getContext(), FeedbackActivity.class);
                        break;
                    case 3:
                        if (mIShare != null) {
                            mIShare.shareApp();
                        }
                        break;
                }
            });
        });
        mAdapter.addItems(info);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AccountInfo.getInstance().isLogin()) {
            mUserView.setText(AccountInfo.getInstance().getUser().name);
        }
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getMessageStatus().enqueue(new NetCallback<MessageStatus>() {
            @Override
            public void onSucceed(MessageStatus data) {
                mAdapter.notifyItemChanged(1);
                mStatus=data;
                if(data.isRead()){
                    mMessageView.setBackground(null);
                }else {
                    mMessageView.setBackground(new RedPointDrawable(getContext()));
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mIShare = null;
    }

    @Override
    int getLayout() {
        return R.layout.fragment_person;
    }

    @OnClick({R.id.order_view, R.id.coupon_view,
            R.id.award_view, R.id.account_view,R.id.setting_view})
    void open(View view) {
        if (view.getId() == R.id.order_view) {
            Util.startActivity(getContext(), OrdersListActivity.class);
        } else if (view.getId() == R.id.coupon_view) {
            Util.startActivity(getContext(), CouponActivity.class);
        } else if (view.getId() == R.id.award_view) {
            Util.startActivity(getContext(), AwardActivity.class);
            AnalyticsUtil.reportEvent(AnalyticsUtil.MY_ACCOUNT);
        } else if (view.getId() == R.id.account_view) {
            Util.startActivity(getContext(), AccountActivity.class);
            AnalyticsUtil.reportEvent(AnalyticsUtil.MY_ACCOUNT);
        }else if(view.getId() == R.id.setting_view){
            Util.startActivity(getContext(), SettingActivity.class);
        }
    }

}
