package cn.xiaoxige.autonet_api.repository.impl;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.client.Client;
import cn.xiaoxige.autonet_api.constant.AutoNetConstant;
import cn.xiaoxige.autonet_api.error.EmptyError;
import cn.xiaoxige.autonet_api.flowable.DefaultFlowable;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetBodyCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataBeforeCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetHeadCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetLocalOptCallBack;
import cn.xiaoxige.autonet_api.net.ProgressRequestBody;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo1;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 */

public class AutoNetRepoImpl1 implements AutoNetRepo1 {

    private Object mFlag;
    private Map requestParams;
    private String url;
    private String mediaType;
    private String responseClazzName;
    private AutoNetTypeAnontation.Type reqType;
    private IAutoNetCallBack callBack;
    private IAutoNetBodyCallBack bodyCallBack;

    private OkHttpClient client;


    public AutoNetRepoImpl1(Object flag, Map requestParams, String extraDynamicParam,
                            String url, String mediaType,
                            Long writeOutTime, Long readOutTime, Long connectOutTime,
                            Long encryptionKey, Boolean isEncryption, List<Interceptor> interceptors, Map<String, Object> heads,
                            String responseClazzName, AutoNetTypeAnontation.Type reqType, IAutoNetEncryptionCallback encryptionCallback, IAutoNetHeadCallBack headCallBack, IAutoNetBodyCallBack bodyCallBack, IAutoNetCallBack callBack) {
        this.mFlag = flag;
        this.requestParams = requestParams;
        this.url = url;
        this.mediaType = mediaType;
        this.responseClazzName = responseClazzName;
        this.reqType = reqType;
        this.callBack = callBack;
        this.bodyCallBack = bodyCallBack;

        this.client = Client.client(flag, heads, writeOutTime, readOutTime, connectOutTime, encryptionKey, isEncryption, interceptors, encryptionCallback, headCallBack);
    }

    @Override
    public Flowable doNetGet() {

        //noinspection UnnecessaryLocalVariable
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(FlowableEmitter emitter) throws Exception {
                String newUrl = restructureUrlWithParams(url, requestParams);
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
                String bodyParams = structureBodyParams(reqType, requestParams);
                RequestBody body = RequestBody.create(MediaType.parse(mediaType), bodyParams);
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
                String bodyParams = structureBodyParams(reqType, requestParams);
                RequestBody body = RequestBody.create(MediaType.parse(mediaType), bodyParams);
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
                String newUrl = restructureUrlWithParams(url, requestParams);
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
        //noinspection UnnecessaryLocalVariable
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(FlowableEmitter emitter) throws Exception {
                if (callBack instanceof IAutoNetLocalOptCallBack) {
                    Object object = ((IAutoNetLocalOptCallBack) callBack).optLocalData(requestParams);
                    if (object == null) {
                        emitter.onError(new EmptyError());
                    } else {
                        //noinspection unchecked
                        emitter.onNext(object);
                    }
                } else {
                    emitter.onError(new EmptyError());
                }

                emitter.onComplete();
            }
        });
        return flowable;
    }

    @Override
    public Flowable pushFile(final String pushFileKey, final String filePath) {
        //noinspection UnnecessaryLocalVariable
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final FlowableEmitter emitter) throws Exception {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                if (requestParams != null) {
                    Set<String> keys = requestParams.keySet();
                    for (String key : keys) {
                        builder.addFormDataPart(key, (String) requestParams.get(key));
                    }
                }

                File file = new File(filePath);
                if (!file.exists()) {
                    emitter.onError(new IllegalArgumentException("File uploading files do not exist."));
                    return;
                }
                builder.addFormDataPart(pushFileKey, file.getName(),
                        ProgressRequestBody.createProgressRequestBody(MediaType.parse(mediaType), file, new IAutoNetFileCallBack() {
                            @Override
                            public void onProgress(float progress) {
                                //noinspection unchecked
                                emitter.onNext(progress);
                            }

                            @Override
                            public void onComplete(File file) {
                                //noinspection unchecked
                                emitter.onNext(file);
                            }
                        }));

                Request request = new Request.Builder().url(url).post(builder.build())
                        .build();
                executeNet(request, emitter);
            }
        });
        return flowable;
    }

    @Override
    public Flowable pullFile(final String filePath, final String fileName) {
        //noinspection UnnecessaryLocalVariable
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(FlowableEmitter emitter) throws Exception {
                String bodyParams = structureBodyParams(reqType, requestParams);
                RequestBody body = RequestBody.create(MediaType.parse(mediaType), bodyParams);
                Request request = new Request.Builder().url(url).post(body).build();
                Response response = client.newCall(request).execute();
                if (response == null) {
                    emitter.onError(new EmptyError());
                    //noinspection UnnecessaryReturnStatement
                    return;
                }

                File file = new File(filePath);
                if (!file.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    file.mkdirs();
                }
                file = new File(filePath + File.separator + fileName);
                if (!file.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    file.createNewFile();
                }

                recvFile(emitter, response, file);
                emitter.onComplete();
            }
        });
        return flowable;
    }

    private void executeNet(Request request, FlowableEmitter emitter) throws Exception {
        Response response = client.newCall(request).execute();
        ResponseBody body = response.body();
        if (body == null) {
            emitter.onError(new EmptyError());
        }
        //noinspection ConstantConditions
        String content = body.string();

        if (bodyCallBack != null) {
            boolean isContinue
                    = bodyCallBack.body(this.mFlag, content);
            if (isContinue) {
                return;
            }
        }

        if (TextUtils.isEmpty(content)) {
            emitter.onError(new EmptyError());
        }

        try {
            if (TextUtils.isEmpty(responseClazzName)
                    || String.class.getCanonicalName().equals(responseClazzName)
                    || Object.class.getCanonicalName().equals(responseClazzName)) {
                if (callBack != null && callBack instanceof IAutoNetDataBeforeCallBack) {
                    boolean isStop = ((IAutoNetDataBeforeCallBack) callBack).handlerBefore(content, emitter);
                    if (isStop) {
                        return;
                    }
                }
                //noinspection SingleStatementInBlock,unchecked
                emitter.onNext(content);
            } else {

                Class<?> clazz = Class.forName(responseClazzName);
                Object object = new Gson().fromJson(content, clazz);
                if (callBack != null && callBack instanceof IAutoNetDataBeforeCallBack) {
                    boolean isStop = ((IAutoNetDataBeforeCallBack) callBack).handlerBefore(object, emitter);
                    if (isStop) {
                        return;
                    }
                }
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

    /**
     * Parameter processing (mainly for Get, Delete requests)
     *
     * @param url
     * @param params
     * @return
     */
    private String restructureUrlWithParams(String url, Map params) {
        if (params == null) {
            return url;
        }

        if (params.size() <= 0) {
            return url;
        }

        //noinspection unchecked
        Set<String> keys =
                params.keySet();
        //noinspection StringBufferMayBeStringBuilder
        StringBuffer paramsBuffer = new StringBuffer(url);
        int i = 0;
        for (String key : keys) {
            if (i == 0 && !url.contains("?")) {
                paramsBuffer.append("?");
            } else {
                paramsBuffer.append("&");
            }
            Object value = params.get(key);
            paramsBuffer.append(key + "=" + value);
            i++;
        }
        url = paramsBuffer.toString();
        if (url.endsWith("&")) {
            url = url.substring(0, url.length() - 1);
        }
        return url;
    }

    /**
     * Parameter processing (mainly for Post, put requests)
     *
     * @param reqType
     * @param requestParams
     * @return
     */
    private String structureBodyParams(AutoNetTypeAnontation.Type reqType, Map requestParams) {
        if (reqType == AutoNetTypeAnontation.Type.JSON) {
            return new Gson().toJson(requestParams);
        } else if (reqType == AutoNetTypeAnontation.Type.FORM) {
            return structureFormParams(requestParams);
        } else {
            // Using JSON format by default
            return new Gson().toJson(requestParams);
        }
    }

    /**
     * Structure form parameter
     *
     * @param requestParams
     * @return
     */
    private String structureFormParams(Map requestParams) {
        if (requestParams == null || requestParams.size() <= 0) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        Set<String> keys = requestParams.keySet();
        for (String key : keys) {
            buffer.append(key + "=" + requestParams.get(key) + "&");
        }
        String bodyParams = buffer.toString();
        if (bodyParams.endsWith("&")) {
            bodyParams = bodyParams.substring(0, bodyParams.length() - 1);
        }
        return bodyParams;
    }

    /**
     * Download the file
     *
     * @param emitter
     * @param response
     * @param file
     * @throws Exception
     */
    private void recvFile(FlowableEmitter emitter, Response response, File file) throws Exception {
        //noinspection ConstantConditions
        long fileSize = response.body().contentLength();
        //noinspection ConstantConditions
        InputStream is =
                response.body().byteStream();

        float preProgress = 0;
        float progress;
        FileOutputStream fos = new FileOutputStream(file);
        long pullLength = 0;
        byte[] b = new byte[AutoNetConstant.DEFAULT_BYBE_SIZE];
        int len;
        while ((len = is.read(b)) != -1) {
            fos.write(b, 0, len);
            pullLength += len;
            progress = (int) (pullLength * AutoNetConstant.MAX_PROGRESS / fileSize);
            if (preProgress != progress && Math.abs(progress - preProgress) >= 1) {
                //noinspection unchecked
                emitter.onNext(progress);
                preProgress = progress;
            }
        }
        //noinspection unchecked
        emitter.onNext(file);
        fos.flush();
        fos.close();
        is.close();
    }


}
