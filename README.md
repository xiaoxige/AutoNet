# AutoNet

封装屏蔽网络层， 使Android开发真正的只专注View层的编写

#Git地址：https://github.com/xiaoxige/AutoNet

#介绍：

	* 使用方式，操作方式简单
    * 支持注解和链式两种方式
    * 可添加固定和动态的头部信息
    * 可自主对数据进行加密请求
    * 可自主处理每次请求返回的头部数据
    * 可自主处理每次请求的body数据
    * 可选择联网策略(网络、本地、先本地后网络、先网络后本地)
    * 上传及下载文件简单已用
    * ......

#注：具体用法请看Demo

gradle依赖
compile 'cn.xiaoxige:autonet-api:1.0.4'

annotationProcessor 'cn.xiaoxige:autonet-processor:1.0.4'

#联系方式：
如果在使用的过程中， 出现什么问题欢迎在GitHub上issues，也可以给我发邮件：xiaoxigexiaoan@outlook.com

#使用：

1.注解介绍：

    * AutoNetAnontation 网络参数设置(value(除去域名)、writeTime、readTime、connectOutTime)
    * AutoNetBaseUrlKeyAnontation BaseUrl的选择标识key(value)
    * AutoNetDisposableBaseUrlAnontation 本次请求临时使用的BaseUrl(value)
    * AutoNetDisposableHeadAnnontation 本次请求临时使用的头部信息(value[])
    * AutoNetEncryptionAnontation 加密参数设置(key, value)
    * AutoNetMediaTypeAnontation 请求的MediaType(value)
    * AutoNetPatternAnontation 请求方式(value(get/post/put/delete))
    * AutoNetResponseEntityClass 请求返回的实体类(value)
    * AutoNetStrategyAnontation 网络请求策略(value(net/local/local_net/net_local))
    * AutoNetTypeAnontation 请求和返回的请求类型(reqType(json/form/stream), resType(json/form/stream))

2.初始化：

    Map<String, String> heads = new ArrayMap<>();
    heads.put("token", "0");
    heads.put("userId", "A");

    Map<String, String> domainNames = new ArrayMap<>();
    domainNames.put("pppig", "https://www.pangpangpig.com");
    domainNames.put("upFile", "http://testimage.xxxx.com:8080");

    AutoNetConfig config = new AutoNetConfig.Builder()
            .isOpenStetho(true)
            .setDefaultDomainName("https:www.baidu.com")
            .setHeadParam(heads)
            .setDomainName(domainNames)
            .build();

    AutoNet.getInstance().initAutoNet(this, config).setEncryptionCallback(new IAutoNetEncryptionCallback() {
        @Override
        public String encryption(Long key, String encryptionContent) {
            Log.e("TAG", "加密信息： key = " + key + ", encryptionContent = " + encryptionContent);
            return encryptionContent;
        }
    }).setHeadsCallback(new IAutoNetHeadCallBack() {
        @Override
        public void head(Headers headers) {
            Log.e("TAG", "头部回调：" + headers);
        }
    }).setBodyCallback(new IAutoNetBodyCallBack() {
        @Override
        public boolean body(String object) {
            Log.e("TAG", "body： " + object);
            return false;
        }
    }).updateOrInsertDomainNames("jsonTestBaseUrl", "http://api.news18a.com");

