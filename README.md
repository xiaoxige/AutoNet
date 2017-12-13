# AutoNet
封装屏蔽Date及Presenter层代码， 使Android开发真正的只专注View层的编写

#Git地址：https://github.com/xiaoxige/AutoNet

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
	* 支持了直接请求网络
	* 支持了普通类也能请求网络
	* 加入动态地址参数拼接(如 www.xxx.com/xxx/10121，最后的10121可以动态添加)
	* 增加上传文件功能（同时支持进度自动回调，及服务器最终相应结果回调）
	* 增加文件下载（支持进度自动回调）
	* 优化代码逻辑


#待加功能：

	* 支持数据库操作

#更新说明：
        更新的版本在release中查看（当前版本为1.0.3）
        增加的功能用法在最后给出最新的用法
#新版特色：
        使用起来更加简单， 可以动态拼接地址参数， 可发送和上传文件，进度自动回调。

#注：具体用法请看Demo

#使用：
1.注解介绍：

	* AutoNetAnontation 网络请求相关。可以设置url（除去域名，BaseUrl统一设置）、连接时间，读取时间及写入的时间。默认为/
	* AutoNetBaseUrlKeyAnontation 设置本次请求使用的是哪一个域名（BaseUrl）。默认的Key是default。 如果使用默认的BaseUrl，不需要设置该注解
	* AutoNetEncyptionAnontation 设置本次是否数据加密，及加密的标识。可通过加密标识去设置不同的加密方式。如果不需要加密，不需要设置该注解
	* AutoNetPatternAnontation 设置请求的方式， Get， Post, Put， Delete. 默认为Get请求。 如果本次请求为Get请求，不需要设置该注解
	* AutoNetResponseEntityClass 设置返回Json对象的Class类型。通过设置该注解， audonet可以自动返回Json对象
	* AutoNetTypeAnontation 设置请求和接受类型， 有JSON和STREAM方式。默认请求和接受都是JSON类型，如果请求为STREAM则表示上传文件，如果接受为STREAM则表示下载文件；接受和请求不可同时为STREAM类型
	* AutoNetMediaTypeAnontation 设置上传文件时的mediaType， 需要和后台一致。默认为 application/octet-stream


2.使用方式：

	* 在项目的Gradle中加入依赖：

compile 'cn.xiaoxige:autonet-api:1.0.3'

annotationProcessor 'cn.xiaoxige:autonet-processor:1.0.3'


	* 初始化：

在Application中进行初始化：
简单初始化入下：

        Map head = new HashMap();
        head.put("token", "xxx");
        Map mapBaseUrl = new HashMap();
        mapBaseUrl.put("BaseFileUrl", "http://www.pangpangpig.com");
        mapBaseUrl.put("BaseFilePushUrl", "http://192.168.199.238:8080");
        mapBaseUrl.put("jsonTestBaseUrl", "http://api.news18a.com");
        AutoNetConfig config = new AutoNetConfig.Buidler()
                .setBaseUrl("http://www.baidu.com")
                .addBaseUrl(mapBaseUrl)
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

支持了在普通类中进行网络请求：
如：

    public class NormalClassNet {

        private TextView mTextView;

        public NormalClassNet() {
        }

        public NormalClassNet(TextView textView) {
            mTextView = textView;
        }

        @AutoNetResponseEntityClass(value = AutoResponseEntity.class)
        public class TestCallback implements IAutoNetDataCallback<AutoResponseEntity> {

            @Override
            public void onSuccess(AutoResponseEntity entity) {
                mTextView.setText("返回：" + entity.autoResponseResult + "\n" + "是否转Json对象失败：" + entity.isJsonTransformationError);

            }

            @Override
            public void onEmpty() {
                mTextView.setText("Get请求为空");
            }

            @Override
            public void onError(Throwable throwable) {
                mTextView.setText(throwable.toString());
            }
        }

    }

在使用的时候更加的简单：

     NormalClassNet normalClassNet = new NormalClassNet(tvResult);
     cn.xiaoxige.autonet.NormalClassNetTestCallbackAutoProxy.startUnSoftNet(normalClassNet);
     注：tvResult 只要是为了显示

可以直接请求网络数据：

    @AutoNetResponseEntityClass(value = AutoResponseEntity.class)
    public class ImmediateNet implements IAutoNetDataCallback<AutoResponseEntity> {
        private TextView mTextView;

        public ImmediateNet() {
        }


        @Override
        public void onSuccess(AutoResponseEntity entity) {
            mTextView.setText("返回：" + entity.autoResponseResult + "\n" + "是否转Json对象失败：" + entity.isJsonTransformationError);
        }

        @Override
        public void onEmpty() {
            mTextView.setText("Get请求为空");
        }

        @Override
        public void onError(Throwable throwable) {
            mTextView.setText(throwable.toString());
        }

        public void setmTextView(TextView mTextView) {
            this.mTextView = mTextView;
        }
    }

使用更加简单：
     ImmediateNet immediateNet = new ImmediateNet();
     immediateNet.setmTextView(tvResult);
     cn.xiaoxige.autonet.autonetImmediateNetAutoProxy.startUnSoftNet(immediateNet);
     注：tvResult 只要是为了显示

拼接动态地址参数
    请求和依赖一样， 在发送请求的地方， 有一个重载的方法， 需要传入extraParam， 这个参数为String类型， 把要动态变化拼接的填入该参数即可。
    比如， 请求的接口模型为：http://xxx.xxx.com/xxx/10123, 其中10123为动态参数， 则在请求网络的地方把该动态参数传入即可。如：XXX.startUnSoftNet(MainActivity.this, "" + 10123);即可

AutoNet可以十分方便的上传文件（进度回调及返回结果回调）：

    /**
     * 上传文件
     */
    @AutoNetBaseUrlKeyAnontation(value = "BaseFilePushUrl")
    @AutoNetTypeAnontation(reqType = AutoNetTypeAnontation.Type.STREAM)
    @AutoNetAnontation(url = "/FileUpload/FileUploadServlet")
    @AutoNetPatternAnontation(AutoNetPatternAnontation.NetPattern.POST)
    public class SendFileCallback extends AAutoNetStreamCallback {
        @Override
        public void onComplete(File file) {
            tvResult.setText("文件上传完成");
        }

        @Override
        public void onPregress(float progress) {
            Log.e("TAG", "progress" + progress);
            tvResult.setText("上传进度" + progress + "%");
        }

        @Override
        public void onEmpty() {
            tvResult.setText("数据为空");
        }

        @Override
        public void onError(Throwable throwable) {
            tvResult.setText(throwable.toString());
        }

        @Override
        public void onSuccess(AutoResponseEntity entity) {
            super.onSuccess(entity);
            tvResult.setText("返回：" + entity.autoResponseResult + "\n" + "是否转Json对象失败："
                    + entity.isJsonTransformationError + "\n\n"
                    + "json： " + entity.toString());
        }
    }

    在要上传文件的地方，传入该文件的地址即可， 还可以拼参数及文件一起上传哦!
     /**
      * 文件可能不存在哦， 这只是测试
      * （你可以先下载后在上传）
      * 改下载的地址及fileKey是本地Tomcat上的，在这里只是指明了一个使用方式而已
    */
      String path = getExternalFilesDir(null).toString();
      /**
      * @param object 最外层类实例
      * @param fileKey 发布文件的key， 需要和后台约定好
      * @param path 要发送的文件的路径
      */
      cn.xiaoxige.autonet.MainActivitySendFileCallbackAutoProxy.pushFile(MainActivity.this, "photo1", path + File.separator + "xiaoxige.apk");

AutoNet可轻松实现文件下载（进度回调）：

    /**
     * 下载文件
     */
    @AutoNetBaseUrlKeyAnontation(value = "BaseFileUrl")
    @AutoNetTypeAnontation(resType = AutoNetTypeAnontation.Type.STREAM)
    @AutoNetAnontation(url = "/APK/DownLoad/PangPangPig_102.apk")
    @AutoNetPatternAnontation(value = AutoNetPatternAnontation.NetPattern.POST)
    public class DownFileCallback extends AAutoNetStreamCallback {

        @Override
        public void onComplete(File file) {
            tvResult.setText("文件下载完成");
        }

        @Override
        public void onPregress(float progress) {
            Log.e("TAG", "progress" + progress);
            tvResult.setText("下载进度" + progress + "%");
        }

        @Override
        public void onEmpty() {
            tvResult.setText("数据为空");
        }

        @Override
        public void onError(Throwable throwable) {
            tvResult.setText(throwable.toString());
        }
    }

    在需要下载文件的地方， 把文件要保存的地址及文件保存的名字填入即可, 也可以传参数和下载文件同时哦!
    String path = getExternalFilesDir(null).toString();
    /**
     * @param object 最外层类实例
     * @param path 文件保存的路径
     * @param fileName 文件保存的名字
     */
     cn.xiaoxige.autonet.MainActivityDownFileCallbackAutoProxy.pullFile(MainActivity.this, path, "xiaoxige.apk");



注意： 在返回的数据中， 每一个请求请求通了，就会吧字符串放到AutoResponseEntity的autoResponseResult中记录， 是否正确的转换成Json对象是在AutoResponseEntity的isJsonTransformationError字段记录。
