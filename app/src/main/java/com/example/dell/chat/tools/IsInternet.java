package com.example.dell.chat.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by courageface on 2018/5/9.
 */

public class IsInternet {
    // check all network connect, WIFI or mobile
    public static boolean isNetworkAvailable(final Context context) {
        boolean hasWifoCon = false;
        boolean hasMobileCon = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfos = cm.getAllNetworkInfo();
        for (NetworkInfo net : netInfos) {

            String type = net.getTypeName();
            if (type.equalsIgnoreCase("WIFI")) {
                //LevelLogUtils.getInstance().i("internet", "get Wifi connection");
                if (net.isConnected()) {
                    hasWifoCon = true;
                }
            }

            if (type.equalsIgnoreCase("MOBILE")) {
                //LevelLogUtils.getInstance().i("internet", "get Mobile connection");
                if (net.isConnected()) {
                    hasMobileCon = true;
                }
            }
        }
        return hasWifoCon || hasMobileCon;

    }
}
