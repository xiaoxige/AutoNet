package cn.xiaoxige.autonet_api.repository.impl;

import java.util.List;
import java.util.Map;

import cn.xiaoxige.autonet_api.client.Client;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo;
import io.reactivex.Flowable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 */

public class AutoNetRepoImpl implements AutoNetRepo {

    private IAutoNetRequest request;
    private String url;
    private String mediaType;
    private String responseClazzName;
    private IAutoNetCallBack callBack;

    private OkHttpClient client;


    public AutoNetRepoImpl(IAutoNetRequest requestEntity, String extraDynamicParam,
                           String url, String mediaType,
                           Long writeOutTime, Long readOutTime, Long connectOutTime,
                           Long encryptionKey, Boolean isEncryption, List<Interceptor> interceptors, Map<String, String> heads,
                           String responseClazzName, IAutoNetEncryptionCallback encryptionCallback, IAutoNetCallBack callBack) {
        this.request = requestEntity;
        this.url = url;
        this.mediaType = mediaType;
        this.responseClazzName = responseClazzName;
        this.callBack = callBack;

        this.client = Client.client(extraDynamicParam, writeOutTime, readOutTime, connectOutTime, heads, encryptionKey, isEncryption, interceptors, encryptionCallback);
    }

    @Override
    public Flowable doNetGet() {
        return null;
    }

    @Override
    public Flowable doNetPost() {
        return null;
    }

    @Override
    public Flowable doPut() {
        return null;
    }

    @Override
    public Flowable doDelete() {
        return null;
    }

    @Override
    public Flowable doLocal() {
        return null;
    }

    @Override
    public Flowable pushFile() {
        return null;
    }

    @Override
    public Flowable pullFile() {
        return null;
    }

}
