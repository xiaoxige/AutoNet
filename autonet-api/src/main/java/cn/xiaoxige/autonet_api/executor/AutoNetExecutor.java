package cn.xiaoxige.autonet_api.executor;

import java.util.List;
import java.util.Map;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.distributor.AutoNetDistributor;
import cn.xiaoxige.autonet_api.distributor.AutoNetSupportLocalDistributor;
import cn.xiaoxige.autonet_api.distributor.IDistributor;
import cn.xiaoxige.autonet_api.distributor.ILocalDistributor;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetBodyCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetHeadCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetLocalOptCallBack;
import cn.xiaoxige.autonet_api.repository.impl.AutoNetRepoImpl;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import okhttp3.Interceptor;

/**
 * @author by xiaoxige on 2018/5/20.
 * The executor of AutoNet
 */

@SuppressWarnings("WeakerAccess")
public class AutoNetExecutor<T, Z> {

    private SynchronizationNetExecutor<T> mSynchronizationNetExecutor;
    private AutoNetFlowableExecutor<Flowable, T> mFlowableExecutor;
    private NetExecutor<T, Z> mStrategyExecutor;

    public AutoNetExecutor(String url, Map<String, Object> heads, Map params, Object flag,
                           long writeOutTime, long readOutTime, long connectOutTime,
                           long encryptionKey, Boolean isEncryption,
                           List<Interceptor> interceptors,
                           String mediaType,
                           AutoNetPatternAnontation.NetPattern netPattern,
                           Class<T> responseClazz,
                           AutoNetStrategyAnontation.NetStrategy netStrategy,
                           Class<Z> targetClazz,
                           AutoNetTypeAnontation.Type reqType,
                           AutoNetTypeAnontation.Type resType,
                           String pushFileKey, String filePath, String fileName,
                           IAutoNetFileCallBack accompanyFileCallback, IAutoNetLocalOptCallBack accompanyLocalOptCallback,
                           IAutoNetEncryptionCallback encryptionCallback, IAutoNetHeadCallBack headCallBack, IAutoNetBodyCallBack bodyCallBack,
                           IAutoNetCallBack callBack,
                           FlowableTransformer transformer) {

        //noinspection unchecked
        ILocalDistributor<T> localDistributor = new AutoNetSupportLocalDistributor(netStrategy,
                new AutoNetDistributor<T>(netPattern, reqType, resType, pushFileKey, filePath, fileName, accompanyFileCallback,
                        new AutoNetRepoImpl(url, heads, params, flag,
                                writeOutTime, readOutTime, connectOutTime,
                                encryptionKey, isEncryption,
                                mediaType,
                                responseClazz,
                                reqType,
                                interceptors, encryptionCallback, headCallBack, bodyCallBack)), params, accompanyLocalOptCallback, callBack);

        // synchronization executor
        this.mSynchronizationNetExecutor = new SynchronizationNetExecutor<>(localDistributor);
        // flowable executor
        this.mFlowableExecutor = new AutoNetFlowableExecutor<>(localDistributor, responseClazz, targetClazz, callBack, transformer);
        // strategy executor
        this.mStrategyExecutor = new NetExecutor<>(netStrategy, reqType, resType, targetClazz, callBack, this.mFlowableExecutor);
    }

    public T synchronizationNet() throws Exception {
        return this.mSynchronizationNetExecutor.start();
    }

    public Flowable structureFlowable(boolean isDefault) {
        return mFlowableExecutor.getFlowable(isDefault);
    }

    public void net() {
        mStrategyExecutor.start();
    }

}
