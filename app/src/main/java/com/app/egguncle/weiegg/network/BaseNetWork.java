package com.app.egguncle.weiegg.network;

import android.content.Context;

import com.app.egguncle.weiegg.entities.HttpResponse;
import com.app.egguncle.weiegg.entities.weibo.PicUrls;
import com.app.egguncle.weiegg.entities.weibo.WeiBoRoot;
import com.app.egguncle.weiegg.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.HttpManager;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;

import java.util.List;

/**
 * Created by egguncle on 16.10.12.
 */
public abstract class BaseNetWork {
    private AsyncWeiboRunner mAsyncWeiboRunner;
    private String url;

    public BaseNetWork(Context context, String url) {
        mAsyncWeiboRunner = new AsyncWeiboRunner(context);

        this.url = url;
    }

    private RequestListener mRequestListener = new RequestListener() {
        @Override
        public void onComplete(String s) {
//            {
//                "request" : "/statuses/home_timeline.json",
//                    "error_code" : "20502",
//                    "error" : "Need you follow uid."
//            }

            boolean success = false;
            HttpResponse response = new HttpResponse();

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(s);
            if (element.isJsonObject()) {
                JsonObject object = element.getAsJsonObject();
                if (object.has("error_code")) {
                    response.code = object.get("error_code").getAsInt();
                }
                if ((object.has("error"))) {
                    response.message = object.get("error").getAsString();
                }
                if (object.has("statuses")) {
                    //response.response = object.get("statuses").toString();
                    response.response=s;
                    success = true;
                } else if (object.has("users")) {
                    response.response = object.get("users").toString();
                    success = true;
                } else if (object.has("comments")) {
                    response.response = object.get("comments").toString();
                    success = true;
                } else {
                    response.response = s;
                    success = true;
                }
            }
            onFinish(response, success);
        }

        @Override
        public void onWeiboException(WeiboException e) {

            HttpResponse response = new HttpResponse();
            response.message = e.getMessage();
            onFinish(response, false);
        }
    };


    public void get() {
        mAsyncWeiboRunner.requestAsync(url, onPrepare(), "GET", mRequestListener);

    }

    public void post() {
        mAsyncWeiboRunner.requestAsync(url, onPrepare(), "POST", mRequestListener);

    }

    public void delete() {
        mAsyncWeiboRunner.requestAsync(url, onPrepare(), "DELETE", mRequestListener);

    }

    public abstract WeiboParameters onPrepare();

    public abstract void onFinish(HttpResponse response, boolean sucess);


}
