package cn.xiaoxige.autonet_api;

import android.content.Context;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;

import java.io.File;
import java.io.IOException;
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
        start(requestEntity, responseEntityClass, baseUrlKey, url, extraParam, null, writeTime, readTime, connectOutTime, isEncryption, encryptionKey,
                pattern, reqType, resType, transformer, callback);
    }

    public void startStream(IRequestEntity requestEntity, String path, String fileName, String baseUrlKey, String url,
                            long writeTime, long readTime, long connectOutTime,
                            AutoNetPatternAnontation.NetPattern pattern, AutoNetTypeAnontation.Type reqType, AutoNetTypeAnontation.Type resType,
                            FlowableTransformer transformer,
                            IAutoNetDataCallback callback) {

        if (reqType == AutoNetTypeAnontation.Type.STREAM && resType == AutoNetTypeAnontation.Type.STREAM) {
            throw new RuntimeException("sorry, AutoNet cann't know push or pull.");
        }

        if (reqType == AutoNetTypeAnontation.Type.STREAM &&
                (pattern == AutoNetPatternAnontation.NetPattern.DELETE
                        || pattern == AutoNetPatternAnontation.NetPattern.PUT
                        || pattern == AutoNetPatternAnontation.NetPattern.GET)) {
            throw new IllegalStateException("reqType -> stream type must post.");
        }

        if (resType == AutoNetTypeAnontation.Type.STREAM &&
                (pattern == AutoNetPatternAnontation.NetPattern.DELETE
                        || pattern == AutoNetPatternAnontation.NetPattern.PUT)) {
            throw new IllegalStateException("resType -> stream type must get or post");
        }

        File file = null;
        // 上传文件
        if (reqType == AutoNetTypeAnontation.Type.STREAM) {
            file = new File(path);
            if (!(file.exists() && file.isFile())) {
                throw new RuntimeException("pushFile is not exists or is not file.");
            }
        }

        // 下载文件
        if (resType == AutoNetTypeAnontation.Type.STREAM) {
            file = new File(path + File.separator + fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException("pull file is create error.\n" + e.getMessage());
                }
            }
        }

        start(requestEntity, null, baseUrlKey, url, null, file,
                writeTime, readTime, connectOutTime, false, 0, pattern, reqType, resType, transformer, callback);
    }


    /**
     * 母体
     *
     * @param requestEntity
     * @param responseEntityClass
     * @param baseUrlKey
     * @param url
     * @param extraParam
     * @param file
     * @param writeTime
     * @param readTime
     * @param connectOutTime
     * @param isEncryption
     * @param encryptionKey
     * @param pattern
     * @param reqType
     * @param resType
     * @param transformer
     * @param callback
     */
    private void start(IRequestEntity requestEntity, Class responseEntityClass, String baseUrlKey, String url, String extraParam,
                       File file,
                       long writeTime, long readTime, long connectOutTime, boolean isEncryption, long encryptionKey,
                       AutoNetPatternAnontation.NetPattern pattern, AutoNetTypeAnontation.Type reqType, AutoNetTypeAnontation.Type resType,
                       FlowableTransformer transformer,
                       IAutoNetDataCallback callback) {
        String baseUrl = mConfig.getBaseUrl().get(baseUrlKey);
        if (TextUtils.isEmpty(baseUrl)) {
            throw new NullPointerException("BaseUrl is NULL.");
        }

        AutoNetPresenter presenter = new AutoNetPresenter(
                requestEntity, responseEntityClass, baseUrl, url, extraParam, file,
                writeTime, readTime, connectOutTime,
                isEncryption, encryptionKey, reqType, resType,
                transformer, mConfig, mAutoNetEncryptionCallback, callback
        );

        if (reqType == AutoNetTypeAnontation.Type.STREAM) {
            startPushStream(presenter, pattern);
        } else if (resType == AutoNetTypeAnontation.Type.STREAM) {
            startPullStream(presenter, pattern);
        } else {
            startJson(presenter, pattern);
        }
    }

    /**
     * 发送文件
     *
     * @param presenter
     * @param pattern
     */
    private void startPushStream(AutoNetPresenter presenter, AutoNetPatternAnontation.NetPattern pattern) {

        if (pattern == AutoNetPatternAnontation.NetPattern.GET) {
            /**
             * 上传文件暂不会走到get请求中...(上面已经做判断)
             */
            presenter.doPushGet();
        } else if (pattern == AutoNetPatternAnontation.NetPattern.POST) {
            presenter.doPushPost();
        }
    }

    /**
     * 接受文件
     *
     * @param presenter
     * @param pattern
     */
    private void startPullStream(AutoNetPresenter presenter, AutoNetPatternAnontation.NetPattern pattern) {

        if (pattern == AutoNetPatternAnontation.NetPattern.GET) {
            presenter.doPullStreamGet();
        } else if (pattern == AutoNetPatternAnontation.NetPattern.POST) {
            presenter.doPullStreamPost();
        }

    }

    /**
     * 发送Json请求
     *
     * @param presenter
     * @param pattern
     */
    private void startJson(AutoNetPresenter presenter, AutoNetPatternAnontation.NetPattern pattern) {
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
