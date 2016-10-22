package com.app.egguncle.weiegg.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by egguncle on 16.10.13.
 */
public class LogUtils {
    private static final String TAG = "MY_TAG";

    public static void e(String message) {
        if (!TextUtils.isEmpty(message)) {
            Log.e(TAG, message);
        }
    }

    public static void d(String message) {
        if (!TextUtils.isEmpty(message)) {
       //     Log.d(TAG, message);
        }
    }
}
