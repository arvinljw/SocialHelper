package net.arvin.socialhelper.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.arvin.socialhelper.SocialHelper;
import net.arvin.socialhelper.sample.utils.SocialUtil;

public class MainActivity extends AppCompatActivity {

    private SocialHelper socialHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        socialHelper = SocialUtil.getInstance().socialHelper();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && socialHelper != null) {//qq分享如果选择留在qq，通过home键退出，再进入app则不会有回调
            socialHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (socialHelper != null) {
            socialHelper.onNewIntent(intent);
        }
    }
}
