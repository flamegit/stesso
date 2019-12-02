package com.tmmt.innersect.mvp.presenter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socks.library.KLog;
import com.tmmt.innersect.common.AccountInfo;
import com.tmmt.innersect.datasource.net.ApiManager;
import com.tmmt.innersect.datasource.net.NetCallback;
import com.tmmt.innersect.mvp.model.AdInfo;
import com.tmmt.innersect.mvp.model.BannerInfo;
import com.tmmt.innersect.mvp.model.BrandInfo;
import com.tmmt.innersect.mvp.model.BrandList;
import com.tmmt.innersect.mvp.model.Common;
import com.tmmt.innersect.mvp.model.CommonData;
import com.tmmt.innersect.mvp.model.Coupon;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.mvp.model.InformationModel;
import com.tmmt.innersect.mvp.model.LinkInfo;
import com.tmmt.innersect.mvp.model.LotteryDetail;
import com.tmmt.innersect.mvp.model.PopupInfo;
import com.tmmt.innersect.mvp.model.RefundItem;
import com.tmmt.innersect.mvp.model.RefundProgress;
import com.tmmt.innersect.mvp.model.RegisterInfo;
import com.tmmt.innersect.mvp.model.ReserveInfo;
import com.tmmt.innersect.mvp.model.ReserveResult;
import com.tmmt.innersect.mvp.model.SearchResult;
import com.tmmt.innersect.mvp.view.CommonView;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by flame on 2017/9/7.
 */

public class CommonPresenter extends BasePresenter<CommonView> {
    private static final long DELAY = 7 * 24 * 3600 * 1000;

    public void loadAd() {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getAdInfo(DELAY + System.currentTimeMillis()).enqueue(new NetCallback<AdInfo>() {
            @Override
            public void onSucceed(AdInfo data) {
                fillView(data);
            }
        });
    }

    public void loadContent(long time, final boolean append,Integer id) {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getInfoList(time, 10,id).enqueue(new Callback<InformationModel>() {
            @Override
            public void onResponse(Call<InformationModel> call, Response<InformationModel> response) {
                if (mView != null) {
                    mView.success(response.body());
                }
            }

            @Override
            public void onFailure(Call<InformationModel> call, Throwable t) {
                if (mView != null) {
                    mView.failed();
                }
            }
        });
    }

    public void loadBanner() {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getBannerInfo().enqueue(new NetCallback<List<BannerInfo>>() {
            @Override
            public void onSucceed(List<BannerInfo> data) {
                fillView(data);
            }
        });
    }

    public void getLotteryDetail(long id){
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getLotteryDetail(id).enqueue(new NetCallback<LotteryDetail>() {
            @Override
            public void onSucceed(LotteryDetail data) {
                fillView(data);
            }

            @Override
            protected void failed(int code) {
                super.failed(code);
                if(mView!=null){
                    mView.failed();
                }
            }
        });
    }

    public void getProductList(String aid, long brandId, String sort) {
        if (aid == null) {
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getBrandList(brandId, sort).enqueue(new NetCallback<BrandInfo>() {
                @Override
                public void onSucceed(BrandInfo data) {
                    fillView(data);
                }
            });
        } else {
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getProductList(aid, brandId, sort).enqueue(new NetCallback<BrandInfo>() {
                @Override
                public void onSucceed(BrandInfo data) {
                    fillView(data);
                }
            });
        }
    }

    public void getDiscountList(String aid, String sort) {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getDiscountList(aid, sort).enqueue(new NetCallback<BrandInfo>() {
            @Override
            public void onSucceed(BrandInfo data) {
                fillView(data);
            }
        });
    }

    public void getCommodityList(String aid, String sort,Long brandId,int index) {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getProductList(aid,sort,brandId,index,10).enqueue(new NetCallback<BrandInfo>() {
            @Override
            public void onSucceed(BrandInfo data) {
                fillView(data);
            }
        });
    }

    public void getBrandList() {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getBrandList().enqueue(new NetCallback<List<CommonData>>() {
            @Override
            public void onSucceed(List<CommonData> data) {
                fillView(data);
            }
        });
    }

    public void getBrandList2() {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getBrandList2().enqueue(new NetCallback<BrandList>() {
            @Override
            public void onSucceed(BrandList data) {
                fillView(data);
            }
        });
    }


    public void getCategoryList() {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getCategories().enqueue(new NetCallback<List<CommonData>>() {
            @Override
            public void onSucceed(List<CommonData> data) {
                fillView(data);
            }
        });
    }

    public void getActivityInfo(long id) {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getReserveInfo(id, AccountInfo.getInstance().getUserId())
                .enqueue(new NetCallback<ReserveInfo>() {
                    @Override
                    public void onSucceed(ReserveInfo data) {
                        fillView(data);
                    }

                    @Override
                    protected void failed(int code) {
                        super.failed(code);
                        if (mView != null) {
                            mView.failed();
                        }
                    }
                });
    }

    public void getCouponProduct(int cid,int sort,int from){
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getCouponProducts(cid,sort,from).enqueue(new NetCallback<SearchResult>() {
            @Override
            public void onSucceed(SearchResult data) {
                fillView(data);
            }
        });
    }

    public void getPopInfo(String aid) {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getPopupInfo(aid).enqueue(new NetCallback<PopupInfo>() {
            @Override
            public void onSucceed(PopupInfo data) {
                fillView(data);
            }
        });
    }

    public void getRefundList(String orderNo){
        if(orderNo==null){
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getRefundList(1,100).enqueue(new NetCallback<List<RefundItem>>() {
                @Override
                public void onSucceed(List<RefundItem> data) {
                    fillView(data);
                }
            });
        }else {
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getRefundList(orderNo,1,100).enqueue(new NetCallback<List<RefundItem>>() {
                @Override
                public void onSucceed(List<RefundItem> data) {
                    fillView(data);
                }
            });
        }

    }

    public void getCancelProgress(String refundNo){
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getCancelProgress(refundNo).enqueue(new NetCallback<List<Common>>() {
            @Override
            public void onSucceed(List<Common> data) {
                fillView(data);
            }
        });
    }

    public void getRefundProgress(String asno){
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getRefundProgress(asno).enqueue(new NetCallback<RefundProgress>() {
            @Override
            public void onSucceed(RefundProgress data) {
                fillView(data);
            }
        });
    }


    public void addRegisterInfo(RegisterInfo info, boolean change) {
        Gson gson;
        if (change) {
            gson = new Gson();
        } else {
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        }
        String json = gson.toJson(info);
        KLog.json(json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).addRegisterInfo(body)
                .enqueue(new NetCallback<Boolean>() {
                    @Override
                    public void onSucceed(Boolean data) {
                        fillView(data);
                    }
                });
    }

    public void getReserveResult(long id) {
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getReserveResult(id, AccountInfo.getInstance().getUserId())
                .enqueue(new NetCallback<ReserveResult>() {
                    @Override
                    public void onSucceed(ReserveResult data) {
                        fillView(data);
                    }
                });
    }

    public void getCoupon(int type) {
        if (type == 1) {
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getAcsCoupon(1, 50).enqueue(new NetCallback<List<Coupon>>() {
                @Override
                public void onSucceed(List<Coupon> data) {
                    fillView(data);
                }
            });
        } else {
            ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getUacsCoupon(1, 50).enqueue(new NetCallback<List<Coupon>>() {
                @Override
                public void onSucceed(List<Coupon> data) {
                    fillView(data);
                }
            });
        }
    }

    public void getLinkInfo(String orderNo){
        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getLinkInfo(orderNo).enqueue(new Callback<HttpBean<LinkInfo>>() {
            @Override
            public void onResponse(Call<HttpBean<LinkInfo>> call, Response<HttpBean<LinkInfo>> response) {
                fillView(response.body().data);
            }

            @Override
            public void onFailure(Call<HttpBean<LinkInfo>> call, Throwable t) {
                if (mView != null) {
                    mView.failed();
                }
            }
        });
    }

    private <T> void fillView(T data) {
        if (mView != null) {
            mView.success(data);
        }
    }

}


