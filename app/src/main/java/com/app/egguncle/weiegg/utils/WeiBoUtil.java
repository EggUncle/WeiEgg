package com.app.egguncle.weiegg.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.egguncle.weiegg.CWConstant;
import com.app.egguncle.weiegg.MyApplication;
import com.app.egguncle.weiegg.R;
import com.app.egguncle.weiegg.activity.HomePageActivity;
import com.app.egguncle.weiegg.adapter.WeiboRecyclerViewAdapter;
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
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.net.HttpManager;
import com.sina.weibo.sdk.net.WeiboParameters;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by egguncle on 16.10.16.
 * 用来进行微博相关操作的模块
 */
public class WeiBoUtil {


    private static String mToken;
    private static WeiBoUtil weiBoUtil;
    private static LocalBroadcastManager localBroadcastManager;

    private static long mUid;
    private static List<Statuses> mListStatuses;//获取到的微博数据
    private static List<Statuses> mListFriendStatuses; //获取到的好友的微博数据
    private static long mSinceId = 0;  //若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
    private static long mMaxId = 0;       //若指定此参数，则返回ID小于或等于max_id的微博，默认为0。

    private static long mFriendSinceId = 0;  //和上面一样，但是用于获取单个用户的微博
    private static long mFriendMaxId = 0;

    public final static int GET_NEW_WEIBO = 1;
    public final static int GET_OLD_WEIBO = 2;

    //是否正在加载数据,主要用于在刷新微博的时候，当请求还没有得到相应的时候，since_id或者max_id都还没有发生变化，
    // 此时如果再次请求，就可能会发出两个一样的请求，返回一样的数据
    //  public static boolean mLoadingKey = false;

    public static User mUser;

 //   public static WeiboParameters mParameters;

//    public static WeiboParameters getParameters() {
//        if (mParameters == null) {
//            mParameters = new WeiboParameters(CWConstant.APP_KEY);
//            return mParameters;
//        }else{
//            return mParameters;
//        }
//    }

    public static WeiBoUtil getWeiboUtils() {
        if (weiBoUtil == null) {
           // mParameters = new WeiboParameters(CWConstant.APP_KEY);
            mToken = MyApplication.getSpUtils().getToken().getToken();
            weiBoUtil = new WeiBoUtil();
            localBroadcastManager = HomePageActivity.getLocalBroadcastManager();
        }
        return weiBoUtil;
    }

    private WeiBoUtil() {

    }

    /**
     * 用于获取新的微博
     *
     * @param context
     * @param requestType 请求类型 ：GET_NEW_WEIBO获取新微博，GET_OLD_WEIBO获取更早的微博
     */
    public void getPublicWeiBo(final Context context, final int requestType) {


        new BaseNetWork(context, CWUrls.HOME_TIME_LINE) {

            @Override
            public WeiboParameters onPrepare() {
                WeiboParameters mParameters=new WeiboParameters(CWConstant.APP_KEY);
                mParameters.put(WBConstants.AUTH_ACCESS_TOKEN, mToken);
                mParameters.put("count", 10);
                if (requestType == GET_NEW_WEIBO) {
                    // 在请求中添加since_id参数，这样在请求的时候只会返回最新的数据，不会重复请求已经获取的数据
                    LogUtils.e("参数中的since_id为： " + mSinceId);
                    mParameters.put("since_id", mSinceId);
                }
                if (requestType == GET_OLD_WEIBO) {
                    //在请求中添加max_id参数，这样请求的时候就会返回早一些的数据，不会重复请求已经获取的数据
                    LogUtils.e("参数中的max_id为： " + mMaxId);
                    mParameters.put("max_id", mMaxId - 1);
                }
                return mParameters;
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


                            if (requestType == GET_NEW_WEIBO) {
                                for (int i = 0; i < weiBoRoot.getStatuses().size(); i++) {
                                    if (weiBoRoot.getStatuses().get(i).getId() < mSinceId)
                                        break;  //防止添加重复的数据进去

                                    mListStatuses.add(i, weiBoRoot.getStatuses().get(i));
                                    LogUtils.e("获取到的新微博为：" + weiBoRoot.getStatuses().get(i).getText() + "\n");
                                }
                            }
                            if (requestType == GET_OLD_WEIBO) {
                                for (int i = 0; i < weiBoRoot.getStatuses().size(); i++) {
                                    if (weiBoRoot.getStatuses().get(i).getId() > mMaxId)
                                        break;//防止添加重复的数据进去
                                    mListStatuses.add(mListStatuses.size(), weiBoRoot.getStatuses().get(i));
                                    LogUtils.e("获取到的较早时间的新微博为：" + weiBoRoot.getStatuses().get(i).getText() + "\n");
                                }
                            }

                        }
                    }
                    LogUtils.e("---------------------------------------------");
                    LogUtils.e(mListStatuses.size() + "");
                    for (int i = 0; i < mListStatuses.size(); i++) {
                        LogUtils.e(i + " " + mListStatuses.get(i).getText() + "\n");
                    }
                    mSinceId = mListStatuses.get(0).getId();
                    mMaxId = mListStatuses.get(mListStatuses.size() - 1).getId();

                    LogUtils.e("最新的微博为： " + mListStatuses.get(0).getText());
                    LogUtils.e("list中最旧的微博为： " + mListStatuses.get(mListStatuses.size() - 1).getText());
                    LogUtils.e("获取到的最新的微博ID为： " + mSinceId);
                    LogUtils.e("获取到的最早的微博ID为： " + mMaxId);

                    LogUtils.e("---------------------------------------------");


                    //发送广播通知home界面更新数据
                    Intent intent = new Intent(HomePageActivity.HOME_BROADCAST);
                    localBroadcastManager.sendBroadcast(intent);
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
     */
    public void sendMyWeiBo(final Context context, final String weiboContent) {


        new BaseNetWork(context, CWUrls.SEND_WEIBO) {

            @Override
            public WeiboParameters onPrepare() {
                WeiboParameters mParameters=new WeiboParameters(CWConstant.APP_KEY);
                mParameters.put(WBConstants.AUTH_ACCESS_TOKEN, mToken);
                mParameters.put("status", weiboContent);
                return mParameters;
            }

            @Override
            public void onFinish(HttpResponse response, boolean sucess) {
                if (sucess) {
                    LogUtils.e("发送成功 " + weiboContent);
                    Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
                } else {
                    LogUtils.e("OnFinish() returned:" + response.message);
                    Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.post();
    }

    /**
     * 发送带图片的微博 仅支持JPEG、GIF、PNG格式，图片大小小于5M。
     * @param context
     * @param weiboContent
     * @param imgUrl
     */
    public void sendMyWeiBoWithImg(final Context context, final String weiboContent, final String imgUrl) {


        //获得图片类型
        String imgTpye=imgUrl.substring(imgUrl.indexOf(".")+1,imgUrl.length());

        final Bitmap bitmap = BitmapFactory.decodeFile(imgUrl);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (imgTpye.equals("png")){
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        }else if(imgTpye.equals("jpeg")){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }


        final byte[] bytes =baos.toByteArray();


        new BaseNetWork(context, CWUrls.SEND_WEIBO_PIC) {

            @Override
            public WeiboParameters onPrepare() {
                WeiboParameters mParameters=new WeiboParameters(CWConstant.APP_KEY);
                mParameters.put(WBConstants.AUTH_ACCESS_TOKEN, mToken);
                mParameters.put("status", weiboContent);
                mParameters.put("pic",bytes);
                mParameters.put("Content-Type","multipart/form-data");


                return mParameters;
            }

            @Override
            public void onFinish(HttpResponse response, boolean sucess) {
                if (sucess) {
                    LogUtils.e("发送成功 " + weiboContent);
                    Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
                } else {
                    LogUtils.e("OnFinish() returned:" + response.message);
                    Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.post();
//        StringRequest stringRequest=new StringRequest(Request.Method.POST, CWUrls.SEND_WEIBO_PIC, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String s) {
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        }){
//            @Override
//            public String getBodyContentType() {
//                return "multipart/form-data";
//            }
//
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String> param=new HashMap<>();
//                param.put(WBConstants.AUTH_ACCESS_TOKEN, mToken);
//                param.put("status", weiboContent);
//                param.put("pic",bytes);
//
//                return param;
//            }
//        };
//        MyApplication.getHttpQueues().add(stringRequest);

    }

    public void getUserInformation(Context context) {
        new BaseNetWork(context, CWUrls.USERS_SHOW) {

            @Override
            public WeiboParameters onPrepare() {
                WeiboParameters mParameters=new WeiboParameters(CWConstant.APP_KEY);
                mParameters.put(WBConstants.AUTH_ACCESS_TOKEN, mToken);
                mParameters.put(WBConstants.GAME_PARAMS_UID, mUid);
                return mParameters;
            }

            @Override
            public void onFinish(HttpResponse response, boolean sucess) {
                if (sucess) {
                    LogUtils.e("s =================== " + response.response);
                    Gson gson = new Gson();
                    mUser = gson.fromJson(response.response, User.class);
                    LogUtils.e("用户名字是 " + mUser.getScreen_name());
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
     */
    public void getUid(Context context) {
        new BaseNetWork(context, CWUrls.GET_UID) {

            @Override
            public WeiboParameters onPrepare() {
                WeiboParameters mParameters=new WeiboParameters(CWConstant.APP_KEY);
                mParameters.put(WBConstants.AUTH_ACCESS_TOKEN, mToken);
                return mParameters;
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


    public List<Statuses> getmListStatuses() {
        if (mListStatuses == null) {
            mListStatuses = new ArrayList<>();
        }

        return mListStatuses;
    }

    public List<Statuses> getmListFriendStatuses() {
        if (mListFriendStatuses == null) {
            mListFriendStatuses = new ArrayList<>();
        }

        return mListFriendStatuses;
    }

    //获取关注状态
    public void getFollowMe(Context context, final String name, final TextView textView, final ImageView imageView) {
        new BaseNetWork(context, CWUrls.GET_USER) {

            @Override
            public WeiboParameters onPrepare() {
                WeiboParameters mParameters=new WeiboParameters(CWConstant.APP_KEY);
                mParameters.put(WBConstants.AUTH_ACCESS_TOKEN, mToken);
                mParameters.put("screen_name", name);
                return mParameters;
            }

            @Override
            public void onFinish(HttpResponse response, boolean sucess) {
                if (sucess) {
                    Gson gson = new Gson();
                    User user = gson.fromJson(response.response, User.class);
                    if (user.getFollowing() && user.getFollow_me()) {
                        textView.setText("互相关注");
                        imageView.setImageResource(R.mipmap.each_other);
                    } else if (user.getFollowing() && !user.getFollow_me()) {
                        textView.setText("正关注");
                        imageView.setImageResource(R.mipmap.following);
                    } else if (!user.getFollowing() && user.getFollow_me()) {
                        textView.setText("被关注");
                        imageView.setImageResource(R.mipmap.add_following);
                    } else if (!user.getFollowing() && !user.getFollow_me()) {
                        textView.setText("未关注");
                        imageView.setImageResource(R.mipmap.add_following);
                    }
                } else {
                    LogUtils.e("OnFinish() returned:" + response.message);
                }

            }
        }.get();
    }

    public void getFriendWeiBo(final Context context, final int requestType) {

        new BaseNetWork(context, CWUrls.GET_FRIEND) {

            @Override
            public WeiboParameters onPrepare() {
                WeiboParameters mParameters=new WeiboParameters(CWConstant.APP_KEY);
                mParameters.put(WBConstants.AUTH_ACCESS_TOKEN, mToken);

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
                return mParameters;
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
                    if (mListFriendStatuses.size() != 0) {
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


    public long getmUid() {
        return mUid;
    }

    public User getmUser() {
        return mUser;
    }

}
