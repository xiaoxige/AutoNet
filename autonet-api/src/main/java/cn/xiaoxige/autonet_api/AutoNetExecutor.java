package cn.xiaoxige.autonet_api;

import android.support.v4.util.ArrayMap;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.abstracts.BaseUseCase;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.constant.AutoNetConstant;
import cn.xiaoxige.autonet_api.error.EmptyError;
import cn.xiaoxige.autonet_api.error.NoNetError;
import cn.xiaoxige.autonet_api.interactors.AutoNetPullFileUseCase;
import cn.xiaoxige.autonet_api.interactors.AutoNetPushFileUseCase;
import cn.xiaoxige.autonet_api.interactors.AutoNetUseCase;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetBodyCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataSuccessCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetHeadCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest;
import cn.xiaoxige.autonet_api.repository.AutoNetRepo;
import cn.xiaoxige.autonet_api.repository.impl.AutoNetRepoImpl;
import cn.xiaoxige.autonet_api.subscriber.DefaultSubscriber;
import cn.xiaoxige.autonet_api.util.DataConvertorUtils;
import cn.xiaoxige.autonet_api.util.NetUtil;
import io.reactivex.FlowableTransformer;
import okhttp3.Interceptor;

/**
 * @author by xiaoxige on 2018/5/20.
 *         The executor of AutoNet
 */

@SuppressWarnings("WeakerAccess")
public class AutoNetExecutor {

    private FlowableTransformer transformer;
    private IAutoNetCallBack callBack;

    private AutoNetRepo mRepo;

    public AutoNetExecutor(IAutoNetRequest requestEntity, Map requestMap, String extraDynamicParam,
                           String url, String mediaType,
                           Long writeOutTime, Long readOutTime, Long connectOutTime,
                           Long encryptionKey, Boolean isEncryption, List<Interceptor> interceptors, Map<String, String> heads,
                           String responseClazzName, FlowableTransformer transformer, AutoNetTypeAnontation.Type reqType, IAutoNetEncryptionCallback encryptionCallback, IAutoNetHeadCallBack headCallBack, IAutoNetBodyCallBack bodyCallBack, IAutoNetCallBack callBack) {
        this.transformer = transformer;
        this.callBack = callBack;
        Map params = integrationParams(requestEntity, requestMap);
        mRepo = new AutoNetRepoImpl(params, extraDynamicParam,
                url, mediaType, writeOutTime, readOutTime, connectOutTime,
                encryptionKey, isEncryption, interceptors, heads, responseClazzName, reqType, encryptionCallback, headCallBack, bodyCallBack, callBack);
    }

    @Deprecated
    public void doNetGet() {
        doNetGet(null);
    }

    @Deprecated
    public void doNetGet(OnInsertOpt insertOpt) {
        net(BaseUseCase.NET_GET, insertOpt == null ? new OnInsertOpt() {
            @Override
            public boolean transmitError() {
                return true;
            }

            @Override
            public void opt() {
            }
        } : insertOpt);
    }

    @Deprecated
    public void doNetPost() {
        doNetPost(null);
    }

    @Deprecated
    public void doNetPost(OnInsertOpt insertOpt) {
        net(BaseUseCase.NET_POST, insertOpt == null ? new OnInsertOpt() {
            @Override
            public boolean transmitError() {
                return true;
            }

            @Override
            public void opt() {
            }
        } : insertOpt);
    }

    @Deprecated
    public void doNetDelete() {
        doNetDelete(null);
    }

    @Deprecated
    public void doNetDelete(OnInsertOpt insertOpt) {
        net(BaseUseCase.NET_DELETE, insertOpt == null ? new OnInsertOpt() {
            @Override
            public boolean transmitError() {
                return true;
            }

            @Override
            public void opt() {
            }
        } : insertOpt);
    }

    @Deprecated
    public void doNetPut() {
        doNetPut(null);
    }

    @Deprecated
    public void doNetPut(OnInsertOpt insertOpt) {
        net(BaseUseCase.NET_PUT, insertOpt == null ? new OnInsertOpt() {
            @Override
            public boolean transmitError() {
                return true;
            }

            @Override
            public void opt() {
            }
        } : insertOpt);
    }


    public void doLocal() {
        doLocal(null);
    }

    public void doLocal(OnInsertOpt insertOpt) {
        net(BaseUseCase.LOCAL, insertOpt == null ? new OnInsertOpt() {
            @Override
            public boolean transmitError() {
                return true;
            }

            @Override
            public void opt() {
            }
        } : insertOpt);
    }

    public void netOpt(AutoNetPatternAnontation.NetPattern netPattern) {
        netOpt(netPattern, null);
    }

    public void netOpt(AutoNetPatternAnontation.NetPattern netPattern, OnInsertOpt insertOpt) {
        int netState = BaseUseCase.NET_GET;
        if (netPattern.equals(AutoNetPatternAnontation.NetPattern.GET)) {
            netState = BaseUseCase.NET_GET;
        } else if (netPattern.equals(AutoNetPatternAnontation.NetPattern.POST)) {
            netState = BaseUseCase.NET_POST;
        } else if (netPattern.equals(AutoNetPatternAnontation.NetPattern.DELETE)) {
            netState = BaseUseCase.NET_DELETE;
        } else if (netPattern.equals(AutoNetPatternAnontation.NetPattern.PUT)) {
            netState = BaseUseCase.NET_PUT;
        }

        net(netState, insertOpt == null ? new OnInsertOpt() {
            @Override
            public boolean transmitError() {
                return true;
            }

            @Override
            public void opt() {
            }
        } : insertOpt);
    }

    public void doLocalNet(final AutoNetPatternAnontation.NetPattern netPattern) {
        doLocal(new OnInsertOpt() {
            @Override
            public boolean transmitError() {
                return false;
            }

            @Override
            public void opt() {
                netOpt(netPattern);
            }
        });
    }

    public void doNetLocal(AutoNetPatternAnontation.NetPattern netPattern) {
        netOpt(netPattern, new OnInsertOpt() {
            @Override
            public boolean transmitError() {
                return false;
            }

            @Override
            public void opt() {
                doLocal();
            }
        });
    }

    public void pushFile(String pushFileKey, final String filePath) {

        if (!NetUtil.isNetworkAvailable(AutoNetConstant.sAutoNetContext)) {
            ansError(new NoNetError());
            return;
        }

        AutoNetPushFileUseCase useCase = new AutoNetPushFileUseCase(mRepo, pushFileKey, filePath);
        useCase.execute(new DefaultSubscriber() {
            @Override
            protected void defaultOnNext(Object object) {
                //noinspection unchecked
                super.defaultOnNext(object);
                ansFile(object);
            }

            @Override
            protected void defaultOnEmptyError() {
                super.defaultOnEmptyError();
                ansEmpty();
            }

            @Override
            protected void defaultOnErrorWithNotEmpty(Throwable throwable) {
                super.defaultOnErrorWithNotEmpty(throwable);
                ansError(throwable);
            }

            @Override
            protected void defaultOnComplete() {
                super.defaultOnComplete();
            }
        }, transformer);
    }

    public void pullFile(final String filePath, final String fileName) {

        if (!NetUtil.isNetworkAvailable(AutoNetConstant.sAutoNetContext)) {
            ansError(new NoNetError());
            return;
        }

        AutoNetPullFileUseCase useCase = new AutoNetPullFileUseCase(mRepo, filePath, fileName);
        useCase.execute(new DefaultSubscriber() {
            @Override
            protected void defaultOnNext(Object object) {
                //noinspection unchecked
                super.defaultOnNext(object);
                ansFile(object);
            }

            @Override
            protected void defaultOnEmptyError() {
                super.defaultOnEmptyError();
                ansEmpty();
            }

            @Override
            protected void defaultOnErrorWithNotEmpty(Throwable throwable) {
                super.defaultOnErrorWithNotEmpty(throwable);
                ansError(throwable);
            }

            @Override
            protected void defaultOnComplete() {
                super.defaultOnComplete();
            }
        }, transformer);
    }

    private void net(int netState, final OnInsertOpt insertOpt) {

        if (!NetUtil.isNetworkAvailable(AutoNetConstant.sAutoNetContext)) {
            ansError(new NoNetError());
            return;
        }

        AutoNetUseCase useCase = new AutoNetUseCase(mRepo, netState);
        useCase.execute(new DefaultSubscriber() {
            @Override
            protected void defaultOnNext(Object entity) {
                //noinspection unchecked
                super.defaultOnNext(entity);
                ansSuccess(entity);
                insertOpt.opt();
            }

            @Override
            protected void defaultOnEmptyError() {
                super.defaultOnEmptyError();
                if (insertOpt.transmitError()) {
                    ansEmpty();
                }
                insertOpt.opt();
            }

            @Override
            protected void defaultOnErrorWithNotEmpty(Throwable throwable) {
                super.defaultOnErrorWithNotEmpty(throwable);
                if (insertOpt.transmitError()) {
                    ansError(throwable);
                }
                insertOpt.opt();
            }

            @Override
            protected void defaultOnComplete() {
                super.defaultOnComplete();
            }
        }, transformer);
    }


    private void ansSuccess(Object object) {
        if (callBack == null) {
            return;
        }

        if (callBack instanceof IAutoNetDataSuccessCallBack) {
            //noinspection unchecked
            ((IAutoNetDataSuccessCallBack) callBack).onSuccess(object);
        }

    }

    private void ansFile(Object object) {
        if (callBack == null) {
            return;
        }
        if (callBack instanceof IAutoNetFileCallBack) {
            if (object instanceof Float) {
                ((IAutoNetFileCallBack) callBack).onPregress((Float) object);
            } else if (object instanceof File) {
                ((IAutoNetFileCallBack) callBack).onComplete((File) object);
            }
        }
        if (!(object instanceof Float) && !(object instanceof File)) {
            ansSuccess(object);
        }
    }

    private void ansEmpty() {
        if (callBack == null) {
            return;
        }
        ((IAutoNetDataCallBack) callBack).onEmpty();
    }

    private void ansError(Throwable throwable) {
        if (callBack == null) {
            return;
        }

        if (throwable == null || throwable instanceof EmptyError) {
            if (callBack instanceof IAutoNetDataCallBack) {
                ((IAutoNetDataCallBack) callBack).onEmpty();
            }
        } else {
            if (callBack instanceof IAutoNetDataCallBack) {
                ((IAutoNetDataCallBack) callBack).onFailed(throwable);
            }
        }
    }

    /**
     * Integration request parameters
     *
     * @param requestEntity
     * @param requestMap
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map integrationParams(IAutoNetRequest requestEntity, Map requestMap) {
        Map params = new ArrayMap();
        if (requestEntity != null) {
            params.putAll(DataConvertorUtils.convertEntityToMap(requestEntity, true));
        }
        if (requestMap != null) {
            params.putAll(requestMap);
        }
        return params;
    }


    private interface OnInsertOpt {

        /**
         * Is it possible to pass the previous error message when inserting operation?
         *
         * @return
         */
        boolean transmitError();

        /**
         * Insert operation
         */
        void opt();

    }

}
