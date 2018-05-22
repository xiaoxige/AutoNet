package cn.xiaoxige.autonet_api.repository.impl;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.xiaoxige.autonet_api.client.Client;
import cn.xiaoxige.autonet_api.constant.AutoNetConstant;
import cn.xiaoxige.autonet_api.error.EmptyError;
import cn.xiaoxige.autonet_api.flowable.DefaultFlowable;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetHeadCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetLocalOptCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest;
import cn.xiaoxige.autonet_api.net.ProgressRequestBody;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo;
import cn.xiaoxige.autonet_api.util.DataConvertorUtils;
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
        //noinspection UnnecessaryLocalVariable
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(FlowableEmitter emitter) throws Exception {
                if (callBack instanceof IAutoNetLocalOptCallBack) {
                    Object object = ((IAutoNetLocalOptCallBack) callBack).optLocalData(requestEntity);
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
                if (requestEntity != null) {
                    Map<String, String> params = DataConvertorUtils.convertEntityToMap(requestEntity, true);
                    Set<String> keys = params.keySet();
                    for (String key : keys) {
                        builder.addFormDataPart(key, params.get(key));
                    }
                }

                File file = new File(filePath);
                builder.addFormDataPart(pushFileKey, file.getName(),
                        ProgressRequestBody.createProgressRequestBody(MediaType.parse(mediaType), file, new IAutoNetFileCallBack() {
                            @Override
                            public void onPregress(float progress) {
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
                String json = new Gson().toJson(requestEntity);
                RequestBody body = RequestBody.create(MediaType.parse(mediaType), json);
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
        int pullLength = 0;
        byte[] b = new byte[AutoNetConstant.DEFAULT_BYBE_SIZE];
        int len;
        while ((len = is.read(b)) != -1) {
            fos.write(b, 0, len);
            pullLength += len;
            progress = (pullLength * AutoNetConstant.MAX_PROGRESS / fileSize);
            if (preProgress != progress) {
                //noinspection unchecked
                emitter.onNext(progress);
            }
            preProgress = progress;
        }
        //noinspection unchecked
        emitter.onNext(file);
        fos.flush();
        fos.close();
        is.close();
    }

}
