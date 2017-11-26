package cn.xiaoxige.autonet_api.presenter;

import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import io.reactivex.FlowableTransformer;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public class AutoNetPresenter {

    private IRequestEntity mRequestEntity;
    private String mBaseUrl;
    private String mUrl;
    private String mResultUrl;
    private long writeTime;
    private long readTime;
    private long connectOutTime;
    private boolean mIsEncryption;
    private FlowableTransformer mTransformer;
    private IAutoNetDataCallback mCallback;

    private AutoNetConfig mConfig;
    private IAutoNetEncryptionCallback mAutoNetEncryptionCallback;

    public AutoNetPresenter(IRequestEntity requestEntity, String baseUrl, String url,
                            long writeTime, long readTime, long connectOutTime, boolean isEncryption,
                            FlowableTransformer transformer, AutoNetConfig config,
                            IAutoNetEncryptionCallback autoNetEncryptionCallback,
                            IAutoNetDataCallback callback) {


    }

    public void doGet() {

    }

    public void doPost() {

    }

    public void doDelete() {

    }

    public void doPut() {

    }

}
