package net.arvin.socialhelper.sample.wxapi;

import net.arvin.socialhelper.SocialHelper;
import net.arvin.socialhelper.WXHelperActivity;
import net.arvin.socialhelper.sample.utils.SocialUtil;

/**
 * Created by arvinljw on 17/7/6 14:43
 * Function：
 * Desc：
 */
public class WXEntryActivity extends WXHelperActivity {

    @Override
    protected SocialHelper getSocialHelper() {
        return SocialUtil.INSTANCE.socialHelper;
    }
}
