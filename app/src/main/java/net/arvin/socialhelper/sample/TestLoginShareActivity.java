package net.arvin.socialhelper.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.arvin.socialhelper.SocialHelper;
import net.arvin.socialhelper.callback.SocialLoginCallback;
import net.arvin.socialhelper.callback.SocialShareCallback;
import net.arvin.socialhelper.entities.QQShareEntity;
import net.arvin.socialhelper.entities.ShareEntity;
import net.arvin.socialhelper.entities.ThirdInfoEntity;
import net.arvin.socialhelper.entities.WBShareEntity;
import net.arvin.socialhelper.entities.WXShareEntity;
import net.arvin.socialhelper.sample.utils.SocialUtil;

import java.util.ArrayList;

public class TestLoginShareActivity extends AppCompatActivity implements View.OnClickListener, SocialLoginCallback, SocialShareCallback {
    private String imgUrl = "https://upload-images.jianshu.io/upload_images/3157525-afe6f0ba902eb523.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240";
    private String localImgUrl = "/storage/emulated/0/DCIM/Camera/IMG_20180422_113944.jpg";
    private String localVideoUrl = "/storage/emulated/0/893.mp4";
    private String title = "个人博客";
    private String summary = "好好学习";
    private String targetUrl = "https://arvinljw.github.io";

    private SocialHelper socialHelper;

    private TextView tvLoginInfo;
    private RadioGroup rgShareInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_login_share);
        setTitle("测试SocialHelper");
        socialHelper = SocialUtil.INSTANCE.socialHelper;
        initView();
        initEvent();
    }

    private void initView() {
        tvLoginInfo = findViewById(R.id.tv_login_info);
        rgShareInfo = findViewById(R.id.rg_share_info);
    }

    private void initEvent() {
        findViewById(R.id.img_qq).setOnClickListener(this);
        findViewById(R.id.img_wx).setOnClickListener(this);
        findViewById(R.id.img_wb).setOnClickListener(this);
        findViewById(R.id.img_qq_share).setOnClickListener(this);
        findViewById(R.id.img_wx_share).setOnClickListener(this);
        findViewById(R.id.img_wb_share).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_qq:
                socialHelper.loginQQ(this, this);
                break;
            case R.id.img_wx:
                socialHelper.loginWX(this, this);
                break;
            case R.id.img_wb:
                socialHelper.loginWB(this, this);
                break;
            case R.id.img_qq_share:
                socialHelper.shareQQ(this, createQQShareEntity(), this);
                break;
            case R.id.img_wx_share:
                socialHelper.shareWX(this, createWXShareEntity(), this);
                break;
            case R.id.img_wb_share:
                socialHelper.shareWB(this, createWBShareEntity(), this);
                break;
        }
    }

    private ShareEntity createQQShareEntity() {
        ShareEntity shareEntity = null;
        int checkedRadioButtonId = rgShareInfo.getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.rb_img:
                shareEntity = QQShareEntity.createImageInfo(localImgUrl, "ni6");
                break;
            case R.id.rb_img_text:
                shareEntity = QQShareEntity.createImageTextInfo(title, targetUrl,
                        imgUrl, summary, "ni6");
                break;
            case R.id.rb_web:
                //分享到qq空间，因为qq图文就包含了targetUrl所以比较常用
                ArrayList<String> imgUrls = new ArrayList<>();
                imgUrls.add(imgUrl);
                shareEntity = QQShareEntity.createImageTextInfoToQZone(title, targetUrl,
                        imgUrls, summary, "ni6");

                //文字说说
//                shareEntity = QQShareEntity.createPublishTextToQZone("好好学习");
                //图片说说，本地文件
//                imgUrls.clear();
//                imgUrls.add(localImgUrl);
//                shareEntity = QQShareEntity.createPublishImageToQZone(imgUrls);
                //视频说说，本地视频，大小限制见方法注释
//                shareEntity = QQShareEntity.createPublishVideoToQZone(localVideoUrl);
                break;
        }
        return shareEntity;
    }

    private ShareEntity createWXShareEntity() {
        ShareEntity shareEntity = null;
        int checkedRadioButtonId = rgShareInfo.getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.rb_img:
                shareEntity = WXShareEntity.createImageInfo(false, localImgUrl);
                break;
            case R.id.rb_img_text:
                //微信图文是分开的，但是在分享到朋友圈的web中是可以有混合的
                shareEntity = WXShareEntity.createImageInfo(false, R.mipmap.ic_launcher);
                break;
            case R.id.rb_web:
                shareEntity = WXShareEntity.createWebPageInfo(false, targetUrl, R.mipmap.ic_launcher,
                        title, summary);
                break;
        }
        return shareEntity;
    }

    private ShareEntity createWBShareEntity() {
        ShareEntity shareEntity = null;
        int checkedRadioButtonId = rgShareInfo.getCheckedRadioButtonId();
        switch (checkedRadioButtonId) {
            case R.id.rb_img:
                shareEntity = WBShareEntity.createImageTextInfo(localImgUrl, title);
                break;
            case R.id.rb_img_text:
                shareEntity = WBShareEntity.createImageTextInfo(R.mipmap.ic_launcher, title);
                break;
            case R.id.rb_web:
                shareEntity = WBShareEntity.createWebInfo(targetUrl,
                        title, summary, R.mipmap.ic_launcher, "这是要说的内容");
                break;
        }
        return shareEntity;
    }

    //用处：qq登录和分享回调，以及微博登录回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (socialHelper != null) {//qq分享如果选择留在qq，通过home键退出，再进入app则不会有回调
            socialHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void loginSuccess(ThirdInfoEntity info) {
        tvLoginInfo.setText(toString(info));
    }

    @Override
    public void socialError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void shareSuccess(int type) {
        Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
    }

    private String toString(ThirdInfoEntity info) {
        return "登录信息 = {" +
                "unionId='" + info.getUnionId() + '\'' +
                ", openId='" + info.getOpenId() + '\'' +
                ", nickname='" + info.getNickname() + '\'' +
                ", sex='" + info.getSex() + '\'' +
                ", avatar='" + info.getAvatar() + '\'' +
                ", platform='" + info.getPlatform() + '\'' +
                '}';
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socialHelper != null) {
            socialHelper.clear();
        }
    }
}
