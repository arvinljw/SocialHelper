package net.arvin.socialhelper.callback;

import net.arvin.socialhelper.entities.ThirdInfoEntity;

/**
 * Created by arvinljw on 17/11/24 15:46
 * Function：
 * Desc：
 */
public interface SocialLoginCallback extends SocialCallback{
    void loginSuccess(ThirdInfoEntity info);
}
