package cn.xiaoxige.autonet_api.repository;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;
import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;
import cn.xiaoxige.autonet_api.error.EmptyException;
import cn.xiaoxige.autonet_api.flowable.DefaultFlowable;
import cn.xiaoxige.autonet_api.interfaces.AAutoNetStreamCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.util.DataConvertorUtils;
import cn.xiaoxige.autonet_api.util.OkHttpUtil;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public class AutoNetRepoImpl implements AutoNetRepo {

    private OkHttpClient mClient;
    private String mUrl;

    public AutoNetRepoImpl(AutoNetConfig config, boolean isencryption, long mencryptionkey, String url,
                           long writeTime, long readtime, long connectOutTime, IAutoNetEncryptionCallback autoNetEncryptionCallback) {
        mUrl = url;
        mClient
                = OkHttpUtil.start(isencryption, mencryptionkey, config, writeTime, readtime, connectOutTime, autoNetEncryptionCallback);

    }

    @Override
    public Flowable doGet(final IRequestEntity entity, final Class responseEntityClass) {
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe<AutoResponseEntity>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<AutoResponseEntity> emitter) throws Exception {
                StringBuffer resultUrl = new StringBuffer();
                resultUrl.append(mUrl);
                if (entity != null) {
                    Map<String, String> map = DataConvertorUtils.convertEntityToMap(entity, true);
                    Set<String> keySet = map.keySet();
                    int i = 0;
                    for (String key : keySet) {
                        if (i == 0) {
                            resultUrl.append("?");
                        } else {
                            resultUrl.append("&");
                        }
                        String value = map.get(key);
                        resultUrl.append(key + "=" + value);
                        i++;
                    }
                }

                Request request = new Request.Builder()
                        .url(resultUrl.toString())
                        .get()
                        .build();
                Response response = mClient.newCall(request).execute();
                if (response == null) {
                    emitter.onError(new EmptyException());
                    return;
                }
                String msg = response.body().string();
                AutoResponseEntity responseEntity = null;
                try {
                    responseEntity = (AutoResponseEntity) new Gson().fromJson(msg, responseEntityClass);
                } catch (Exception e) {
                }
                if (responseEntity == null) {
                    responseEntity = (AutoResponseEntity) responseEntityClass.newInstance();
                    responseEntity.isJsonTransformationError = true;
                }
                responseEntity.autoResponseResult = msg;
                emitter.onNext(responseEntity);
                emitter.onComplete();
            }
        });
        return flowable;
    }

    @Override
    public Flowable doPost(final IRequestEntity entity, final Class responseEntityClass) {

        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe<AutoResponseEntity>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<AutoResponseEntity> emitter) throws Exception {
                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                Gson gson = new Gson();
                String json = gson.toJson(entity);
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(mUrl)
                        .post(body)
                        .build();
                Response response = mClient.newCall(request).execute();
                if (response == null) {
                    emitter.onError(new EmptyException());
                    return;
                }
                String msg = response.body().string();
                AutoResponseEntity responseEntity = null;
                try {
                    responseEntity = (AutoResponseEntity) new Gson().fromJson(msg, responseEntityClass);
                } catch (Exception e) {
                }
                if (responseEntity == null) {
                    responseEntity = (AutoResponseEntity) responseEntityClass.newInstance();
                    responseEntity.isJsonTransformationError = true;
                }
                responseEntity.autoResponseResult = msg;
                emitter.onNext(responseEntity);
                emitter.onComplete();
            }
        });

        return flowable;
    }

    @Override
    public Flowable doDelete(final IRequestEntity entity, final Class responseEntityClass) {
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe<AutoResponseEntity>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<AutoResponseEntity> emitter) throws Exception {
                StringBuffer resultUrl = new StringBuffer();
                resultUrl.append(mUrl);
                if (entity != null) {
                    Map<String, String> map = DataConvertorUtils.convertEntityToMap(entity, true);
                    Set<String> keySet = map.keySet();
                    int i = 0;
                    for (String key : keySet) {
                        if (i == 0) {
                            resultUrl.append("?");
                        } else {
                            resultUrl.append("&");
                        }
                        String value = map.get(key);
                        resultUrl.append(key + "=" + value);
                        i++;
                    }
                }

                Request request = new Request.Builder()
                        .url(resultUrl.toString())
                        .delete()
                        .build();
                Response response = mClient.newCall(request).execute();
                if (response == null) {
                    emitter.onError(new EmptyException());
                    return;
                }
                String msg = response.body().string();
                AutoResponseEntity responseEntity = null;
                try {
                    responseEntity = (AutoResponseEntity) new Gson().fromJson(msg, responseEntityClass);
                } catch (Exception e) {
                }
                if (responseEntity == null) {
                    responseEntity = (AutoResponseEntity) responseEntityClass.newInstance();
                    responseEntity.isJsonTransformationError = true;
                }
                responseEntity.autoResponseResult = msg;
                emitter.onNext(responseEntity);
                emitter.onComplete();
            }
        });
        return flowable;
    }

    @Override
    public Flowable doPut(final IRequestEntity entity, final Class responseEntityClass) {

        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe<AutoResponseEntity>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<AutoResponseEntity> emitter) throws Exception {
                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                Gson gson = new Gson();
                String json = gson.toJson(entity);
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(mUrl)
                        .put(body)
                        .build();
                Response response = mClient.newCall(request).execute();
                if (response == null) {
                    emitter.onError(new EmptyException());
                    return;
                }
                String msg = response.body().string();
                AutoResponseEntity responseEntity = null;
                try {
                    responseEntity = (AutoResponseEntity) new Gson().fromJson(msg, responseEntityClass);
                } catch (Exception e) {
                }
                if (responseEntity == null) {
                    responseEntity = (AutoResponseEntity) responseEntityClass.newInstance();
                    responseEntity.isJsonTransformationError = true;
                }
                responseEntity.autoResponseResult = msg;
                emitter.onNext(responseEntity);
                emitter.onComplete();
            }
        });
        return flowable;
    }

    @Override
    public Flowable doPullStreamGet(final IRequestEntity requestEntity, final File file) {
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(FlowableEmitter emitter) throws Exception {
                StringBuffer resultUrl = new StringBuffer();
                resultUrl.append(mUrl);
                if (requestEntity != null) {
                    Map<String, String> map = DataConvertorUtils.convertEntityToMap(requestEntity, true);
                    Set<String> keySet = map.keySet();
                    int i = 0;
                    for (String key : keySet) {
                        if (i == 0) {
                            resultUrl.append("?");
                        } else {
                            resultUrl.append("&");
                        }
                        String value = map.get(key);
                        resultUrl.append(key + "=" + value);
                        i++;
                    }
                }
                Request request = new Request.Builder()
                        .url(resultUrl.toString())
                        .get()
                        .build();
                Response response = mClient.newCall(request).execute();
                if (response == null) {
                    emitter.onError(new EmptyException());
                    return;
                }
                recvFile(emitter, response, file);
                emitter.onComplete();
            }
        });
        return flowable;
    }

    @Override
    public Flowable doPullStreamPost(final IRequestEntity requestEntity, final File file) {

        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(FlowableEmitter emitter) throws Exception {
                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                Gson gson = new Gson();
                String json = gson.toJson(requestEntity);
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url(mUrl)
                        .post(body)
                        .build();
                Response response = mClient.newCall(request).execute();
                if (response == null) {
                    emitter.onError(new EmptyException());
                    return;
                }
                recvFile(emitter, response, file);
                emitter.onComplete();
            }
        });
        return flowable;
    }

    @Override
    public Flowable doPushStreamGet(IRequestEntity requestEntity, String mediaType, String fileKey, File file) {
        return doPushStreamPost(requestEntity, mediaType, fileKey, file);
    }

    @Override
    public Flowable doPushStreamPost(final IRequestEntity requestEntity, final String mediaType, final String fileKey, final File file) {
        final Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(@NonNull final FlowableEmitter emitter) throws Exception {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                //设置类型
                builder.setType(MultipartBody.FORM);
                if (requestEntity != null) {
                    Map<String, String> map = DataConvertorUtils.convertEntityToMap(requestEntity, true);
                    Set<String> keySet = map.keySet();
                    for (String key : keySet) {
                        builder.addFormDataPart(key, map.get(key));
                    }
                }
                builder.addFormDataPart(fileKey, file.getName(),
                        createProgressRequestBody(MediaType.parse(mediaType), file, new AAutoNetStreamCallback() {
                            @Override
                            public void onComplete(File file) {
                                emitter.onComplete();
                            }

                            @Override
                            public void onPregress(float progress) {
                                emitter.onNext(progress);
                            }

                            @Override
                            public void onEmpty() {
                                emitter.onError(new EmptyException());
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                emitter.onError(throwable);
                            }
                        }));
            }
        });
        return flowable;
    }

    /**
     * 下载文件
     *
     * @param emitter
     * @param response
     * @param file
     * @throws Exception
     */
    private void recvFile(FlowableEmitter emitter, Response response, File file) throws Exception {
        long fileSize = response.body().contentLength();
        InputStream is =
                response.body().byteStream();

        float preProgress = 0;
        float progress = 0;
        FileOutputStream fos = new FileOutputStream(file);
        int pullLength = 0;
        byte[] b = new byte[1024];
        int len;
        while ((len = is.read(b)) != -1) {
            fos.write(b, 0, len);
            pullLength += len;
            progress = (float) (pullLength * 100 / fileSize);
            if (preProgress != progress) {
                emitter.onNext(progress);
            }
            preProgress = progress;
        }
        fos.flush();
        fos.close();
        is.close();
    }

    /**
     * 创建带进度的RequestBody
     *
     * @param contentType MediaType
     * @param file        准备上传的文件
     * @param callBack    回调
     * @return
     */
    private RequestBody createProgressRequestBody(final MediaType contentType, final File file, final AAutoNetStreamCallback callBack) {

        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                source = Okio.source(file);
                Buffer buf = new Buffer();
                long remaining = contentLength();
                long current = 0;
                float preProgress = 0;
                float progress = 0;
                for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                    sink.write(buf, readCount);
                    current += readCount;
                    progress = current * 100 / remaining;
                    if (preProgress != progress) {
                        callBack.onPregress(progress);
                    }
                    preProgress = progress;
                }
                callBack.onSuccess(null);
                callBack.onComplete(file);
            }
        };
    }

}
