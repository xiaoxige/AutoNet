package cn.xiaoxige.autonet;

import android.app.Application;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import cn.xiaoxige.autonet.entity.BaseResponse;
import cn.xiaoxige.autonet_api.AutoNet;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.error.CustomError;
import cn.xiaoxige.autonet_api.error.EmptyError;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetBodyCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetHeadCallBack;
import io.reactivex.FlowableEmitter;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.Headers;


/**
 * Created by 小稀革 on 2017/11/26.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
            }
        });

        Map<String, Object> heads = new ArrayMap<>();
        heads.put("token", "0");
        heads.put("userId", "A");

        Map<String, String> domainNames = new ArrayMap<>();
        domainNames.put("pppig", "https://newpc.pangpangpig.com");
        domainNames.put("upFile", "https://zimg.pangpangpig.com");
        domainNames.put("wanandroid", "http://www.wanandroid.com");

        AutoNetConfig config = new AutoNetConfig.Builder()
                .isOpenStetho(true)
                .setDefaultDomainName("http://www.baidu.com")
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
            public void head(Object flag, Headers headers) throws IOException {
                Log.e("TAG", "flag = " + flag);
                Log.e("TAG", "头部回调：" + headers);
            }
        }).setBodyCallback(new IAutoNetBodyCallBack() {

            @Override
            public boolean body(Object flag, int i, String body) throws Exception {
                Log.e("TAG", "flag = " + flag);
                Log.e("TAG", "body： " + body);

                if (flag instanceof Integer) {
                    if ((int) flag == 666) {
                        return false;
                    }
                }

                if (flag instanceof Integer) {
                    if ((int) flag == 1) {
                        throw new CustomError("flag 标志 为1， 我自动进行了拦截， 并给你想要的返回了错误...");
                    }
                }

//                BaseResponse baseResponse = new Gson().fromJson(body, BaseResponse.class);
//                if (!baseResponse.isTokenInvalid()) {
//                    // token 失效, 返回 true自己进行处理
//                    handleTokenInvalid();
//                    return true;
//                }
//                if (!baseResponse.isSuccess()) {
//                    // 返回系统返回的错误， 直接到对应接口的onFi里
//                    throw new CustomError(baseResponse.getErrorMsg());
//                }

                return false;
            }

        }).updateOrInsertDomainNames("jsonTestBaseUrl", "http://api.news18a.com");


    }

    private void handleTokenInvalid() {

    }
}
