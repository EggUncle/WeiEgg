package com.app.egguncle.weiegg;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.app.egguncle.weiegg.utils.SPUtils;

/**
 * Created by egguncle on 16.7.17.
 */
public class MyApplication extends Application {

    public static RequestQueue queues;
    private static SPUtils spUtils;

    @Override
    public void onCreate() {
        super.onCreate();
       // queues = Volley.newRequestQueue(getApplicationContext());
        spUtils=  SPUtils.getInstance(this);
    }

    public static SPUtils getSpUtils(){
        return spUtils;
    }

    public static RequestQueue getHttpQueues(){
        return queues;
    }
}
