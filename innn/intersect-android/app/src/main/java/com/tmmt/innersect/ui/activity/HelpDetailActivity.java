package com.tmmt.innersect.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.Problem;
import com.tmmt.innersect.ui.adapter.AdvancedAdapter;
import com.tmmt.innersect.ui.adapter.decoration.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpDetailActivity extends AppCompatActivity {

    @BindView(R.id.title_view)
    TextView mTitleView;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_detail);
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra(Constants.TITLE);
        mTitleView.setText(title);
        List<Problem> problemList = getIntent().getParcelableArrayListExtra(Constants.PROBLEM_INFO);
        //ProblemAdapter adapter=new ProblemAdapter();
        AdvancedAdapter<Problem> adapter = new AdvancedAdapter<>(R.layout.viewholder_problem,
                (holder, position, data) -> {
                    holder.<TextView>get(R.id.title_view).setText(data.title);
                    holder.<TextView>get(R.id.content_view).setText(data.note);
                    final View contentView = holder.get(R.id.content_view);
                    final ImageView arrowView = holder.get(R.id.arrow_view);
                    holder.get(R.id.title_layout).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (contentView.getVisibility() == View.GONE) {
                                contentView.setVisibility(View.VISIBLE);
                                arrowView.setImageResource(R.mipmap.black_up_arrow);
                            } else {
                                contentView.setVisibility(View.GONE);
                                arrowView.setImageResource(R.mipmap.gray_down_arrow);
                            }
                        }
                    });
                });
        adapter.addItems(problemList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

    }

    @OnClick(R.id.back_view)
    void back() {
        onBackPressed();
    }
}
