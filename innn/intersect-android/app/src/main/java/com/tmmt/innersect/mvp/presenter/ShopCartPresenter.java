package com.tmmt.innersect.mvp.presenter;

import com.socks.library.KLog;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.ShopCart;
import com.tmmt.innersect.mvp.model.ShopCartInfo;
import com.tmmt.innersect.mvp.model.ShopItem;
import com.tmmt.innersect.mvp.view.ExtraView;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by flame on 2017/5/3.
 */

public class ShopCartPresenter extends BasePresenter<ExtraView> {

    private String mUserId;

    public ShopCartPresenter() {
        mUserId = AccountInfo.getInstance().getUserId();
        KLog.i(mUserId);
    }

    public void loadShopCart() {
        if (!AccountInfo.getInstance().isLogin()) {
            if (mView != null) {
                mView.onFailure(1);
            }
            return;
        }
        if (ShopCart.getInstance().isRefresh()) {
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getShopCartList(mUserId, 1, 100)
                    .enqueue(new NetCallback<ShopCartInfo>() {
                        @Override
                        public void onSucceed(ShopCartInfo data) {
                            ShopCart shopCart = ShopCart.getInstance();
                            shopCart.addItems(data.cartList);
                            shopCart.headText=data.headText;
                            shopCart.headDesc=data.headDesc;
                            shopCart.headSchema=data.headSchema;
                            shopCart.setRefresh(false);
                            if (mView != null) {
                                mView.onSuccess(null);
                            }
                        }
                        @Override
                        protected void failed(int code) {
                            if (mView != null) {
                                mView.onFailure(1);
                            }
                        }
                    });
        } else {
            if (mView != null) {
                mView.onSuccess(null);
            }
        }
    }

    public void updateQuantity(String json) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).updateShopCart(body).enqueue(new NetCallback<String>() {
            @Override
            public void onSucceed(String data) {
            }
        });
    }

    public void deleteShopItem(List<ShopItem> list) {
        JSONStringer jsonStringer = new JSONStringer();
        try {
            JSONStringer array = jsonStringer.array();
            for (ShopItem item : list) {
                array.value(item.id);
            }
            array.endArray();
            KLog.json(jsonStringer.toString());
        } catch (JSONException e) {
            KLog.i("JsonException");
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonStringer.toString());
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).removeShopCart(body).enqueue(new NetCallback<String>() {
            @Override
            public void onSucceed(String data) {
            }

        });
    }

}
