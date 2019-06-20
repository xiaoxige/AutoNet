package cn.xiaoxige.autonet;


import android.os.Bundle;
import android.util.Log;

import com.trello.rxlifecycle2.components.RxActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet.entity.MainEntity;
import cn.xiaoxige.autonet.entity.MainResponse;
import cn.xiaoxige.autonet.entity.TestResponseEntity;
import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.abstracts.AbsAutoNetCallback;
import cn.xiaoxige.autonet_api.error.EmptyError;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetComplete;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataBeforeCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataSuccessCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetLocalOptCallBack;
import cn.xiaoxige.autonet_api.subscriber.DefaultSubscriber;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends RxActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {


//                    String rs = AutoNet.getInstance().createNet()
//                            .setBaseUrl("https://zimg.pangpangpig.com")
//                            .doPost()
//                            .setReqType(AutoNetTypeAnontation.Type.STREAM)
//                            .setPushFileParams("upload", getCacheDir().getAbsolutePath() + File.separator + "AutoNet.png")
//                            .setAutoNetFileCallback(new IAutoNetFileCallBack() {
//                                @Override
//                                public void onProgress(float progress) {
//                                    Log.e("TAG", "progress = " + progress);
//                                }
//
//                                @Override
//                                public void onComplete(File file) {
//                                    Log.e("TAG", "");
//                                }
//                            })
//                            .synchronizationNet(String.class);
//                    Log.e("TAG", "");

                    MainResponse res = AutoNet.getInstance().createNet()
                            .setSuffixUrl("/wxarticle/chapters/json")
                            .doGet()
                            .setFlag(1)
                            .setNetStrategy(AutoNetStrategyAnontation.NetStrategy.NET)
                            .setAutoNetLocalOptCallback(new IAutoNetLocalOptCallBack<MainResponse>() {
                                @Override
                                public MainResponse optLocalData(Map request) {
                                    return null;
                                }
                            })
                            .synchronizationNet(MainResponse.class);
                    Log.e("TAG", "res = " + res);
                } catch (Exception e) {
                    e.printStackTrace();
                }


//                try {
//                    File file = AutoNet.getInstance().createNet().setBaseUrl("https://newpc.pangpangpig.com")
//                            .setSuffixUrl("/apk/downLoad/android_4.4.5.apk")
//                            .setAutoNetFileCallback(new IAutoNetFileCallBack() {
//                                @Override
//                                public void onProgress(float progress) {
//                                    Log.e("TAG", "progress = " + progress);
//                                }
//
//                                @Override
//                                public void onComplete(File file) {
//                                    Log.e("TAG", "成功");
//                                }
//                            }).setPullFileParams(getCacheDir().getAbsolutePath(), "test.apk")
//                            .setResType(AutoNetTypeAnontation.Type.STREAM)
//                            .synchronizationNet(File.class);
//                    Log.e("TAG", "file");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        }).start();


//        try {
//            TestResponseEntity testResponseEntity = AutoNet.getInstance().createNet().synchronizationNet(TestResponseEntity.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//

//        AutoNet.getInstance().createNet()
//                .setBaseUrl("https://newpc.pangpangpig.com")
//                .setSuffixUrl("/apk/downLoad/android_4.4.5.apk")
//                .setAutoNetFileCallback(new IAutoNetFileCallBack() {
//                    @Override
//                    public void onProgress(float progress) {
//                        Log.e("TAG", "progress = " + progress);
//                    }
//
//                    @Override
//                    public void onComplete(File file) {
//                        Log.e("TAG", "成功");
//                    }
//                }).setPullFileParams(getCacheDir().getAbsolutePath(), "test.apk")
//                .setResType(AutoNetTypeAnontation.Type.STREAM).
//                getFlowable(File.class)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new DefaultSubscriber() {
//                    @Override
//                    protected void defaultOnNext(Object entity) {
//                        super.defaultOnNext(entity);
//                    }
//
//                    @Override
//                    protected void defaultOnEmptyError() {
//                        super.defaultOnEmptyError();
//                    }
//
//                    @Override
//                    protected void defaultOnErrorWithNotEmpty(Throwable throwable) {
//                        super.defaultOnErrorWithNotEmpty(throwable);
//                    }
//
//                    @Override
//                    protected void defaultOnComplete() {
//                        super.defaultOnComplete();
//                    }
//                });


//        Flowable flowable = AutoNet.getInstance().createNet()
//                .setSuffixUrl("/wxarticle/chapters/json")
//                .doGet()
//                .getFlowable(MainResponse.class)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io());

//        flowable.subscribe(new DefaultSubscriber() {
//            @Override
//            protected void defaultOnNext(Object entity) {
//                super.defaultOnNext(entity);
//            }
//
//            @Override
//            protected void defaultOnEmptyError() {
//                super.defaultOnEmptyError();
//            }
//
//            @Override
//            protected void defaultOnErrorWithNotEmpty(Throwable throwable) {
//                super.defaultOnErrorWithNotEmpty(throwable);
//            }
//
//            @Override
//            protected void defaultOnComplete() {
//                super.defaultOnComplete();
//            }
//        });

        AutoNet.getInstance().createNet()
                .setSuffixUrl("/wxarticle/chapters/json")
                .doGet()
                .setFlag(1)
                .setNetStrategy(AutoNetStrategyAnontation.NetStrategy.NET)
                .start(new TestCallback());

//        AutoNet.getInstance().createNet()
//                .setBaseUrl("https://newpc.pangpangpig.com")
//                .setSuffixUrl("/apk/downLoad/android_4.4.5.apk")
//                .setResType(AutoNetTypeAnontation.Type.STREAM)
//                .setPullFileParams(getCacheDir().getAbsolutePath(), "test.apk")
//                .start(new TestCallback1());

//        Flowable flowable1 = AutoNet.getInstance().createNet()
//                .setBaseUrl("http://www.baidu.com")
//                .getFlowable(String.class)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io());
//
//        Flowable.zip(flowable1, flowable, new BiFunction<String, MainResponse, Object>() {
//            @Override
//            public Object apply(String o, MainResponse o2) throws Exception {
//                return "123456789";
//            }
//        }).subscribe(new DefaultSubscriber() {
//            @Override
//            protected void defaultOnNext(Object entity) {
//                super.defaultOnNext(entity);
//            }
//
//            @Override
//            protected void defaultOnEmptyError() {
//                super.defaultOnEmptyError();
//            }
//
//            @Override
//            protected void defaultOnErrorWithNotEmpty(Throwable throwable) {
//                super.defaultOnErrorWithNotEmpty(throwable);
//            }
//
//            @Override
//            protected void defaultOnComplete() {
//                super.defaultOnComplete();
//            }
//        });

    }

    private class TestCallback extends AbsAutoNetCallback<MainResponse, List<MainEntity>> implements IAutoNetLocalOptCallBack<MainResponse> {

        @Override
        public boolean handlerBefore(MainResponse mainResponse, FlowableEmitter<List<MainEntity>> emitter) {
            emitter.onNext(mainResponse.getData());
            return true;
        }

        @Override
        public void onSuccess(List<MainEntity> entity) {
            super.onSuccess(entity);

        }

        @Override
        public void onFailed(Throwable throwable) {
            super.onFailed(throwable);
        }

        @Override
        public void onEmpty() {
            super.onEmpty();
        }

        @Override
        public void onComplete() {
            super.onComplete();
        }

        @Override
        public MainResponse optLocalData(Map request) throws Exception {
            MainResponse mainResponse = new MainResponse();
            List<MainEntity> data = new ArrayList<>();
            mainResponse.setData(data);
            return mainResponse;
        }
    }

    private class TestCallback1 implements IAutoNetFileCallBack, IAutoNetComplete {

        @Override
        public void onProgress(float progress) {
            Log.e("TAG", "progress" + progress);
        }

        @Override
        public void onComplete(File file) {
            Log.e("TAG", "");
        }

        @Override
        public void onComplete() {
            Log.e("TAG", "fjdjklldas ");
        }
    }

}
