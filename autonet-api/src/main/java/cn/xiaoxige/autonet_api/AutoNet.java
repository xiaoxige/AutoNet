package cn.xiaoxige.autonet_api;

import android.content.Context;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;

import java.util.HashMap;
import java.util.Map;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.presenter.AutoNetPresenter;
import io.reactivex.FlowableTransformer;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */
public class AutoNet {

    private static AutoNet mAutoNet;
    private AutoNetConfig mConfig;
    private IAutoNetEncryptionCallback mAutoNetEncryptionCallback;

    private AutoNet() {
    }

    public static AutoNet getInstance() {
        if (mAutoNet == null) {
            mAutoNet = new AutoNet();
        }
        return mAutoNet;
    }

    public AutoNet init(Context config, AutoNetConfig autoNetConfig) {
        this.mConfig = autoNetConfig;

        Stetho.initializeWithDefaults(config);

        return this;
    }

    public void updateOrInsertHeader(String key, String value) {
        Map map = new HashMap();
        map.put(key, value);
        updateOrInsertHeader(map);
    }

    public void updateOrInsertHeader(Map mapHead) {
        this.mConfig.updateOrInsertHeader(mapHead);
    }

    public void updateOrInsertGetDelParam(String key, String value) {
        Map map = new HashMap();
        map.put(key, value);
        updateOrInsertGetDelParam(map);
    }

    public void updateOrInsertGetDelParam(Map map) {
        this.mConfig.updateOrInsertGetDelParam(map);
    }

    public void setAutoNetEncryption(IAutoNetEncryptionCallback autoNetEncryptionCallback) {
        this.mAutoNetEncryptionCallback = autoNetEncryptionCallback;
    }

    /**
     * 不安全的请求
     *
     * @param requestEntity
     * @param responseEntityClass
     * @param baseUrlKey
     * @param url
     * @param writeTime
     * @param readTime
     * @param connectOutTime
     * @param isEncryption
     * @param encryptionKey
     * @param pattern
     * @param callback
     */
    public void startNet(IRequestEntity requestEntity, Class responseEntityClass, String baseUrlKey, String url,
                         long writeTime, long readTime, long connectOutTime, boolean isEncryption, long encryptionKey,
                         AutoNetPatternAnontation.NetPattern pattern,
                         IAutoNetDataCallback callback) {

        startNet(requestEntity, responseEntityClass, baseUrlKey, url, null,
                writeTime, readTime, connectOutTime, isEncryption, encryptionKey,
                pattern, AutoNetTypeAnontation.Type.JSON, AutoNetTypeAnontation.Type.JSON,
                null, callback);

    }

    /**
     * 安全的请求
     *
     * @param requestEntity
     * @param responseEntityClass
     * @param baseUrlKey
     * @param url
     * @param writeTime
     * @param readTime
     * @param connectOutTime
     * @param isEncryption
     * @param encryptionKey
     * @param pattern
     * @param transformer
     * @param callback
     */
    public void startNet(IRequestEntity requestEntity, Class responseEntityClass, String baseUrlKey, String url, String extraParam,
                         long writeTime, long readTime, long connectOutTime, boolean isEncryption, long encryptionKey,
                         AutoNetPatternAnontation.NetPattern pattern, AutoNetTypeAnontation.Type reqType, AutoNetTypeAnontation.Type resType,
                         FlowableTransformer transformer,
                         IAutoNetDataCallback callback) {
        String baseUrl = mConfig.getBaseUrl().get(baseUrlKey);
        if (TextUtils.isEmpty(baseUrl)) {
            throw new NullPointerException("BaseUrl is NULL.");
        }
        AutoNetPresenter presenter = new AutoNetPresenter(
                requestEntity, responseEntityClass, baseUrl, url, extraParam,
                writeTime, readTime, connectOutTime,
                isEncryption, encryptionKey,
                transformer, mConfig, mAutoNetEncryptionCallback, callback
        );

        if (pattern == AutoNetPatternAnontation.NetPattern.GET) {
            presenter.doGet();
        } else if (pattern == AutoNetPatternAnontation.NetPattern.POST) {
            presenter.doPost();
        } else if (pattern == AutoNetPatternAnontation.NetPattern.DELETE) {
            presenter.doDelete();
        } else if (pattern == AutoNetPatternAnontation.NetPattern.PUT) {
            presenter.doPut();
        } else {
            throw new RuntimeException("This pattern is not Support.");
        }
    }
}
