package com.example.dell.chat.model.Main;

import com.example.dell.chat.model.Callback;

/**
 * Created by wang on 2018/4/25.
 */

public interface MainModel {
    //注销
    void SignOut(final Callback<Void> callback);
}
