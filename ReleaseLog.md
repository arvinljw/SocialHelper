### Release Log

**v1.2.0**

* 修正qq获取的unionId

    之前返回的是loginResult的pfKey，若是没有申请unionId的话是获取不到正确的unionId的
    对于qq的unionId的问题可以查看这个文档[unionid介绍](http://wiki.connect.qq.com/unionid介绍)
    
* 删除微博回调调用的onNewIntent方法，使用onActivityResult代替

**v1.1.2**

* 更新sdk版本
* 废弃微博分享回调，调用的onNewIntent方法，直接使用onActivityResult即可，预计下个版本删除该方法

**v1.1.1:**

* 增加第三方登陆返回的accessToken等信息
* 封装微信登陆和分享回调类WxHelperActivity
* 将微信的广播注册方式换成本地广播
* 将v7和gson包只是编译，需要自己引入，避免重复使用
* 调整相关包版本以及编译版本等

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
