package net.arvin.socialhelper.entities;

import android.os.Bundle;

import net.arvin.socialhelper.ParamsRequired;

/**
 * Created by arvinljw on 17/11/29 10:34
 * Function：
 * Desc：
 */
public final class WXShareEntity extends ShareEntity {
    public static final String KEY_WX_TYPE = "key_wx_type";
    /**
     * 依次为：文本，图片，音乐，视频，网页
     */
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMG = 1;
    public static final int TYPE_MUSIC = 2;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_WEB = 4;

    public static final String KEY_WX_TITLE = "key_wx_title";
    public static final String KEY_WX_SUMMARY = "key_wx_summary";
    public static final String KEY_WX_TEXT = "key_wx_text";
    public static final String KEY_WX_IMG_LOCAL = "key_wx_local_img";
    public static final String KEY_WX_IMG_RES = "key_wx_img_res";
    public static final String KEY_WX_MUSIC_URL = "key_wx_music_url";
    public static final String KEY_WX_VIDEO_URL = "key_wx_video_url";
    public static final String KEY_WX_WEB_URL = "key_wx_web_url";

    private WXShareEntity(int type) {
        super(type);
    }

    /**
     * 分享文本
     *
     * @param isTimeLine 是否分享到朋友圈，false为微信好友列表，true为朋友圈
     * @param text       文本
     */
    public static ShareEntity createTextInfo(@ParamsRequired boolean isTimeLine, @ParamsRequired String text) {
        ShareEntity entity = new ShareEntity(isTimeLine ? TYPE_PYQ : TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_TEXT);
        addParams(entity.params, KEY_WX_TEXT, text);
        return entity;
    }

    /**
     * 分享图片
     *
     * @param isTimeLine 是否分享到朋友圈，false为微信好友列表，true为朋友圈
     * @param imgUrl     本地图片地址
     */
    public static ShareEntity createImageInfo(@ParamsRequired boolean isTimeLine, @ParamsRequired String imgUrl) {
        ShareEntity entity = new ShareEntity(isTimeLine ? TYPE_PYQ : TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_IMG);
        addParams(entity.params, KEY_WX_IMG_LOCAL, imgUrl);
        return entity;
    }

    /**
     * 分享图片
     *
     * @param isTimeLine 是否分享到朋友圈，false为微信好友列表，true为朋友圈
     * @param imgRes     应用内图片资源
     */
    public static ShareEntity createImageInfo(@ParamsRequired boolean isTimeLine, @ParamsRequired int imgRes) {
        ShareEntity entity = new ShareEntity(isTimeLine ? TYPE_PYQ : TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_IMG);
        addParams(entity.params, KEY_WX_IMG_RES, imgRes);
        return entity;
    }

    /**
     * 分享音乐
     *
     * @param isTimeLine 是否分享到朋友圈，false为微信好友列表，true为朋友圈
     * @param musicUrl   音乐url，不支持本地音乐
     * @param imgUrl     本地图片地址，缩略图大小
     * @param title      音乐标题
     * @param summary    音乐摘要
     */
    public static ShareEntity createMusicInfo(@ParamsRequired boolean isTimeLine, @ParamsRequired String musicUrl, String imgUrl, String title, String summary) {
        ShareEntity entity = new ShareEntity(isTimeLine ? TYPE_PYQ : TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_MUSIC);
        addParams(entity.params, KEY_WX_MUSIC_URL, musicUrl);
        addTitleSummaryAndThumb(entity.params, title, summary, imgUrl);
        return entity;
    }

    /**
     * 分享音乐
     *
     * @param isTimeLine 是否分享到朋友圈，false为微信好友列表，true为朋友圈
     * @param musicUrl   音乐url，不支持本地音乐
     * @param imgRes     应用内图片资源，缩略图大小
     * @param title      音乐标题
     * @param summary    音乐摘要
     */
    public static ShareEntity createMusicInfo(@ParamsRequired boolean isTimeLine, @ParamsRequired String musicUrl, int imgRes, String title, String summary) {
        ShareEntity entity = new ShareEntity(isTimeLine ? TYPE_PYQ : TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_MUSIC);
        addParams(entity.params, KEY_WX_MUSIC_URL, musicUrl);
        addTitleSummaryAndThumb(entity.params, title, summary, imgRes);
        return entity;
    }

    /**
     * 分享视频
     *
     * @param isTimeLine 是否分享到朋友圈，false为微信好友列表，true为朋友圈
     * @param videoUrl   视频url，不支持本地音乐
     * @param imgUrl     本地图片地址，缩略图大小
     * @param title      视频标题
     * @param summary    视频摘要
     */
    public static ShareEntity createVideoInfo(@ParamsRequired boolean isTimeLine, @ParamsRequired String videoUrl, String imgUrl, String title, String summary) {
        ShareEntity entity = new ShareEntity(isTimeLine ? TYPE_PYQ : TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_VIDEO);
        addParams(entity.params, KEY_WX_VIDEO_URL, videoUrl);
        addTitleSummaryAndThumb(entity.params, title, summary, imgUrl);
        return entity;
    }

    /**
     * 分享视频
     *
     * @param isTimeLine 是否分享到朋友圈，false为微信好友列表，true为朋友圈
     * @param videoUrl   音乐url，不支持本地音乐
     * @param imgRes     应用内图片资源，缩略图大小
     * @param title      视频标题
     * @param summary    视频摘要
     */
    public static ShareEntity createVideoInfo(@ParamsRequired boolean isTimeLine, @ParamsRequired String videoUrl, int imgRes, String title, String summary) {
        ShareEntity entity = new ShareEntity(isTimeLine ? TYPE_PYQ : TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_VIDEO);
        addParams(entity.params, KEY_WX_VIDEO_URL, videoUrl);
        addTitleSummaryAndThumb(entity.params, title, summary, imgRes);
        return entity;
    }

    /**
     * 分享网页
     *
     * @param isTimeLine 是否分享到朋友圈，false为微信好友列表，true为朋友圈
     * @param webUrl     视频url，不支持本地音乐
     * @param imgUrl     本地图片地址，缩略图大小
     * @param title      网页标题
     * @param summary    网页摘要
     */
    public static ShareEntity createWebPageInfo(@ParamsRequired boolean isTimeLine, @ParamsRequired String webUrl, String imgUrl, String title, String summary) {
        ShareEntity entity = new ShareEntity(isTimeLine ? TYPE_PYQ : TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_WEB);
        addParams(entity.params, KEY_WX_WEB_URL, webUrl);
        addTitleSummaryAndThumb(entity.params, title, summary, imgUrl);
        return entity;
    }

    /**
     * 分享网页
     *
     * @param isTimeLine 是否分享到朋友圈，false为微信好友列表，true为朋友圈
     * @param webUrl     音乐url，不支持本地音乐
     * @param imgRes     应用内图片资源，缩略图大小
     * @param title      网页标题
     * @param summary    网页摘要
     */
    public static ShareEntity createWebPageInfo(@ParamsRequired boolean isTimeLine, @ParamsRequired String webUrl, int imgRes, String title, String summary) {
        ShareEntity entity = new ShareEntity(isTimeLine ? TYPE_PYQ : TYPE_WX);
        addParams(entity.params, KEY_WX_TYPE, TYPE_WEB);
        addParams(entity.params, KEY_WX_WEB_URL, webUrl);
        addTitleSummaryAndThumb(entity.params, title, summary, imgRes);
        return entity;
    }

    /**
     * @param title   标题
     * @param summary 摘要
     * @param imgUrl  本地图片地址
     */
    private static void addTitleSummaryAndThumb(Bundle params, String title, String summary, String imgUrl) {
        addParams(params, KEY_WX_TITLE, title);
        addParams(params, KEY_WX_SUMMARY, summary);
        addParams(params, KEY_WX_IMG_LOCAL, imgUrl);
    }

    /**
     * @param title   标题
     * @param summary 摘要
     * @param imgRes  应用内图片资源
     */
    private static void addTitleSummaryAndThumb(Bundle params, String title, String summary, int imgRes) {
        addParams(params, KEY_WX_TITLE, title);
        addParams(params, KEY_WX_SUMMARY, summary);
        addParams(params, KEY_WX_IMG_RES, imgRes);
    }
}
