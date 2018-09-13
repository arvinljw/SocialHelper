## SocialHelper

目前几乎所有app都包含了第三方的登录以及分享功能，之前大多都使用ShareSDK或者其他SDK来实现，但是有些情况不希望通过第三方的sdk来间接集成，所以这个组件就有了用武之地。

这个组件在demo中是没有包含相关调用的代码的只有一些配置和使用的东西，因为应用的申请实在麻烦，但是已经在项目中测试通过了，所以可以放心使用。

### 优点

* 便捷实现第三方登录和分享及其回调
* 了解第三方登录或分享的实现流程

### 用法

包含了：如何引用、使用以及混淆

#### 引用

1、在根目录的build.gradle中加入如下配置

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
        maven { url "https://dl.bintray.com/thelasterstar/maven/" }
    }
}
```

2、在要是用的module中增加如下引用

```
dependencies {
    ...
    compile 'com.github.arvinljw:SocialHelper:v1.0.9'
}
```

*注：如果在该module中使用了v7包，那么可使用exclude命令移除本库的引用避免重复，gson也是一样，大体方法如下*

```
compile ('com.github.arvinljw:SocialHelper:v1.0.9'){
    exclude group: 'com.android.support'
}
```

#### 使用

**1、相应配置**

AndroidManifest.xml配置

添加权限

```
<!--微信权限，微博权限，qq只需前两个-->
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

在application下添加Activity

```
<!--qq配置开始-->
<activity
    android:name="com.tencent.tauth.AuthActivity"
    android:launchMode="singleTask"
    android:noHistory="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <category android:name="android.intent.category.BROWSABLE"/>
        <data android:scheme="tencentqqAppId"/>
    </intent-filter>
</activity>
<activity
    android:name="com.tencent.connect.common.AssistActivity"
    android:configChanges="orientation|keyboardHidden|screenSize"
    android:theme="@android:style/Theme.Translucent.NoTitleBar"/>
<!--qq配置结束-->
<!--微信配置开始-->
<activity
    android:name=".wxapi.WXEntryActivity"
    android:exported="true"
    android:label="@string/app_name"
    />
<!--微信配置结束-->

<!--微博不需要-->
```

*注：其中需要注意的是，qq配置中<data android:scheme="tencentqqAppId"/>
qqAppId换成您申请的qqAppId即可*

微信还需要在packageName.wxapi的包下创建WXEntryActivity，具体实现可在[这里查看](app/src/main/java/net/arvin/socialhelper/sample/wxapi/WXEntryActivity.java)

拷贝过去即可，注意路径，其中主要的处理是在其onResp的时候判断是登录还是分享做出相应的处理，通知本库触发回调

**2、获取实例**

首先采用外观模式，暴露出来一个SocialHelper，可以通过构建者模式获取实例，调用相关方法，配置回调。

```
socialHelper = new SocialHelper.Builder()
        .setQqAppId("qqAppId")
        .setWxAppId("wxAppId")
        .setWxAppSecret("wxAppSecret")
        .setWbAppId("wbAppKey")
        .setWbRedirectUrl("wbRedirectUrl")
        .build();
```

**3、调用相关方法**

这里提供了QQ、微信和微博的相应的登录和分享方法

* public void loginQQ(Activity activity, SocialLoginCallback callback)
* public void loginWX(Activity activity, SocialLoginCallback callback)
* public void loginWB(Activity activity, SocialLoginCallback callback)
* public void shareQQ(Activity activity, ShareEntity shareInfo, SocialShareCallback callback)
* public void shareWX(Activity activity, ShareEntity shareInfo, SocialShareCallback callback)
* public void shareWB(Activity activity, ShareEntity shareInfo, SocialShareCallback callback)

*注：分享方法的参数顺序和老版本有一点变化，老版本的被遗弃了，计划下个版本删除*

这个其实通过名字就能知道是什么意思，就不解释了，需要解释的是参数：

**a、传入的Activity**

主要是为了回调回来都在当前Activity中处理回调事情，避免在Fragment中去处理，这样也能减少一些工作

*注：当然遇到在Fragment中调用登录或者分享的时候，请使用您最熟悉的方式回调到其Activity中处理相关操作*

**b、传入的回调**

这里回调也分为了两种登录回调和分享回调

回调接口如下：

```
public interface SocialCallback {
    void socialError(String msg);
}

public interface SocialLoginCallback extends SocialCallback{
    void loginSuccess(ThirdInfoEntity info);
}

public interface SocialShareCallback extends SocialCallback{
    void shareSuccess(int type);
}
```

登录和分享回调失败都会有一个信息返回；

登录成功会将封装的第三方信息返回，包含了常用的昵称，性别，头像，登录平台，更多信息也能在其中找到，详细的含义在Lib中有详细的注释；

分享成功就只有一个回调，便于提示。


**c、ShareEntity**

这个参数是分享的重点，对于QQ、微信和微博对应着QQShareEntity、WXShareEntity以及WBShareEntity

都可以通过其相应的静态方法创建相应分享类型

* QQShareEntity
    
    * createImageTextInfo 图文信息，网页信息
    * createImageInfo 纯图片信息
    * createMusicInfo 音乐信息
    * createAppInfo 应用信息
    * createImageTextInfoToQZone 图文信息到QQ空间
    
* WXShareEntity

    * createTextInfo 文本信息
    * createImageInfo 纯图片信息
    * createMusicInfo 音乐信息
    * createVideoInfo 视频信息
    * createWebPageInfo 网页信息

* WBShareEntity

    * createTextInfo 纯文本信息
    * createImageTextInfo 图文信息
    * createMultiImageInfo 多图信息
    * createVideoInfo 视频信息
    * createWebInfo 网页信息

具体详细的参数以及注释在相应类中都有明确的注释，这里就不过多的展开了；其中有ParamsRequired注解的参数代表必传。

注意，有些不必传的参数，不传虽然也能分享成功，但是有可能会影响分享出去的样式，请自行调试。

**4、回调配置**

在调用的Activity中配置

```
//用处：qq登录和分享回调，以及微博登录回调
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (data != null && socialHelper != null) {//qq分享如果选择留在qq，通过home键退出，再进入app则不会有回调
        socialHelper.onActivityResult(requestCode, resultCode, data);
    }
}
//用处：微博分享回调
@Override
protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    if (socialHelper != null) {
        socialHelper.onNewIntent(intent);
    }
}
```

至于微信的回调配置，在一开始的时候说的WXEntryActivity的onResp中的处理就是了。

至此使用方式就基本完毕，算下来也就这么四部配置，就能愉快的使用了，当然前提是相应的app申请已经成功。

**其中对于SocialHelper的实例，可以像Demo中一样使用一个工具简单封装成单例来使用。**

**5、销毁**

```
@Override
protected void onDestroy() {
    super.onDestroy();
    if (socialHelper != null) {
        socialHelper.clear();
    }
}
```

在onDestroy方法中调用socialHelper.clear()方法，注销掉可能注册的广播以及把activity引用赋为空。

### Release Log

**v1.0.9:**

* 分享回调增加分享类型

**v1.0.8:**

* 优化appId等参数为空是不抛异常，只打印日志，避免使用奔溃
* 删除SocialHelper中上个版本Deprecate了的方法
* 增加使用SocialHelper的[使用例子](https://github.com/arvinljw/SocialHelper/blob/master/app/src/main/java/net/arvin/socialhelper/sample/TestLoginShareActivity.java)，但是无法直接使用，因为各种appId不匹配，需要在自己的app中按需使用。

**v1.0.7:**

* 更新微信，qq，微博sdk版本
* 调整SocialHelper中shareXX相关方法的参数顺序，老的方法Deprecated了，下个版本移除

**v1.0.5:**

* 第一个稳定版本，包含微信，qq，微博登录和分享


#### 混淆

```
#qq
-keep class com.tencent.**{*;}
#微博
-keep class com.sina.weibo.sdk.** { *; }
#微信
-keep class com.tencent.mm.opensdk.** { *; }
-keep class packageName.wxapi.** { *; }
#Gson
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
#Socialhelper
-keep class net.arvin.socialhelper.**{*;}
```

*注：微信的packageName换成自己的包名*


### 三方文档链接

[QQ平台](http://wiki.open.qq.com/wiki/%E7%A7%BB%E5%8A%A8%E5%BA%94%E7%94%A8%E6%8E%A5%E5%85%A5%E6%96%B0%E6%89%8B%E6%8C%87%E5%BC%95)

[微信平台](https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=1417751808&token=&lang=zh_CN)

[微博平台](http://open.weibo.com/wiki/Sdk/android)

### 反馈

若是有什么好的建议或者问题，请多多指教，感激不尽。

待做事项

* 参数解释以及对应样式的整理
* 完善demo的代码

### License

```
Copyright 2017 arvinljw

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

