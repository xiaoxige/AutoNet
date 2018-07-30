# AutoNet (网络框架)
	AutoNet 代理了OkHttp， 处理了复杂繁琐的网络请求代码！ 使Android开发网络应用更加简单，只需关注业务即可。
# Git地址
	https://github.com/xiaoxige/AutoNet
# 特色
	* 使用简单、调用方便
	* 支持注解、 链式
	* 支持实体类、map传值
	* 可动态添加和修改头部
	* 可对请求参数数据进行加密
	* 可自主处理返回的头部数据
	* 可自主处理返回的body数据
	* 可自定义返回数据的类型
	* 可定义固定、灵活及临时的域名、头部信息（优先级： 临时>灵活>固定。 有效性： 固定 >= 灵活 > 临时）
	* 支持网络策略（网络、本地、先本地后网络、先网络后本地）
	* 支持上传文件和下载文件
# gradle依赖
	compile 'cn.xiaoxige:autonet-api:1.0.9'
	annotationProcessor 'cn.xiaoxige:autonet-processor:1.0.9'
# 使用
## 1. 初始化
### 1.1 AutoNetConfig(配置AutoNet的基本配置)注意：改配置基本是固定，如域名、头部数据
	* 设置是否开启Stetho调试配置
	* 设置默认域名（key: default）
	* 设置多个域名
	* 设置头部参数
	* 设置Okhttp的拦截器
### 1.2 AutoNet的初始化操作
	AutoNet.getInstance().initAutoNet(Context, AutoNetConfig);
	可以链式去设置加密的回调，头部数据的回调，Body数据的回调, eg:
	AutoNet.getInstance().initAutoNet(Context, AutoNetConfig)
	.setEncryptionCallback(new IAutoNetEncryptionCallback() {
            @Override
            public String encryption(Long key, String encryptionContent) {
				// 可通过key去加密参数
                return encryptionContent;
            }
        }).setHeadsCallback(new IAutoNetHeadCallBack() {
            @Override
            public void head(Headers headers) {
				// 请求返回的头部数据回调
            }
        }).setBodyCallback(new IAutoNetBodyCallBack() {
            @Override
            public boolean body(String object, FlowableEmitter emitter) {
				// 自己处理需要返回true
                return false;
            }
	});
	还可以再次设置域名和头部数据（这里的配置为灵活的， 可在任意地方修改）
	AutoNet.getInstance().updateOrInsertHead(key, value);
	AutoNet.getInstance().updateOrInsertDomainNames(key, value);
## 2. 链式调用
## 3. 注解方式