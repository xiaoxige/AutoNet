package cn.xiaoxige.autonet_api.util;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.net.BaseApplicationInterceptor;
import okhttp3.OkHttpClient;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public class OkHttpUtil {

    public static OkHttpClient start(boolean encryption, AutoNetConfig config, IAutoNetEncryptionCallback autoNetEncryption) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new BaseApplicationInterceptor(encryption, config, autoNetEncryption))
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        return client;
    }
}
