package cn.xiaoxige.autonet_api;

import java.util.List;
import java.util.Map;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.autonet_api.client.Client;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest;
import io.reactivex.FlowableTransformer;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @author by xiaoxige on 2018/5/20.
 *         The executor of AutoNet
 */

public class AutoNetExecutor {

    private IAutoNetRequest requestEntity;
    private String responseClazzName;
    private String mediaType;
    private FlowableTransformer transformer;
    private IAutoNetCallBack callBack;

    private OkHttpClient client;
    private Request.Builder requestBuilder;

    public AutoNetExecutor(IAutoNetRequest requestEntity, String extraDynamicParam,
                           String url, String mediaType,
                           Long writeOutTime, Long readOutTime, Long connectOutTime,
                           Long encryptionKey, Boolean isEncryption, List<Interceptor> interceptors, Map<String, String> heads,
                           String responseClazzName, FlowableTransformer transformer, IAutoNetEncryptionCallback encryptionCallback, IAutoNetCallBack callBack) {
        this.requestEntity = requestEntity;
        this.responseClazzName = responseClazzName;
        this.mediaType = mediaType;
        this.transformer = transformer;
        this.callBack = callBack;

        this.client = Client.client(extraDynamicParam, writeOutTime, readOutTime, connectOutTime, heads, encryptionKey, isEncryption, interceptors, encryptionCallback);
        requestBuilder = new Request.Builder().url(url);
    }

    public void doNetGet() {
        Request request = requestBuilder.get().build();
    }

    public void doNetPost() {
    }

    public void doNetDelete() {

    }

    public void doNetPut() {

    }

    public void doLocal() {

    }

    public void doLocalNet(AutoNetPatternAnontation.NetPattern netPattern) {

    }

    public void doNetLocal(AutoNetPatternAnontation.NetPattern netPattern) {

    }

    public void pushFile(String pushFileKey, String filePath) {

    }

    public void pullFile(String filePath, String fileName) {

    }

}
