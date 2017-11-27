# AutoNet
封装屏蔽Date及Presenter层代码， 使Android开发真正的只专注View层的编写

#介绍：


	* Android开发离不开网络请求后台数据。该项目也是一个封装请求网络的库。它完全解放了安卓开发的P层和D层代码， 使开发者只需要关注于View层的编写即可。
	* 使用方式，操作方式简单
	* 支持统一设置多个BaseUrl
	* 可以设置固定头部参数，Get、Delete请求的固定参数
	* 支持请求参数分别加密（分类加密）
	* 暂时支持Post/Get/Delete/Put请求
	* 支持使用Stetho调试查看网络请求、本地数据等
	* 封装OkHttp网络框架
	* 封装RxJava, RxLife, 可以使用网络请求防止内存泄漏
	* 支持请求网络直接返回Json对象


#待加功能：

	* 支持数据库操作



#使用：
1.注解介绍：

	* AutoNetAnontation 网络请求相关。可以设置url（除去域名，BaseUrl统一设置）、连接时间，读取时间及写入的时间。默认为/
	* AutoNetBaseUrlKeyAnontation 设置本次请求使用的是哪一个域名（BaseUrl）。默认的Key是default。 如果使用默认的BaseUrl，不需要设置该注解
	* AutoNetEncyptionAnontation 设置本次是否数据加密，及加密的标识。可通过加密标识去设置不同的加密方式。如果不需要加密，不需要设置该注解
	* AutoNetPatternAnontation 设置请求的方式， Get， Post, Put， Delete. 默认为Get请求。 如果本次请求为Get请求，不需要设置该注解
	* AutoNetResponseEntityClass 设置返回Json对象的Class类型。通过设置该注解， audonet可以自动返回Json对象

2.使用方式：

	* 在项目的Gradle中加入依赖：

compile 'cn.xiaoxige:autonet-api:1.0.1'
annotationProcessor 'cn.xiaoxige:autonet-processor:1.0.1'

如果请求不下依赖， 请在项目的Gradle中加入仓库地址：
maven { url "https://dl.bintray.com/xiaoxige/autonet"; }


	* 初始化：

在Application中进行初始化：
简单初始化入下：
        Map head = new HashMap();
        head.put("token", "xxxxxx");
        Map mapBaseUrl = new HashMap();
        mapBaseUrl.put("jsonTestBaseUrl", "http://xxxjson.com";);
        AutoNetConfig config = new AutoNetConfig.Buidler()
                .setBaseUrl("http://www.baidu.com")
                .setBaseUrl(mapBaseUrl)
                .setHeader(head)
                .build();
        AutoNet.getInstance().init(this, config).setAutoNetEncryption(new IAutoNetEncryptionCallback() {
            @Override
            public String encryption(long encryptionKey, String beforeValue) {
                return beforeValue;
            }
        });
以上初始化设置了BaseUrl, 该BaseUrl没有设置key， 及为默认的key(default), 当然还可以通过
.setBaseUrl(Map map)去设置更多的BaseUrl.
其中setHeader/setGetDelParams跟setBaseUrl设置方法一样。
下面的监听为加密的回调， 可以通过key去设置不同的加密方法

注意：
head/getdelparams/baseurl都提供了set和add的方法， set会把以前的清空， add则不会。
在设置公共的参数, head和getdelparms。设置上去是不会变化的， 如果需要改变其值，或者再次添加。可以调用AutoNet.getInstance().updateOrInsertHeader()和AutoNet.getInstance().updateOrInsertGetDelParam()；方法去改变。比如去设置token， 当启动的时候token可能为空，在登入后就需要去更新下改头部的参数。

	* 使用

在请求前， 先定义请求的参数类，和接受后的json数据类。请让你的请求参数类去实现IRequestEntity接口， 让你的接受后的json数据类去集成AutoResponseEntity。
如下所示：
public class JsonTestRequestEntity implements IRequestEntity {
    private String m;
    private String c;
    private String a;
    public String getM() {
        return m;
    }
    public void setM(String m) {
        this.m = m;
    }
    public String getC() {
        return c;
    }
    public void setC(String c) {
        this.c = c;
    }
    public String getA() {
        return a;
    }
    public void setA(String a) {
        this.a = a;
    }
}


              public class JsonTestResponseEntity extends AutoResponseEntity {
            private String status;
            private long code;
            private String message;
            private Data data;
            public static class Data{
                 ......
           }
}

现在开始可以写请求的数据接口了：注意 请实现IAutoNetDataCallback接口， 泛型最好写出我们的数据要返回的类型

比如我们要请求的场景为Get请求， 默认的BaseUrl, 不加密， url为/xxx.php

我们就可以这样写
    @AutoNetResponseEntityClass(value = JsonTestResponseEntity.class)
    @AutoNetPatternAnontation(value = AutoNetPatternAnontation.NetPattern.GET)
    @AutoNetAnontation(url = "/xxx.php")
    public class TestCallback implements IAutoNetDataCallback<TestResponseEntity> {
        @Override
        public void onSuccess(TestResponseEntity entity) {
        }
        @Override
        public void onEmpty() {
        }
        @Override
        public void onError(Throwable throwable) {
        }
    }
对是的，没错这样就写好了。 数据会自动请求及返回。

这样就直接请求了？肯定不是呀。先听我慢慢说

现在去ReBuild Project。编译完成后，在你需要去联网获得数据的时候去掉一个类的方法。该类的结构特点有如下：
如果该请求接口是内部类的话，其类名为外面的类名+该接口的类名+AutoProxy。如果是单独的类的话， 则是改类的前一级报名+该类的类名+AutoProxy,
该类中有两个方法， 一个是安全的网络请求， 一个是不安全的网络请求， 只要是是否绑定Rxlefe来定的。 如果使用安全的网络请求， 比如说吧， 你在Activity中写了改网络请求的接口， 那么这个Activity就要去集成RxActivity了。通过bindUntilEvent(ActivityEvent.DESTROY)，使该生命周期和RxJave绑定。
JsonTestRequestEntity entity = new JsonTestRequestEntity();
entity.setA("guidepage");
entity.setM("ina_app");
entity.setC("other");
cn.xiaoxige.autonet.MainActivityTestCallbackAutoProxy.startSoftNet(entity, bindUntilEvent(ActivityEvent.DESTROY), new TestCallback());


再比如情景：Post请求，使用keyjsonTestBaseUrl的BaseUrl, 为需要加密，加密的type为1,  网络连接数据为15秒， url为/xxx.xxx
则改接口类的写法如下：

    @AutoNetResponseEntityClass(value = TestResponseEntity.class)
    @AutoNetEncryptionAnontation(value = true, key = 1)
    @AutoNetBaseUrlKeyAnontation(value = "keyjsonTestBaseUrl")
    @AutoNetPatternAnontation(value = AutoNetPatternAnontation.NetPattern.POST)
    @AutoNetAnontation(url = "/xxx.xxx", connectOutTime = 15000)
    public class TestCallback implements IAutoNetDataCallback<TestResponseEntity> {
        @Override
        public void onSuccess(TestResponseEntity entity) {
        }
        @Override
        public void onEmpty() {
        }
        @Override
        public void onError(Throwable throwable) {
        }
    }

其中就在Application的加密回调中，判断type用什么样的加密方法，把加密后的字符串返回即可。


注意： 在返回的数据中， 每一个请求请求通了，就会吧字符串放到AutoResponseEntity的autoResponseResult中记录， 是否正确的转换成Json对象是在AutoResponseEntity的isJsonTransformationError字段记录。
