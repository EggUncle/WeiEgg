package com.app.egguncle.weiegg.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.egguncle.weiegg.R;
import com.app.egguncle.weiegg.entities.HttpResponse;
import com.app.egguncle.weiegg.entities.weibo.Statuses;
import com.app.egguncle.weiegg.entities.weibo.User;
import com.app.egguncle.weiegg.entities.weibo.WeiBoRoot;
import com.app.egguncle.weiegg.network.BaseNetWork;
import com.app.egguncle.weiegg.network.CWUrls;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.net.WeiboParameters;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by egguncle on 16.10.16.
 * 用来进行微博相关操作的模块
 */
public class WeiBoUtils {

    private static long mUid;
    private static List<Statuses> mListStatuses;//获取到的微博数据
    private static List<Statuses> mListFriendStatuses; //获取到的好友的微博数据
    private static long mSinceId = 0;  //若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
    private static long mMaxId = 0;       //若指定此参数，则返回ID小于或等于max_id的微博，默认为0。

    private static long mFriendSinceId=0;  //和上面一样，但是用于获取单个用户的微博
    private static long mFriendMaxId=0;

    public final static int GET_NEW_WEIBO = 1;
    public final static int GET_OLD_WEIBO = 2;

    /**
     * 用于获取新的微博
     *
     * @param context
     * @param parameters
     * @param accessToken
     * @param requestType 请求类型 ：GET_NEW_WEIBO获取新微博，GET_OLD_WEIBO获取更早的微博
     */
    public static void getPublicWeiBo(final Context context, final WeiboParameters parameters, final String accessToken, final int requestType) {

        new BaseNetWork(context, CWUrls.HOME_TIME_LINE) {

            @Override
            public WeiboParameters onPrepare() {
                parameters.put(WBConstants.AUTH_ACCESS_TOKEN, accessToken);
                parameters.put("count", 10);
                if (requestType == GET_NEW_WEIBO) {
                    // 在请求中添加since_id参数，这样在请求的时候只会返回最新的数据，不会重复请求已经获取的数据
                    LogUtils.e("参数中的since_id为： " + mSinceId);
                    parameters.put("since_id", mSinceId);
                }
                if (requestType == GET_OLD_WEIBO) {
                    //在请求中添加max_id参数，这样请求的时候就会返回早一些的数据，不会重复请求已经获取的数据
                    LogUtils.e("参数中的max_id为： " + mMaxId);
                    parameters.put("max_id", mMaxId);
                }
                return parameters;
            }

            @Override
            public void onFinish(HttpResponse response, boolean sucess) {
                if (sucess) {
                    LogUtils.e("返回的数据为 " + response.response);
                    Gson gson = new Gson();
                    WeiBoRoot weiBoRoot = gson.fromJson(response.response, WeiBoRoot.class);
                    if (weiBoRoot.getStatuses().size() != 0) {
                        if (mListStatuses == null) {
                            mListStatuses = weiBoRoot.getStatuses();
                        } else {
                            for (int i = 0; i < weiBoRoot.getStatuses().size(); i++) {
                                if (requestType == GET_NEW_WEIBO) {
                                    mListStatuses.add(i, weiBoRoot.getStatuses().get(i));
                                }
                                if (requestType == GET_OLD_WEIBO) {
                                    mListStatuses.add(weiBoRoot.getStatuses().get(i));
                                }
                            }
                        }
                    }
                    LogUtils.e("---------------------------------------------");
                    LogUtils.e(mListStatuses.size() + "");
                    for (int i =0 ;i<mListStatuses.size();i++){
                        LogUtils.e(i+" "+mListStatuses.get(i).getText() +"\n");
                    }

                    mSinceId = mListStatuses.get(0).getId();
                    mMaxId = mListStatuses.get(mListStatuses.size() - 1).getId();

                    LogUtils.e("最新的微博为： " + mListStatuses.get(0).getText());
                    LogUtils.e("list中最旧的微博为： " + mListStatuses.get(mListStatuses.size() - 1).getText());
                    LogUtils.e("获取到的最新的微博ID为： " + mSinceId);
                    LogUtils.e("获取到的最早的微博ID为： " + mMaxId);

                    LogUtils.e("---------------------------------------------");

                } else {
                    LogUtils.e("OnFinish() returned:" + response.message);
                }
                LogUtils.e("请求发送完毕");
            }
        }.get();
    }

    /**
     * 发送微博
     *
     * @param weiboContent
     * @param accessToken
     */
    public static void sendMyWeiBo(final String weiboContent, final String accessToken) {
//        String url = "https://api.weibo.com/2/statuses/update.json";
//        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//                Log.v("MY_TAG", "SUCESS :" + s);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.v("MY_TAG", "Fail");
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<String, String>();
//                map.put("access_token",accessToken);
//                map.put("status", weiboContent);
//                return map;
//            }
//
//        };
//        request.setTag("sendWeiBo");
//        MyApplication.getHttpQueues().add(request);
    }

    public static void getUserInformation(Context context, final WeiboParameters parameters, final String accessToken) {
        new BaseNetWork(context, CWUrls.USERS_SHOW) {

            @Override
            public WeiboParameters onPrepare() {
                parameters.put(WBConstants.AUTH_ACCESS_TOKEN, accessToken);
                parameters.put(WBConstants.GAME_PARAMS_UID, mUid);
                return parameters;
            }

            @Override
            public void onFinish(HttpResponse response, boolean sucess) {
                if (sucess) {
                    LogUtils.e("s =================== " + response.response);
                    Gson gson = new Gson();
                    User user = gson.fromJson(response.response, User.class);
                    LogUtils.e("d ==================== " + user.getScreen_name());
                } else {
                    LogUtils.e("OnFinish() returned:" + response.message);
                }
            }
        }.get();
    }

    /**
     * 获取用户的UID，结果存于UID对象中
     *
     * @param context
     * @param parameters
     * @param accessToken
     */
    public static void getUid(Context context, final WeiboParameters parameters, final String accessToken) {
        new BaseNetWork(context, CWUrls.GET_UID) {

            @Override
            public WeiboParameters onPrepare() {
                parameters.put(WBConstants.AUTH_ACCESS_TOKEN, accessToken);
                return parameters;
            }

            @Override
            public void onFinish(HttpResponse response, boolean sucess) {
                if (sucess) {
                    LogUtils.e("s =================== " + response.response);
                    JsonParser parser = new JsonParser();
                    JsonElement element = parser.parse(response.response);
                    if (element.isJsonObject()) {
                        JsonObject object = element.getAsJsonObject();
                        long uid = Long.parseLong(object.get("uid").toString());
                        LogUtils.e("s =================== " + uid);
//                        Uid mUid = Uid.getIntance();
//                        mUid.setlUid(uid);
                        mUid = uid;
                    }
                } else {
                    LogUtils.e("OnFinish() returned:" + response.message);
                }

            }
        }.get();
    }


    public static List<Statuses> getmListStatuses() {
        if (mListStatuses == null) {
            mListStatuses = new ArrayList<>();
        }

        return mListStatuses;
    }
    public static List<Statuses> getmListFriendStatuses() {
        if (mListFriendStatuses == null) {
            mListFriendStatuses = new ArrayList<>();
        }

        return mListFriendStatuses;
    }

    public static void getFollowMe(Context context, final WeiboParameters parameters, final String accessToken, final String name, final TextView textView, final ImageView imageView) {
        new BaseNetWork(context, CWUrls.GET_USER) {

            @Override
            public WeiboParameters onPrepare() {
                parameters.put(WBConstants.AUTH_ACCESS_TOKEN, accessToken);
                parameters.put("screen_name",name);
                return parameters;
            }

            @Override
            public void onFinish(HttpResponse response, boolean sucess) {
                if (sucess) {
                    Gson gson = new Gson();
                    User user=gson.fromJson(response.response,User.class);
                    if (user.getFollowing()&&user.getFollow_me()){
                        textView.setText("互相关注");
                        imageView.setImageResource(R.mipmap.each_other);
                    }else if(user.getFollowing()&&!user.getFollow_me()){
                        textView.setText("正关注");
                        imageView.setImageResource(R.mipmap.following);
                    }else if(!user.getFollowing()&&user.getFollow_me()){
                        textView.setText("被关注");
                        imageView.setImageResource(R.mipmap.add_following);
                    }else if(!user.getFollowing()&&!user.getFollow_me()){
                        textView.setText("未关注");
                        imageView.setImageResource(R.mipmap.add_following);
                    }
                } else {
                    LogUtils.e("OnFinish() returned:" + response.message);
                }

            }
        }.get();
    }

    public static void getFriendWeiBo(final Context context, final WeiboParameters parameters, final String accessToken, final int requestType) {

        new BaseNetWork(context, CWUrls.GET_FRIEND) {

            @Override
            public WeiboParameters onPrepare() {
                parameters.put(WBConstants.AUTH_ACCESS_TOKEN, accessToken);

//                parameters.put("count", 10);
//                if (requestType == GET_NEW_WEIBO) {
//                    // 在请求中添加since_id参数，这样在请求的时候只会返回最新的数据，不会重复请求已经获取的数据
//                    LogUtils.e("参数中的since_id为： " + mFriendSinceId);
//                    parameters.put("since_id", mFriendSinceId);
//                }
//                if (requestType == GET_OLD_WEIBO) {
//                    //在请求中添加max_id参数，这样请求的时候就会返回早一些的数据，不会重复请求已经获取的数据
//                    LogUtils.e("参数中的max_id为： " + mFriendMaxId);
//                    parameters.put("max_id", mFriendMaxId);
//                }
                return parameters;
            }

            @Override
            public void onFinish(HttpResponse response, boolean sucess) {
                if (sucess) {
                    LogUtils.e("返回的数据为 " + response.response);
                    Gson gson = new Gson();
                    WeiBoRoot weiBoRoot = gson.fromJson(response.response, WeiBoRoot.class);
                    if (weiBoRoot.getStatuses().size() != 0) {
                        if (mListFriendStatuses == null) {
                            mListFriendStatuses = weiBoRoot.getStatuses();
                        } else {
                            for (int i = 0; i < weiBoRoot.getStatuses().size(); i++) {
                                if (requestType == GET_NEW_WEIBO) {
                                    mListFriendStatuses.add(i, weiBoRoot.getStatuses().get(i));
                                }
                                if (requestType == GET_OLD_WEIBO) {
                                    mListFriendStatuses.add(weiBoRoot.getStatuses().get(i));
                                }
                            }
                        }
                    }
                    LogUtils.e("------------------Friend---------------------------");
                    LogUtils.e(mListFriendStatuses.size() + "");
                    if (mListFriendStatuses.size()!=0) {
                        for (int i = 0; i < mListFriendStatuses.size(); i++) {
                            LogUtils.e(i + " " + mListFriendStatuses.get(i).getText() + "\n");
                        }

                        mSinceId = mListFriendStatuses.get(0).getId();
                        mMaxId = mListFriendStatuses.get(mListFriendStatuses.size() - 1).getId();

                        LogUtils.e("最新的微博为： " + mListFriendStatuses.get(0).getText());
                        LogUtils.e("list中最旧的微博为： " + mListFriendStatuses.get(mListFriendStatuses.size() - 1).getText());
                        LogUtils.e("获取到的最新的微博ID为： " + mFriendSinceId);
                        LogUtils.e("获取到的最早的微博ID为： " + mFriendMaxId);
                    }
                    LogUtils.e("-----------------Friend----------------------------");
                } else {
                    LogUtils.e("OnFinish() returned:" + response.message);
                }
                LogUtils.e("请求发送完毕");
            }
        }.get();


    }




    public static long getmUid() {
        return mUid;
    }

}
