package cn.xiaoxige.autonet_api.repository;

import java.util.Map;
import java.util.Set;

import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;
import cn.xiaoxige.autonet_api.error.EmptyException;
import cn.xiaoxige.autonet_api.flowable.DefaultFlowable;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.util.DataConvertorUtils;
import cn.xiaoxige.autonet_api.util.OkHttpUtil;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public class AutoNetRepoImpl implements AutoNetRepo {

    private OkHttpClient mClient;
    private String mUrl;

    public AutoNetRepoImpl(AutoNetConfig config, boolean isencryption, String url,
                           long writeTime, long readtime, long connectOutTime, IAutoNetEncryptionCallback autoNetEncryptionCallback) {
        mUrl = url;
        mClient
                = OkHttpUtil.start(isencryption, config, writeTime, readtime, connectOutTime, autoNetEncryptionCallback);

    }

    @Override
    public Flowable doGet(final IRequestEntity entity) {
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(@NonNull FlowableEmitter emitter) throws Exception {
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
                        .get()
                        .build();
                Response response = mClient.newCall(request).execute();
                if (response == null) {
                    emitter.onError(new EmptyException());
                }
                String msg = response.body().string();
                // TODO: 2017/11/27 onnext???
                emitter.onComplete();
            }
        });
        return flowable;
    }

    @Override
    public Flowable doPost(IRequestEntity entity) {

        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(@NonNull FlowableEmitter emitter) throws Exception {
                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                // TODO: 2017/11/27 json ???
                RequestBody body = RequestBody.create(JSON, "");
                Request request = new Request.Builder()
                        .url(mUrl)
                        .post(body)
                        .build();
                Response response = mClient.newCall(request).execute();
                if (response == null) {
                    emitter.onError(new EmptyException());
                }
                String msg = response.body().string();
                // TODO: 2017/11/27 onnext ???
                emitter.onComplete();
            }
        });

        return flowable;
    }

    @Override
    public Flowable doDelete(final IRequestEntity entity) {
        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(@NonNull FlowableEmitter emitter) throws Exception {
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
                }
                String msg = response.body().string();
                // TODO: 2017/11/27 onnext???
                emitter.onComplete();
            }
        });
        return flowable;
    }

    @Override
    public Flowable doPut(IRequestEntity entity) {

        Flowable flowable = DefaultFlowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(@NonNull FlowableEmitter emitter) throws Exception {
                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                // TODO: 2017/11/27 json ???
                RequestBody body = RequestBody.create(JSON, "");
                Request request = new Request.Builder()
                        .url(mUrl)
                        .put(body)
                        .build();
                Response response = mClient.newCall(request).execute();
                if (response == null) {
                    emitter.onError(new EmptyException());
                }
                String msg = response.body().string();
                // TODO: 2017/11/27 onnext ???
                emitter.onComplete();
            }
        });
        return flowable;
    }
}
