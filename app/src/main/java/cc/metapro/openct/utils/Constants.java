package cc.metapro.openct.utils;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;

/**
 * Created by jeffrey on 11/30/16.
 */

public final class Constants {

    public final static int
            VIEWSTATE_INDEX = 0,
            USER_INDEX = 1,
            PASSWD_INDEX = 2,
            VCODE_INDEX = 3;

    public final static String USERNAME = "username", PASSWORD = "password", CAPTCHA = "captcha";

    public final static String[] colorString = {
            "#8BC34A", "#03A9F4",
            "#FF9800", "#C5CAE9", "#FFCDD2",
            "#009688", "#536DFE"};

    public final static int
            RESULT_OK = 1, RESULT_FAIL = 2, RESULT_EMPTY = 3,
            CAPTCHA_IMG_OK = 4, CAPTCHA_IMG_FAIL = 5, MORE_OK = 6, MORE_FAIL = 7;

    public static int getColor(int seq) {
        return Color.parseColor(colorString[seq]);
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}