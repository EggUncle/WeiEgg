package com.app.egguncle.weiegg.entities.weibo;

import java.util.List;

/**
 * Created by egguncle on 16.10.18.
 */
public class RetweetedStatus {

    //微博创建时间
    private String created_at;
    //微博ID
    private long id;
    //微博MID
    private String mid;
    //字符串型的微博ID
    private String idstr;
    //微博信息内容
    private String text;

    private int textLength;

    private int source_allowclick;

    private int source_type;
    //微博来源
    private String source;
    //是否已收藏，true：是，false：否
    private boolean favorited;
    //是否被截断，true：是，false：否
    private boolean truncated;
    //（暂未支持）回复ID
    private String in_reply_to_status_id;
    //（暂未支持）回复人UID
    private String in_reply_to_user_id;
    //（暂未支持）回复人昵称
    private String in_reply_to_screen_name;
    //图片链接
    private List<PicUrls> pic_urls;//微博的图片内容，此处用JSON工具解析出来是List<String>类型，解析报错，故新建一个PicUrl类代替之
    //地理信息字段
    private Geo geo;
    //微博作者的用户信息字段
    private User user;

    //被转发的原微博信息字段
    private RetweetedStatus retweeted_status;

    private List<Annotations> annotations;
    //转发数
    private int reposts_count;
    //评论数
    private int comments_count;
    //表态数
    private int attitudes_count;

    private boolean isLongText;
    //暂未支持
    private int mlevel;
    //微博的可见性及指定可见分组信息。该object中type取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；list_id为分组的组号
    private Visible visible;

    private long biz_feature;

    private int hasActionTypeCard;

    private List<String> darwin_tags;

    private List<String> hot_weibo_tags;

    private List<String> text_tag_tips;

    private int userType;

    private int positive_recom_flag;

    private String gif_ids;

    private int is_show_bulletin;

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMid() {
        return this.mid;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getIdstr() {
        return this.idstr;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }

    public int getTextLength() {
        return this.textLength;
    }

    public void setSource_allowclick(int source_allowclick) {
        this.source_allowclick = source_allowclick;
    }

    public int getSource_allowclick() {
        return this.source_allowclick;
    }

    public void setSource_type(int source_type) {
        this.source_type = source_type;
    }

    public int getSource_type() {
        return this.source_type;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return this.source;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean getFavorited() {
        return this.favorited;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public boolean getTruncated() {
        return this.truncated;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public RetweetedStatus getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(RetweetedStatus retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public String getIn_reply_to_status_id() {
        return this.in_reply_to_status_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_user_id() {
        return this.in_reply_to_user_id;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public String getIn_reply_to_screen_name() {
        return this.in_reply_to_screen_name;
    }

    public void setPic_urls(List<PicUrls> pic_urls) {
        this.pic_urls = pic_urls;
    }

    public List<PicUrls> getPic_urls() {
        return this.pic_urls;
    }

    public void setGeo(Geo geo) {
        this.geo = geo;
    }


    public Geo getGeo() {
        return this.geo;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setAnnotations(List<Annotations> annotations) {
        this.annotations = annotations;
    }

    public List<Annotations> getAnnotations() {
        return this.annotations;
    }

    public void setReposts_count(int reposts_count) {
        this.reposts_count = reposts_count;
    }

    public int getReposts_count() {
        return this.reposts_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getComments_count() {
        return this.comments_count;
    }

    public void setAttitudes_count(int attitudes_count) {
        this.attitudes_count = attitudes_count;
    }

    public int getAttitudes_count() {
        return this.attitudes_count;
    }

    public void setIsLongText(boolean isLongText) {
        this.isLongText = isLongText;
    }

    public boolean getIsLongText() {
        return this.isLongText;
    }

    public void setMlevel(int mlevel) {
        this.mlevel = mlevel;
    }

    public int getMlevel() {
        return this.mlevel;
    }

    public void setVisible(Visible visible) {
        this.visible = visible;
    }

    public Visible getVisible() {
        return this.visible;
    }

    public void setBiz_feature(long biz_feature) {
        this.biz_feature = biz_feature;
    }

    public long getBiz_feature() {
        return this.biz_feature;
    }

    public void setHasActionTypeCard(int hasActionTypeCard) {
        this.hasActionTypeCard = hasActionTypeCard;
    }

    public int getHasActionTypeCard() {
        return this.hasActionTypeCard;
    }

    public void setDarwin_tags(List<String> darwin_tags) {
        this.darwin_tags = darwin_tags;
    }

    public List<String> getDarwin_tags() {
        return this.darwin_tags;
    }

    public void setHot_weibo_tags(List<String> hot_weibo_tags) {
        this.hot_weibo_tags = hot_weibo_tags;
    }

    public List<String> getHot_weibo_tags() {
        return this.hot_weibo_tags;
    }

    public void setText_tag_tips(List<String> text_tag_tips) {
        this.text_tag_tips = text_tag_tips;
    }

    public List<String> getText_tag_tips() {
        return this.text_tag_tips;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getUserType() {
        return this.userType;
    }

    public void setPositive_recom_flag(int positive_recom_flag) {
        this.positive_recom_flag = positive_recom_flag;
    }

    public int getPositive_recom_flag() {
        return this.positive_recom_flag;
    }

    public void setGif_ids(String gif_ids) {
        this.gif_ids = gif_ids;
    }

    public String getGif_ids() {
        return this.gif_ids;
    }

    public void setIs_show_bulletin(int is_show_bulletin) {
        this.is_show_bulletin = is_show_bulletin;
    }

    public int getIs_show_bulletin() {
        return this.is_show_bulletin;
    }
}


