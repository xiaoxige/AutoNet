package cn.xiaoxige.autonet_api.presenter;

import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;
import cn.xiaoxige.autonet_api.data.responsentity.IResponseEntity;
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
    //data
    private FlowableTransformer mTransformer;
    //data
    private AutoNetConfig mConfig;
    //data
    private IAutoNetEncryptionCallback mAutoNetEncryptionCallback;

    private String mBaseUrl;
    private String mUrl;
    private IAutoNetDataCallback mCallback;

    private AutoNetRepo mRepo;

    public AutoNetPresenter(IRequestEntity requestEntity, String baseUrl, String url,
                            long writeTime, long readTime, long connectOutTime, boolean isEncryption,
                            FlowableTransformer transformer, AutoNetConfig config,
                            IAutoNetEncryptionCallback autoNetEncryptionCallback,
                            IAutoNetDataCallback callback) {
        this.mRequestEntity = requestEntity;

        this.mBaseUrl = baseUrl;
        this.mUrl = url;
        this.mResultUrl = this.mBaseUrl + this.mUrl;

        this.mWriteTime = writeTime;
        this.mReadTime = readTime;
        this.mConnectOutTime = connectOutTime;
        this.mIsEncryption = isEncryption;
        this.mConfig = config;

        this.mTransformer = transformer;

        this.mCallback = callback;
        this.mAutoNetEncryptionCallback = autoNetEncryptionCallback;

        mRepo = new AutoNetRepoImpl();
    }

    public void doGet() {

        DoGetUsecase usecase = new DoGetUsecase(mRepo, mRequestEntity);
        usecase.execute(new DefaultSubscriber<IResponseEntity>() {
            @Override
            public void DefaultOnNext(IResponseEntity data) {
                super.DefaultOnNext(data);
                mCallback.onSuccess(data);
            }

            @Override
            public void DefaultOnError(Throwable throwable) {
                super.DefaultOnError(throwable);
                mCallback.onError(throwable);
            }

            @Override
            public void DefaultOnEmpty() {
                super.DefaultOnEmpty();
                mCallback.onEmpty();
            }
        }, mTransformer);
    }

    public void doPost() {
        DoPostUsecase usecase = new DoPostUsecase(mRepo, mRequestEntity);
        usecase.execute(new DefaultSubscriber<IResponseEntity>() {
            @Override
            public void DefaultOnNext(IResponseEntity data) {
                super.DefaultOnNext(data);
                mCallback.onSuccess(data);
            }

            @Override
            public void DefaultOnError(Throwable throwable) {
                super.DefaultOnError(throwable);
                mCallback.onError(throwable);
            }

            @Override
            public void DefaultOnEmpty() {
                super.DefaultOnEmpty();
                mCallback.onEmpty();
            }
        }, mTransformer);
    }

    public void doDelete() {
        DoDeleteUsecase usecase = new DoDeleteUsecase(mRepo, mRequestEntity);
        usecase.execute(new DefaultSubscriber<IResponseEntity>() {
            @Override
            public void DefaultOnNext(IResponseEntity data) {
                super.DefaultOnNext(data);
                mCallback.onSuccess(data);
            }

            @Override
            public void DefaultOnError(Throwable throwable) {
                super.DefaultOnError(throwable);
                mCallback.onError(throwable);
            }

            @Override
            public void DefaultOnEmpty() {
                super.DefaultOnEmpty();
                mCallback.onEmpty();
            }
        }, mTransformer);
    }

    public void doPut() {
        DoPutUsecase usecase = new DoPutUsecase(mRepo, mRequestEntity);
        usecase.execute(new DefaultSubscriber<IResponseEntity>() {
            @Override
            public void DefaultOnNext(IResponseEntity data) {
                super.DefaultOnNext(data);
                mCallback.onSuccess(data);
            }

            @Override
            public void DefaultOnError(Throwable throwable) {
                super.DefaultOnError(throwable);
                mCallback.onError(throwable);
            }

            @Override
            public void DefaultOnEmpty() {
                super.DefaultOnEmpty();
                mCallback.onEmpty();
            }
        }, mTransformer);
    }

}
