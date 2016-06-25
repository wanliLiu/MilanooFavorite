package com.superdeal.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

    /*
     * 检查网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo ifo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (ifo != null) {
            if (ifo.isAvailable()) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

}
