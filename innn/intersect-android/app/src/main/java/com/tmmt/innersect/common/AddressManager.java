//package com.tmmt.innersect.common;
//
//import com.socks.library.KLog;
//import com.tmmt.innersect.datasource.net.ApiManager;
//import com.tmmt.innersect.mvp.model.Address;
//import com.tmmt.innersect.mvp.model.HttpBean;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * Created by flame on 2017/7/11.
// */
//
//public class AddressManager {
//
//    public static AddressManager mInstance;
//
//    private Address mDefault;
//
//    private AddressManager(){}
//
//    public static AddressManager getInstance() {
//        if (mInstance == null) {
//            synchronized (AddressManager.class) {
//                if (mInstance == null) {
//                    mInstance = new AddressManager();
//                }
//            }
//        }
//        return mInstance;
//    }
//
//    public Address getAddress(){
//        return mDefault;
//    }
//
//    public void setDefault(Address address){
//        mDefault=address;
//    }
//
////    public void loadDefaultAddress() {
////
////        ApiManager.getApi(ApiManager.TEST_REMOTE_TYPE).getDefaultAddress(AccountInfo.getInstance().getUserId())
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(new Subscriber<HttpBean<Address>>() {
////                    @Override
////                    public void onCompleted() {}
////
////                    @Override
////                    public void onError(Throwable e) {
////                        KLog.d(e);
////                    }
////                    @Override
////                    public void onNext(HttpBean<Address> result) {
////                        mDefault=result.data;
////                    }
////                });
////    }
//}
