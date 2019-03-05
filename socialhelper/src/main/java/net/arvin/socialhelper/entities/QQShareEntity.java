package net.arvin.socialhelper.entities;

import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;

import net.arvin.socialhelper.ParamsRequired;

import java.util.ArrayList;

/**
 * Created by arvinljw on 17/11/28 18:16
 * Function：
 * Desc：
 */
public final class QQShareEntity extends ShareEntity {

    private QQShareEntity(int type) {
        super(type);
    }

    /**
     * 创建分享图文类型到qq
     *
     * @param title     标题，长度限制30个字符
     * @param targetUrl 跳转地址
     * @param imgUrl    图片地址，本地路径或者url
     * @param summary   摘要，长度限制40个字
     * @param appName   应用名；手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
     */
    public static ShareEntity createImageTextInfo(@ParamsRequired String title, @ParamsRequired String targetUrl,
                                                  String imgUrl, String summary, String appName) {
        ShareEntity entity = new ShareEntity(ShareEntity.TYPE_QQ);
        addParams(entity.params, QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        addParams(entity.params, QQShare.SHARE_TO_QQ_TITLE, title);
        addParams(entity.params, QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        addParams(entity.params, QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
        addParams(entity.params, QQShare.SHARE_TO_QQ_SUMMARY, summary);
        addParams(entity.params, QQShare.SHARE_TO_QQ_APP_NAME, appName);
        return entity;
    }

    /**
     * 创建分享纯图片到qq
     *
     * @param imgUrl  本地图片地址
     * @param appName 应用名；手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
     */
    public static ShareEntity createImageInfo(@ParamsRequired String imgUrl, String appName) {
        ShareEntity entity = new ShareEntity(ShareEntity.TYPE_QQ);
        addParams(entity.params, QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        addParams(entity.params, QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imgUrl);
        addParams(entity.params, QQShare.SHARE_TO_QQ_APP_NAME, appName);
        return entity;
    }

    /**
     * 创建分享音乐到qq
     *
     * @param title     标题，长度限制30个字符
     * @param targetUrl 跳转地址，
     * @param musicUrl  音乐地址，不支持本地音乐
     * @param imgUrl    图片地址，本地路径或者url
     * @param summary   摘要，长度限制40个字
     * @param appName   应用名；手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
     */
    public static ShareEntity createMusicInfo(@ParamsRequired String title, @ParamsRequired String targetUrl,
                                              @ParamsRequired String musicUrl, String imgUrl, String summary, String appName) {
        ShareEntity entity = new ShareEntity(ShareEntity.TYPE_QQ);
        addParams(entity.params, QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        addParams(entity.params, QQShare.SHARE_TO_QQ_TITLE, title);
        addParams(entity.params, QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        addParams(entity.params, QQShare.SHARE_TO_QQ_AUDIO_URL, musicUrl);
        addParams(entity.params, QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
        addParams(entity.params, QQShare.SHARE_TO_QQ_SUMMARY, summary);
        addParams(entity.params, QQShare.SHARE_TO_QQ_APP_NAME, appName);
        return entity;
    }

    /**
     * 创建分享应用到qq
     *
     * @param title   标题，长度限制30个字符
     * @param imgUrl  图片地址，本地路径或者url
     * @param summary 摘要，长度限制40个字
     * @param appName 应用名；手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
     */
    public static ShareEntity createAppInfo(@ParamsRequired String title, String imgUrl, String summary, String appName) {
        ShareEntity entity = new ShareEntity(ShareEntity.TYPE_QQ);
        addParams(entity.params, QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);
        addParams(entity.params, QQShare.SHARE_TO_QQ_TITLE, title);
        addParams(entity.params, QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
        addParams(entity.params, QQShare.SHARE_TO_QQ_SUMMARY, summary);
        addParams(entity.params, QQShare.SHARE_TO_QQ_APP_NAME, appName);
        return entity;
    }

    /**
     * 创建分享图文到qq空间
     *
     * @param title     标题，长度限制200个字符
     * @param targetUrl 跳转地址
     * @param imgUrl    图片地址，目前会第一张有效，待qq优化
     * @param summary   摘要，长度限制600个字
     * @param appName   应用名；手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
     */
    public static ShareEntity createImageTextInfoToQZone(@ParamsRequired String title, @ParamsRequired String targetUrl,
                                                         @ParamsRequired ArrayList<String> imgUrl, String summary, String appName) {
        ShareEntity entity = new ShareEntity(ShareEntity.TYPE_Q_ZONE);
        addParams(entity.params, QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        addParams(entity.params, QzoneShare.SHARE_TO_QQ_TITLE, title);
        addParams(entity.params, QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        addParams(entity.params, QzoneShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
        addParams(entity.params, QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        addParams(entity.params, QzoneShare.SHARE_TO_QQ_APP_NAME, appName);
        return entity;
    }

    /**
     * 创建分享图文说说到qq空间
     *
     * @param summary 摘要，长度限制600个字
     */
    public static ShareEntity createPublishTextToQZone(@ParamsRequired String summary) {
        ShareEntity entity = new ShareEntity(ShareEntity.TYPE_PUBLISH);
        addParams(entity.params, QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
        addParams(entity.params, QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        return entity;
    }

    /**
     * 创建分享图文说说到qq空间
     *
     * @param imgUrl 图片地址，只支持本地图片；注：<=9张图片为发表说说，>9张为上传图片到相册
     */
    public static ShareEntity createPublishImageToQZone(@ParamsRequired ArrayList<String> imgUrl) {
        ShareEntity entity = new ShareEntity(ShareEntity.TYPE_PUBLISH);
        addParams(entity.params, QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);
        addParams(entity.params, QzonePublish.PUBLISH_TO_QZONE_IMAGE_URL, imgUrl);
        return entity;
    }

    /**
     * 创建分享视频说说到qq空间
     *
     * @param videoUrl 视频地址，只支持本地视频；上传视频的大小最好控制在100M以内（因为QQ普通用户上传视频必须在100M以内，黄钻用户可上传1G以内视频，大于1G会直接报错。）
     */
    public static ShareEntity createPublishVideoToQZone(@ParamsRequired String videoUrl) {
        ShareEntity entity = new ShareEntity(ShareEntity.TYPE_PUBLISH);
        addParams(entity.params, QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHVIDEO);
        addParams(entity.params, QzonePublish.PUBLISH_TO_QZONE_VIDEO_PATH, videoUrl);
        return entity;
    }
}
