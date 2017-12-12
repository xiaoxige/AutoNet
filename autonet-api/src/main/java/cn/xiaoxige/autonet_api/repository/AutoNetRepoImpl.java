package cn.xiaoxige.autonet_api.repository;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;
import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;
import cn.xiaoxige.autonet_api.error.EmptyException;
import cn.xiaoxige.autonet_api.flowable.DefaultFlowable;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.util.DataConvertorUtils;
import cn.xiaoxige.autonet_api.util.OkHttpUtil;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

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
                resultUrl.append(mUrl).append("?");
                if (entity != null) {
                    Map<String, String> map = DataConvertorUtils.convertEntityToMap(entity, true);
                    Set<String> keySet = map.keySet();
                    for (String key : keySet) {
                        String value = map.get(key);
                        resultUrl.append(key + "=" + value);
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
    public Flowable doPullStreamGet(final File file) {
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(FlowableEmitter emitter) throws Exception {
                Request request = new Request.Builder()
                        .url(mUrl)
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
    public Flowable doPullStreamPost(final File file) {
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(FlowableEmitter emitter) throws Exception {
                Request request = new Request.Builder()
                        .url(mUrl)
                        .post(null)
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
    public Flowable doPushStreamGet(File file) {
        return null;
    }

    @Override
    public Flowable doPushStreamPost(File file) {
        return null;
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
}
