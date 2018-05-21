package cn.xiaoxige.autonet_api.repository.impl;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.xiaoxige.autonet_api.client.Client;
import cn.xiaoxige.autonet_api.error.EmptyError;
import cn.xiaoxige.autonet_api.flowable.DefaultFlowable;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetHeadCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo;
import cn.xiaoxige.autonet_api.util.DataConvertorUtils;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 */

public class AutoNetRepoImpl implements AutoNetRepo {

    private IAutoNetRequest requestEntity;
    private String url;
    private String mediaType;
    private String responseClazzName;
    private IAutoNetCallBack callBack;

    private OkHttpClient client;


    public AutoNetRepoImpl(IAutoNetRequest requestEntity, String extraDynamicParam,
                           String url, String mediaType,
                           Long writeOutTime, Long readOutTime, Long connectOutTime,
                           Long encryptionKey, Boolean isEncryption, List<Interceptor> interceptors, Map<String, String> heads,
                           String responseClazzName, IAutoNetEncryptionCallback encryptionCallback, IAutoNetHeadCallBack headCallBack, IAutoNetCallBack callBack) {
        this.requestEntity = requestEntity;
        this.url = url;
        this.mediaType = mediaType;
        this.responseClazzName = responseClazzName;
        this.callBack = callBack;

        this.client = Client.client(extraDynamicParam, writeOutTime, readOutTime, connectOutTime, heads, encryptionKey, isEncryption, interceptors, encryptionCallback, headCallBack);
    }

    @Override
    public Flowable doNetGet() {

        //noinspection UnnecessaryLocalVariable
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(FlowableEmitter emitter) throws Exception {
                String newUrl = restructureUrlWithParams(url, requestEntity);
                Request request = new Request.Builder()
                        .get()
                        .url(newUrl)
                        .build();
                executeNet(request, emitter);
            }
        });
        return flowable;
    }

    @Override
    public Flowable doNetPost() {
        //noinspection UnnecessaryLocalVariable
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(FlowableEmitter emitter) throws Exception {
                String json = new Gson().toJson(requestEntity);
                RequestBody body = RequestBody.create(MediaType.parse(mediaType), json);
                Request request = new Request.Builder().url(url).post(body).build();
                executeNet(request, emitter);
            }
        });
        return flowable;
    }

    @Override
    public Flowable doPut() {
        //noinspection UnnecessaryLocalVariable
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(FlowableEmitter emitter) throws Exception {
                String json = new Gson().toJson(requestEntity);
                RequestBody body = RequestBody.create(MediaType.parse(mediaType), json);
                Request request = new Request.Builder().url(url).put(body).build();
                executeNet(request, emitter);
            }
        });
        return flowable;
    }

    @Override
    public Flowable doDelete() {
        //noinspection UnnecessaryLocalVariable
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(FlowableEmitter emitter) throws Exception {
                String newUrl = restructureUrlWithParams(url, requestEntity);
                Request request = new Request.Builder()
                        .delete()
                        .url(newUrl)
                        .build();
                executeNet(request, emitter);
            }
        });
        return flowable;
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

    private void executeNet(Request request, FlowableEmitter emitter) throws Exception {
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        if (body == null) {
            emitter.onError(new EmptyError());
        }
        //noinspection ConstantConditions
        String content = body.string();
        if (TextUtils.isEmpty(content)) {
            emitter.onError(new EmptyError());
        }

        try {
            if (TextUtils.isEmpty(responseClazzName)
                    || String.class.getCanonicalName().equals(responseClazzName)
                    || Object.class.getCanonicalName().equals(responseClazzName)) {
                //noinspection SingleStatementInBlock,unchecked
                emitter.onNext(content);
            } else {

                Class<?> clazz = Class.forName(responseClazzName);
                Object object = new Gson().fromJson(content, clazz);
                //noinspection unchecked
                emitter.onNext(object);
            }

        } catch (Exception e) {
            //noinspection unchecked
            emitter.onError(e);
        } finally {
            emitter.onComplete();
        }
    }

    private String restructureUrlWithParams(String url, IAutoNetRequest request) {
        if (request == null) {
            return url;
        }
        Map<String, String> params = DataConvertorUtils.convertEntityToMap(request, true);
        if (params == null || params.size() <= 0) {
            return url;
        }

        Set<String> keys =
                params.keySet();
        //noinspection StringBufferMayBeStringBuilder
        StringBuffer paramsBuffer = new StringBuffer(url);
        int i = 0;
        for (String key : keys) {
            if (i == 0) {
                paramsBuffer.append("?");
            } else {
                paramsBuffer.append("&");
            }
            String value = params.get(key);
            paramsBuffer.append(key + "=" + value);
            i++;
        }
        url = paramsBuffer.toString();
        if (url.endsWith("&")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

}
