package net.arvin.socialhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tencent.connect.UnionInfo;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import net.arvin.socialhelper.callback.SocialCallback;
import net.arvin.socialhelper.callback.SocialLoginCallback;
import net.arvin.socialhelper.callback.SocialShareCallback;
import net.arvin.socialhelper.entities.QQInfoEntity;
import net.arvin.socialhelper.entities.QQLoginResultEntity;
import net.arvin.socialhelper.entities.QQUnionIdEntity;
import net.arvin.socialhelper.entities.ShareEntity;
import net.arvin.socialhelper.entities.ThirdInfoEntity;

/**
 * Created by arvinljw on 17/11/24 15:59
 * Function：
 * Desc：
 */
final class QQHelper implements ISocial, INeedLoginResult {
    private Activity activity;
    private Tencent tencent;
    private String appId;

    private SocialLoginCallback loginCallback;
    private IUiListener loginListener;
    private QQLoginResultEntity loginResult;
    private IUiListener userInfoListener;
    private IUiListener unionIdListener;
    private QQInfoEntity qqInfo;
    private QQUnionIdEntity unionIdInfo;
    private boolean unionIdOk;
    private boolean qqInfoOk;

    private boolean needLoginResult;

    private SocialShareCallback shareCallback;
    private IUiListener shareListener;

    QQHelper(Activity activity, String appId) {
        this.activity = activity;
        this.appId = appId;
        if (TextUtils.isEmpty(appId)) {
            Log.w("QQHelper", "QQ's appId is empty!");
            return;
        }
        tencent = Tencent.createInstance(appId, activity.getApplicationContext());
    }

    private void initLoginListener() {
        loginListener = new NormalUIListener(activity, loginCallback) {
            @Override
            public void onComplete(Object obj) {
                try {
                    loginResult = new Gson().fromJson(obj.toString(), QQLoginResultEntity.class);
                    getUserInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void initShareListener(final ShareEntity shareInfo) {
        shareListener = new NormalUIListener(activity, shareCallback) {
            @Override
            public void onComplete(Object o) {
                if (shareCallback != null) {
                    shareCallback.shareSuccess(shareInfo.getType());
                }
            }
        };
    }

    /**
     * 1、登录，成功后会回调调用这个方法的fragment或activity的onActivityResult方法，
     * 这时候调用该类的{@link #onActivityResult(int, int, Intent)}方法，那样就能回调登录的IUIListener
     */
    @Override
    public void login(SocialLoginCallback callback) {
        this.loginCallback = callback;
        if (baseVerify(callback)) {
            return;
        }
        initLoginListener();
        if (!tencent.isSessionValid()) {
            tencent.login(activity, "all", loginListener);
        } else {
            UserInfo info = new UserInfo(activity, tencent.getQQToken());
            info.getUserInfo(userInfoListener);
        }
    }

    /**
     * 2、被调用，回调到{@link #loginListener}，然后调用{@link #loginListener}的onComplete方法
     */
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (loginListener != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener);
        }
        if (shareListener != null) {
            Tencent.onActivityResultData(requestCode, resultCode, data, shareListener);
        }
    }

    /**
     * 3、获取用户信息，并回调{@link #userInfoListener}的onComplete方法
     */
    private void getUserInfo() {
        tencent.setAccessToken(loginResult.getAccess_token(), loginResult.getExpires_in());
        tencent.setOpenId(loginResult.getOpenid());

        if (!tencent.isSessionValid()) {
            return;
        }
        initUnionIdListener();
        UnionInfo unionInfo = new UnionInfo(activity, tencent.getQQToken());
        unionInfo.getUnionId(unionIdListener);

        initUserInfoListener();
        UserInfo info = new UserInfo(activity, tencent.getQQToken());
        info.getUserInfo(userInfoListener);
    }

    private void initUnionIdListener() {
        unionIdListener = new NormalUIListener(activity, loginCallback) {
            @Override
            public void onComplete(Object o) {
                try {
                    unionIdInfo = new Gson().fromJson(o.toString(), QQUnionIdEntity.class);
                    if (unionIdInfo.getError() == 100048) {
                        String unionIdNotSet = activity.getResources().getString(R.string.social_qq_unionid_not_set);
                        Log.d("QQHelper", unionIdNotSet);
                    }
                    if (TextUtils.isEmpty(unionIdInfo.getUnionid())) {
                        unionIdInfo.setUnionid("unionId not apply");
                    }
                    unionIdOk = true;
                    loginOk();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void initUserInfoListener() {
        userInfoListener = new NormalUIListener(activity, loginCallback) {
            @Override
            public void onComplete(Object o) {
                try {
                    qqInfo = new Gson().fromJson(o.toString(), QQInfoEntity.class);
                    if (isNeedLoginResult()) {
                        qqInfo.setLoginResultEntity(loginResult);
                    }
                    qqInfoOk = true;
                    loginOk();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private synchronized void loginOk() {
        if (qqInfoOk && unionIdOk) {
            if (loginCallback != null) {
                loginCallback.loginSuccess(createThirdInfo());
            }
            tencent.logout(activity);
        }
    }

    @Override
    public ThirdInfoEntity createThirdInfo() {
        return ThirdInfoEntity.createQQThirdInfo(unionIdInfo.getUnionid(), tencent.getOpenId(), qqInfo.getNickname(),
                SocialUtil.getQQSex(qqInfo.getGender()), qqInfo.getFigureurl_qq_2(), qqInfo);
    }

    @Override
    public void setNeedLoginResult(boolean needLoginResult) {
        this.needLoginResult = needLoginResult;
    }

    @Override
    public boolean isNeedLoginResult() {
        return needLoginResult;
    }

    /**
     * qq有个坑，采用onActivityResult的方式，如果留在qq的话，home键退出之后无法正确回调
     */
    @Override
    public void share(SocialShareCallback callback, ShareEntity shareInfo) {
        this.shareCallback = callback;
        if (baseVerify(callback)) {
            return;
        }
        if (!SocialUtil.isQQInstalled(activity)) {
            if (callback != null) {
                callback.socialError(activity.getString(R.string.social_qq_uninstall));
            }
            return;
        }
        initShareListener(shareInfo);
        if (shareInfo.getType() == ShareEntity.TYPE_QQ) {
            tencent.shareToQQ(activity, shareInfo.getParams(), shareListener);
        } else if (shareInfo.getType() == ShareEntity.TYPE_PUBLISH) {
            tencent.publishToQzone(activity, shareInfo.getParams(), shareListener);
        } else {
            tencent.shareToQzone(activity, shareInfo.getParams(), shareListener);
        }
    }

    /*基本信息验证*/
    private boolean baseVerify(SocialCallback callback) {
        if (TextUtils.isEmpty(appId)) {
            if (callback != null) {
                callback.socialError(activity.getString(R.string.social_error_appid_empty));
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

    private abstract static class NormalUIListener implements IUiListener {
        private SocialCallback callback;
        private Context context;

        NormalUIListener(Context context, SocialCallback callback) {
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onError(UiError uiError) {
            if (callback != null) {
                callback.socialError(uiError.errorMessage);
            }
        }

        @Override
        public void onCancel() {
            if (callback != null && context != null) {
                callback.socialError(context.getString(R.string.social_cancel));
            }
        }
    }
}
