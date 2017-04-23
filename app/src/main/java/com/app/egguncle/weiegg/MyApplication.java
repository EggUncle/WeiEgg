package com.app.egguncle.weiegg;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.app.egguncle.weiegg.utils.SPUtils;

/**
 * Created by egguncle on 16.7.17.
 */
public class MyApplication extends Application {

    public static RequestQueue queues;
    private static SPUtils spUtils;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        queues = Volley.newRequestQueue(getApplicationContext());
        spUtils=  SPUtils.getInstance(this);
        context=getApplicationContext();
    }

    public static SPUtils getSpUtils(){
        return spUtils;
    }

    public static Context getMyContext(){
        return context;
    }

    public static RequestQueue getHttpQueues(){
        return queues;
    }
}
