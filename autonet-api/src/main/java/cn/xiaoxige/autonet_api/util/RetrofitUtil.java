package cn.xiaoxige.autonet_api.util;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import cn.xiaoxige.autonet_api.net.BaseApplicationInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * @author by zhuxiaoan on 2017/11/8 0008.
 *         retrofit的工具类
 */

public class RetrofitUtil {

    /**
     * 不加密
     * 链接时间为默认链接时间（5s）
     */
    public static Retrofit create(String baseUrl) {
        return create(baseUrl, false);
    }

    /**
     * 可以选择是否加密， 默认时间（5s）
     */
    public static Retrofit create(String baseUrl, boolean encryption) {
        return create(baseUrl, encryption, 5000, 5000, 5000);
    }

    public static Retrofit create(String baseUrl, boolean encryption, long connectTimeOut, long readTimeOut, long writeTimeOut) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client(encryption, connectTimeOut, readTimeOut, writeTimeOut))
                .build();
        return retrofit;
    }

    private static OkHttpClient client(boolean encryption, long connectTimeOut, long readTimeOut, long writeTimeOut) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.SECONDS)
                .addNetworkInterceptor(new BaseApplicationInterceptor(encryption))
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        return okHttpClient;
    }

}
