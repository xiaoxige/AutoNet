# AutoNet (网络框架)
	AutoNet 代理了OkHttp， 处理了复杂繁琐的网络请求代码！ 使Android开发网络应用更加简单，只需关注业务即可。
# Git地址
	https://github.com/xiaoxige/AutoNet
# 特色
	* 使用简单、调用方便
	* 支持注解、 链式
	* 支持实体类、map传值
	* 可防止内存泄漏（需继承RxFragmentActivity、或者RxFragment。并向AutoNet传入相应的生命周期）
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
## 2. 回调介绍
	回调需要继承实现AutoNet提供好的接口或者抽象类。AutoNet已经分类， 用户需要什么功能就去集成相应的接口或者抽象类即可
### 2.1 IAutoNetDataBeforeCallBack（数据返回前的处理， 可定制要继续返回给客户前端的数据）
	public interface IAutoNetDataBeforeCallBack<T> extends IAutoNetCallBack {
		// T为用户指定的body要返回的实体类（AutoNet会自动转换）， emitter为Rxjava的上游， 可改变其返回结果。 如果自己处理需要返回true。eg: T为一个实体类， 里面有一个List集合， 我们在View层只需要关注List集合，则可以在这里直接重新定义并返回List集合， 并返回true。 （注意：其实这里还有一个功能就是， 根据自己的需求去判断是否集合为空更妙。emitter.onError(new EmptyError())。）
    	boolean handlerBefore(T t, FlowableEmitter emitter);
	}
### 2.2 IAutoNetDataSuccessCallBack（只关心成功的数据， 不关心失败和数据为空的结果）
	public interface IAutoNetDataSuccessCallBack<T> extends IAutoNetCallBack {
		// T为用户需要返回的实体类
    	void onSuccess(T entity);
	}
### 2.3 IAutoNetDataCallBack（数据返回比较全的回调, 包含了数据成功、数据失败、数据为空）
	public interface IAutoNetDataCallBack<T> extends IAutoNetDataSuccessCallBack<T> {
		// 失败
    	void onFailed(Throwable throwable);
		// 数据为空（注意：如果不在IAutoNetDataBeforeCallBack去根据自己的业务去手动抛出emitter.onError(new EmptyError())的话， AutoNet是不知道你的业务是什么的， 所以在这个情况下AutoNet在body都为空的时候才调用onEmpty()）
    	void onEmpty();
	}
### 2.4 IAutoNetLocalOptCallBack（需要用到本地操作， eg：网络策略， 本地、 先本地后网络， 先网络后本地。 其实AutoNet并不能自动根据你的业务和字段给你建立数据库， 需要自己去实现）
	public interface IAutoNetLocalOptCallBack extends IAutoNetCallBack {
    	Object optLocalData(Map request);
	}
### 2.5 IAutoNetFileCallBack（文件操作时的回调， 需要关心上传错误等需要继承上面的IAutoNetDataCallBack）
	public interface IAutoNetFileCallBack extends IAutoNetCallBack {
		// 上传文件或者下载文件的进度（0~100）
    	void onPregress(float progress);
		// 上传成功或者下载成功后的File文件回调
    	void onComplete(File file);
	}
### 2.6 AbsAutoNetCallback（数据回调的集合， 其实数据写这个就行了， 需要什么方法重写什么方法即可）
	// 其中 T为返回的body的实体类，Z为自己处理后需要返回给View层后的实体类
	public abstract class AbsAutoNetCallback<T, Z> implements IAutoNetDataBeforeCallBack<T>, IAutoNetDataCallBack<Z> {

	    @Override
	    public boolean handlerBefore(T t, FlowableEmitter emitter) {
	        return false;
	    }
	
	    @Override
	    public void onSuccess(Z entity) {
	
	    }
	
	    @Override
	    public void onFailed(Throwable throwable) {
	
	    }
	
	    @Override
	    public void onEmpty() {
	
	    }

	}
## 3. 链式调用
## 4. 注解方式