package net.arvin.socialhelper.entities;

/**
 * Created by arvinljw on 17/11/27 16:59
 * Function：
 * Desc：
 */
public final class WBInfoEntity {
    /**
     * id	int64	用户UID
     * idstr	string	字符串型的用户UID
     * screen_name	string	用户昵称
     * name	string	友好显示名称
     * province	int	用户所在省级ID
     * city	int	用户所在城市ID
     * location	string	用户所在地
     * description	string	用户个人描述
     * url	string	用户博客地址
     * profile_image_url	string	用户头像地址（中图），50×50像素
     * profile_url	string	用户的微博统一URL地址
     * domain	string	用户的个性化域名
     * weihao	string	用户的微号
     * gender	string	性别，m：男、f：女、n：未知
     * followers_count	int	粉丝数
     * friends_count	int	关注数
     * statuses_count	int	微博数
     * favourites_count	int	收藏数
     * created_at	string	用户创建（注册）时间
     * following	boolean	暂未支持
     * allow_all_act_msg	boolean	是否允许所有人给我发私信，true：是，false：否
     * geo_enabled	boolean	是否允许标识用户的地理位置，true：是，false：否
     * verified	boolean	是否是微博认证用户，即加V用户，true：是，false：否
     * verified_type	int	暂未支持
     * remark	string	用户备注信息，只有在查询用户关系时才返回此字段
     * status	object	用户的最近一条微博信息字段 详细//这个没有要
     * allow_all_comment	boolean	是否允许所有人对我的微博进行评论，true：是，false：否
     * avatar_large	string	用户头像地址（大图），180×180像素
     * avatar_hd	string	用户头像地址（高清），高清头像原图
     * verified_reason	string	认证原因
     * follow_me	boolean	该用户是否关注当前登录用户，true：是，false：否
     * online_status	int	用户的在线状态，0：不在线、1：在线
     * bi_followers_count	int	用户的互粉数
     * lang	string	用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语
     */

    private String id;
    private String idstr;
    private String screen_name;
    private String name;
    private String province;
    private String city;
    private String location;
    private String description;
    private String url;
    private String profile_image_url;
    private String domain;
    private String gender;
    private int followers_count;
    private int friends_count;
    private int statuses_count;
    private int favourites_count;
    private String created_at;
    private boolean following;
    private boolean allow_all_act_msg;
    private boolean geo_enabled;
    private boolean verified;
    private boolean allow_all_comment;
    private String avatar_large;
    private String verified_reason;
    private boolean follow_me;
    private int online_status;
    private int bi_followers_count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public void setFriends_count(int friends_count) {
        this.friends_count = friends_count;
    }

    public int getStatuses_count() {
        return statuses_count;
    }

    public void setStatuses_count(int statuses_count) {
        this.statuses_count = statuses_count;
    }

    public int getFavourites_count() {
        return favourites_count;
    }

    public void setFavourites_count(int favourites_count) {
        this.favourites_count = favourites_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public boolean isAllow_all_act_msg() {
        return allow_all_act_msg;
    }

    public void setAllow_all_act_msg(boolean allow_all_act_msg) {
        this.allow_all_act_msg = allow_all_act_msg;
    }

    public boolean isGeo_enabled() {
        return geo_enabled;
    }

    public void setGeo_enabled(boolean geo_enabled) {
        this.geo_enabled = geo_enabled;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isAllow_all_comment() {
        return allow_all_comment;
    }

    public void setAllow_all_comment(boolean allow_all_comment) {
        this.allow_all_comment = allow_all_comment;
    }

    public String getAvatar_large() {
        return avatar_large;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

    public String getVerified_reason() {
        return verified_reason;
    }

    public void setVerified_reason(String verified_reason) {
        this.verified_reason = verified_reason;
    }

    public boolean isFollow_me() {
        return follow_me;
    }

    public void setFollow_me(boolean follow_me) {
        this.follow_me = follow_me;
    }

    public int getOnline_status() {
        return online_status;
    }

    public void setOnline_status(int online_status) {
        this.online_status = online_status;
    }

    public int getBi_followers_count() {
        return bi_followers_count;
    }

    public void setBi_followers_count(int bi_followers_count) {
        this.bi_followers_count = bi_followers_count;
    }

    @Override
    public String toString() {
        return "WBUserInfoTO{" +
                "id=" + id +
                ", idstr='" + idstr + '\'' +
                ", screen_name='" + screen_name + '\'' +
                ", name='" + name + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", domain='" + domain + '\'' +
                ", gender='" + gender + '\'' +
                ", followers_count=" + followers_count +
                ", friends_count=" + friends_count +
                ", statuses_count=" + statuses_count +
                ", favourites_count=" + favourites_count +
                ", created_at='" + created_at + '\'' +
                ", following=" + following +
                ", allow_all_act_msg=" + allow_all_act_msg +
                ", geo_enabled=" + geo_enabled +
                ", verified=" + verified +
                ", allow_all_comment=" + allow_all_comment +
                ", avatar_large='" + avatar_large + '\'' +
                ", verified_reason='" + verified_reason + '\'' +
                ", follow_me=" + follow_me +
                ", online_status=" + online_status +
                ", bi_followers_count=" + bi_followers_count +
                '}';
    }
}
