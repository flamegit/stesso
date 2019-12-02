package com.tmmt.innersect.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.tmmt.innersect.R;
import com.tmmt.innersect.mvp.model.BaseItem;
import com.tmmt.innersect.mvp.model.ColorInfo;
import com.tmmt.innersect.mvp.model.CommodityItem2;
import com.tmmt.innersect.mvp.model.CommodityTitle;
import com.tmmt.innersect.mvp.model.CommonItem;
import com.tmmt.innersect.mvp.model.ModelItem;
import com.tmmt.innersect.mvp.model.MoreItem;
import com.tmmt.innersect.ui.adapter.viewholder.CommonViewHolder;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.Util;
import com.tmmt.innersect.widget.CirclePageIndicator;

import java.util.List;

/**
 * Created by flame on 2017/9/19.
 */

public class HomeAdapter extends BaseAdapter<BaseItem> {

    @Override
    public int getItemViewType(int position) {
        return mContent.get(position).getType();
    }

    @Override
    protected int getLayoutId(int viewType) {
        switch (viewType) {
            case 0:
                return R.layout.viewholder_new_list;
            case 1:
                return R.layout.viewholder_select;
            case 2:
                return R.layout.viewholder_brand_list;
            case 3:
                return R.layout.viewholder_view_pager;
            case 4:
                return R.layout.viewholder_commodity_head;
            case 5:
                return R.layout.viewholder_commodity_list;
            case 6:
                return R.layout.viewholder_center_button;
            case 7:
                return R.layout.viewholder_lottery_enter;
        }
        return R.layout.viewholder_line;
    }

    public void fillContent(List<ModelItem> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        mContent.clear();
        for (ModelItem item : list) {
            if (item.getItem() == null) {
                mContent.add(item);
            } else {
                mContent.addAll(item.getItem());
            }
        }
        notifyDataSetChanged();
    }

    @Override
    protected void fillViewHolder(CommonViewHolder holder, int position, final BaseItem data) {
        final Context context = holder.itemView.getContext();
        final int margin = Util.dip2px(8);
        String showAll = null;
        String event="home_productExhibition_all";
        switch (data.getType()) {
            case 0:
                ModelItem weekItem = (ModelItem) data;
                showAll = weekItem.more;
                fillView(context,weekItem,holder);
                break;
            case 1:
                ModelItem selectItem = (ModelItem) data;
                holder.<TextView>get(R.id.title_view).setText(selectItem.title);
                List<CommonItem> selectList = selectItem.content;
                if (selectList != null) {
                    if (selectList.size() >= 1) {
                        ImageView leftImage = holder.get(R.id.image_left);
                        leftImage.setOnClickListener(v -> {
                            Util.openTarget(context, selectList.get(0).schema, "");
                        });
                        leftImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        Glide.with(context).load(selectList.get(0).bgUrl).into(leftImage);
                        holder.<TextView>get(R.id.text_left).setText(selectList.get(0).name);
                    }
                    if (selectList.size() >= 2) {
                        ImageView topImage = holder.get(R.id.image_right_top);
                        topImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        Glide.with(context).load(selectList.get(1).bgUrl).into(topImage);
                        topImage.setOnClickListener(v -> {
                            Util.openTarget(context, selectList.get(1).schema, "");
                        });
                        holder.<TextView>get(R.id.text_right_top).setText(selectList.get(1).name);
                    }
                    if (selectList.size() >= 3) {
                        ImageView bottomImage = holder.get(R.id.image_right_bottom);
                        bottomImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        bottomImage.setOnClickListener(v -> {
                            Util.openTarget(context, selectList.get(2).schema, "");
                        });
                        Glide.with(context).load(selectList.get(2).bgUrl).into(bottomImage);
                        holder.<TextView>get(R.id.text_right_bottom).setText(selectList.get(2).name);
                    }
                }
                break;
            case 2:
                ModelItem brandItem = (ModelItem) data;
                holder.<TextView>get(R.id.title_view).setText(brandItem.title);
                showAll = brandItem.more;
                event="home_brand_all";
                List<CommonItem> brandList = brandItem.content;
                FlexboxLayout layout = holder.get(R.id.brand_container);
                layout.removeAllViews();
                int size = Util.getWindowWidth() / 3;
                holder.<TextView>get(R.id.title_view).setText(brandItem.title);
                int brandSize = Math.min(brandList.size(), 6);
                for (int i = 0; i < brandSize; i++) {
                    View item = LayoutInflater.from(context).inflate(R.layout.couple_item, layout, false);
                    final int index = i;
                    item.setOnClickListener(v -> {
                        Util.openTarget(context, brandList.get(index).schema, "");
                        AnalyticsUtil.reportEvent("home_brand_click");
                    });
                    ImageView imageView = item.findViewById(R.id.icon_view);
                    TextView textView = item.findViewById(R.id.title_view);
                    Util.fillImage(context,brandList.get(i).picUrl,imageView);
                    textView.setText(brandList.get(i).name);
                    FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(size - 2 * margin, size);
                    params.setMargins(margin, margin, margin, margin);
                    item.setLayoutParams(params);
                    layout.addView(item);
                }
                break;
            case 3:
                ModelItem popupItem = (ModelItem) data;
                showAll = popupItem.more;
                event="home_popup_all";
                holder.<TextView>get(R.id.title_view).setText(popupItem.title);
                ViewPager viewPager = holder.get(R.id.view_pager);
                //viewPager.setNestedScrollingEnabled(false);
                CirclePageIndicator indicator = holder.get(R.id.pager_indicator);
                PopupPagerAdapter adapter = new PopupPagerAdapter();
                adapter.addItems(popupItem.content);
                viewPager.setAdapter(adapter);
                indicator.setViewPager(viewPager);
                if (adapter.getCount() <= 1) {
                    indicator.setVisibility(View.GONE);
                }
                break;
            case 4:
                CommodityTitle titleItem = (CommodityTitle) data;
                event="home_productPick_allTop";
                showAll = titleItem.more;
                ImageView bgView = holder.get(R.id.bg_view);
                Util.fillImage(context,titleItem.headerBgUrl,bgView);
                holder.<TextView>get(R.id.name_view).setText(titleItem.headerText);
                if (titleItem.title == null || titleItem.title.isEmpty()) {
                    holder.<TextView>get(R.id.title_view).setVisibility(View.GONE);
                } else {
                    holder.<TextView>get(R.id.title_view).setText(titleItem.title);
                }
                holder.<TextView>get(R.id.name_view).setText(titleItem.headerText);
                break;
            case 5:
                fillView(context, (CommodityItem2) data, holder);
                break;
            case 6:
                holder.itemView.setOnClickListener(v -> {
                    Util.openTarget(context, ((MoreItem) data).more, "");
                    AnalyticsUtil.reportEvent("home_productPick_allBottom");
                });
                break;
            case 7:
                ModelItem lotteryData = (ModelItem)data;
                if(lotteryData.title==null|| lotteryData.title.isEmpty()){
                    holder.<TextView>get(R.id.title_view).setVisibility(View.GONE);
                }else {
                    holder.<TextView>get(R.id.title_view).setText(lotteryData.title);
                }
                ImageView enter=holder.get(R.id.image_view);
                CommonItem lotteryItem = lotteryData.content.get(0);
                Glide.with(context).load(lotteryItem.picUrl).into(enter);
                enter.setOnClickListener(v -> Util.openTarget(context,lotteryItem.schema,""));

        }
        View moreView = holder.get(R.id.show_all);
        if (moreView != null && showAll != null) {
            final String schema = showAll;
            final String all=event;
            moreView.setOnClickListener(v -> {
                Util.openTarget(context, schema, "");
                AnalyticsUtil.reportEvent(all);
            });
        }
        View moreText=holder.get(R.id.show_all_text);
        if((showAll==null ||showAll.isEmpty()) && moreText!=null){
            moreText.setVisibility(View.GONE);
        }
    }

    private void fillView(Context context, ModelItem data, CommonViewHolder holder) {
        final int margin = Util.dip2px(8);
        FlexboxLayout container = holder.get(R.id.container);
        container.removeAllViews();
        holder.<TextView>get(R.id.title_view).setText(data.title);
        int width = Util.getWindowWidth();
        int w = (int) (width / 2.5);
        int h = Util.dip2px(300);
        List<CommonItem> itemList = data.content;
        for (int i = 0; i < itemList.size(); i++) {
            View item = LayoutInflater.from(context).inflate(R.layout.include_new_item, container, false);
            final int index = i;
            ImageView imageView = item.findViewById(R.id.icon_view);
            TextView textView = item.findViewById(R.id.title_view);
            TextView priceView = item.findViewById(R.id.price_view);
            TextView originPrice = item.findViewById(R.id.origin_price);
            Util.fillImage(context,itemList.get(i).picUrl,imageView);
            textView.setText(itemList.get(i).brand);
            priceView.setText("￥"+Util.getPrice(itemList.get(i).salePrice));
            item.setOnClickListener(v -> {
                Util.openTarget(context, itemList.get(index).schema, "");
                AnalyticsUtil.reportEvent("home_content_click");
            });
            if (itemList.get(i).originalPrice != itemList.get(i).salePrice) {

                originPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                originPrice.setText(String.format("￥%.2f",itemList.get(i).originalPrice));
            }else {
                originPrice.setText("");
            }
            FlexboxLayout.LayoutParams params;
            if (itemList.get(i).type == 1) {
                params = new FlexboxLayout.LayoutParams(w + 3 * margin, h);
            } else {
                params = new FlexboxLayout.LayoutParams(w, (h - margin) / 2);
            }
            item.setLayoutParams(params);
            container.addView(item);
        }
    }
    private void fillView(Context context, CommodityItem2 data, CommonViewHolder holder) {
        final int margin = Util.dip2px(8);
        int radius = Util.dip2px(8);
        List<ColorInfo> colors = data.color;
        LinearLayout colorLayout = holder.get(R.id.color_container);
        colorLayout.removeAllViews();
        if (colors != null && colors.size() > 1) {
            for (int i = 0; i < Math.min(5, colors.size()); i++) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(radius, radius);
                params.setMargins(i == 0 ? 0 : margin, margin, 0, margin);
                View view = new View(context);
                if (colors.get(i).displayName != null) {
                    try {
                        setColor(view, Color.parseColor(colors.get(i).displayName));
                        colorLayout.addView(view, params);
                    } catch (IllegalArgumentException e) {}
                }
            }
        }
        holder.get(R.id.sale_out).setVisibility(data.isSaleOut() ? View.VISIBLE : View.GONE);
        holder.<TextView>get(R.id.title_view).setText(data.brand);
        ImageView imageView = holder.get(R.id.icon_view);

        Util.fillImage(context,data.picUrl,imageView);
        holder.<TextView>get(R.id.desc_view).setText(data.name);
        holder.<TextView>get(R.id.price_view).setText(String.format("￥%.2f",data.salePrice));
        TextView originPrice = holder.get(R.id.origin_price);


        if (data.originalPrice > data.salePrice) {
            originPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
            originPrice.setText(String.format("￥%.2f",data.originalPrice));
        }else {
            originPrice.setText("");
        }
        holder.itemView.setOnClickListener(v -> {
            Util.openTarget(context, data.schema, "");
            AnalyticsUtil.reportEvent("home_productPick_click");
        });
    }
    private void setColor(View view, int color) {
        view.setBackgroundResource(R.drawable.circle_fill_bg);
        GradientDrawable mm = (GradientDrawable) view.getBackground();
        mm.setColor(color);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return mContent.get(position).getSpanSize();
                }
            });
        }
    }
}
