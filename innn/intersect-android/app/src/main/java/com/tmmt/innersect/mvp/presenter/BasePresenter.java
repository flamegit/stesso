package com.tmmt.innersect.mvp.presenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by flame on 2017/5/3.
 */

public abstract class BasePresenter<T>{

    T mView;

    CompositeSubscription mSubscriptions;

    public void attachView(T view){
        mView=view;
        //mSubscriptions.clear();

    }

    protected void addSubscription(Subscription s){
        if(s==null) return;
        if(mSubscriptions==null){
            mSubscriptions=new CompositeSubscription();
        }
        mSubscriptions.add(s);
    }


    public void onDestory(){
        mView=null;
        if(mSubscriptions!=null){
            mSubscriptions.clear();
        }
    }

}
