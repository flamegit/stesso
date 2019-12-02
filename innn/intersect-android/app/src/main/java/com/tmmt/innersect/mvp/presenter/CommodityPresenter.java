package com.tmmt.innersect.mvp.presenter;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socks.library.KLog;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.CommodityViewModel;
import com.tmmt.innersect.mvp.model.ImageDetail;
import com.tmmt.innersect.mvp.model.ShopCart;
import com.tmmt.innersect.mvp.model.ShopItem;
import com.tmmt.innersect.mvp.model.SpuViewModel;
import com.tmmt.innersect.mvp.model.Status;
import com.tmmt.innersect.mvp.view.CommodityView;
import com.tmmt.innersect.ui.activity.AwardActivity;
import com.tmmt.innersect.ui.activity.AwardSettlementActivity;
import com.tmmt.innersect.ui.activity.SettlementActivity;
import com.tmmt.innersect.utils.SystemUtil;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by flame on 2017/5/3.
 */

public class CommodityPresenter extends BasePresenter<CommodityView> {


    public CommodityPresenter(boolean isPrize){
        this.isPrize=isPrize;
    }

    private boolean isPrize;
    private SpuViewModel.Data mSpuInfo;
    private CommodityViewModel mCommodityInfo;
    public void loadCommodityInfo(long productId,int skuId) {
        if (mCommodityInfo != null) {
            mView.fillView(mCommodityInfo.data);
            return;
        }
        String store=null;
        if(isPrize){
            store="onpr";
        }
        Subscription subscription=ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getCommodityInfo("v1",productId,store)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommodityViewModel>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e);
                        SystemUtil.reportNetError(e);
                    }
                    @Override
                    public void onNext(CommodityViewModel viewModel) {
                        KLog.d(viewModel.code);
                        if(viewModel.code==200){
                            mCommodityInfo = viewModel;
                            if(viewModel==null ||viewModel.data==null){
                                return;
                            }
                            mCommodityInfo.data.parse(skuId);

                            if(mView!=null){
                                mView.fillView(mCommodityInfo.data);
                            }
                        }else {
                            SystemUtil.reportServerError(viewModel.msg);
                        }

                    }
                });
        addSubscription(subscription);
    }
    public void loadSpuInfo(final long productId,String shop) {
        if (mSpuInfo != null) {
            mView.fillView(mSpuInfo);
            return;
        }
        JSONStringer jsonStringer = new JSONStringer();
        try {
            jsonStringer.object()
                    .key("deliveryAddress").value("APP")
                    .key("productId").value(productId);
            if(isPrize){
                jsonStringer.key("store").value("onpr");
            }
            if(shop!=null){
                jsonStringer.key("shop").value(shop);
            }
            jsonStringer.endObject();

            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
        Subscription subscription =ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getGroupInfo("v1",body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SpuViewModel>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        KLog.d(e);
                        SystemUtil.reportNetError(e);
                    }
                    @Override
                    public void onNext(SpuViewModel viewModel) {
                        KLog.d(viewModel.code);
                        mSpuInfo = viewModel.data;
                        mSpuInfo.parseSpuInfo();
                        if(mView!=null){
                            mView.fillView(mSpuInfo);
                        }
                    }
                });
        addSubscription(subscription);
    }

    public void getTopImage(final int index,long product) {
        ImageDetail imageInfos = mCommodityInfo.getHeadImages(index);
        List<CommodityViewModel.ColorInfo> colorInfos = mCommodityInfo.data.skuColors;
        if (imageInfos == null) {
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getTopImages("v1",product,colorInfos.get(index).skuId).enqueue(new NetCallback<ImageDetail>() {
                @Override
                public void onSucceed(ImageDetail data) {
                    mCommodityInfo.saveImages(index,data);
                    if(mView!=null){
                        mView.changeColor(data);
                    }
                }
            });
        } else {
            if(mView!=null){
                mView.changeColor(imageInfos);
            }
        }
    }

    public boolean shouldShowChoseWindow(int colorIndex, int sizeIndex, int quantity ){
        SpuViewModel.Group group = mSpuInfo.getGroup(colorIndex, sizeIndex);
        if(group.availableCount>=quantity && group.pavailableCount>=quantity){
            return true;
        }
        return false;
    }

    public void buyNow(Context context,int colorIndex, int sizeIndex,int quantity,String shop){
        buyNow(context,colorIndex,sizeIndex,quantity,-1,0,shop);
    }

    public void buyNow(Context context,int colorIndex, int sizeIndex, int quantity,long prizeId,float salePrize,String shop) {
        if(mSpuInfo==null){
            return;
        }
        SpuViewModel.Group group = mSpuInfo.getGroup(colorIndex, sizeIndex);
        ShopItem item = new ShopItem();
        item.color = mSpuInfo.sku_color.get(colorIndex).valdesc;
        item.size = mSpuInfo.sku_size.get(sizeIndex).displayName;
        item.quantity = quantity;
        item.skuId = group.skuId;
        item.productId = group.productId;
        item.omsSkuId=group.omsSkuId;
        //TODO
        item.store="APP";
        item.shop=shop;
        item.name=mCommodityInfo.data.baseInfo.name;
        item.skuThumbnail=mSpuInfo.sku_color.get(colorIndex).imgUrl;
        item.price=group.salePrice;
        if(prizeId!=-1){
            AwardSettlementActivity.start(context,item,prizeId,group.salePrice-salePrize);
        }else {
            SettlementActivity.start(context,item,shop);
        }
        //addShopCart(item,quantity);
    }

    public void addShopCart(int colorIndex, int sizeIndex, int quantity,String shop) {
        if(mSpuInfo==null){
            return;
        }
        SpuViewModel.Group group = mSpuInfo.getGroup(colorIndex, sizeIndex);
        ShopItem item = new ShopItem();
        item.color = mSpuInfo.sku_color.get(colorIndex).displayName;
        item.size = mSpuInfo.sku_size.get(sizeIndex).displayName;
        item.quantity = quantity;
        item.skuId = group.skuId;
        item.productId = group.productId;
        item.userId = AccountInfo.getInstance().getUserId();
        item.shop=shop;
        addShopCart(item,quantity);
    }

    private void addShopCart(ShopItem item, final int quantity) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(item);
        KLog.i(json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        Subscription subscription= ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).addShopCart(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        KLog.i(e);
                        SystemUtil.reportNetError(e);
                    }
                    @Override
                    public void onNext(Status status) {
                        ShopCart.getInstance().setRefresh(true);
                        if(status.code==200){
                            mView.addSuccess(quantity);
                        }else {
                            SystemUtil.reportServerError(status.msg);
                        }
                    }
                });
        addSubscription(subscription);
    }

}
