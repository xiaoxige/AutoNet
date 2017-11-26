package cn.xiaoxige.autonet_api;

import android.text.TextUtils;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
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

    public AutoNet init(AutoNetConfig config) {
        this.mConfig = config;
        return this;
    }

    public void setAutoNetEncryption(IAutoNetEncryptionCallback autoNetEncryptionCallback) {
        this.mAutoNetEncryptionCallback = autoNetEncryptionCallback;
    }

    public void startNet(IAutoNetDataCallback callback) {
        startNet(null, AutoNetPatternAnontation.NetPattern.GET, callback);
    }

    public void startNet(IRequestEntity requestEntity, AutoNetPatternAnontation.NetPattern pattern, IAutoNetDataCallback callback) {
        startNet(requestEntity, 5000, 5000, 5000, pattern, callback);
    }

    public void startNet(IRequestEntity requestEntity, long writeTime, long readTime, long connectOutTime,
                         AutoNetPatternAnontation.NetPattern pattern, IAutoNetDataCallback callback) {
        startNet(requestEntity, "/", writeTime, readTime, connectOutTime, false, pattern, callback);
    }

    public void startNet(IRequestEntity requestEntity, String url,
                         long writeTime, long readTime, long connectOutTime, boolean isEncryption,
                         AutoNetPatternAnontation.NetPattern pattern, IAutoNetDataCallback callback) {
        startNet(requestEntity, "default", url, writeTime, readTime, connectOutTime, isEncryption, pattern, callback);
    }

    public void startNet(IRequestEntity requestEntity, String baseUrlKey, String url,
                         long writeTime, long readTime, long connectOutTime, boolean isEncryption,
                         AutoNetPatternAnontation.NetPattern pattern, IAutoNetDataCallback callback) {
        startNet(requestEntity, baseUrlKey, url, writeTime, readTime, connectOutTime, isEncryption, pattern, null, callback);
    }

    public void startNet(IRequestEntity requestEntity, String baseUrlKey, String url,
                         long writeTime, long readTime, long connectOutTime, boolean isEncryption,
                         AutoNetPatternAnontation.NetPattern pattern,
                         FlowableTransformer transformer,
                         IAutoNetDataCallback callback) {
        String baseUrl = mConfig.getBaseUrl().get(baseUrlKey);
        if (TextUtils.isEmpty(baseUrl)) {
            throw new NullPointerException("BaseUrl is NULL.");
        }

        AutoNetPresenter presenter = new AutoNetPresenter(
                requestEntity, baseUrl, url,
                writeTime, readTime, connectOutTime,
                isEncryption,
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
