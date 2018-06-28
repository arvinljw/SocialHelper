package net.arvin.socialhelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import net.arvin.socialhelper.callback.SocialCallback;
import net.arvin.socialhelper.callback.SocialLoginCallback;
import net.arvin.socialhelper.callback.SocialShareCallback;
import net.arvin.socialhelper.entities.ShareEntity;
import net.arvin.socialhelper.entities.ThirdInfoEntity;
import net.arvin.socialhelper.entities.WXInfoEntity;
import net.arvin.socialhelper.entities.WXLoginResultEntity;
import net.arvin.socialhelper.entities.WXShareEntity;

import java.io.File;
import java.net.URL;

/**
 * Created by arvinljw on 17/11/27 13:33
 * Function：
 * Desc：
 */
final class WXHelper implements ISocial {
    private static final int GET_INFO_ERROR = 10000;
    private static final int GET_INFO_SUCCESS = 10001;

    private Activity activity;
    private String appId;
    private String appSecret;
    private IWXAPI api;

    private SocialLoginCallback loginCallback;
    private BroadcastReceiver wxAuthReceiver;
    private WXInfoEntity wxInfo;

    private SocialShareCallback shareCallback;
    private BroadcastReceiver wxShareReceiver;

    WXHelper(Activity activity, String appId, String appSecret) {
        this.activity = activity;
        this.appId = appId;
        this.appSecret = appSecret;

        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appSecret)) {
            Log.w("WXHelper", "Wechat's appId or appSecret is empty!");
            return;
        }

        api = WXAPIFactory.createWXAPI(activity, appId, true);
        api.registerApp(appId);
    }

    /**
     * 1、发起授权，会在{ packageName.WXEntryActivity#onResp(BaseResp)}返回成功与否,
     * 成功则通过广播发送回调{@link #wxAuthReceiver}的onReceive方法
     */
    @Override
    public void login(SocialLoginCallback callback) {
        this.loginCallback = callback;
        if (baseVerify(callback)) {
            return;
        }
        initLoginReceiver();
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = SocialUtil.getAppStateName(activity) + "_app";
        api.sendReq(req);
    }

    /**
     * 2、授权回调成功
     */
    private void initLoginReceiver() {
        if (wxAuthReceiver == null) {
            wxAuthReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context activity, Intent intent) {
                    String code = intent.getStringExtra(SocialHelper.KEY_WX_AUTH_CODE);
                    if (code.equals(SocialHelper.KEY_WX_AUTH_CANCEL_CODE)) {
                        if (loginCallback != null && activity != null) {
                            loginCallback.socialError(activity.getString(R.string.social_cancel));
                        }
                        return;
                    }
                    getAccessToken(code);
                }
            };
            activity.registerReceiver(wxAuthReceiver, new IntentFilter(SocialHelper.WX_AUTH_RECEIVER_ACTION));
        }
    }

    /**
     * 3、通过code获取accessToken
     *
     * @param code 用户换取access_token的code，仅在ErrCode为0时有效
     */
    private void getAccessToken(final String code) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.weixin.qq.com/sns/oauth2/access_token?" +
                            "appid=" + appId + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code");
                    WXLoginResultEntity wxLoginResult = new Gson().fromJson(SocialUtil.get(url), WXLoginResultEntity.class);
                    getUserInfo(wxLoginResult);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(GET_INFO_ERROR);
                }
            }
        }).start();
    }

    /**
     * 4、获取个人信息
     */
    private void getUserInfo(final WXLoginResultEntity wxLoginResult) throws Exception {
        if (activity == null || wxLoginResult == null) {
            handler.sendEmptyMessage(GET_INFO_ERROR);
            return;
        }
        URL url = new URL("https://api.weixin.qq.com/sns/userinfo?access_token=" + wxLoginResult.getAccess_token() +
                "&openid=" + wxLoginResult.getOpenid() + "");
        wxInfo = new Gson().fromJson(SocialUtil.get(url), WXInfoEntity.class);
        handler.sendEmptyMessage(GET_INFO_SUCCESS);
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
                    if (wxInfo != null) {
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
        return ThirdInfoEntity.createWxThirdInfo(wxInfo.getUnionid(), wxInfo.getOpenid(), wxInfo.getNickname(),
                SocialUtil.getWXSex(String.valueOf(wxInfo.getSex())), wxInfo.getHeadimgurl(), wxInfo);
    }

    @Override
    public void share(SocialShareCallback callback, ShareEntity shareInfo) {
        this.shareCallback = callback;
        if (baseVerify(callback)) {
            return;
        }
        //是否分享到朋友圈，微信4.2以下不支持朋友圈
        boolean isTimeLine = shareInfo.getType() == ShareEntity.TYPE_PYQ;
        if (isTimeLine && api.getWXAppSupportAPI() < 0x21020001) {
            if (shareCallback != null) {
                shareCallback.socialError(activity.getString(R.string.social_wx_version_low_error));
            }
            return;
        }

        initShareReceiver();

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.message = createMessage(req, shareInfo.getParams());
        if (req.message == null) {
            return;
        }
        req.scene = isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        api.sendReq(req);
    }

    /**
     * 分享回调
     */
    private void initShareReceiver() {
        if (wxShareReceiver == null) {
            wxShareReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context activity, Intent intent) {
                    boolean shareSuccess = intent.getBooleanExtra(SocialHelper.KEY_WX_SHARE_CALL_BACK, false);
                    if (shareCallback != null) {
                        if (shareSuccess) {
                            shareCallback.shareSuccess();
                        } else {
                            shareCallback.socialError(activity.getString(R.string.social_cancel));
                        }
                    }
                }
            };
            activity.registerReceiver(wxShareReceiver, new IntentFilter(SocialHelper.WX_SHARE_RECEIVER_ACTION));
        }
    }

    private WXMediaMessage createMessage(SendMessageToWX.Req req, Bundle params) {
        WXMediaMessage msg = new WXMediaMessage();
        int type = params.getInt(WXShareEntity.KEY_WX_TYPE);
        boolean success = false;
        switch (type) {
            case WXShareEntity.TYPE_TEXT:
                success = addText(req, msg, params);
                break;
            case WXShareEntity.TYPE_IMG:
                success = addImage(req, msg, params);
                break;
            case WXShareEntity.TYPE_MUSIC:
                success = addMusic(req, msg, params);
                break;
            case WXShareEntity.TYPE_VIDEO:
                success = addVideo(req, msg, params);
                break;
            case WXShareEntity.TYPE_WEB:
                success = addWeb(req, msg, params);
                break;
        }
        if (!success) {
            return null;
        }
        return msg;
    }

    private boolean addText(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = params.getString(WXShareEntity.KEY_WX_TEXT);

        msg.mediaObject = textObj;
        msg.description = textObj.text;

        req.transaction = SocialUtil.buildTransaction("text");
        return true;
    }

    private boolean addImage(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        WXImageObject imgObj;
        Bitmap bitmap;
        if (params.containsKey(WXShareEntity.KEY_WX_IMG_LOCAL)) {//分为本地文件和应用内资源图片
            String imgUrl = params.getString(WXShareEntity.KEY_WX_IMG_LOCAL);
            if (notFoundFile(imgUrl)) {
                return false;
            }

            imgObj = new WXImageObject();
            imgObj.imagePath = imgUrl;
            bitmap = BitmapFactory.decodeFile(imgUrl);
        } else {
            bitmap = BitmapFactory.decodeResource(activity.getResources(), params.getInt(WXShareEntity.KEY_WX_IMG_RES));
            imgObj = new WXImageObject(bitmap);
        }
        msg.mediaObject = imgObj;
        msg.thumbData = SocialUtil.bmpToByteArray(bitmap, true);

        req.transaction = SocialUtil.buildTransaction("img");
        return true;
    }

    private boolean addMusic(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        WXMusicObject musicObject = new WXMusicObject();
        musicObject.musicUrl = params.getString(WXShareEntity.KEY_WX_MUSIC_URL);

        msg.mediaObject = musicObject;
        if (addTitleSummaryAndThumb(msg, params)) return false;

        req.transaction = SocialUtil.buildTransaction("music");
        return true;
    }

    private boolean addVideo(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        WXVideoObject musicObject = new WXVideoObject();
        musicObject.videoUrl = params.getString(WXShareEntity.KEY_WX_VIDEO_URL);

        msg.mediaObject = musicObject;
        if (addTitleSummaryAndThumb(msg, params)) return false;

        req.transaction = SocialUtil.buildTransaction("video");
        return true;
    }

    private boolean addWeb(SendMessageToWX.Req req, WXMediaMessage msg, Bundle params) {
        WXWebpageObject musicObject = new WXWebpageObject();
        musicObject.webpageUrl = params.getString(WXShareEntity.KEY_WX_WEB_URL);

        msg.mediaObject = musicObject;
        if (addTitleSummaryAndThumb(msg, params)) return false;

        req.transaction = SocialUtil.buildTransaction("webpage");
        return true;
    }

    private boolean addTitleSummaryAndThumb(WXMediaMessage msg, Bundle params) {
        if (params.containsKey(WXShareEntity.KEY_WX_TITLE)) {
            msg.title = params.getString(WXShareEntity.KEY_WX_TITLE);
        }

        if (params.containsKey(WXShareEntity.KEY_WX_SUMMARY)) {
            msg.description = params.getString(WXShareEntity.KEY_WX_SUMMARY);
        }

        if (params.containsKey(WXShareEntity.KEY_WX_IMG_LOCAL) || params.containsKey(WXShareEntity.KEY_WX_IMG_RES)) {
            Bitmap bitmap;
            if (params.containsKey(WXShareEntity.KEY_WX_IMG_LOCAL)) {//分为本地文件和应用内资源图片
                String imgUrl = params.getString(WXShareEntity.KEY_WX_IMG_LOCAL);
                if (notFoundFile(imgUrl)) {
                    return true;
                }
                bitmap = BitmapFactory.decodeFile(imgUrl);
            } else {
                bitmap = BitmapFactory.decodeResource(activity.getResources(), params.getInt(WXShareEntity.KEY_WX_IMG_RES));
            }
            msg.thumbData = SocialUtil.bmpToByteArray(bitmap, true);
        }
        return false;
    }

    /*基本信息验证*/
    private boolean baseVerify(SocialCallback callback) {
        if (TextUtils.isEmpty(appId) || TextUtils.isEmpty(appSecret)) {
            if (callback != null) {
                callback.socialError(activity.getString(R.string.social_error_appid_empty));
            }
            return true;
        }
        if (!api.isWXAppInstalled()) {
            if (callback != null) {
                callback.socialError(activity.getString(R.string.social_wx_uninstall));
            }
            return true;
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
            if (wxAuthReceiver != null) {
                activity.unregisterReceiver(wxAuthReceiver);
            }
            if (wxShareReceiver != null) {
                activity.unregisterReceiver(wxShareReceiver);
            }
            activity = null;
        }
    }
}
