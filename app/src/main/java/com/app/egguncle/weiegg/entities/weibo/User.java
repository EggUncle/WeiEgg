package com.app.egguncle.weiegg.entities.weibo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by egguncle on 16.10.13.
 */
public class User {
    //用户UID
    private long id;
    //字符串型的用户UID
    private String idstr;

    @SerializedName("class")
    private int class2;
    //	用户昵称
    private String screen_name;
    //友好显示名称
    private String name;
    //	用户所在省级ID
    private String province;
    //用户所在城市ID
    private String city;
    //用户所在地
    private String location;
    //用户个人描述
    private String description;
    //用户博客地址
    private String url;
    //用户头像地址（中图），50×50像素
    private String profile_image_url;
    //	用户的微博统一URL地址
    private String profile_url;
    //用户的个性化域名
    private String domain;
    //	用户的微号
    private String weihao;
    //性别，m：男、f：女、n：未知
    private String gender;
    //	粉丝数
    private int followers_count;
    //	关注数
    private int friends_count;

    private int pagefriends_count;
    //微博数
    private int statuses_count;
    //收藏数
    private int favourites_count;
    //	用户创建（注册）时间
    private String created_at;
    //暂未支持
    private boolean following;
    //是否允许所有人给我发私信，true：是，false：否
    private boolean allow_all_act_msg;
    //是否允许标识用户的地理位置，true：是，false：否
    private boolean geo_enabled;
    //是否是微博认证用户，即加V用户，true：是，false：否
    private boolean verified;
    //	暂未支持
    private int verified_type;
    //用户备注信息，只有在查询用户关系时才返回此字段
    private String remark;

    private int ptype;
    //是否允许所有人对我的微博进行评论，true：是，false：否
    private boolean allow_all_comment;
    //用户头像地址（大图），180×180像素
    private String avatar_large;
    //用户头像地址（高清），高清头像原图
    private String avatar_hd;
    //认证原因
    private String verified_reason;

    private String verified_trade;

    private String verified_reason_url;

    private String verified_source;

    private String verified_source_url;
    //该用户是否关注当前登录用户，true：是，false：否
    private boolean follow_me;
    //用户的在线状态，0：不在线、1：在线
    private int online_status;
    //	用户的互粉数
    private int bi_followers_count;
//用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语

    private String lang;

    private int star;

    private int mbtype;

    private int mbrank;

    private int block_word;

    private int block_app;

    private int credit_score;

    private int user_ability;

    private int urank;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getIdstr() {
        return this.idstr;
    }

    public void setClass2(int class2) {
        this.class2 = class2;
    }

    public int getClass2() {
        return this.class2;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getScreen_name() {
        return this.screen_name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return this.province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return this.location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getProfile_image_url() {
        return this.profile_image_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getProfile_url() {
        return this.profile_url;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return this.domain;
    }

    public void setWeihao(String weihao) {
        this.weihao = weihao;
    }

    public String getWeihao() {
        return this.weihao;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return this.gender;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getFollowers_count() {
        return this.followers_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public int getFriends_count() {
        return this.friends_count;
    }

    public void setPagefriends_count(int pagefriends_count) {
        this.pagefriends_count = pagefriends_count;
    }

    public int getPagefriends_count() {
        return this.pagefriends_count;
    }

    public void setStatuses_count(int statuses_count) {
        this.statuses_count = statuses_count;
    }

    public int getStatuses_count() {
        return this.statuses_count;
    }

    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    public int getFavourites_count() {
        return this.favourites_count;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean getFollowing() {
        return this.following;
    }

    public void setAllow_all_act_msg(boolean allow_all_act_msg) {
        this.allow_all_act_msg = allow_all_act_msg;
    }

    public boolean getAllow_all_act_msg() {
        return this.allow_all_act_msg;
    }

    public void setGeo_enabled(boolean geo_enabled) {
        this.geo_enabled = geo_enabled;
    }

    public boolean getGeo_enabled() {
        return this.geo_enabled;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean getVerified() {
        return this.verified;
    }

    public void setVerified_type(int verified_type) {
        this.verified_type = verified_type;
    }

    public int getVerified_type() {
        return this.verified_type;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setPtype(int ptype) {
        this.ptype = ptype;
    }

    public int getPtype() {
        return this.ptype;
    }

    public void setAllow_all_comment(boolean allow_all_comment) {
        this.allow_all_comment = allow_all_comment;
    }

    public boolean getAllow_all_comment() {
        return this.allow_all_comment;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

    public String getAvatar_large() {
        return this.avatar_large;
    }

    public void setAvatar_hd(String avatar_hd) {
        this.avatar_hd = avatar_hd;
    }

    public String getAvatar_hd() {
        return this.avatar_hd;
    }

    public void setVerified_reason(String verified_reason) {
        this.verified_reason = verified_reason;
    }

    public String getVerified_reason() {
        return this.verified_reason;
    }

    public void setVerified_trade(String verified_trade) {
        this.verified_trade = verified_trade;
    }

    public String getVerified_trade() {
        return this.verified_trade;
    }

    public void setVerified_reason_url(String verified_reason_url) {
        this.verified_reason_url = verified_reason_url;
    }

    public String getVerified_reason_url() {
        return this.verified_reason_url;
    }

    public void setVerified_source(String verified_source) {
        this.verified_source = verified_source;
    }

    public String getVerified_source() {
        return this.verified_source;
    }

    public void setVerified_source_url(String verified_source_url) {
        this.verified_source_url = verified_source_url;
    }

    public String getVerified_source_url() {
        return this.verified_source_url;
    }

    public void setFollow_me(boolean follow_me) {
        this.follow_me = follow_me;
    }

    public boolean getFollow_me() {
        return this.follow_me;
    }

    public void setOnline_status(int online_status) {
        this.online_status = online_status;
    }

    public int getOnline_status() {
        return this.online_status;
    }

    public void setBi_followers_count(int bi_followers_count) {
        this.bi_followers_count = bi_followers_count;
    }

    public int getBi_followers_count() {
        return this.bi_followers_count;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLang() {
        return this.lang;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getStar() {
        return this.star;
    }

    public void setMbtype(int mbtype) {
        this.mbtype = mbtype;
    }

    public int getMbtype() {
        return this.mbtype;
    }

    public void setMbrank(int mbrank) {
        this.mbrank = mbrank;
    }

    public int getMbrank() {
        return this.mbrank;
    }

    public void setBlock_word(int block_word) {
        this.block_word = block_word;
    }

    public int getBlock_word() {
        return this.block_word;
    }

    public void setBlock_app(int block_app) {
        this.block_app = block_app;
    }

    public int getBlock_app() {
        return this.block_app;
    }

    public void setCredit_score(int credit_score) {
        this.credit_score = credit_score;
    }

    public int getCredit_score() {
        return this.credit_score;
    }

    public void setUser_ability(int user_ability) {
        this.user_ability = user_ability;
    }

    public int getUser_ability() {
        return this.user_ability;
    }

    public void setUrank(int urank) {
        this.urank = urank;
    }

    public int getUrank() {
        return this.urank;
    }
}
