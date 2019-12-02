package com.tmmt.innersect.datasource.net;

import com.tmmt.innersect.mvp.model.AdInfo;
import com.tmmt.innersect.mvp.model.Address;
import com.tmmt.innersect.mvp.model.AfterInfo;
import com.tmmt.innersect.mvp.model.AwardCommodity;
import com.tmmt.innersect.mvp.model.AwardInfo;
import com.tmmt.innersect.mvp.model.BannerInfo;
import com.tmmt.innersect.mvp.model.BrandInfo;
import com.tmmt.innersect.mvp.model.BrandList;
import com.tmmt.innersect.mvp.model.ChanceDesc;
import com.tmmt.innersect.mvp.model.CommodityViewModel;
import com.tmmt.innersect.mvp.model.Common;
import com.tmmt.innersect.mvp.model.CommonData;
import com.tmmt.innersect.mvp.model.Coupon;
import com.tmmt.innersect.mvp.model.DefaultConfig;
import com.tmmt.innersect.mvp.model.DeliverInfo;
import com.tmmt.innersect.mvp.model.DiscountInfo;
import com.tmmt.innersect.mvp.model.DrawResult;
import com.tmmt.innersect.mvp.model.HotSpotItem;
import com.tmmt.innersect.mvp.model.HttpBean;
import com.tmmt.innersect.mvp.model.ImageDetail;
import com.tmmt.innersect.mvp.model.Information;
import com.tmmt.innersect.mvp.model.InformationModel;
import com.tmmt.innersect.mvp.model.LinkInfo;
import com.tmmt.innersect.mvp.model.Location;
import com.tmmt.innersect.mvp.model.LogisticsInfo;
import com.tmmt.innersect.mvp.model.LotteryDetail;
import com.tmmt.innersect.mvp.model.LotteryInfo;
import com.tmmt.innersect.mvp.model.Message;
import com.tmmt.innersect.mvp.model.MessageStatus;
import com.tmmt.innersect.mvp.model.ModelItem;
import com.tmmt.innersect.mvp.model.NotificationInfo;
import com.tmmt.innersect.mvp.model.OrderDetailInfo;
import com.tmmt.innersect.mvp.model.PayModel;
import com.tmmt.innersect.mvp.model.PopupInfo;
import com.tmmt.innersect.mvp.model.PrizeInfo;
import com.tmmt.innersect.mvp.model.Problem;
import com.tmmt.innersect.mvp.model.QualificationInfo;
import com.tmmt.innersect.mvp.model.RefundCommodity;
import com.tmmt.innersect.mvp.model.RefundInfo;
import com.tmmt.innersect.mvp.model.RefundItem;
import com.tmmt.innersect.mvp.model.RefundProgress;
import com.tmmt.innersect.mvp.model.RegisterInfo;
import com.tmmt.innersect.mvp.model.ReserveInfo;
import com.tmmt.innersect.mvp.model.ReserveResult;
import com.tmmt.innersect.mvp.model.SearchNews;
import com.tmmt.innersect.mvp.model.SearchResult;
import com.tmmt.innersect.mvp.model.ShopAddress;
import com.tmmt.innersect.mvp.model.ShopCartInfo;
import com.tmmt.innersect.mvp.model.SpuViewModel;
import com.tmmt.innersect.mvp.model.Status;
import com.tmmt.innersect.mvp.model.Tips;
import com.tmmt.innersect.mvp.model.TopNews;
import com.tmmt.innersect.mvp.model.UpdateInfo;
import com.tmmt.innersect.mvp.model.User;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by flame on 2017/4/12.
 */

public interface NetApi {

    @POST("order/topay")
    Observable<PayModel> payOrder(@Body RequestBody body);

    @POST("cart/add")
    Observable<Status> addShopCart(@Body RequestBody body);

    @POST("cart/remove")
    Call<HttpBean<String>> removeShopCart(@Body RequestBody body);

    @POST("cart/update")
    Call<HttpBean<String>> updateShopCart(@Body RequestBody body);

    @POST("order/add")
    Observable<Status> addOrders(@Body RequestBody body);

    @GET("cart/v2/list")
    Call<HttpBean<ShopCartInfo>> getShopCartList(@Query("uid") String uid, @Query("num") int num, @Query("size") int size);

    @POST("order/cancel")
    Call<Status> cancelOrder(@Body RequestBody body);

    @GET("order/list")
    Observable<HttpBean<List<OrderDetailInfo>>> getOrderList(@Query("uid") String uid, @Query("num") int num, @Query("size") int size);

    @GET("order/v2/detail")
    Call<HttpBean<OrderDetailInfo>> getOrderDetail(@Query("orderNo") String orderNo);

    /*************address*************************/
    @GET("address/default")
    Call<HttpBean<Address>> getDefaultAddress(@Query("uid") String uid);

    @GET("address/list")
    Observable<HttpBean<List<Address>>> getAddressList(@Query("uid") String uid);

    @POST("address/add")
    Call<Status> addAddress(@Body RequestBody body);

    @POST("address/update")
    Call<Status> updateAddress(@Body RequestBody body);

    @POST("address/remove")
    Call<Status> removeAddress(@Body RequestBody body);

    @POST("address/default")
    Call<Status> setDefaultAddress(@Body RequestBody body);

    @POST("user/feedback")
    Call<Status> feedback(@Body RequestBody body);

    /*****************login************************/
    @POST("user/login")
    Observable<HttpBean<User>> login(@Body RequestBody body);

    @POST("user/logout")
    Call<Status> logout(@Body RequestBody body);

    @POST("user/login/sendVerifyCode")
    Call<Status> getVerifyCode(@Body RequestBody body);

    @POST("user/editPwd")
    Call<Status> setPassWord(@Body RequestBody body);

    @POST("user/bindthirdpart")
    Call<HttpBean<Boolean>> bind3part(@Body RequestBody body);

    @POST("user/unbindthirdpart")
    Call<HttpBean<Boolean>> unbind3part(@Body RequestBody body);

    @POST("user/existmobile")
    Observable<Status> existMobile(@Body RequestBody body);

    @POST("user/editUser")
    Call<HttpBean<User>> editUser(@Body RequestBody body);

    @POST("user/checkPwdExist")
    Call<HttpBean<Boolean>> isPassWordExit(@Body RequestBody body);

    /****************commodity*********/
    @GET("product/{apiVersion}/{productId}/detail")
    Observable<CommodityViewModel> getCommodityInfo(@Path("apiVersion")String version,@Path("productId") long product,@Query("store") String store);

    @POST("product/{apiVersion}/sku/groupinfo")
    Observable<SpuViewModel> getGroupInfo(@Path("apiVersion")String version,@Body RequestBody body);

    @GET("product/{apiVersion}/{productId}/skupic/{skuId}")
    Call<HttpBean<ImageDetail>> getTopImages(@Path("apiVersion")String version, @Path("productId") long product, @Path("skuId") int sku);

    @POST("user/checkOldPwd")
    Observable<HttpBean<Boolean>> verifyPassword(@Body RequestBody body);

    @GET("version/v1/changeInfo")
    Call<HttpBean<UpdateInfo>> getAppInfo(@Query("version")String version, @Query("platform")String platform);

    @GET("home/commercial")
    Call<HttpBean<AdInfo>> getAdInfo(@Query("endTime") long end);

    @GET("home/banner")
    Call<HttpBean<List<BannerInfo>>> getBannerInfo();

    @GET("news/scroll")
    Call<InformationModel> getInfoList(@Query("pt") long qt, @Query("s") int size,@Query("columnId") Integer columnId);

    @GET("news/get")
    Call<HttpBean<Information>> getInfoDetail(@Query("id") long id);

    @GET("news/banner")
    Call<HttpBean<List<TopNews>>> getNewsBanner();

    @GET("aftersales/{apiVersion}/address")
    Call<HttpBean<AfterInfo>> getSaleAfterInfo(@Path("apiVersion")String version);

    /****************ticket*******************/
    @GET("order/delivery")
    Call<HttpBean<DeliverInfo>> getDeliverInfo(@Query("orderNo") String  orderNo);

    @GET("delivery/info")
    Call<HttpBean<DeliverInfo>> getLogisticsInfo(@Query("shippingCom") String com, @Query("shippingNo") String no);

    @POST("pay/bt/checkout")
    Call<HttpBean<String>> reportServer(@Body RequestBody body);

    @POST("order/computeFee")
    Call<HttpBean<DiscountInfo>> computeFee(@Body RequestBody body);

    /***popup***/
    @GET("popup/activity/get")
    Call<HttpBean<PopupInfo>> getPopupInfo(@Query("aid")String aid);

    @GET ("product/brand/products")
    Call<HttpBean<BrandInfo>> getBrandList(@Query("brandId")long brandId,@Query("sort") String sort);

    @POST("reservation/identity")
    Call<HttpBean<Boolean>> addRegisterInfo(@Body RequestBody body);

    @GET("reservation/identity")
    Call<HttpBean<RegisterInfo>> getRegisterInfo(@Query("id") long id, @Query("userId")String uid);

    @GET("reservation")
    Call<HttpBean<ReserveInfo>> getReserveInfo(@Query("id") long id, @Query("userId")String uid);

    @POST("reservation/qualification")
    Call<HttpBean<Boolean>> reserve(@Body RequestBody body);

    @GET("reservation/qualification")
    Call<HttpBean<ReserveResult>> getReserveResult(@Query("id") long id, @Query("userId")String uid);

    @GET("reservation/location/check")
    Call<HttpBean<Boolean>> checkLocation(@Query("id") long id, @Query("location")String location);

    @GET("user/coupon/getacs")
    Call<HttpBean<List<Coupon>>> getAcsCoupon(@Query("page")int page,@Query("size")int size);

    @GET("user/coupon/getuacs")
    Call<HttpBean<List<Coupon>>> getUacsCoupon(@Query("page")int page,@Query("size")int size);

    @GET("user/coupon/bind")
    Call<HttpBean<String>> bindCoupon(@Query("cdkey") String key);

    @POST("user/coupon/bindbyClick")
    Call<HttpBean<Problem>> bindCoupon(@Body RequestBody body);

    @POST("order/listCoupons")
    Call<HttpBean<List<Coupon>>> getCoupons(@Body RequestBody body);

    @GET("home/list")
    Call<HttpBean<List<ModelItem>>> getHomeList();

    @GET("home/brands")
    Call<HttpBean<List<CommonData>>> getBrandList();

    @GET("home/brands2")
    Call<HttpBean<BrandList>> getBrandList2();

    @GET("product/list")
    Call<HttpBean<BrandInfo>> getProductList(@Query("pid")String pid,@Query("sort") String sort,@Query("brandId")Long brandId,
                                              @Query("index")int index,@Query("psize")int size);
    @GET("popup/discount/products")
    Call<HttpBean<BrandInfo>> getDiscountList(@Query("aid")String aid,@Query("sort") String sort);

    @GET("popup/brand/products")
    Call<HttpBean<BrandInfo>> getProductList(@Query("aid")String aid,@Query("brandId")long brandId,@Query("sort") String sort);

    @GET("search/products")
    Call<HttpBean<SearchResult>> searchProducts(@Query("qwd")String qwd, @Query("from") int from);

    @GET("user/coupon/getProducts")
    Call<HttpBean<SearchResult>> getCouponProducts(@Query("cid")int cid,@Query("sort") int sort, @Query("from") int from);

    @GET("search/news")
    Call<HttpBean<SearchNews>> searchNews(@Query("qwd")String qwd, @Query("from") int from);

    @GET("search/hotspot")
    Call<HttpBean<HotSpotItem>> getHotSpot();

    @GET("home/categories")
    Call<HttpBean<List<CommonData>>> getCategories();

    @GET("share/order/link")
    Call<HttpBean<LinkInfo>>getLinkInfo(@Query("orderNo")String orderNo);

    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    @GET("lottery/detail")
    Call<HttpBean<LotteryDetail>> getLotteryDetail(@Query("id") long id);

    @GET("lottery/resource")
    Call<HttpBean<List<Common>>> getResInfo();

    @GET("lottery/draw")
    Call<HttpBean<DrawResult>> draw(@Query("id") long id);

    @GET("lottery/sources/info")
    Call<HttpBean<List<ChanceDesc>>> getChanceInfo(@Query("userId") String userId);

    @GET("user/prize/list")
    Call<HttpBean<List<LotteryInfo>>> getLotteryList(@Query("page")int page,@Query("size")int size);

    @GET("user/prize/getoffpr")
    Call<HttpBean<QualificationInfo>> getQualificationInfo(@Query("prid")long prid);

    @GET("user/action")
    Call<HttpBean<String>> reportAction(@Query("type") int type);

    @GET("order/product/getByPrid")
    Call<HttpBean<AwardCommodity>> getAwardProduct(@Query("prid")long prid);

    @GET("lottery/payed/info")
    Call<HttpBean<AwardInfo>> getPayedInfo();


    @GET("user/prize/get/{userPrizeId}")
    Call<HttpBean<PrizeInfo>> getPrizeInfo(@Path("userPrizeId") int id);

    /***********************Refund***********/
    @POST("asale/apply")
    Call<HttpBean<String>> apply(@Body RequestBody body);

    @GET("aftersales/cancel/progress")
    Call<HttpBean<List<Common>>> getCancelProgress(@Query("refundNo")String refundNo);

    @POST("aftersales/cancel/apply")
    Call<HttpBean<String>> applyCancelOrder(@Body RequestBody body);

    @GET("aftersales/cancel/reason")
    Call<HttpBean<List<Common>>> getCancelReason();

    @GET("asale/reasons")
    Call<HttpBean<List<Common>>> getRefundReason();

    @GET("asale/list")
    Call<HttpBean<List<RefundItem>>> getRefundList(@Query("page")int page,@Query("size")int size);

    @GET("asale/{orderNo}/list")
    Call<HttpBean<List<RefundItem>>> getRefundList(@Path("orderNo") String orderNo,@Query("page")int page,@Query("size")int size);

    @GET("asale/load")
    Call<HttpBean<RefundInfo>> getRefundInfo();

    @GET("asale/sb/load")
    Call<HttpBean<LogisticsInfo>> getCommanyList(@Query("asno") String asno);

    @GET("asale/loadApply")
    Call<HttpBean<RefundCommodity>> getRefundCommodity(@Query("topId") long productId, @Query("ast") int ast);

    @POST("asale/sb/post")
    Call<HttpBean<String>> postLogistics(@Body RequestBody body);

    @GET("asale/{asno}")
    Call<HttpBean<RefundProgress>> getRefundProgress(@Path("asno") String asno);

    @GET("user/coupon/getacount")
    Call<HttpBean<Tips>> getCouponTip();

    @GET("config/basic")
    Call<DefaultConfig> getDefaultConfig();

    @GET("user/notification/digest")
    Call<HttpBean<MessageStatus>> getMessageStatus();

    @POST("user/notification/{type}")
    Call<HttpBean<String>> setMessageRead(@Path("type") String path);

    @GET("user/notification/home")
    Call<HttpBean<Message>> getMessage();

    @GET ("user/notification/sns")
    Call<HttpBean<NotificationInfo>> getServiceMessage(@Query("from") int from);

    @GET("config/shop/info")
    Call<HttpBean<Location>> getShopAddress(@Query("shop") String shop);

}
