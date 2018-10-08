package net.arvin.socialhelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.BaseMediaObject;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.MultiImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoSourceObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;

import net.arvin.socialhelper.callback.SocialCallback;
import net.arvin.socialhelper.callback.SocialLoginCallback;
import net.arvin.socialhelper.callback.SocialShareCallback;
import net.arvin.socialhelper.entities.ShareEntity;
import net.arvin.socialhelper.entities.ThirdInfoEntity;
import net.arvin.socialhelper.entities.WBInfoEntity;
import net.arvin.socialhelper.entities.WBShareEntity;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by arvinljw on 17/11/27 15:58
 * Function：
 * Desc：
 */
final class WBHelper implements ISocial, INeedLoginResult {
    private static final int GET_INFO_ERROR = 10002;
    private static final int GET_INFO_SUCCESS = 10003;
    private static final String SCOPE = "";

    private Activity activity;
    private String appId;
    private String redirectUrl;
    private SsoHandler mSsoHandler;

    private SocialLoginCallback loginCallback;
    private Oauth2AccessToken loginResult;
    private WbAuthListener wbAuthCallback;
    private WBInfoEntity wbInfo;
    private boolean needLoginResult;

    private WbShareHandler shareHandler;
    private SocialShareCallback shareCallback;
    private WbShareCallback wbShareCallback;

    WBHelper(Activity activity, String appId, String redirectUrl) {
        this.activity = activity;
        this.appId = appId;
        this.redirectUrl = redirectUrl;
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(redirectUrl)) {
            Log.w("WBHelper", "WeBo's appId or redirectUrl is empty!");
            return;
        }
        WbSdk.install(activity.getApplicationContext(), new AuthInfo(activity.getApplicationContext(), appId, redirectUrl, SCOPE));
    }

    /**
     * 1、登录
     */
    @Override
    public void login(SocialLoginCallback callback) {
        this.loginCallback = callback;
        if (baseVerify(callback)) {
            return;
        }
        initLoginListener();
        mSsoHandler = new SsoHandler(activity);
        mSsoHandler.authorize(wbAuthCallback);
    }

    /*基本信息验证*/
    private boolean baseVerify(SocialCallback callback) {
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(redirectUrl)) {
            if (callback != null) {
                callback.socialError(activity.getString(R.string.social_error_appid_empty));
            }
            return true;
        }
        return false;
    }

    /**
     * 2、被调用，回调到{@link #wbAuthCallback}，然后调用{@link #wbAuthCallback}的onSuccess方法
     */
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        if (shareHandler != null) {
            shareHandler.doResultIntent(data, wbShareCallback);
        }
    }

    private void initLoginListener() {
        wbAuthCallback = new WbAuthListener() {
            @Override
            public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
                if (oauth2AccessToken.isSessionValid()) {
                    loginResult = oauth2AccessToken;
                    AccessTokenKeeper.writeAccessToken(activity, oauth2AccessToken);
                    getUserInfo(oauth2AccessToken);
                } else {
                    handler.sendEmptyMessage(GET_INFO_ERROR);
                }
            }

            @Override
            public void cancel() {
                if (loginCallback != null && activity != null) {
                    loginCallback.socialError(activity.getString(R.string.social_cancel));
                }
            }

            @Override
            public void onFailure(WbConnectErrorMessage error) {
                if (loginCallback != null) {
                    loginCallback.socialError(error.getErrorMessage());
                }
            }
        };
    }

    /**
     * 3、获取用户信息
     */
    private void getUserInfo(final Oauth2AccessToken accessToken) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (accessToken == null) {
                        handler.sendEmptyMessage(GET_INFO_ERROR);
                        return;
                    }
                    URL url = new URL("https://api.weibo.com/2/users/show.json?access_token=" + accessToken.getToken() + "&uid=" + accessToken.getUid() + "");
                    wbInfo = new Gson().fromJson(SocialUtil.get(url), WBInfoEntity.class);
                    if (isNeedLoginResult()) {
                        wbInfo.setLoginResultEntity(loginResult);
                    }
                    handler.sendEmptyMessage(GET_INFO_SUCCESS);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(GET_INFO_ERROR);
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (activity == null || loginCallback == null) {
                return;
            }
            switch (msg.what) {
                case GET_INFO_SUCCESS:
                    if (wbInfo != null) {
                        loginCallback.loginSuccess(createThirdInfo());
                    } else {
                        loginCallback.socialError(activity.getString(R.string.social_cancel));
                    }
                    break;
                case GET_INFO_ERROR:
                    loginCallback.socialError(activity.getString(R.string.social_cancel));
                    break;
            }
        }
    };

    @Override
    public ThirdInfoEntity createThirdInfo() {
        return ThirdInfoEntity.createWbThirdInfo(appId, wbInfo.getIdstr(), wbInfo.getScreen_name(),
                SocialUtil.getWBSex(wbInfo.getGender()), wbInfo.getAvatar_large(), wbInfo);
    }

    @Override
    public void share(SocialShareCallback callback, ShareEntity shareInfo) {
        this.shareCallback = callback;
        if (baseVerify(callback)) {
            return;
        }
        if (!WbSdk.isWbInstall(activity)) {
            if (callback != null) {
                callback.socialError(activity.getString(R.string.social_wb_uninstall));
            }
            return;
        }
        initShareLister(shareInfo);
        shareHandler = new WbShareHandler(activity);
        shareHandler.registerApp();

        WeiboMultiMessage weiboMessage = getShareMessage(shareInfo.getParams());
        if (weiboMessage == null) {
            return;
        }
        shareHandler.shareMessage(weiboMessage, false);
    }

    private void initShareLister(final ShareEntity shareInfo) {
        wbShareCallback = new WbShareCallback() {
            @Override
            public void onWbShareSuccess() {
                if (shareCallback != null) {
                    shareCallback.shareSuccess(shareInfo.getType());
                }
            }

            @Override
            public void onWbShareCancel() {
                if (shareCallback != null && activity != null) {
                    shareCallback.socialError(activity.getString(R.string.social_cancel));
                }
            }

            @Override
            public void onWbShareFail() {
                if (shareCallback != null && activity != null) {
                    shareCallback.socialError(activity.getString(R.string.social_share_error));
                }
            }
        };
    }

    private WeiboMultiMessage getShareMessage(Bundle params) {
        WeiboMultiMessage msg = new WeiboMultiMessage();
        int type = params.getInt(WBShareEntity.KEY_WB_TYPE);
        BaseMediaObject mediaObject = null;
        switch (type) {
            case WBShareEntity.TYPE_TEXT:
                msg.textObject = getTextObj(params);
                mediaObject = msg.textObject;
                break;
            case WBShareEntity.TYPE_IMG_TEXT:
                msg.imageObject = getImageObj(params);
                msg.textObject = getTextObj(params);
                mediaObject = msg.imageObject;
                break;
            case WBShareEntity.TYPE_MULTI_IMAGES:
                msg.multiImageObject = getMultiImgObj(params);
                msg.textObject = getTextObj(params);
                mediaObject = msg.multiImageObject;
                break;
            case WBShareEntity.TYPE_VIDEO:
                msg.videoSourceObject = getVideoObj(params);
                msg.textObject = getTextObj(params);
                mediaObject = msg.videoSourceObject;
                break;
            case WBShareEntity.TYPE_WEB:
                msg.mediaObject = getWebPageObj(params);
                msg.textObject = getTextObj(params);
                mediaObject = msg.mediaObject;
                break;
        }
        if (mediaObject == null) {
            return null;
        }
        return msg;
    }

    private TextObject getTextObj(Bundle params) {
        TextObject textObj = new TextObject();
        textObj.text = params.getString(WBShareEntity.KEY_WB_TEXT);
        return textObj;
    }

    private ImageObject getImageObj(Bundle params) {
        ImageObject imgObj = new ImageObject();
        if (params.containsKey(WBShareEntity.KEY_WB_IMG_LOCAL)) {//分为本地文件和应用内资源图片
            String imgUrl = params.getString(WBShareEntity.KEY_WB_IMG_LOCAL);
            if (notFoundFile(imgUrl)) {
                return null;
            }
            imgObj.imagePath = imgUrl;
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), params.getInt(WBShareEntity.KEY_WB_IMG_RES));
            imgObj.setImageObject(bitmap);
            bitmap.recycle();
        }
        return imgObj;
    }

    private MultiImageObject getMultiImgObj(Bundle params) {
        MultiImageObject multiImageObject = new MultiImageObject();
        ArrayList<String> images = params.getStringArrayList(WBShareEntity.KEY_WB_MULTI_IMG);
        ArrayList<Uri> uris = new ArrayList<>();
        if (images != null) {
            for (String image : images) {
                uris.add(Uri.fromFile(new File(image)));
            }
        }
        multiImageObject.setImageList(uris);
        if (addTitleSummaryAndThumb(multiImageObject, params)) {
            return null;
        }
        return multiImageObject;
    }

    private VideoSourceObject getVideoObj(Bundle params) {
        VideoSourceObject videoSourceObject = new VideoSourceObject();
        String videoUrl = params.getString(WBShareEntity.KEY_WB_VIDEO_URL);
        if (!TextUtils.isEmpty(videoUrl)) {
            videoSourceObject.videoPath = Uri.fromFile(new File(videoUrl));
        }

        if (params.containsKey(WBShareEntity.KEY_WB_IMG_LOCAL)) {
            String coverPath = params.getString(WBShareEntity.KEY_WB_IMG_LOCAL);
            if (!TextUtils.isEmpty(coverPath)) {
                videoSourceObject.coverPath = Uri.fromFile(new File(coverPath));
            }
        }

        return videoSourceObject;
    }

    private WebpageObject getWebPageObj(Bundle params) {
        WebpageObject webpageObject = new WebpageObject();
        webpageObject.identify = Utility.generateGUID();
        webpageObject.actionUrl = params.getString(WBShareEntity.KEY_WB_WEB_URL);
        if (addTitleSummaryAndThumb(webpageObject, params)) {
            return null;
        }
        return webpageObject;
    }

    /**
     * 当有设置缩略图但是找不到的时候阻止分享
     */
    private boolean addTitleSummaryAndThumb(BaseMediaObject msg, Bundle params) {
        if (params.containsKey(WBShareEntity.KEY_WB_TITLE)) {
            msg.title = params.getString(WBShareEntity.KEY_WB_TITLE);
        }

        if (params.containsKey(WBShareEntity.KEY_WB_SUMMARY)) {
            msg.description = params.getString(WBShareEntity.KEY_WB_SUMMARY);
        }

        if (params.containsKey(WBShareEntity.KEY_WB_IMG_LOCAL) || params.containsKey(WBShareEntity.KEY_WB_IMG_RES)) {
            Bitmap bitmap;
            if (params.containsKey(WBShareEntity.KEY_WB_IMG_LOCAL)) {//分为本地文件和应用内资源图片
                String imgUrl = params.getString(WBShareEntity.KEY_WB_IMG_LOCAL);
                if (notFoundFile(imgUrl)) {
                    return true;
                }
                bitmap = BitmapFactory.decodeFile(imgUrl);
            } else {
                bitmap = BitmapFactory.decodeResource(activity.getResources(), params.getInt(WBShareEntity.KEY_WB_IMG_RES));
            }
            msg.thumbData = SocialUtil.bmpToByteArray(bitmap, true);
        }
        return false;
    }

    private boolean notFoundFile(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (!file.exists()) {
                if (shareCallback != null) {
                    shareCallback.socialError(activity.getString(R.string.social_img_not_found));
                }
                return true;
            }
        } else {
            if (shareCallback != null) {
                shareCallback.socialError(activity.getString(R.string.social_img_not_found));
            }
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        if (activity != null) {
            activity = null;
        }
    }

    @Override
    public void setNeedLoginResult(boolean needLoginResult) {
        this.needLoginResult = needLoginResult;
    }

    @Override
    public boolean isNeedLoginResult() {
        return needLoginResult;
    }
}
