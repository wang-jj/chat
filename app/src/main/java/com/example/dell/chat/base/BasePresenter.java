package com.example.dell.chat.base;

import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

/**
 * Created by wang on 2018/4/12.
 */

public class BasePresenter<T> {

    protected WeakReference<T> mViewRef;

    //关联
    public void attachView(T view) {
        mViewRef = new WeakReference<T>(view);
    }

    //解除关联
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
        }
    }
    //得到view
    protected T getView() {
        return mViewRef.get();
    }
/*
    protected T mViewRef;
    public void attachView(T view) {
        mViewRef = view;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef=null;
        }
    }
    protected T getView() {
        return mViewRef;
    }
    */

}
