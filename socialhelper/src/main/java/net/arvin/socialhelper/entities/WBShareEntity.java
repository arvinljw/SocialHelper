package net.arvin.socialhelper.entities;

import android.os.Bundle;

import net.arvin.socialhelper.ParamsRequired;

import java.util.ArrayList;

/**
 * Created by arvinljw on 17/11/29 14:04
 * Function：
 * Desc：
 */
public class WBShareEntity extends ShareEntity {

    public static final String KEY_WB_TYPE = "key_wb_type";
    /**
     * 依次为：文本，图片，音乐，视频，网页
     */
    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMG_TEXT = 1;
    public static final int TYPE_MULTI_IMAGES = 2;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_WEB = 4;

    public static final String KEY_WB_TITLE = "key_wb_title";
    public static final String KEY_WB_SUMMARY = "key_wb_summary";
    public static final String KEY_WB_TEXT = "key_wb_text";
    public static final String KEY_WB_IMG_LOCAL = "key_wb_local_img";
    public static final String KEY_WB_IMG_RES = "key_wb_img_res";
    public static final String KEY_WB_MULTI_IMG = "key_wb_multi_img";
    public static final String KEY_WB_VIDEO_URL = "key_wb_video_url";
    public static final String KEY_WB_WEB_URL = "key_wb_web_url";

    private WBShareEntity(int type) {
        super(type);
    }

    /**
     * @param text 分享文本内容
     */
    public static ShareEntity createTextInfo(@ParamsRequired String text) {
        ShareEntity entity = new ShareEntity(TYPE_WB);
        addParams(entity.params, KEY_WB_TYPE, TYPE_TEXT);
        addParams(entity.params, KEY_WB_TEXT, text);
        return entity;
    }

    /**
     * 分享图文
     *
     * @param img  本地图片
     * @param text 文本内容
     */
    public static ShareEntity createImageTextInfo(@ParamsRequired String img, String text) {
        ShareEntity entity = new ShareEntity(TYPE_WB);
        addParams(entity.params, KEY_WB_TYPE, TYPE_IMG_TEXT);
        addParams(entity.params, KEY_WB_TEXT, text);
        addTitleSummaryAndThumb(entity.params, "", "", img);
        return entity;
    }

    /**
     * 分享图文
     *
     * @param img  应用内图片资源
     * @param text 文本内容
     */
    public static ShareEntity createImageTextInfo(@ParamsRequired int img, String text) {
        ShareEntity entity = new ShareEntity(TYPE_WB);
        addParams(entity.params, KEY_WB_TYPE, TYPE_IMG_TEXT);
        addParams(entity.params, KEY_WB_TEXT, text);
        addTitleSummaryAndThumb(entity.params, "", "", img);
        return entity;
    }

    /**
     * 分享多图
     *
     * @param images 图片List，最多9张
     * @param text   文本内容
     */
    public static ShareEntity createMultiImageInfo(@ParamsRequired ArrayList<String> images, String text) {
        ShareEntity entity = new ShareEntity(TYPE_WB);
        addParams(entity.params, KEY_WB_TYPE, TYPE_MULTI_IMAGES);
        addParams(entity.params, KEY_WB_MULTI_IMG, images);
        addParams(entity.params, KEY_WB_TEXT, text);
        return entity;
    }

    /**
     * 分享视频
     *
     * @param videoUrl   视频路径，本地视频
     * @param coverImage 视频封面
     * @param text       文本内容
     */
    public static ShareEntity createVideoInfo(@ParamsRequired String videoUrl, String coverImage, String text) {
        ShareEntity entity = new ShareEntity(TYPE_WB);
        addParams(entity.params, KEY_WB_TYPE, TYPE_VIDEO);
        addParams(entity.params, KEY_WB_VIDEO_URL, videoUrl);
        addParams(entity.params, KEY_WB_TEXT, text);
        addTitleSummaryAndThumb(entity.params, "", "", coverImage);
        return entity;
    }

    /**
     * 分享网页
     *
     * @param webUrl  网页链接
     * @param title   网页标题
     * @param summary 网页摘要
     * @param img     网页左边图标，本地路径
     * @param text    文本内容
     */
    public static ShareEntity createWebInfo(@ParamsRequired String webUrl, String title, String summary, String img, String text) {
        ShareEntity entity = new ShareEntity(TYPE_WB);
        addParams(entity.params, KEY_WB_TYPE, TYPE_WEB);
        addParams(entity.params, KEY_WB_WEB_URL, webUrl);
        addParams(entity.params, KEY_WB_TEXT, text);
        addTitleSummaryAndThumb(entity.params, title, summary, img);
        return entity;
    }

    /**
     * 分享网页
     *
     * @param webUrl  网页链接
     * @param title   网页标题
     * @param summary 网页摘要
     * @param img     网页左边图标，应用内图片资源
     * @param text    文本内容
     */
    public static ShareEntity createWebInfo(@ParamsRequired String webUrl, String title, String summary, int img, String text) {
        ShareEntity entity = new ShareEntity(TYPE_WB);
        addParams(entity.params, KEY_WB_TYPE, TYPE_WEB);
        addParams(entity.params, KEY_WB_WEB_URL, webUrl);
        addParams(entity.params, KEY_WB_TEXT, text);
        addTitleSummaryAndThumb(entity.params, title, summary, img);
        return entity;
    }

    /**
     * @param title   标题
     * @param summary 摘要
     * @param imgUrl  本地图片地址
     */
    private static void addTitleSummaryAndThumb(Bundle params, String title, String summary, String imgUrl) {
        addParams(params, KEY_WB_TITLE, title);
        addParams(params, KEY_WB_SUMMARY, summary);
        addParams(params, KEY_WB_IMG_LOCAL, imgUrl);
    }

    /**
     * @param title   标题
     * @param summary 摘要
     * @param imgRes  应用内图片资源
     */
    private static void addTitleSummaryAndThumb(Bundle params, String title, String summary, int imgRes) {
        addParams(params, KEY_WB_TITLE, title);
        addParams(params, KEY_WB_SUMMARY, summary);
        addParams(params, KEY_WB_IMG_RES, imgRes);
    }
}
