package cn.xiaoxige.autonet_api;

import android.util.Log;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;

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
        startNet(AutoNetPatternAnontation.NetPattern.GET, callback);
    }

    public void startNet(AutoNetPatternAnontation.NetPattern pattern, IAutoNetDataCallback callback) {
        startNet(5000, 5000, 5000, pattern, callback);
    }

    public void startNet(long writeTime, long readTime, long connectOutTime,
                         AutoNetPatternAnontation.NetPattern pattern, IAutoNetDataCallback callback) {
        startNet("/", writeTime, readTime, connectOutTime, false, pattern, callback);
    }

    public void startNet(String url,
                         long writeTime, long readTime, long connectOutTime, boolean isEncryption,
                         AutoNetPatternAnontation.NetPattern pattern, IAutoNetDataCallback callback) {
        startNet("default", url, writeTime, readTime, connectOutTime, isEncryption, pattern, callback);
    }

    public void startNet(String baseUrlKey, String url,
                         long writeTime, long readTime, long connectOutTime, boolean isEncryption,
                         AutoNetPatternAnontation.NetPattern pattern, IAutoNetDataCallback callback) {
        Log.e("TAG", "baseUrlKey = " + baseUrlKey + ", url = " + url + ", writeTime = " + writeTime
                + ", readTime = " + readTime + ", connectOutTime = "
                + connectOutTime + ", pattern = " + pattern);
    }
}
