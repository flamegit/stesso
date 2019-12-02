package com.tmmt.innersect.ui.fragment;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.BrandList;
import com.tmmt.innersect.mvp.presenter.CommonPresenter;
import com.tmmt.innersect.mvp.view.CommonView;
import com.tmmt.innersect.ui.adapter.BrandListAdapter2;
import com.tmmt.innersect.widget.PinnedHeaderDecoration;
import com.tmmt.innersect.widget.WaveSideBarView;
import butterknife.BindView;

/**
 * Created by Administrator on 2016/10/8.
 */
public class BrandListFragment extends BaseFragment implements CommonView<BrandList> {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.side_view)
    WaveSideBarView mSideBarView;

    BrandListAdapter2 mAdapter;
    CommonPresenter mPresenter;
    BrandList mContent;

    @Override
    int getLayout() {
        return R.layout.fragment_brand_list;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

//        mAdapter=new AdvancedAdapter<>(R.layout.viewholder_brand_item,((holder, position, data) -> {
//            final Context context=holder.itemView.getContext();
//            ImageView imageView=holder.get(R.id.icon_view);
//            holder.<TextView>get(R.id.title_view).setText(data.name);
//            Glide.with(context).load(data.url).into(imageView);
//            holder.itemView.setOnClickListener(v -> Util.openTarget(context,data.schema,""));
//        }));

        mAdapter=new BrandListAdapter2();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.addItemDecoration(new PinnedHeaderDecoration());

        mPresenter=new CommonPresenter();
        mPresenter.attachView(this);
        mPresenter.getBrandList2();
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
    }

    private int getLetterPosition(String letter){

        for(int i=0;i<mAdapter.getItemCount();i++){
            if(letter.equalsIgnoreCase(mAdapter.getLetter(i))){
                return i;
            }
        }
        return -1;
    }

    @Override
    public void success(BrandList data) {
        mContent=data;
        mAdapter.addItems(data);
    }
    @Override
    public void failed() {}



}
