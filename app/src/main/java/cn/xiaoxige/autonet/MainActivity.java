package cn.xiaoxige.autonet;


import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.RxActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.xiaoxige.annotation.AutoNetAnontation;
import cn.xiaoxige.annotation.AutoNetBaseUrlKeyAnontation;
import cn.xiaoxige.annotation.AutoNetDisposableHeadAnnontation;
import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet.entity.WanAndroidEntity;
import cn.xiaoxige.autonet.entity.WanAndroidResponse;
import cn.xiaoxige.autonet.entity.ZipEntity;
import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.abstracts.AbsAutoNetCallback;
import cn.xiaoxige.autonet_api.error.EmptyError;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetComplete;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetLocalOptCallBack;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("AlibabaAvoidManuallyCreateThread")
public class MainActivity extends RxActivity {

    private TextView tvAnnotationNet;
    private TextView tvAnnotationLocalNet;
    private TextView tvAnnotationLocal;
    private TextView tvAnnotationNetLocal;
    private TextView tvAnnotationPushFile;
    private TextView tvAnnotationPullFile;

    private TextView tvChainSynNet;
    private TextView tvChainSynLocal;
    private TextView tvChainSynPushFile;
    private TextView tvChainSynPullFile;

    private TextView tvChainAsyNet;
    private TextView tvChainAsyLocalNet;
    private TextView tvChainAsyLocal;
    private TextView tvChainAsyNetLocal;
    private TextView tvChainAsyPushFile;
    private TextView tvChainAsyPullFile;

    private TextView tvFlagTrack;
    private TextView tvZip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        registerListener();
    }

    private void initView() {
        // 注解相关
        tvAnnotationNet = (TextView) findViewById(R.id.tvAnnotationNet);
        tvAnnotationLocalNet = (TextView) findViewById(R.id.tvAnnotationLocalNet);
        tvAnnotationLocal = (TextView) findViewById(R.id.tvAnnotationLocal);
        tvAnnotationNetLocal = (TextView) findViewById(R.id.tvAnnotationNetLocal);
        tvAnnotationPushFile = (TextView) findViewById(R.id.tvAnnotationPushFile);
        tvAnnotationPullFile = (TextView) findViewById(R.id.tvAnnotationPullFile);

        // 链式同步相关
        tvChainSynNet = (TextView) findViewById(R.id.tvChainSynNet);
        tvChainSynLocal = (TextView) findViewById(R.id.tvChainSynLocal);
        tvChainSynPushFile = (TextView) findViewById(R.id.tvChainSynPushFile);
        tvChainSynPullFile = (TextView) findViewById(R.id.tvChainSynPullFile);

        // 链式异步相关
        tvChainAsyNet = (TextView) findViewById(R.id.tvChainAsyNet);
        tvChainAsyLocalNet = (TextView) findViewById(R.id.tvChainAsyLocalNet);
        tvChainAsyLocal = (TextView) findViewById(R.id.tvChainAsyLocal);
        tvChainAsyNetLocal = (TextView) findViewById(R.id.tvChainAsyNetLocal);
        tvChainAsyPushFile = (TextView) findViewById(R.id.tvChainAsyPushFile);
        tvChainAsyPullFile = (TextView) findViewById(R.id.tvChainAsyPullFile);

        // 其他
        tvZip = (TextView) findViewById(R.id.tvZip);
        tvFlagTrack = (TextView) findViewById(R.id.tvFlagTrack);
    }

    private void registerListener() {
        // 注解相关监听
        annotationListener();
        // 链式同步监听
        chainSynListener();
        // 链式异步监听
        chainAsyListener();
        // 其他监听
        otherListener();
    }

    private void annotationListener() {
        tvAnnotationNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityAnnotationNetCallbackAutoProxy.startNet(MainActivity.this);
            }
        });

        tvAnnotationLocalNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map params = new ArrayMap();
                //noinspection unchecked
                ((ArrayMap) params).put("oneParam", "one");
                //noinspection unchecked
                ((ArrayMap) params).put("twoParam", "two");
                MainActivityAnnotationLocalNetCallbackAutoProxy.startNet(MainActivity.this, params);
            }
        });

        tvAnnotationLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map params = new ArrayMap();
                //noinspection unchecked
                ((ArrayMap) params).put("oneParam", "one");
                //noinspection unchecked
                ((ArrayMap) params).put("twoParam", "two");
                MainActivityAnnotationLocalCallbackAutoProxy.startNet(MainActivity.this, params);
            }
        });

        tvAnnotationNetLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map params = new ArrayMap();
                //noinspection unchecked
                ((ArrayMap) params).put("oneParam", "one");
                //noinspection unchecked
                ((ArrayMap) params).put("twoParam", "two");

                String extraDynamicParam = "1";
                MainActivityAnnotationNetLocalCallbackAutoProxy.startNet(MainActivity.this, params, extraDynamicParam);
            }
        });

        tvAnnotationPushFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityAnnotationPushFileAutoProxy.pushFile(MainActivity.this, "upload", getCacheDir().getAbsolutePath() + File.separator + "AutoNet.png");
            }
        });

        tvAnnotationPullFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivityAnnotationPullFileAutoProxy.pullFile(MainActivity.this, getCacheDir().getAbsolutePath(), "pangpangpig.apk");
            }
        });
    }

    private void chainSynListener() {
        tvChainSynNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WanAndroidResponse response = AutoNet.getInstance().createNet()
                                    .setDomainNameKey("wanandroid")
                                    .setSuffixUrl("/wxarticle/chapters/json")
                                    .doGet()
                                    .synchronizationNet(WanAndroidResponse.class);
                            runMainToast("成功：\n" + response.toString());
                        } catch (Exception e) {
                            runMainToast("失败：\n" + e.getMessage());
                        }
                    }
                }).start();
            }
        });

        tvChainSynLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final String response = AutoNet.getInstance().createNet()
                                    .setParam("xiaoxige", "真帅")
                                    .setNetStrategy(AutoNetStrategyAnontation.NetStrategy.LOCAL)
                                    .setAutoNetLocalOptCallback(new IAutoNetLocalOptCallBack<String>() {
                                        @Override
                                        public String optLocalData(Map request) throws Exception {
                                            return "本地数据: 请求参数" + request.toString() + "， 结果： 小稀革真帅！";
                                        }
                                    })
                                    .synchronizationNet(String.class);
                            runMainToast("成功：\n" + response.toString());
                        } catch (Exception e) {
                            runMainToast("失败：\n" + e.getMessage());
                        }
                    }
                }).start();
            }
        });

        tvChainSynPushFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String response = AutoNet.getInstance().createNet()
                                    .setDomainNameKey("upFile")
                                    .setParam("xiaoxige", "真帅")
                                    .setPushFileParams("upload", getCacheDir().getAbsolutePath() + File.separator + "AutoNet.png")
                                    .doPost()
                                    .setReqType(AutoNetTypeAnontation.Type.STREAM)
                                    .setAutoNetFileCallback(new IAutoNetFileCallBack() {
                                        @Override
                                        public void onProgress(final float progress) {
                                            runMainTextViewChangeShowText(tvChainSynPushFile, "发送文件（" + progress + ")");
                                        }

                                        @Override
                                        public void onComplete(File file) {
                                            runMainToast("发送完成");
                                        }
                                    })
                                    .synchronizationNet(String.class);

                            runMainToast("成功：\n" + response.toString());
                            runMainTextViewChangeShowText(tvChainSynPushFile, "发送文件");

                        } catch (Exception e) {
                            runMainToast("失败：\n" + e.getMessage());
                            runMainTextViewChangeShowText(tvChainSynPushFile, "发送文件");
                        }
                    }
                }).start();
            }
        });

        tvChainSynPullFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            File file = AutoNet.getInstance().createNet()
                                    .setDomainNameKey("pppig")
                                    .setSuffixUrl("/apk/downLoad/android_4.4.5.apk")
                                    .setResType(AutoNetTypeAnontation.Type.STREAM)
                                    .setPullFileParams(getCacheDir().getAbsolutePath(), "pangpangpig.apk")
                                    .setAutoNetFileCallback(new IAutoNetFileCallBack() {
                                        @Override
                                        public void onProgress(float progress) {
                                            runMainTextViewChangeShowText(tvChainSynPullFile, "下载文件（" + progress + ")");
                                        }

                                        @Override
                                        public void onComplete(File file) {
                                            runMainToast("下载文件成功！");
                                        }
                                    })
                                    .synchronizationNet(File.class);
                            runMainToast("下载文件完成");
                            runMainTextViewChangeShowText(tvChainSynPullFile, "下载文件");
                        } catch (Exception e) {
                            runMainToast("失败：\n" + e.getMessage());
                            runMainTextViewChangeShowText(tvChainSynPullFile, "下载文件");
                        }
                    }
                }).start();
            }
        });
    }

    private void chainAsyListener() {
        tvChainAsyNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map params = new ArrayMap();
                // ..
                AutoNet.getInstance().createNet()
                        .doGet()
                        .setDomainNameKey("wanandroid")
                        .setSuffixUrl("/wxarticle/chapters/json")
                        .setParams(params)
                        .setTransformer(bindUntilEvent(ActivityEvent.DESTROY))
                        .start(new AsyNetCallback());
            }
        });

        tvChainAsyLocalNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoNet.getInstance().createNet()
                        .setNetStrategy(AutoNetStrategyAnontation.NetStrategy.LOCAL_NET)
                        .setTransformer(bindUntilEvent(ActivityEvent.DESTROY))
                        .start(new AsyLocalNetOrNetLocalOrLocalCallback());
            }
        });

        tvChainAsyLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoNet.getInstance().createNet()
                        .setNetStrategy(AutoNetStrategyAnontation.NetStrategy.LOCAL)
                        .setTransformer(bindUntilEvent(ActivityEvent.DESTROY))
                        .start(new AsyLocalNetOrNetLocalOrLocalCallback());
            }
        });

        tvChainAsyNetLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoNet.getInstance().createNet()
                        .setNetStrategy(AutoNetStrategyAnontation.NetStrategy.NET_LOCAL)
                        .setTransformer(bindUntilEvent(ActivityEvent.DESTROY))
                        .start(new AsyLocalNetOrNetLocalOrLocalCallback());
            }
        });

        tvChainAsyPushFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoNet.getInstance().createNet()
                        .setDomainNameKey("upFile")
                        .setParam("xiaoxige", "真帅")
                        .setPushFileParams("upload", getCacheDir().getAbsolutePath() + File.separator + "AutoNet.png")
                        .doPost()
                        .setReqType(AutoNetTypeAnontation.Type.STREAM)
                        .start(new AsyPushFileCallback());
            }
        });

        tvChainAsyPullFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoNet.getInstance().createNet()
                        .setDomainNameKey("pppig")
                        .setSuffixUrl("/apk/downLoad/android_4.4.5.apk")
                        .setResType(AutoNetTypeAnontation.Type.STREAM)
                        .setPullFileParams(getCacheDir().getAbsolutePath(), "pangpangpig.apk")
                        .start(new AsyPullFileCallback());

            }
        });
    }

    private void otherListener() {
        tvZip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        tvFlagTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // flag 追踪， 会在全局中回调， 这里在body里拦截， 其实实际开发中， 用后台返回的数据进行判读比较多， 不如在body中
                // 返回值是失败的， 直接throw CustomError("服务器返回的错误信息")， 完美。
                // flag 追踪也有很大的作用， 比如对一个接口进行不拦截处理， 比如上面的zip 一个接口请求出错， 但是并不能全部都不返回对吧
                AutoNet.getInstance().createNet()
                        .doGet()
                        .setDomainNameKey("wanandroid")
                        .setSuffixUrl("/wxarticle/chapters/json")
                        // 添加追踪标志
                        .setFlag(1)
                        .setTransformer(bindUntilEvent(ActivityEvent.DESTROY))
                        .start(new AsyNetCallback());
            }
        });
    }


    /**
     * ---------------------------------------------------------------------------------------------
     * 注解相关
     **/
    @AutoNetBaseUrlKeyAnontation(value = "wanandroid")
    @AutoNetAnontation(value = "/wxarticle/chapters/json")
    @AutoNetStrategyAnontation(value = AutoNetStrategyAnontation.NetStrategy.NET)
    @AutoNetPatternAnontation(value = AutoNetPatternAnontation.NetPattern.GET)
    @AutoNetDisposableHeadAnnontation(value = {
            "autonet: haoyong",
            "author: xiaoxige"
    })
    public class AnnotationNetCallback extends AbsAutoNetCallback<WanAndroidResponse, List<WanAndroidEntity>> {
        @Override
        public boolean handlerBefore(WanAndroidResponse wanAndroidResponse, FlowableEmitter<List<WanAndroidEntity>> emitter) {
            List<WanAndroidEntity> data = wanAndroidResponse.getData();
            if (data == null || data.isEmpty()) {
                emitter.onError(new EmptyError());
                return true;
            }
            emitter.onNext(data);
            return true;
        }

        @Override
        public void onSuccess(List<WanAndroidEntity> entity) {
            super.onSuccess(entity);
            Toast.makeText(MainActivity.this, "成功：\n" + entity.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(Throwable throwable) {
            super.onFailed(throwable);
            Toast.makeText(MainActivity.this, "失败了： \n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEmpty() {
            super.onEmpty();
            Toast.makeText(MainActivity.this, "失败了： \n 数据为空", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {
            super.onComplete();
            Log.i("TAG", "请求结束了");
        }
    }

    @AutoNetStrategyAnontation(value = AutoNetStrategyAnontation.NetStrategy.NET_LOCAL)
    @AutoNetDisposableHeadAnnontation(value = {
            "autonet: haoyong",
            "author: xiaoxige"
    })
    public class AnnotationNetLocalCallback implements IAutoNetDataCallBack<String>, IAutoNetLocalOptCallBack<String>, IAutoNetComplete {

        @Override
        public String optLocalData(Map request) throws Exception {

            return "本地数据：请求参数：" + request.toString() + ", 本地数据： test";
        }

        @Override
        public void onSuccess(String entity) {
            Toast.makeText(MainActivity.this, entity, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(Throwable throwable) {
            Toast.makeText(MainActivity.this, "失败了： \n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEmpty() {
            Toast.makeText(MainActivity.this, "失败了： \n 数据为空", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {
            Toast.makeText(MainActivity.this, "结束", Toast.LENGTH_SHORT).show();
        }
    }

    @AutoNetBaseUrlKeyAnontation(value = "wanandroid")
    @AutoNetAnontation(value = "/wxarticle/chapters/json")
    @AutoNetStrategyAnontation(value = AutoNetStrategyAnontation.NetStrategy.LOCAL_NET)
    @AutoNetDisposableHeadAnnontation(value = {
            "autonet: haoyong",
            "author: xiaoxige"
    })
    public class AnnotationLocalNetCallback extends AbsAutoNetCallback<WanAndroidResponse, List<WanAndroidEntity>> implements IAutoNetLocalOptCallBack<WanAndroidResponse> {

        @Override
        public WanAndroidResponse optLocalData(Map request) throws Exception {
            WanAndroidResponse response = new WanAndroidResponse();
            List<WanAndroidEntity> data = new ArrayList<>();
            WanAndroidEntity entity = new WanAndroidEntity();
            entity.setName("小稀革");
            entity.setOrder("本地测试数据");
            data.add(entity);
            response.setData(data);
            return response;
        }

        @Override
        public boolean handlerBefore(WanAndroidResponse wanAndroidResponse, FlowableEmitter<List<WanAndroidEntity>> emitter) {
            List<WanAndroidEntity> data = wanAndroidResponse.getData();
            if (data == null || data.isEmpty()) {
                emitter.onError(new EmptyError());
                return true;
            }
            emitter.onNext(data);
            return true;
        }

        @Override
        public void onSuccess(List<WanAndroidEntity> entity) {
            super.onSuccess(entity);
            Toast.makeText(MainActivity.this, entity.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(Throwable throwable) {
            Toast.makeText(MainActivity.this, "失败了： \n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEmpty() {
            Toast.makeText(MainActivity.this, "失败了： \n 数据为空", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {
            Toast.makeText(MainActivity.this, "结束", Toast.LENGTH_SHORT).show();
        }
    }

    @AutoNetStrategyAnontation(value = AutoNetStrategyAnontation.NetStrategy.LOCAL)
    @AutoNetDisposableHeadAnnontation(value = {
            "autonet: haoyong",
            "author: xiaoxige"
    })
    public class AnnotationLocalCallback implements IAutoNetLocalOptCallBack<String>, IAutoNetDataCallBack<String> {
        @Override
        public String optLocalData(Map request) throws Exception {
            return "本地数据： 请求参数：" + request.toString() + "， 本地数据：小稀革真帅！！！";
        }

        @Override
        public void onSuccess(String entity) {
            Toast.makeText(MainActivity.this, entity, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(Throwable throwable) {
            Toast.makeText(MainActivity.this, "失败了： \n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEmpty() {
            Toast.makeText(MainActivity.this, "失败了： \n 数据为空", Toast.LENGTH_SHORT).show();
        }
    }

    @AutoNetBaseUrlKeyAnontation(value = "pppig")
    @AutoNetAnontation(value = "/apk/downLoad/android_4.4.5.apk")
    @AutoNetTypeAnontation(resType = AutoNetTypeAnontation.Type.STREAM)
    public class AnnotationPullFile implements IAutoNetFileCallBack, IAutoNetDataCallBack<File>, IAutoNetComplete {

        @Override
        public void onProgress(float progress) {
            tvAnnotationPullFile.setText("下载文件(" + progress + ")");
            Log.i("TAG", "进度： " + progress);
        }

        @Override
        public void onComplete(File file) {
            Log.i("TAG", "下载文件完成!");
        }

        @Override
        public void onFailed(Throwable throwable) {
            Toast.makeText(MainActivity.this, "失败了： \n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEmpty() {
            Toast.makeText(MainActivity.this, "失败了： \n 数据为空", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(File file) {
            Toast.makeText(MainActivity.this, "文件下载成功了!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {
            tvAnnotationPullFile.setText("下载文件");
        }
    }

    @AutoNetBaseUrlKeyAnontation(value = "upFile")
    @AutoNetPatternAnontation(value = AutoNetPatternAnontation.NetPattern.POST)
    @AutoNetTypeAnontation(reqType = AutoNetTypeAnontation.Type.STREAM)
    public class AnnotationPushFile implements IAutoNetFileCallBack, IAutoNetDataCallBack<String>, IAutoNetComplete {

        @Override
        public void onProgress(float progress) {
            tvAnnotationPushFile.setText("发送文件（" + progress + ")");
        }

        @Override
        public void onComplete(File file) {
            Toast.makeText(MainActivity.this, "文件发送完成!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(String entity) {
            Toast.makeText(MainActivity.this, "文件发送成功后， 后台返回的数据：" + entity, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(Throwable throwable) {
            Toast.makeText(MainActivity.this, "失败了： \n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEmpty() {
            Toast.makeText(MainActivity.this, "失败了： \n 数据为空", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {
            tvAnnotationPushFile.setText("发送文件");
        }
    }


    /**
     * ---------------------------------------------------------------------------------------------
     * 异步相关回调
     */
    private class AsyNetCallback extends AbsAutoNetCallback<WanAndroidResponse, List<WanAndroidEntity>> {

        @Override
        public boolean handlerBefore(WanAndroidResponse wanAndroidResponse, FlowableEmitter<List<WanAndroidEntity>> emitter) {
            List<WanAndroidEntity> data = wanAndroidResponse.getData();
            if (data == null || data.isEmpty()) {
                emitter.onError(new EmptyError());
                return true;
            }
            emitter.onNext(data);
            return true;
        }

        @Override
        public void onSuccess(List<WanAndroidEntity> entity) {
            super.onSuccess(entity);
            Toast.makeText(MainActivity.this, entity.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(Throwable throwable) {
            Toast.makeText(MainActivity.this, "失败了： \n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEmpty() {
            Toast.makeText(MainActivity.this, "失败了： \n 数据为空", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete() {
            Log.i("TAG", "请求结束了");
        }
    }

    private class AsyLocalNetOrNetLocalOrLocalCallback implements IAutoNetDataCallBack<String>, IAutoNetLocalOptCallBack<String>, IAutoNetComplete {

        @Override
        public String optLocalData(Map request) throws Exception {
            return "本地数据， 小稀革真帅！";
        }

        @Override
        public void onSuccess(String entity) {
            Toast.makeText(MainActivity.this, entity, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed(Throwable throwable) {
            Toast.makeText(MainActivity.this, "失败了： \n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEmpty() {
            Toast.makeText(MainActivity.this, "失败了： \n 数据为空", Toast.LENGTH_SHORT).show();
        }


        @Override
        public void onComplete() {
            Toast.makeText(MainActivity.this, "结束", Toast.LENGTH_SHORT).show();
        }
    }

    private class AsyPushFileCallback implements IAutoNetDataCallBack<String>, IAutoNetFileCallBack, IAutoNetComplete {

        @Override
        public void onComplete() {
            tvChainAsyPushFile.setText("发送文件");
        }

        @Override
        public void onFailed(Throwable throwable) {
            Toast.makeText(MainActivity.this, "发送失败:" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEmpty() {
            Toast.makeText(MainActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(String entity) {
            Toast.makeText(MainActivity.this, "文件发送成功后， 后台返回的数据： " + entity, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProgress(float progress) {
            tvChainAsyPushFile.setText("发送文件（" + progress + ")");
        }

        @Override
        public void onComplete(File file) {
            Toast.makeText(MainActivity.this, "文件发送成功", Toast.LENGTH_SHORT).show();
        }
    }

    private class AsyPullFileCallback implements IAutoNetDataCallBack<File>, IAutoNetFileCallBack, IAutoNetComplete {

        @Override
        public void onComplete() {
            tvChainAsyPullFile.setText("下载文件");
        }

        @Override
        public void onFailed(Throwable throwable) {
            Toast.makeText(MainActivity.this, "接受失败:" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEmpty() {
            Toast.makeText(MainActivity.this, "数据为空", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(File entity) {
            Toast.makeText(MainActivity.this, "文件接受成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProgress(float progress) {
            tvChainAsyPullFile.setText("下载文件（" + progress + ")");
        }

        @Override
        public void onComplete(File file) {
            Toast.makeText(MainActivity.this, "文件下载完成", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * ---------------------------------------------------------------------------------------------
     * 其他
     */
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

    /**
     * ---------------------------------------------------------------------------------------------
     */
    private void runMainToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void runMainTextViewChangeShowText(final TextView textView, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(text);
            }
        });
    }

}
