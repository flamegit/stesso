package com.tmmt.innersect.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.mvp.model.ImageInfo;
import com.tmmt.innersect.ui.adapter.CommonPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

public class ImageDetailActivity extends AppCompatActivity {


    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.brand_view)
    TextView mBrandView;

    @BindView(R.id.title_view)
    TextView mTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        ButterKnife.bind(this);
        CommonPagerAdapter<ImageInfo> adapter=new CommonPagerAdapter<>(false,(container, content) -> {
            final PhotoView contentView = new PhotoView(container.getContext());
            //container.addView(contentView);
            Glide.with(container.getContext())
                    .load(content.imgUrl)
                    .fitCenter()
                    .into(contentView);
            return contentView;
        });

        String title=getIntent().getStringExtra(Constants.TITLE);
        String brand =getIntent().getStringExtra(Constants.BRAND_ID);

        List<ImageInfo> list=getIntent().getParcelableArrayListExtra(Constants.LIST);
        mViewPager.setAdapter(adapter);
        mTitleView.setText(title);
        mBrandView.setText(brand);
        adapter.addItems(list);
        mViewPager.setCurrentItem(getIntent().getIntExtra(Constants.INDEX,0));

    }

    @OnClick(R.id.close_view)
    void close(){
        onBackPressed();
    }


    public static void start(Context context, String title, String brand, ArrayList<ImageInfo> list,int index) {
        Intent intent = new Intent(context, ImageDetailActivity.class);
        intent.putExtra(Constants.TITLE, title);
        intent.putExtra(Constants.BRAND_ID, brand);
        //intent.putStringArrayListExtra(Constants.LIST,list);
        intent.putParcelableArrayListExtra(Constants.LIST,list);
        intent.putExtra(Constants.INDEX, index);
        context.startActivity(intent);
    }
}
