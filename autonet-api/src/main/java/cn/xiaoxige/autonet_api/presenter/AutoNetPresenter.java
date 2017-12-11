package cn.xiaoxige.autonet_api.presenter;

import android.text.TextUtils;

import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;
import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;
import cn.xiaoxige.autonet_api.interactors.DoDeleteUsecase;
import cn.xiaoxige.autonet_api.interactors.DoGetUsecase;
import cn.xiaoxige.autonet_api.interactors.DoPostUsecase;
import cn.xiaoxige.autonet_api.interactors.DoPutUsecase;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo;
import cn.xiaoxige.autonet_api.repository.AutoNetRepoImpl;
import cn.xiaoxige.autonet_api.subscriber.DefaultSubscriber;
import io.reactivex.FlowableTransformer;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public class AutoNetPresenter {
    // data
    private IRequestEntity mRequestEntity;
    // data
    private Class mResponseEntityClass;
    //data
    private String mResultUrl;
    //data
    private long mWriteTime;
    //data
    private long mReadTime;
    //data
    private long mConnectOutTime;
    //data
    private boolean mIsEncryption;
    //date
    private long mEncryptionKey;
    //data
    private FlowableTransformer mTransformer;
    //data
    private AutoNetConfig mConfig;
    //data
    private IAutoNetEncryptionCallback mAutoNetEncryptionCallback;

    AutoNetTypeAnontation.Type mReqType;
    AutoNetTypeAnontation.Type mResType;

    private String mBaseUrl;
    private String mUrl;
    private IAutoNetDataCallback mCallback;

    private AutoNetRepo mRepo;

    public AutoNetPresenter(IRequestEntity requestEntity, Class responseEntityClass, String baseUrl, String url, String extraParam,
                            long writeTime, long readTime, long connectOutTime, boolean isEncryption,
                            long encryptionKey, AutoNetTypeAnontation.Type reqType, AutoNetTypeAnontation.Type resType,
                            FlowableTransformer transformer, AutoNetConfig config,
                            IAutoNetEncryptionCallback autoNetEncryptionCallback,
                            IAutoNetDataCallback callback) {
        this.mRequestEntity = requestEntity;
        this.mResponseEntityClass = responseEntityClass;

        this.mBaseUrl = baseUrl;
        this.mUrl = url;
        this.mResultUrl = this.mBaseUrl + this.mUrl;

        if (!TextUtils.isEmpty(extraParam)) {
            if (!extraParam.startsWith("/") && !this.mResultUrl.endsWith("/")) {
                extraParam = "/" + extraParam;
            } else if (extraParam.startsWith("/") && this.mResultUrl.endsWith("/")) {
                extraParam = extraParam.replaceFirst("/", "");
            }
            this.mResultUrl += extraParam;
        }

        this.mWriteTime = writeTime;
        this.mReadTime = readTime;
        this.mConnectOutTime = connectOutTime;
        this.mIsEncryption = isEncryption;
        this.mEncryptionKey = encryptionKey;
        this.mConfig = config;
        this.mAutoNetEncryptionCallback = autoNetEncryptionCallback;

        this.mReqType = reqType;
        this.mResType = resType;

        this.mTransformer = transformer;

        this.mCallback = callback;

        mRepo = new AutoNetRepoImpl(mConfig, mIsEncryption, mEncryptionKey, mResultUrl, mWriteTime, mReadTime, mConnectOutTime, mAutoNetEncryptionCallback);
    }

    public void doGet() {

        DoGetUsecase usecase = new DoGetUsecase(mRepo, mRequestEntity, mResponseEntityClass);
        usecase.execute(new DefaultSubscriber<AutoResponseEntity>() {
            @Override
            public void DefaultOnNext(AutoResponseEntity data) {
                super.DefaultOnNext(data);
                if (mCallback != null) {
                    mCallback.onSuccess(data);
                }
            }

            @Override
            public void DefaultOnError(Throwable throwable) {
                super.DefaultOnError(throwable);
                if (mCallback != null) {
                    mCallback.onError(throwable);
                }
            }

            @Override
            public void DefaultOnEmpty() {
                super.DefaultOnEmpty();
                if (mCallback != null) {
                    mCallback.onEmpty();
                }
            }
        }, mTransformer);
    }

    public void doPost() {
        DoPostUsecase usecase = new DoPostUsecase(mRepo, mRequestEntity, mResponseEntityClass);
        usecase.execute(new DefaultSubscriber<AutoResponseEntity>() {
            @Override
            public void DefaultOnNext(AutoResponseEntity data) {
                super.DefaultOnNext(data);
                if (mCallback != null) {
                    mCallback.onSuccess(data);
                }
            }

            @Override
            public void DefaultOnError(Throwable throwable) {
                super.DefaultOnError(throwable);
                if (mCallback != null) {
                    mCallback.onError(throwable);
                }
            }

            @Override
            public void DefaultOnEmpty() {
                super.DefaultOnEmpty();
                if (mCallback != null) {
                    mCallback.onEmpty();
                }
            }
        }, mTransformer);
    }

    public void doDelete() {
        DoDeleteUsecase usecase = new DoDeleteUsecase(mRepo, mRequestEntity, mResponseEntityClass);
        usecase.execute(new DefaultSubscriber<AutoResponseEntity>() {
            @Override
            public void DefaultOnNext(AutoResponseEntity data) {
                super.DefaultOnNext(data);
                if (mCallback != null) {
                    mCallback.onSuccess(data);
                }
            }

            @Override
            public void DefaultOnError(Throwable throwable) {
                super.DefaultOnError(throwable);
                if (mCallback != null) {
                    mCallback.onError(throwable);
                }
            }

            @Override
            public void DefaultOnEmpty() {
                super.DefaultOnEmpty();
                if (mCallback != null) {
                    mCallback.onEmpty();
                }
            }
        }, mTransformer);
    }

    public void doPut() {
        DoPutUsecase usecase = new DoPutUsecase(mRepo, mRequestEntity, mResponseEntityClass);
        usecase.execute(new DefaultSubscriber<AutoResponseEntity>() {
            @Override
            public void DefaultOnNext(AutoResponseEntity data) {
                super.DefaultOnNext(data);
                if (mCallback != null) {
                    mCallback.onSuccess(data);
                }
            }

            @Override
            public void DefaultOnError(Throwable throwable) {
                super.DefaultOnError(throwable);
                if (mCallback != null) {
                    mCallback.onError(throwable);
                }
            }

            @Override
            public void DefaultOnEmpty() {
                super.DefaultOnEmpty();
                if (mCallback != null) {
                    mCallback.onEmpty();
                }
            }
        }, mTransformer);
    }

}
