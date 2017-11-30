package net.arvin.socialhelper.sample.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import net.arvin.socialhelper.sample.utils.SocialUtil;

/**
 * Created by arvinljw on 17/7/6 14:43
 * Function：
 * Desc：
 */
public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String wxAppId = SocialUtil.getInstance().socialHelper().getBuilder().getWxAppId();
        api = WXAPIFactory.createWXAPI(this, wxAppId, true);
        api.registerApp(wxAppId);

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        //登录
        Log.d("WXEntryActivity", baseResp.errCode + baseResp.errStr);
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                String code = ((SendAuth.Resp) baseResp).code;
                SocialUtil.getInstance().socialHelper().sendAuthBackBroadcast(this, code);
            } else {
                SocialUtil.getInstance().socialHelper().sendAuthBackBroadcast(this, null);
            }
        } else if (baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
                SocialUtil.getInstance().socialHelper().sendShareBackBroadcast(this, true);
            } else {
                SocialUtil.getInstance().socialHelper().sendShareBackBroadcast(this, false);
            }
        }
        onBackPressed();
    }
}
