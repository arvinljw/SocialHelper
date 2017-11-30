package net.arvin.socialhelper;

import net.arvin.socialhelper.callback.SocialLoginCallback;
import net.arvin.socialhelper.callback.SocialShareCallback;
import net.arvin.socialhelper.entities.ShareEntity;
import net.arvin.socialhelper.entities.ThirdInfoEntity;

/**
 * Created by arvinljw on 17/11/24 16:06
 * Function：
 * Desc：
 */
public interface ISocial {
    void login(SocialLoginCallback callback);

    ThirdInfoEntity createThirdInfo();

    void share(SocialShareCallback callback, ShareEntity shareInfo);

    void onDestroy();
}
