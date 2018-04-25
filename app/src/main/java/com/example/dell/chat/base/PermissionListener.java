package com.example.dell.chat.base;

import java.util.List;

/**
 * Created by courageface on 2018/4/25.
 */

public interface PermissionListener {
    //授权成功
    void onGranted();

    //授权部分
    void onGranted(List<String> grantedPermission);

    //拒绝授权
    void onDenied(List<String> deniedPermission);
}
