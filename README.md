
# AutoNet (网络框架)
> AutoNet 代理了OkHttp， 处理了复杂繁琐的网络请求代码！ 使Android开发网络应用更加简单，只需关注业务即可。

# Git地址
## 1.Android版本：
> [Android版AutoNet](https://github.com/xiaoxige/AutoNet)

## 2.ios版本：
> [ios版AutoNet](https://github.com/xiaoxige/AutoNet-Ios)

# AutoNet 技术交流群：
> QQ: **141027175**

# Demo说明
> * app： AutoNet各个方式使用介绍
> * recommendeddemo： AutoNet推荐用法（注：Api使用了wanandroid的Api）

# 特色
> * 使用简单、调用方便
> * 支持了同步、异步操作
> * 支持注解、 链式
> * 支持实体类、map传值
> * 可防止内存泄漏（需继承RxFragmentActivity、或者RxFragment。并向AutoNet传入相应的生命周期）
> * 可动态添加和修改头部
> * 可对请求参数数据进行加密
> * 可自主处理返回的头部数据
> * 可自主处理返回的body数据
> * 可自定义返回数据的类型
> * 可定义固定、灵活及临时的域名、头部信息（优先级： 临时>灵活>固定。 有效性： 固定 >= 灵活 > 临时）
> * 支持网络策略（网络、本地、先本地后网络、先网络后本地）
> * 支持上传文件和下载文件
> * 可直接获得上游的Flowable, 用户自己进行操作结果。（eg: 使用zip去合并多个请求等）

# gradle依赖
	compile 'cn.xiaoxige:autonet-api:2.0.0'
	annotationProcessor 'cn.xiaoxige:autonet-processor:2.0.0'
# 简易使用demo
![image](https://github.com/xiaoxige/AutoNet/raw/master/Screenshots/AutoNet_Android.png)
# 使用
## 1. 初始化
### 1.1 AutoNetConfig(配置AutoNet的基本配置)注意：改配置基本是固定，如域名、头部数据
> * 设置是否开启Stetho调试配置
> * 设置默认域名（key: default）
> * 设置多个域名
> * 设置头部参数
> * 设置Okhttp的拦截器

### 1.2 AutoNet的初始化操作
``` java
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
        public void head(Object flag, Headers headers) {
			// 请求返回的头部数据回调
        }
    }).setBodyCallback(new IAutoNetBodyCallBack() {
        @Override
        public boolean body(Object flag, String body) throw Exception {
			// 自己处理需要返回true
            return false;
        }
});
还可以再次设置域名和头部数据（这里的配置为灵活的， 可在任意地方修改）
AutoNet.getInstance().updateOrInsertHead(key, value);
AutoNet.getInstance().updateOrInsertDomainNames(key, value);
```
## 2. 支持清单

### 2.1. 支持请求类型方式及策略
|.|请求类型|请求方式|策略|
|:------:|:------:|:------:|:------:|
|注解方式| GET/POST/DELETE/PUT|JSON/FORM/STREAM|NET/LOCAL_NET/LOCAL/NET_LOCAL|
|链式同步|GET/POST/DELETE/PUT|JSON/FORM/STREAM|NET/LOCAL|
|获取上游发射器|GET/POST/DELETE/PUT|JSON/FORM/STREAM|NET/LOCAL|
|链式异步（推荐）|GET/POST/DELETE/PUT|JSON/FORM/STREAM|NET/LOCAL_NET/LOCAL/NET_LOCAL|

### 2.2. 支持的回调
|.|注解方式|链式同步|获取上游发射器|链式异步（推荐）|
|:------:|:------:|:------:|:------:|:------:|
|IAutoNetBodyCallBack|✔|✔|✔|✔|
|IAutoNetHeadCallBack|✔|✔|✔|✔|
|IAutoNetEncryptionCallback|✔|✔|✔|✔|
|AbsAutoNetCallback|✔|✘|✘|✔|
|IAutoNetCallBack|✔|✘|✘|✔|
|IAutoNetComplete|✔|✘|✘|✔|
|IAutoNetDataBeforeCallBack|✔|✘|✘|✔|
|IAutoNetDataSuccessCallBack|✔|✘|✘|✔|
|IAutoNetDataCallBack|✔|✘|✘|✔|
|IAutoNetFileCallBack|✔|✔|✔|✔|
|IAutoNetLocalOptCallBack|✔|✔|✔|✔|

## 3. 回调介绍
	回调需要继承实现AutoNet提供好的接口或者抽象类。AutoNet已经分类， 用户需要什么功能就去集成相应的接口或者抽象类即可
### 3.1 IAutoNetDataBeforeCallBack（数据返回前的处理， 可定制要继续返回给客户前端的数据）
``` java
public interface IAutoNetDataBeforeCallBack<T, Z> extends IAutoNetCallBack {
	// T为用户指定的body要返回的实体类（AutoNet会自动转换）， emitter为Rxjava的上游， 可改变其返回结果。 如果自己处理需要返回true。eg: T为一个实体类， 里面有一个List集合， 我们在View层只需要关注List集合，则可以在这里直接重新定义并返回List集合， 并返回true。 （注意：其实这里还有一个功能就是， 根据自己的需求去判断是否集合为空更妙。emitter.onError(new EmptyError())。）
	boolean handlerBefore(T t, FlowableEmitter<Z> emitter);
}
```
### 3.2 IAutoNetDataSuccessCallBack（只关心成功的数据， 不关心失败和数据为空的结果）
``` java
public interface IAutoNetDataSuccessCallBack<T> extends IAutoNetCallBack {
	// T为用户需要返回的实体类
	void onSuccess(T entity);
}
```
### 3.3 IAutoNetDataCallBack（数据返回比较全的回调, 包含了数据成功、数据失败、数据为空）
``` java
public interface IAutoNetDataCallBack<T> extends IAutoNetDataSuccessCallBack<T> {
	// 失败
	void onFailed(Throwable throwable);
	// 数据为空（注意：如果不在IAutoNetDataBeforeCallBack去根据自己的业务去手动抛出emitter.onError(new EmptyError())的话， AutoNet是不知道你的业务是什么的， 所以在这个情况下AutoNet在body都为空的时候才调用onEmpty()）
	void onEmpty();
}
```
### 3.4 IAutoNetLocalOptCallBack（需要用到本地操作， eg：网络策略， 本地、 先本地后网络， 先网络后本地。 其实AutoNet并不能自动根据你的业务和字段给你建立数据库， 需要自己去实现）
``` java
public interface IAutoNetLocalOptCallBack<T> extends IAutoNetCallBack {
    T optLocalData(Map request) throws Exception;
}
```
### 3.5 IAutoNetFileCallBack（文件操作时的回调， 需要关心上传错误等需要继承上面的IAutoNetDataCallBack）
``` java
public interface IAutoNetFileCallBack extends IAutoNetCallBack {
	// 上传文件或者下载文件的进度（0~100）
	void onPregress(float progress);
	// 上传成功或者下载成功后的File文件回调
	void onComplete(File file);
}
```
### 3.6 IAutoNetComplete （请求结束后， 该回调会被调用，不管错误成功后都会调用）
``` java
public interface IAutoNetComplete extends IAutoNetCallBack {
    void onComplete();
}
```
### 3.7 AbsAutoNetCallback（数据回调的集合， 其实数据写这个就行了， 需要什么方法重写什么方法即可）
``` java
// 其中 T为返回的body的实体类，Z为自己处理后需要返回给View层后的实体类
public abstract class AbsAutoNetCallback<T, Z> implements IAutoNetDataBeforeCallBack<T, Z>, IAutoNetDataCallBack<Z>, IAutoNetComplete {

    @Override
    public boolean handlerBefore(T t, FlowableEmitter<Z> emitter) {
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

    @Override
    public void onComplete() {
    }
}
}
```

## 4. 链式调用
``` java
AutoNet.getInstance().createNet()
	// 设置url后缀（除去域名）
    .setSuffixUrl(String)
	// 设置一个标志， 会在全局的Head和Body中回调
	.setFlag(flag)
	// 参数
    .setParams(Map)
    .setParam(key, value)
    .setRequestEntity(IAutoNetRequest)
	// post请求（可传参数）
    .doPost(...)
	// get请求（可传参数）
    .doGet(...)
	// put(可传参数)
    .doPut(...)
	// delete(可传参数)
    .doDelete(...)
	// 指定使用的域名的Key（默认default）
    .setDomainNameKey(String)
	// 设置网络请求方式
    .setNetPattern(NetPattern)
	// 设置网络策略
    .setNetStrategy(NetStrategy)
	// 设置请求类型（JSON/FORM/STREAM/OTHER）
    .setReqType(Type)
	// 设置返回类型（JSON/FORM/STREAM/OTHER）
    .setResType(Type)
	// 设置额外的参数（主要解决动态的拼在url中的参数。eg： www.xxx.com/news/1, 最后的那个1为动态）
    .setExtraDynamicParam(String)
	// 临时的BaseUrl
    .setBaseUrl(String)
	// 链接超时时间
    .setConnectOutTime(Long)
	// 读取时间
    .setReadOuTime(Long)
	// 写入时间
    .setWriteOutTime(Long)
	// 需要加密的参数的key， 可根据key去加密其中类型的参数， 在初始化时使用到了，还记得吗（上去看看）
    .setEncryptionKey(Long)
	// 设置MediaType
    .setMediaType(String)
	// 发送文件
    .setPullFileParams()
	// 接受文件
    .setPushFileParams()
	// 设置临时头部
    .setHeads(String[])
	// 绑定生命周期，防止内存泄漏（忘了？上面有说）
    .setTransformer(...)
	// 数据回调（2章节中讲到的一些回调）
    (1).start(CallBack);
	// 获得上游， 用户自己处理结果
	(2).getFlowable(Class);
	// 同步请求
	(3).synchronizationNet（Class）
```
## 5. 获取上游并处理（已zip合并为例， 这里只是用了两个， 其实RxJava提供了好多， 当然还有其他用法，详情可以看RxJava的用法）
``` java
// zip
// 1. 得到wanAndroid的上游发射器
Flowable wanAndroidFlowable = getWanAndroidFlowable();
// 2. 得到百度的上游发射器
Flowable baiduFlowable = getBaiduFlowable();
// 3. 合并
//noinspection unchecked
Flowable.zip(wanAndroidFlowable, baiduFlowable, new BiFunction<WanAndroidResponse, String, ZipEntity>() {
    @Override
    public ZipEntity apply(WanAndroidResponse wanAndroidResponse, String s) throws Exception {
        ZipEntity zipEntity = new ZipEntity();
        List<WanAndroidEntity> data = wanAndroidResponse.getData();
        zipEntity.setData(data);
        zipEntity.setBaidu(s);
        return zipEntity;
    }
}).subscribe(new Subscriber<ZipEntity>() {
    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(Integer.MAX_VALUE);
    }

    @Override
    public void onNext(ZipEntity o) {
        List<WanAndroidEntity> data = o.getData();
        // 这里进行模拟， 如果 wanAndroid 数据是必须的数据， 为空就是错误
        if (data == null || data.isEmpty()) {
            Toast.makeText(MainActivity.this, "数据为空了", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(MainActivity.this, "成功：\n" + o.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof EmptyError) {
            Toast.makeText(MainActivity.this, "数据为空了", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(MainActivity.this, "数据错误：\n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete() {
        Toast.makeText(MainActivity.this, "结束", Toast.LENGTH_SHORT).show();
    }
});


private Flowable getWanAndroidFlowable() {
    return AutoNet.getInstance().createNet()
            .setDomainNameKey("wanandroid")
            .setSuffixUrl("/wxarticle/chapters/json")
            .doGet()
            // 设置追踪， 为了在body拦截中， 不让其他逻辑进行了拦截
            .setFlag(666)
            .getFlowable(WanAndroidResponse.class)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io());
}

private Flowable getBaiduFlowable() {
    return AutoNet.getInstance().createNet()
            // 设置追踪， 为了在body拦截中， 不让其他逻辑进行了拦截
            .setFlag(666)
            .getFlowable(String.class)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io());
}
```

## 6. 注解方式
### 6.1 注解介绍
> * AutoNetAnontation 网络参数设置(value(除去域名)、flag、 writeTime、readTime、connectOutTime)
> * AutoNetBaseUrlKeyAnontation BaseUrl的选择标识key(value)
> * AutoNetDisposableBaseUrlAnontation 本次请求临时使用的BaseUrl(value)
> * AutoNetDisposableHeadAnnontation 本次请求临时使用的头部信息(value[])
> * AutoNetEncryptionAnontation 加密参数设置(key, value)
> * AutoNetMediaTypeAnontation 请求的MediaType(value)
> * AutoNetPatternAnontation 请求方式(value(get/post/put/delete))
> * AutoNetStrategyAnontation 网络请求策略(value(net/local/local_net/net_local))
> * AutoNetTypeAnontation 请求和返回的请求类型(reqType(json/form/stream), resType(json/form/stream))

### 6.2 代理类名规则
    如果是回调是内部 则代理类名为 外层类名 + 回调类名 + AutoProxy
    如果回调就是一个类 则代理类名为 回调类名 + AutoProxy
### 6.3 注意
	如果使用的是注解方式请求网络， 在写完类后，请build -> rebuild project。
### 6.4 例子
#### 一、普通请求
``` java
@AutoNetPatternAnontation(AutoNetPatternAnontation.NetPattern.GET)
@AutoNetAnontation("/init.php")
@AutoNetBaseUrlKeyAnontation("jsonTestBaseUrl")
public class doGet implements IAutoNetDataBeforeCallBack<TestResponseEntity>, IAutoNetDataCallBack<List<Entity>> {
    @Override
    public boolean handlerBefore(TestResponseEntity o, FlowableEmitter emitter) {
		List<Entity> entitys = o.getList();
		if(entitys == null || entitys.isEmpty()){
            emitter.onError(new EmptyError());
			return true;
		}
		emitter.onNext(entitys);
        return true;
    }

    @Override
    public void onSuccess(List<Entity> entitys) {

    }

    @Override
    public void onFailed(Throwable throwable) {

    }

    @Override
    public void onEmpty() {
    }
}

先build下， 然后再需要发送该网络连接时：
MainActivitydoGetAutoProxy.startNet(MainActivity.this， bindUntilEvent(ActivityEvent.DESTROY));
注意：MainActivitydoGetAutoProxy这个类生成的规则前面已给出
```
#### 二、 上传文件
``` java
@AutoNetBaseUrlKeyAnontation("upFile")
@AutoNetTypeAnontation(reqType = AutoNetTypeAnontation.Type.STREAM)
@AutoNetPatternAnontation(AutoNetPatternAnontation.NetPattern.POST)
public class PushFile implements IAutoNetDataCallBack, IAutoNetFileCallBack {

    @Override
    public void onFailed(Throwable throwable) {
    }

    @Override
    public void onEmpty() {
    }

    @Override
    public void onSuccess(Object entity) {
    }

    @Override
    public void onPregress(float progress) {
    }

    @Override
    public void onComplete(File file) {
    }
}

请求方式：
MainActivityPushFileAutoProxy.pushFile(MainActivity.this, "upload", path + File.separator + "a.png");
```
#### 三、 下载文件
``` java
@AutoNetBaseUrlKeyAnontation("downFile")
@AutoNetTypeAnontation(resType = AutoNetTypeAnontation.Type.STREAM)
@AutoNetAnontation("/apk/downLoad/android_4.2.4.apk")
public class PullFile implements IAutoNetDataCallBack<File>, IAutoNetFileCallBack {
    @Override
    public void onFailed(Throwable throwable) {
    }

    @Override
    public void onEmpty() {
    }

    @Override
    public void onSuccess(File entity) {
    }

    @Override
    public void onPregress(float progress) {
    }

    @Override
    public void onComplete(File file) {
    }
}

请求方式：
MainActivityPullFileAutoProxy.pullFile(MainActivity.this, path, "pppig.apk");
```
## 7. 简单的例子
### 初始化
``` java
AutoNetConfig config = new AutoNetConfig.Builder()
            .isOpenStetho(BuildConfig.DEBUG)
            .setDefaultDomainName(ApiConstant.BASE_POST_URL)
            .build();

AutoNet.getInstance().initAutoNet(this, config).setBodyCallback(new IAutoNetBodyCallBack() {
        @Override
        public boolean body(Object flag, String body) {
			// 全局的， 所有的请求都会到这里
			// 在这里可以根据自己的统一的字段去判断code什么的是否成功了
			// 如果不成功可以以异常出去， 最后会在onFailed回调
            if (!TextUtils.isEmpty(body)) {
                try {
                    BaseResponse baseResponse = new Gson().fromJson(body, BaseResponse.class);
                    if (!baseResponse.isSuccess()) {
                        throw new CustomError(baseResponse.getMessage());
                        return true;
                    }
                } catch (Exception e) {
                    emitter.onError(e);
                    return true;
                }
            }
            return false;
        }
    });
```
### 简单使用
``` java
AutoNet.getInstance().createNet()
    .setSuffixUrl("/test")
    .setParam("a", "a")
    .setParam("b", 1)
    .doPost()
    .start(new TestListCallback());
private class TestListCallback extends AbsAutoNetCallback<TestListResponse, List<TestEntity>> {

    @Override
    public boolean handlerBefore(TestListResponse response, FlowableEmitter emitter) {
		// 这里可以在数据返回以前， 再次指定要返回的数据， 并根据自己的业务去判断是否为空（注意这里是在分线程中）
        List<TestEntity> entitys = response.getData();
        if (entitys == null || entitys.isEmpty()) {
            emitter.onError(new EmptyError());
            return true;
        }
        //noinspection unchecked
        emitter.onNext(entitys);
        return true;
    }

    @Override
    public void onSuccess(List<TestEntity> entitys) {
        bindUserInfo(entitys);
        mEmptyLayout.showContent();
        refreshLayout.refreshComplete();
    }

    @Override
    public void onFailed(Throwable throwable) {
        HandlerError.handlerError(throwable);
        mEmptyLayout.showContent();
        refreshLayout.refreshComplete();
    }

    @Override
    public void onEmpty() {
        HandlerError.handlerEmpty();
        mEmptyLayout.showContent();
        refreshLayout.refreshComplete();
    }
}
```

# 2.0.0 修改点（该版本修改较大， 架构等都有修改， 所以和之前的版本可能有个别用法不同，如果切换最新版本需要主要一下几点）
### 一、 支持了同步请求，异步请求
* 增添了synchronizationNet（Class）方法。 Class 即后台返回的数据结构类型。
* 修改了getFlowable（Class）, 增加参数Class。Class 即后台返回的数据结构类型。

### 二、 body回调
* 去除 FlowableEmitter 参数， 在之前需要进行特出处理的比如 **emitter.onError(new Custom("..."))** 改为 **throw new Custom("...")**

### 三、 增加了IAutoNetComplete 接口， 如果你想监听请求结束， 就集成IAutoNetComplete。 它不管是否成功失败最后都会回调
### 四、 如果是下载文件操作， 那么请求返回的ResponseClass 需要制定为 File.class类型