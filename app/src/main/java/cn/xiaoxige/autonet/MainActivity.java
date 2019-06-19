package cn.xiaoxige.autonet;


import android.os.Bundle;
import android.util.Log;

import com.trello.rxlifecycle2.components.RxActivity;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet.entity.MainResponse;
import cn.xiaoxige.autonet.entity.TestResponseEntity;
import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.abstracts.AbsAutoNetCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataBeforeCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetLocalOptCallBack;
import cn.xiaoxige.autonet_api.subscriber.DefaultSubscriber;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends RxActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    MainResponse res = AutoNet.getInstance().createNet()
//                            .setSuffixUrl("/wxarticle/chapters/json")
//                            .doGet()
//                            .setNetStrategy(AutoNetStrategyAnontation.NetStrategy.LOCAL)
//                            .setAutoNetLocalOptCallback(new IAutoNetLocalOptCallBack<MainResponse>() {
//                                @Override
//                                public MainResponse optLocalData(Map request) {
//                                    return null;
//                                }
//                            })
//                            .synchronizationNet(MainResponse.class);
//                    Log.e("TAG", "res = " + res);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


                try {
                    File file = AutoNet.getInstance().createNet().setBaseUrl("https://newpc.pangpangpig.com")
                            .setSuffixUrl("/apk/downLoad/android_4.4.5.apk")
                            .setAutoNetFileCallback(new IAutoNetFileCallBack() {
                                @Override
                                public void onProgress(float progress) {
                                    Log.e("TAG", "progress = " + progress);
                                }

                                @Override
                                public void onComplete(File file) {
                                    Log.e("TAG", "成功");
                                }
                            }).setPullFileParams(getCacheDir().getAbsolutePath(), "test.apk")
                            .setResType(AutoNetTypeAnontation.Type.STREAM)
                            .synchronizationNet(File.class);
                    Log.e("TAG", "file");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();


//        try {
//            TestResponseEntity testResponseEntity = AutoNet.getInstance().createNet().synchronizationNet(TestResponseEntity.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Flowable flowable = AutoNet.getInstance().createNet()
                .setSuffixUrl("/wxarticle/chapters/json")
                .doGet()
                .getFlowable(MainResponse.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());

        flowable.subscribe(new DefaultSubscriber<MainResponse>() {
            @Override
            protected void defaultOnNext(MainResponse entity) {
                super.defaultOnNext(entity);
            }

            @Override
            protected void defaultOnEmptyError() {
                super.defaultOnEmptyError();
            }

            @Override
            protected void defaultOnErrorWithNotEmpty(Throwable throwable) {
                super.defaultOnErrorWithNotEmpty(throwable);
            }

            @Override
            protected void defaultOnComplete() {
                super.defaultOnComplete();
            }
        });
//
//        AutoNet.getInstance().createNet().start(new TestCallback1());

    }

    private class TestCallback extends AbsAutoNetCallback<TestResponseEntity, List<String>> {

    }

    private class TestCallback1 implements IAutoNetDataCallBack<TestResponseEntity>, IAutoNetDataBeforeCallBack<String, List<String>> {

        @Override
        public void onFailed(Throwable throwable) {

        }

        @Override
        public void onEmpty() {

        }

        @Override
        public void onSuccess(TestResponseEntity entity) {

        }


        @Override
        public boolean handlerBefore(String s, FlowableEmitter<List<String>> emitter) {
            return false;
        }
    }

}
