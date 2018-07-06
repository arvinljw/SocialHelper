package net.arvin.socialhelper.sample.utils;

import net.arvin.socialhelper.SocialHelper;

/**
 * Created by arvinljw on 17/11/27 17:33
 * Function：
 * Desc：
 */
public enum SocialUtil {
    INSTANCE();

    public SocialHelper socialHelper;

    SocialUtil() {
        socialHelper = new SocialHelper.Builder()
                .setQqAppId("qqAppId")
                .setWxAppId("wxAppId")
                .setWxAppSecret("wxAppSecret")
                .setWbAppId("wbAppId")
                .setWbRedirectUrl("wbRedirectUrl")
                .build();
    }
}
