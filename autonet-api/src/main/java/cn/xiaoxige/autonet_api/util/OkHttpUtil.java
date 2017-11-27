package cn.xiaoxige.autonet_api.util;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.net.BaseApplicationInterceptor;
import okhttp3.OkHttpClient;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public class OkHttpUtil {


    public static OkHttpClient start(boolean isencryption, long mencryptionkey, AutoNetConfig config,
                                     long writeTime, long readtime, long connectOutTime,
                                     IAutoNetEncryptionCallback autoNetEncryptionCallback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new BaseApplicationInterceptor(isencryption, mencryptionkey, config, autoNetEncryptionCallback))
                .addNetworkInterceptor(new StethoInterceptor())
                .writeTimeout(writeTime, TimeUnit.SECONDS)
                .readTimeout(readtime, TimeUnit.SECONDS)
                .connectTimeout(connectOutTime, TimeUnit.SECONDS)
                .build();
        return client;
    }
}
