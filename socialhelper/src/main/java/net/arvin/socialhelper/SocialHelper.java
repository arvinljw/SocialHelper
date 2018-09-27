package net.arvin.socialhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import net.arvin.socialhelper.callback.SocialLoginCallback;
import net.arvin.socialhelper.callback.SocialShareCallback;
import net.arvin.socialhelper.entities.ShareEntity;

/**
 * Created by arvinljw on 17/11/24 15:14
 * Function：
 * Desc：
 */
public final class SocialHelper {
    static final String WX_AUTH_RECEIVER_ACTION = "wx_auth_receiver_action";
    static final String KEY_WX_AUTH_CODE = "key_wx_auth_code";
    static final String KEY_WX_AUTH_CANCEL_CODE = "key_wx_auth_cancel_code";

    static final String WX_SHARE_RECEIVER_ACTION = "wx_auth_receiver_action";
    static final String KEY_WX_SHARE_CALL_BACK = "key_wx_share_call_back";

    private Builder builder;
    private QQHelper qqHelper;
    private WXHelper wxHelper;
    private WBHelper wbHelper;

    private SocialHelper(Builder builder) {
        this.builder = builder;
    }

    public Builder getBuilder() {
        return builder;
    }

    public void loginQQ(Activity activity, SocialLoginCallback callback) {
        clear();
        qqHelper = new QQHelper(activity, builder.getQqAppId());
        qqHelper.setNeedLoginResult(builder.isNeedLoinResult());
        qqHelper.login(callback);
    }

    public void loginWX(Activity activity, SocialLoginCallback callback) {
        clear();
        wxHelper = new WXHelper(activity, builder.getWxAppId(), builder.getWxAppSecret());
        wxHelper.setNeedLoginResult(builder.isNeedLoinResult());
        wxHelper.login(callback);
    }

    public void loginWB(Activity activity, SocialLoginCallback callback) {
        clear();
        wbHelper = new WBHelper(activity, builder.getWbAppId(), builder.getWbRedirectUrl());
        wbHelper.setNeedLoginResult(builder.isNeedLoinResult());
        wbHelper.login(callback);
    }

    public void shareQQ(Activity activity, ShareEntity shareInfo, SocialShareCallback callback) {
        clear();
        qqHelper = new QQHelper(activity, builder.getQqAppId());
        qqHelper.share(callback, shareInfo);
    }

    public void shareWX(Activity activity, ShareEntity shareInfo, SocialShareCallback callback) {
        clear();
        wxHelper = new WXHelper(activity, builder.getWxAppId(), builder.getWxAppSecret());
        wxHelper.share(callback, shareInfo);
    }

    public void shareWB(Activity activity, ShareEntity shareInfo, SocialShareCallback callback) {
        clear();
        wbHelper = new WBHelper(activity, builder.getWbAppId(), builder.getWbRedirectUrl());
        wbHelper.share(callback, shareInfo);
    }

    /**
     * qq登录和分享以及微博登录都需要在其当前的activity的onActivityResult中调用该方法
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (qqHelper != null) {
            qqHelper.onActivityResult(requestCode, resultCode, data);
        }
        if (wbHelper != null) {
            wbHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 微博分享需要在其当前的activity中的onNewIntent中调用该方法
     */
    public void onNewIntent(Intent intent) {
        if (wbHelper != null) {
            wbHelper.onNewIntent(intent);
        }
    }

    /**
     * 微信登录，在微信回调到WXEntryActivity的onResp方法中调用
     *
     * @param code 空表示失败，正常就是有值的
     */
    public void sendAuthBackBroadcast(Context context, String code) {
        Intent intent = new Intent(WX_AUTH_RECEIVER_ACTION);
        if (TextUtils.isEmpty(code)) {
            code = KEY_WX_AUTH_CANCEL_CODE;
        }
        intent.putExtra(KEY_WX_AUTH_CODE, code);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * 微信分享，在微信回调到WXEntryActivity的onResp方法中调用
     *
     * @param success 表示是否分享成功
     */
    public void sendShareBackBroadcast(Context context, boolean success) {
        Intent intent = new Intent(WX_SHARE_RECEIVER_ACTION);
        intent.putExtra(KEY_WX_SHARE_CALL_BACK, success);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public void clear() {
        if (wxHelper != null) {
            wxHelper.onDestroy();
            wxHelper = null;
        }
        if (qqHelper != null) {
            qqHelper.onDestroy();
            qqHelper = null;
        }
        if (wbHelper != null) {
            wbHelper.onDestroy();
            wbHelper = null;
        }
    }

    public static final class Builder {
        private String qqAppId;

        private String wxAppId;
        private String wxAppSecret;

        private String wbAppId;
        private String wbRedirectUrl;

        private boolean needLoinResult;

        public String getQqAppId() {
            return qqAppId;
        }

        public Builder setQqAppId(String qqAppId) {
            this.qqAppId = qqAppId;
            return this;
        }

        public String getWxAppId() {
            return wxAppId;
        }

        public Builder setWxAppId(String wxAppId) {
            this.wxAppId = wxAppId;
            return this;
        }

        public String getWxAppSecret() {
            return wxAppSecret;
        }

        public Builder setWxAppSecret(String wxAppSecret) {
            this.wxAppSecret = wxAppSecret;
            return this;
        }

        public String getWbAppId() {
            return wbAppId;
        }

        public Builder setWbAppId(String wbAppId) {
            this.wbAppId = wbAppId;
            return this;
        }

        public String getWbRedirectUrl() {
            return wbRedirectUrl;
        }

        public Builder setWbRedirectUrl(String wbRedirectUrl) {
            this.wbRedirectUrl = wbRedirectUrl;
            return this;
        }

        public Builder setNeedLoinResult(boolean needLoinResult) {
            this.needLoinResult = needLoinResult;
            return this;
        }

        public boolean isNeedLoinResult() {
            return needLoinResult;
        }

        public SocialHelper build() {
            return new SocialHelper(this);
        }
    }
}
