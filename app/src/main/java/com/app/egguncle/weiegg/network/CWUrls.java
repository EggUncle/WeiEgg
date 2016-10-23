package com.app.egguncle.weiegg.network;

/**
 * Created by egguncle on 16.10.12.
 */
public class CWUrls {
    private static String PREFIX = "https://api.weibo.com/2/statuses/";
    public static final String HOME_TIME_LINE = PREFIX + "home_timeline.json"; //获取最新的微博
    public static final String USERS_SHOW="https://api.weibo.com/2/users/show.json";//获取用户信息
    public static final String GET_UID="https://api.weibo.com/2/account/get_uid.json";//获取当前用户UID

    //获取用户信息，此处这个URL用来仅获取对方是否关注自己以及自己是否关注对方
    public static final String GET_USER= "https://api.weibo.com/2/users/show.json";

    //获取好友微博
    public static final String GET_FRIEND="https://api.weibo.com/2/statuses/user_timeline.json";
}
