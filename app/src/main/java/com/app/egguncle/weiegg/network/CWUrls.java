package com.app.egguncle.weiegg.network;

/**
 * Created by egguncle on 16.10.12.
 */
public class CWUrls {
    private static String PREFIX = "https://api.weibo.com/2/statuses/";
    public static final String HOME_TIME_LINE = PREFIX + "home_timeline.json"; //获取最新的微博
    public static final String USERS_SHOW="https://api.weibo.com/2/users/show.json";//获取用户信息
    public static final String GET_UID="https://api.weibo.com/2/account/get_uid.json";//获取当前用户UID
}
