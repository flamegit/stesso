package com.tmmt.innersect.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.m7.imkfsdk.MoorActivity;
import com.socks.library.KLog;
import com.tmmt.innersect.BuildConfig;
import com.tmmt.innersect.R;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.common.Constants;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.CommodityViewModel;
import com.tmmt.innersect.mvp.model.ImageDetail;
import com.tmmt.innersect.mvp.model.ImageInfo;
import com.tmmt.innersect.mvp.model.ShopCart;
import com.tmmt.innersect.mvp.model.SpuViewModel;
import com.tmmt.innersect.mvp.presenter.CommodityPresenter;
import com.tmmt.innersect.mvp.presenter.ShopCartPresenter;
import com.tmmt.innersect.mvp.view.CommodityView;
import com.tmmt.innersect.mvp.view.ExtraView;

import com.tmmt.innersect.ui.adapter.TopPagerAdapter;
import com.tmmt.innersect.utils.AnalyticsUtil;
import com.tmmt.innersect.utils.SystemUtil;
import com.tmmt.innersect.utils.Util;
import com.tmmt.innersect.widget.CirclePageIndicator;
import com.tmmt.innersect.widget.CustomScrollView;
import com.tmmt.innersect.widget.QuantityView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CommodityDetailActivity extends BaseActivity implements CustomScrollView.ScrollViewListener,
        CommodityView, ExtraView {

    PopupWindow mPopupWindow;
    View mPopupView;
    @BindView(R.id.action_view)
    TextView mActionView;
    @BindView(R.id.root_view)
    FrameLayout mRootView;
    @BindView(R.id.shop_cart)
    View mShopCart;
    @BindView(R.id.shop_cart_size)
    TextView mShopSizeView;
    @BindView(R.id.info_view)
    TextView mInfoView;
    @BindView(R.id.progress_view)
    View mProgressView;
    QuantityView mQuantityView;
    PathMeasure mPathMeasure;
    float mCurrentPosition[] = new float[2];
    ShopCartPresenter mShopCartPresenter;
    CommodityPresenter mPresenter;
    private int mColorIndex = -1;
    private int mSizeIndex = -1;
    private View mColorView;
    private int mShopCartSize;
    /************************divide**********8*/
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.name_view)
    TextView mNameView;
    @BindView(R.id.indicator)
    CirclePageIndicator mIndicator;
    @BindView(R.id.scroll_view)
    CustomScrollView mScrollView;
    @BindView(R.id.top_bar)
    View mTopBar;
    @BindView(R.id.title_layout)
    View mTitleLayout;
    @BindView(R.id.size_table)
    ImageView mSizeTable;

    @BindView(R.id.color_container)
    ViewGroup mColorContainer;
    @BindView(R.id.detail_container)
    ViewGroup mDetailContainer;
    @BindView(R.id.brand_icon)
    ImageView mBrandIcon;
    @BindView(R.id.brand_title)
    TextView mBrandTitle;
    @BindView(R.id.desc_view)
    TextView mDescView;

    @BindView(R.id.info_switch)
    ImageView mInfoSwitch;
    @BindView(R.id.size_switch)
    ImageView mSizeSwitch;
    @BindView(R.id.origin_price)
    TextView mOriginPrice;

    @BindView(R.id.size_layout)
    View mSizeLayout;
    @BindView(R.id.title_brand)
    TextView mTitleBrand;
    @BindView(R.id.title_price)
    TextView mTitlePrice;
    @BindView(R.id.detail_text)
    View mDetailText;
    @BindView(R.id.detail_line)
    View mDetailLine;

    @BindView(R.id.info_detail)
    TextView mInfoDetail;
    @BindView(R.id.brand_view)
    View mBrandView;

    @BindView(R.id.share_text)
    TextView mShareHint;
    @BindView(R.id.buy_now)
    TextView mBuyView;

    View mHintView;

    TopPagerAdapter mAdapter;
    private View mSelectView;
    private long mProductId;
    private TextView[] mSizeViews;
    private View[] mColorViews;
    private TextView mAddCartView;

    SpuViewModel.Data mSpuInfo;
    CommodityViewModel.Commodity mCommodityInfo;
    private int mSelectIndex;
    TextView mPriceView;
    @BindView(R.id.share_result)
    TextView mShareResult;

    boolean createAndShow = false;
    boolean isSaleOut = false;
    boolean isBuyNow = false;
    boolean isPrize=false;

    long prizeId;
    float salePrice;
    boolean show;
    String shop;
    int skuId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_commodity_detail;
    }

    @Override
    protected String getTitleString() {
        return getString(R.string.commodity_detail);
    }

    @Override
    protected void initView() {
        super.initView();
        AnalyticsUtil.reportEvent("productdetail_staytime");
        mShopCartPresenter = new ShopCartPresenter();
        mShopCartPresenter.attachView(this);
        mScrollView.setScrollViewListener(this);
        //mInfoView.setText("￥0.00");
        mAdapter = new TopPagerAdapter(this);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
        mProductId = getIntent().getLongExtra(Constants.INFO_ID, -1);
        prizeId= getIntent().getLongExtra(Constants.ACTIVITY_ID,-1);

        shop=getIntent().getStringExtra(Constants.SHOP);

        skuId=getIntent().getIntExtra(Constants.SKU_ID,0);

        if(prizeId!=-1){
            isPrize=true;
            mActionView.setVisibility(View.GONE);
        }

        mPresenter = new CommodityPresenter(isPrize);
        mPresenter.attachView(this);
        salePrice=getIntent().getFloatExtra(Constants.TOTAL_PRICE,0);
        isSaleOut = getIntent().getBooleanExtra(Constants.IS_SALE_OUT, false);
        mPresenter.loadCommodityInfo(mProductId,skuId);

        mPresenter.loadSpuInfo(mProductId,shop);
        mShopCartPresenter.loadShopCart();
        if (isSaleOut) {
            mActionView.setText(getString(R.string.sale_out));
            mActionView.setTextColor(Color.WHITE);
            mBuyView.setVisibility(View.GONE);
            mActionView.setBackgroundColor(Color.parseColor("#9A9B9D"));
        }
    }

    public static void start(Context context, long id,int skuId) {
        Intent intent = new Intent(context, CommodityDetailActivity.class);
        intent.putExtra(Constants.INFO_ID, id);
        intent.putExtra(Constants.SKU_ID,skuId);
        context.startActivity(intent);
    }

    public static void start(Context context, long id) {
        Intent intent = new Intent(context, CommodityDetailActivity.class);
        intent.putExtra(Constants.INFO_ID, id);
        context.startActivity(intent);
    }

    public static void start(Context context, long id,String shop) {
        Intent intent = new Intent(context, CommodityDetailActivity.class);
        intent.putExtra(Constants.SHOP, shop);
        intent.putExtra(Constants.INFO_ID, id);
        context.startActivity(intent);
    }

    public static void start(Context context, long productId,long prizeId,float salePrice) {
        Intent intent = new Intent(context, CommodityDetailActivity.class);
        intent.putExtra(Constants.INFO_ID, productId);
        intent.putExtra(Constants.ACTIVITY_ID, prizeId);
        intent.putExtra(Constants.TOTAL_PRICE, salePrice);
        context.startActivity(intent);
    }
    public static void start(Context context, long id, boolean saleout,int skuId) {
        Intent intent = new Intent(context, CommodityDetailActivity.class);
        intent.putExtra(Constants.INFO_ID, id);
        intent.putExtra(Constants.SKU_ID,skuId);
        intent.putExtra(Constants.IS_SALE_OUT, false);
        context.startActivity(intent);
    }

    public static void start(Context context, long id, boolean saleout) {
        Intent intent = new Intent(context, CommodityDetailActivity.class);
        intent.putExtra(Constants.INFO_ID, id);
        //intent.putExtra(Constants.SKU_ID,skuId);
        intent.putExtra(Constants.IS_SALE_OUT, false);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartSize();
    }

    private void updateProcess() {
        int count = mAdapter.getCount();
        if (count == 0) {
            return;
        }
        mViewPager.setCurrentItem(0);
    }

    @OnClick(R.id.action_view)
    public void showShopCart() {
        isBuyNow = false;
        if (AccountInfo.getInstance().isLogin()) {
            showPopupWindow();
        } else {
            Util.startActivity(this, LoginActivity.class);
        }
    }

    @OnClick(R.id.buy_now)
    public void buyNow() {
        isBuyNow = true;
        if (AccountInfo.getInstance().isLogin()) {
            showPopupWindow();
        } else {
            Util.startActivity(this, LoginActivity.class);
        }
    }

    private void colorViewSelected(boolean isSelected) {
        if (mColorView == null) {
            return;
        }
        if (isSelected) {
            mColorView.setBackgroundColor(Color.BLACK);
        } else {
            mColorView.setBackgroundColor(Color.WHITE);
        }
    }

    private View createPopupView(final SpuViewModel.Data spuInfo) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popwindow_select_product, null);
        mQuantityView = contentView.findViewById(R.id.quantity_view);
        mQuantityView.setQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged(int newQuantity, boolean programmatically) {}

            @Override
            public void onMinReached() {}

            @Override
            public void onLimitReached() {
                SystemUtil.reportServerError("超过限购数量！");
            }
        });
        mHintView=contentView.findViewById(R.id.popup_hint);

        mAddCartView = contentView.findViewById(R.id.pop_action_view);
        mProgressView = contentView.findViewById(R.id.progress_view);
        mPriceView = contentView.findViewById(R.id.price_view);
        mPriceView.setText("￥" + spuInfo.getPrice() + getString(R.string.item));
        FlexboxLayout sizeContainer = contentView.findViewById(R.id.size_container);
        FlexboxLayout colorContainer = contentView.findViewById(R.id.color_container);
        View colorView;
        int w = Util.dip2px(50);
        int h = Util.dip2px(50);
        int margin = Util.dip2px(10);
        List<SpuViewModel.ColorInfo> colorInfoList = spuInfo.sku_color;
        mColorViews = new View[colorInfoList.size()];
        for (int i = 0; i < colorInfoList.size(); i++) {

            final int index = i;
            colorView = LayoutInflater.from(this).inflate(R.layout.bolder_view, mColorContainer, false);
            ImageView innerView = colorView.findViewById(R.id.color_view);
            innerView.setScaleType(ImageView.ScaleType.FIT_XY);
            String url = colorInfoList.get(i).imgUrl;
            if (Util.isUrl(url)) {
                Glide.with(this).load(colorInfoList.get(i).imgUrl)
                        .crossFade()
                        .into(innerView);
            } else {
                try {
                    innerView.setImageDrawable(new ColorDrawable(Color.parseColor(colorInfoList.get(i).displayName)));
                } catch (IllegalArgumentException e) {
                }
            }

            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(w, h);
            params.setMargins(margin, 0, margin, 0);
            mColorViews[i] = colorView;
            colorContainer.addView(colorView, params);
            colorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectColorView(v, index);
                    refreshView(index, spuInfo, mSizeIndex);
                }
            });
        }

        TextView sizeView;
        int size = Util.dip2px(35);
        List<SpuViewModel.SizeInfo> sizeInfoList = spuInfo.sku_size;
        mSizeViews = new TextView[sizeInfoList.size()];
        for (int i = 0; i < sizeInfoList.size(); i++) {
            final int index = i;
            sizeView = new TextView(this);
            sizeView.setGravity(Gravity.CENTER);
            sizeView.setTextSize(12);
            sizeView.setText(sizeInfoList.get(i).displayName);
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(size, size);
            params.setMargins(margin, 0, margin, 0);
            mSizeViews[i] = sizeView;
            sizeContainer.addView(sizeView, params);
            sizeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSizeIndex = index;
                    refreshView(mColorIndex, spuInfo, mSizeIndex);
                }
            });
        }
        return contentView;
    }

    @OnClick({R.id.size_layout,R.id.info_layout})
    void spread(View view){
        if(view.getId()==R.id.size_layout){
            if(mSizeTable.getVisibility()==View.GONE){
                mSizeTable.setVisibility(View.VISIBLE);
                mSizeSwitch.setImageResource(R.mipmap.gray_minus_icon);
            }
        }else {
            if(mInfoDetail.getVisibility()==View.GONE){
                mInfoDetail.setVisibility(View.VISIBLE);
                mInfoSwitch.setImageResource(R.mipmap.gray_minus_icon);
            }
        }
    }

    private void selectColorView(View select, int index) {
        colorViewSelected(false);
        mColorView = select;
        colorViewSelected(true);
        mColorIndex = index;
    }

    private void refreshView(int colorIndex, SpuViewModel.Data spu, int select) {
        if (mSizeViews != null && colorIndex != -1) {
            for (int i = 0; i < mSizeViews.length; i++) {
                SpuViewModel.Group group = spu.getGroup(colorIndex, i);
                mSizeViews[i].setBackgroundColor(Color.WHITE);
                if (group.getMinCount() <= 0) {
                    mSizeViews[i].setTextColor(Color.GRAY);
                    if (i == select) {
                        mSizeViews[i].setBackgroundColor(Color.parseColor("#AAABAD"));
                        mAddCartView.setEnabled(false);
                        mAddCartView.setText(R.string.sale_out);
                        mAddCartView.setBackgroundResource(R.drawable.solid_gray_bg);
                    }
                } else {
                    mSizeViews[i].setTextColor(Color.BLACK);

                    if (i == select) {
                        mSizeViews[i].setBackgroundColor(Color.BLACK);
                        mSizeViews[i].setTextColor(Color.WHITE);
                        mAddCartView.setEnabled(true);
                        mAddCartView.setText(isBuyNow ? R.string.buy_now : R.string.add_shop_cart);
                        mAddCartView.setBackgroundResource(R.drawable.common_yellow_bg);
                    }
                }
                if (i == select) {
                    mPriceView.setText("￥" + group.salePrice + getString(R.string.item));
                    if (mQuantityView != null) {
                        mQuantityView.setQuantity(1);
                        if(group.pavailableCount<1 && shop!=null ){
                            mHintView.setVisibility(View.VISIBLE);
                        }else {
                            mHintView.setVisibility(View.GONE);
                        }
                        if(isPrize){
                            mQuantityView.setMaxQuantity(1);
                        }else {
                            mQuantityView.setMaxQuantity(group.getMinCount());
                        }
                    }
                }
            }
            if (select == -1) {
            }
        }
    }

    private boolean createPopupWindow() {
        if (mPopupView == null) {
            mPresenter.loadSpuInfo(mProductId,shop);
            createAndShow = true;
            // Toast.makeText(this, "数据还未加载", Toast.LENGTH_SHORT).show();
            return false;
        }
        mPopupWindow = new PopupWindow(mPopupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);// 取得焦点
        mPopupWindow.setAnimationStyle(R.style.pop_window_anim);
        mPopupWindow.setOnDismissListener(() -> backgroundAlpha(1f));
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        return true;
    }


    @Override
    public void addFailed() {
        showProgress(false);
    }

    @OnClick(R.id.promise_view)
    void showPromiseWindow() {

        View view = LayoutInflater.from(this).inflate(R.layout.popup_promise, null);
        PopupWindow popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);// 取得焦点
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        backgroundAlpha(0.4f);
        popupWindow.setOnDismissListener(() -> backgroundAlpha(1));
        popupWindow.setAnimationStyle(R.style.pop_window_anim);
        popupWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
    }

    private void showChoseWindow(int quantity){
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_chose, null);
        PopupWindow popupWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);// 取得焦点

        view.findViewById(R.id.shop_view).setOnClickListener(v -> {
            mPresenter.buyNow(this, mColorIndex, mSizeIndex, quantity,shop);
            popupWindow.dismiss();
        });

        view.findViewById(R.id.transport_view).setOnClickListener(v -> {
            mPresenter.buyNow(this, mColorIndex, mSizeIndex, quantity,null);
            popupWindow.dismiss();
        });

        //view.findViewById(R.id.close_view).setOnClickListener(v -> mPopupWindow.dismiss());

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        backgroundAlpha(0.4f);
        popupWindow.setOnDismissListener(() -> backgroundAlpha(1));
        popupWindow.setAnimationStyle(R.style.pop_window_anim);
        popupWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
    }

    @OnClick({R.id.brand_view,R.id.name_view})
    void openBrandList() {
        if (mCommodityInfo != null) {
            Util.openTarget(this, mCommodityInfo.brand.schema, "");
            //CommodityListActivity.start(this, null,mCommodityInfo.brand.brandId);
        }
    }

    private void showPopupWindow() {
        if (mPopupWindow == null) {
            if (!createPopupWindow()) {
                return;
            }
        }
        colorViewSelected(false);
        mQuantityView.setQuantity(1);
        mSizeIndex = -1;
        backgroundAlpha(0.4f);
        mAddCartView.setOnClickListener(v -> addCartOrBuy());
        mAddCartView.setText(isBuyNow ? R.string.buy_now : R.string.add_shop_cart);
        //mAddCartView.setBackgroundResource(R.drawable.common_yellow_bg);
        mPopupWindow.showAtLocation(mPopupView, Gravity.BOTTOM, 0, 0);
        //TODO
        mColorViews[mSelectIndex].callOnClick();
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    private boolean check() {
        if (mColorIndex == -1) {
            Toast toast = Toast.makeText(this, "选择颜色", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }
        if (mSizeIndex == -1) {
            Toast toast = Toast.makeText(this, "选择尺码", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }
        return true;
    }

    private void addCartOrBuy() {
        if (!check()) {
            return;
        }
        int quantity = mQuantityView.getQuantity();
        //showProgress(true);
        if (isBuyNow) {
            mPopupWindow.dismiss();

            if(mPresenter.shouldShowChoseWindow(mColorIndex,mSizeIndex,quantity)){
                showChoseWindow(quantity);

            }else {
                mPresenter.buyNow(this, mColorIndex, mSizeIndex, quantity,prizeId,salePrice,shop);
            }

        } else {
            mPresenter.addShopCart(mColorIndex, mSizeIndex, quantity,shop);
        }
    }

    @OnClick(R.id.shop_cart)
    public void openShopCart() {
        if (AccountInfo.getInstance().isLogin()) {
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeCustomAnimation(this,
                            R.anim.activity_open, R.anim.activity_outgoing);
            Intent intent = new Intent(this, ShopCartActivity.class);
            ActivityCompat.startActivity(this, intent, options.toBundle());
        } else {
            Util.startActivity(this, LoginActivity.class);
        }

    }

    @TargetApi(16)
    private void playAnimation() {
//      一、创造出执行动画的主题---imageview
        //代码new一个imageview，图片资源是上面的imageview的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里)
        final TextView commodity = new TextView(this);
        commodity.setBackground((getResources().getDrawable(R.drawable.circle_yellow_bg)));
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(50, 50);
        mRootView.addView(commodity, params);

//        二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        //int[] parentLocation = new int[2];
        //rl.getLocationInWindow(parentLocation);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        mActionView.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        mShopCart.getLocationInWindow(endLoc);

//        三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = startLoc[0] + mActionView.getWidth() / 2;
        float startY = startLoc[1] + mActionView.getHeight() / 2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0];
        float toY = endLoc[1];

//        四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo(startX / 2, startY / 2, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);

        //★★★属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(500);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                commodity.setTranslationX(mCurrentPosition[0]);
                commodity.setTranslationY(mCurrentPosition[1]);
            }
        });
//      五、 开始执行动画
        valueAnimator.start();

//      六、动画结束后的处理
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mRootView.removeView(commodity);
                if (mShopSizeView.getVisibility() == View.INVISIBLE) {
                    mShopSizeView.setVisibility(View.VISIBLE);
                }
                mShopSizeView.setText(String.valueOf(mShopCartSize));
                mShopSizeView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setCartSize() {
        ShopCart shopCart = ShopCart.getInstance();
        mShopCartSize = shopCart.getCommodityCount();
        if (mShopCartSize > 0) {
            mShopSizeView.setVisibility(View.VISIBLE);
            mShopSizeView.setText(String.valueOf(mShopCartSize));
        } else {
            mShopSizeView.setVisibility(View.GONE);
        }
    }

    private void setPrice(float price ,float origin) {
        mInfoView.setText(String.format("￥%.2f", price));
        if(origin>price){
            mOriginPrice.setText(String.format("￥%.2f", origin));
            mOriginPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        }
    }

    @Override
    public void onSuccess(Object data) {
        setCartSize();
    }

    @Override
    public void onFailure(int code) {}

    private void showProgress(boolean show) {}

    @Override
    public void onScrollChanged(ScrollView view, int x, int y, int oldx, int oldy) {
        float h = Util.dip2px(200);
        float alpha = y / h;
        if(alpha<=1){
            mTopBar.setAlpha(alpha);
            show=false;
            mTitleLayout.setVisibility(View.INVISIBLE);
        }else if(!show){
            show=true;
            mTitleLayout.setVisibility(View.VISIBLE);
            if(mCommodityInfo!=null && mSpuInfo!=null){
                mTitleBrand.setText(mCommodityInfo.brand.name);
                mTitlePrice.setText("￥"+mSpuInfo.getPrice());
            }
        }
    }

    @Override
    public void changeColor(ImageDetail images) {
        mAdapter.changeContent(images.headPics);
        updateProcess();
        fillDetail(images.detailInfo);
    }

    @Override
    public void addSuccess(int count) {
        showProgress(false);
        mShopCartSize += count;
        mPopupWindow.dismiss();
        playAnimation();
    }

    @Override
    public void fillView(SpuViewModel.Data viewModel) {
        mSpuInfo = viewModel;
        if (mSpuInfo.isSaleOut()) {
            mBuyView.setText(R.string.sale_out);
            mBuyView.setTextColor(Color.WHITE);
            mActionView.setVisibility(View.GONE);
            mBuyView.setBackgroundColor(Color.parseColor("#9A9B9D"));
            mBuyView.setEnabled(false);
        }
        mPopupView = createPopupView(viewModel);
        setPrice(viewModel.getPrice(),viewModel.getOriginPrice());
        if (createAndShow) {
            showPopupWindow();
        }
    }

    @Override
    public void fillView(CommodityViewModel.Commodity viewModel) {
        mCommodityInfo = viewModel;
        mAdapter.changeContent(viewModel.headPics);
        mAdapter.setListener(index ->
            ImageDetailActivity.start(this,viewModel.baseInfo.name,viewModel.brand.name,viewModel.headPics,index));
        updateProcess();
        CommodityViewModel.UseInfo sizeInfo = viewModel.size;
        mSizeTable.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (sizeInfo.imgUrl == null || sizeInfo.imgUrl.isEmpty()) {
            mSizeLayout.setVisibility(View.GONE);
            mSizeTable.setVisibility(View.GONE);
        }
        Glide.with(this).load(sizeInfo.imgUrl).into(mSizeTable);
        Glide.with(this).load(viewModel.brand.logoUrl).into(mBrandIcon);
        mBrandTitle.setText(viewModel.brand.name);
        if(Util.isNotEmpty(viewModel.getCommodityInfo())){
            mInfoDetail.setText(viewModel.getCommodityInfo());
        }else {}

        List<CommodityViewModel.ColorInfo> colorInfo = viewModel.skuColors;
        mDescView.setText(viewModel.baseInfo.name);
        mNameView.setText(viewModel.brand.name);

        View colorView;
        int size = Util.dip2px(50);
        int margin = Util.dip2px(5);
        int count = colorInfo.size();
        if (count > 1) {
            for (int i = 0; i < count; i++) {
                final int index = i;
                colorView = LayoutInflater.from(this).inflate(R.layout.bottom_bolder_view, mColorContainer, false);
                ImageView innerView = colorView.findViewById(R.id.color_view);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, Util.dip2px(54));
                params.setMargins(i == 0 ? 0 : margin, 0, margin, 0);
                mColorContainer.addView(colorView, params);
                if (Util.isUrl(colorInfo.get(i).shadeUrl)) {
                    Glide.with(this).load(colorInfo.get(i).shadeUrl).into(innerView);
                } else {
                    try {
                        innerView.setImageDrawable(new ColorDrawable(Color.parseColor(colorInfo.get(i).name)));
                    } catch (IllegalArgumentException e) {
                    }
                }
                if (i == 0) {
                    mSelectIndex = 0;
                    mSelectView = colorView;
                    colorView.setBackgroundColor(Color.BLACK);
                }
                colorView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mSelectView != null) {
                            mSelectView.setBackgroundColor(Color.WHITE);
                        }
                        mSelectView = v;
                        // warm
                        v.setBackgroundColor(Color.BLACK);
                        mPresenter.getTopImage(index, mProductId);
                        mSelectIndex = index;
                    }
                });

                if(skuId==colorInfo.get(i).skuValId){
                    colorView.callOnClick();
                }
            }
        }

        if (viewModel.isShowShareButton) {
            mShareHint.setVisibility(View.VISIBLE);
            mShareHint.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            mShareHint.setText(String.format(getString(R.string.share_hint), viewModel.couponName));
        }

        List<ImageInfo> detailInfo = viewModel.detailInfo;
        if (detailInfo == null || detailInfo.isEmpty()) {
            mDetailText.setVisibility(View.GONE);
            mDetailLine.setVisibility(View.GONE);
            mDetailContainer.setVisibility(View.GONE);
        }
        fillDetail(detailInfo);
    }

    private void fillDetail(List<ImageInfo> details) {
        if (details == null) {
            return;
        }
        mDetailContainer.removeAllViews();
        ImageView detailView;
        TextView descView;
        int padding = Util.dip2px(16);

        for (int i = 0; i < details.size(); i++) {
            ImageInfo info = details.get(i);

            if (info.imgUrl != null && info.imgUrl.length() > 3) {
                detailView = new ImageView(this);
                detailView.setScaleType(ImageView.ScaleType.FIT_XY);
                detailView.setAdjustViewBounds(true);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mDetailContainer.addView(detailView, params);
                Glide.with(this).load(details.get(i).imgUrl)
                        .into(detailView);
            }
            if (info.desc != null && info.desc.length() > 0) {
                descView = new TextView(this);
                descView.setPadding(0, padding, 0, padding);
                descView.setTextColor(getResources().getColor(android.R.color.secondary_text_dark));
                descView.setText(info.desc);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mDetailContainer.addView(descView, params);
            }
        }
    }

    @OnClick({R.id.share_view, R.id.share_text})
    void doShare() {
        if (mCommodityInfo != null) {
            UMWeb umWeb = new UMWeb(BuildConfig.SHAREURL + mCommodityInfo.baseInfo.productId);
            umWeb.setTitle(mCommodityInfo.baseInfo.name);
            umWeb.setDescription("我发现了这件单品挺适合你，推荐你看一下");
            umWeb.setThumb(new UMImage(this, mCommodityInfo.headPics.get(0).imgUrl));
            new ShareAction(this)
                    //.withText("VLONE亚洲首发，加入innersect顶尖正价潮货抢先GO！")
                    .withMedia(umWeb)
                    .setDisplayList(Util.getDisplayList(this))
                    .setCallback(umShareListener)
                    .open();
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //分享开始的回调
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            if (mCommodityInfo != null && mCommodityInfo.isShowShareButton) {
                ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).bindCoupon(mCommodityInfo.couponCDKey).enqueue(new NetCallback<String>() {
                    @Override
                    public void onSucceed(String data) {
                        SystemUtil.reportServerError(getString(R.string.share_toast));
                    }
                });
            }
            mShareResult.setVisibility(View.VISIBLE);
            mShareResult.setText(String.format(getString(R.string.share_result), mCommodityInfo.couponName));
            mShareHint.setVisibility(View.GONE);
            SystemUtil.reportServerError("分享成功");
            KLog.d("plat", "platform" + platform);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (t != null) {
                KLog.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            AnalyticsUtil.reportEvent("home_news_detail_share_cancel");
        }
    };

    @OnClick(R.id.open_service)
    void open() {
        Util.startMoor(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
        mShopCartPresenter.onDestory();
    }
}
