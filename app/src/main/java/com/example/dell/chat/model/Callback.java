package com.example.dell.chat.model;

/**
 * Created by wang on 2018/4/12.
 */

public interface Callback<T> {
    void execute(T datas);
}
