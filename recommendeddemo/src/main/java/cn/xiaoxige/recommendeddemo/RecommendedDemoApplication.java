package cn.xiaoxige.recommendeddemo;

import android.app.Application;

import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetBodyCallBack;
import io.reactivex.FlowableEmitter;

/**
 * @author by zhuxiaoan on 2018/12/17 0017.
 */
public class RecommendedDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        initAutoNet();

    }

    private void initAutoNet() {

        AutoNetConfig config = new AutoNetConfig.Builder()
                // 只有在debug状态下在开启stetho
                .isOpenStetho(BuildConfig.DEBUG)

                .build();

        /****
         * 推荐使用一:
         * 1. 监听全局body回调， 默认返回false不拦截， 使用AutoNet自动处理
         * 2. 根据实际后台指定的json规范， 封装BaseResponse, 其中code及message统一在body中处理
         * 3. 可以根据实际情况抛出异常并拦截， 所有的异常情况都会回调到相应的onError中， 可以通过判断类型去处理相应的错误（可参考这里的方式）
         * 推荐使用二：
         * 如果自己处理上游流下来的结果，比如使用zip等等， 可以在获取上游指定flag， 并在这里通过flag进行排除
         */
        AutoNet.getInstance().initAutoNet(this, config).setBodyCallback(new IAutoNetBodyCallBack() {
            @Override
            public boolean body(Object flag, String response, FlowableEmitter emitter) {

                return false;
            }
        });
    }
}
