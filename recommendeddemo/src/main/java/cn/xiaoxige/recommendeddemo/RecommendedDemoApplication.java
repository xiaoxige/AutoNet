package cn.xiaoxige.recommendeddemo;

import android.app.Application;

import com.google.gson.Gson;

import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.error.CustomError;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetBodyCallBack;
import cn.xiaoxige.recommendeddemo.base.BaseResponse;
import cn.xiaoxige.recommendeddemo.constant.CommonConstant;
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
                .setDefaultDomainName(CommonConstant.BASE_URL)
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
            public boolean body(Object flag, String body) throws Exception {

                try {
                    BaseResponse baseResponse = new Gson().fromJson(body, BaseResponse.class);

                    if (baseResponse.isTokenInvalid()) {
                        handlerTokenInvalid();
                        return true;
                    }

                    // ...
                    // 其他自己需要处理的逻辑

                    if (flag != null && (Integer) flag == CommonConstant.FLAG_EXCLUDE_ERROR) {
                        // 这里就不再处理, 有可能需要在下游自行处理（eg: 如果zip, 一个接口为空了， 不会影响其他接口展示）
                        return false;
                    }

                    if (!baseResponse.isSuccess()) {
                        throw new CustomError(baseResponse.getErrorMsg());
                    }

                } catch (Exception e) {
                    throw e;
                }

                return false;
            }

        });
    }

    private void handlerTokenInvalid() {
        // 处理token失效的情况
        // 1. 清空用户本地数据并更新相应数据
        // 2. 跳转登录界面
        // ...
    }
}
