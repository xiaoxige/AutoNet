package cn.xiaoxige.autonet_api;

import java.util.List;
import java.util.Map;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.distributor.AutoNetDistributor;
import cn.xiaoxige.autonet_api.distributor.AutoNetFlowableDistributor;
import cn.xiaoxige.autonet_api.distributor.AutoNetStrategyDistributor;
import cn.xiaoxige.autonet_api.distributor.AutoNetSupportLocalDistributor;
import cn.xiaoxige.autonet_api.distributor.IDistributorExecution;
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

    private Class<Z> targetClass;

    private IDistributorExecution<T> mDistributor;
    private AutoNetFlowableDistributor<Flowable, T> mFlowableDistributor;
    private AutoNetStrategyDistributor<T, Z> mStrategyDistributor;

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

        this.targetClass = targetClazz;

        //noinspection unchecked
        this.mDistributor = new AutoNetSupportLocalDistributor(netStrategy,
                new AutoNetDistributor<T>(netPattern, reqType, resType, pushFileKey, filePath, fileName, accompanyFileCallback,
                        new AutoNetRepoImpl(url, heads, params, flag,
                                writeOutTime, readOutTime, connectOutTime,
                                encryptionKey, isEncryption,
                                mediaType,
                                responseClazz,
                                reqType,
                                interceptors, encryptionCallback, headCallBack, bodyCallBack)), params, accompanyLocalOptCallback, callBack);

        // flowable distributor
        this.mFlowableDistributor = new AutoNetFlowableDistributor<>(this.mDistributor, callBack, transformer);
        // strategy distributor
        this.mStrategyDistributor = new AutoNetStrategyDistributor<>(netStrategy, targetClazz, callBack, this.mFlowableDistributor);
    }

//    @Deprecated
//    public void doNetGet() {
//        doNetGet(null);
//    }
//
//    @Deprecated
//    public void doNetGet(OnInsertOpt insertOpt) {
//        net(BaseUseCase.NET_GET, insertOpt == null ? new OnInsertOpt() {
//            @Override
//            public boolean transmitError() {
//                return true;
//            }
//
//            @Override
//            public void opt() {
//            }
//        } : insertOpt);
//    }
//
//    @Deprecated
//    public void doNetPost() {
//        doNetPost(null);
//    }
//
//    @Deprecated
//    public void doNetPost(OnInsertOpt insertOpt) {
//        net(BaseUseCase.NET_POST, insertOpt == null ? new OnInsertOpt() {
//            @Override
//            public boolean transmitError() {
//                return true;
//            }
//
//            @Override
//            public void opt() {
//            }
//        } : insertOpt);
//    }
//
//    @Deprecated
//    public void doNetDelete() {
//        doNetDelete(null);
//    }
//
//    @Deprecated
//    public void doNetDelete(OnInsertOpt insertOpt) {
//        net(BaseUseCase.NET_DELETE, insertOpt == null ? new OnInsertOpt() {
//            @Override
//            public boolean transmitError() {
//                return true;
//            }
//
//            @Override
//            public void opt() {
//            }
//        } : insertOpt);
//    }
//
//    @Deprecated
//    public void doNetPut() {
//        doNetPut(null);
//    }
//
//    @Deprecated
//    public void doNetPut(OnInsertOpt insertOpt) {
//        net(BaseUseCase.NET_PUT, insertOpt == null ? new OnInsertOpt() {
//            @Override
//            public boolean transmitError() {
//                return true;
//            }
//
//            @Override
//            public void opt() {
//            }
//        } : insertOpt);
//    }
//
//
//    public void doLocal() {
//        doLocal(null);
//    }
//
//    public void doLocal(OnInsertOpt insertOpt) {
//        net(BaseUseCase.LOCAL, insertOpt == null ? new OnInsertOpt() {
//            @Override
//            public boolean transmitError() {
//                return true;
//            }
//
//            @Override
//            public void opt() {
//            }
//        } : insertOpt);
//    }
//
//    public void netOpt(AutoNetPatternAnontation.NetPattern netPattern) {
//        netOpt(netPattern, null);
//    }
//
//    public void netOpt(AutoNetPatternAnontation.NetPattern netPattern, OnInsertOpt insertOpt) {
//
//        int netState = analysisTransformationRequestMethod(netPattern);
//        net(netState, insertOpt == null ? new OnInsertOpt() {
//            @Override
//            public boolean transmitError() {
//                return true;
//            }
//
//            @Override
//            public void opt() {
//            }
//        } : insertOpt);
//    }
//
//    public void doLocalNet(final AutoNetPatternAnontation.NetPattern netPattern) {
//        doLocal(new OnInsertOpt() {
//            @Override
//            public boolean transmitError() {
//                return false;
//            }
//
//            @Override
//            public void opt() {
//                netOpt(netPattern);
//            }
//        });
//    }
//
//    public void doNetLocal(AutoNetPatternAnontation.NetPattern netPattern) {
//        netOpt(netPattern, new OnInsertOpt() {
//            @Override
//            public boolean transmitError() {
//                return false;
//            }
//
//            @Override
//            public void opt() {
//                doLocal();
//            }
//        });
//    }
//
////    public void pushFile(String pushFileKey, final String filePath) {
////
//////        if (!NetUtil.isNetworkAvailable(AutoNetConstant.sAutoNetContext)) {
//////            ansError(new NoNetError());
//////            return;
//////        }
//////
//////        AutoNetPushFileUseCase useCase = new AutoNetPushFileUseCase(mRepo, pushFileKey, filePath);
//////        useCase.execute(new DefaultSubscriber() {
//////            @Override
//////            protected void defaultOnNext(Object object) {
//////                //noinspection unchecked
//////                super.defaultOnNext(object);
//////                ansFile(object);
//////            }
//////
//////            @Override
//////            protected void defaultOnEmptyError() {
//////                super.defaultOnEmptyError();
//////                ansEmpty();
//////            }
//////
//////            @Override
//////            protected void defaultOnErrorWithNotEmpty(Throwable throwable) {
//////                super.defaultOnErrorWithNotEmpty(throwable);
//////                ansError(throwable);
//////            }
//////
//////            @Override
//////            protected void defaultOnComplete() {
//////                super.defaultOnComplete();
//////            }
//////        }, transformer);
////    }
////
////    public void pullFile(final String filePath, final String fileName) {
////
////        if (!NetUtil.isNetworkAvailable(AutoNetConstant.sAutoNetContext)) {
////            ansError(new NoNetError());
////            return;
////        }
////
////        AutoNetPullFileUseCase useCase = new AutoNetPullFileUseCase(mRepo, filePath, fileName);
////        useCase.execute(new DefaultSubscriber() {
////            @Override
////            protected void defaultOnNext(Object object) {
////                //noinspection unchecked
////                super.defaultOnNext(object);
////                ansFile(object);
////            }
////
////            @Override
////            protected void defaultOnEmptyError() {
////                super.defaultOnEmptyError();
////                ansEmpty();
////            }
////
////            @Override
////            protected void defaultOnErrorWithNotEmpty(Throwable throwable) {
////                super.defaultOnErrorWithNotEmpty(throwable);
////                ansError(throwable);
////            }
////
////            @Override
////            protected void defaultOnComplete() {
////                super.defaultOnComplete();
////            }
////        }, transformer);
////    }
//
//    private void net(int netState, final OnInsertOpt insertOpt) {
//
////        if (!NetUtil.isNetworkAvailable(AutoNetConstant.sAutoNetContext)) {
////            ansError(new NoNetError());
////            return;
////        }
////
////        AutoNetUseCase useCase = new AutoNetUseCase(mRepo, netState);
////        useCase.execute(new DefaultSubscriber() {
////            @Override
////            protected void defaultOnNext(Object entity) {
////                //noinspection unchecked
////                super.defaultOnNext(entity);
////                ansSuccess(entity);
////                insertOpt.opt();
////            }
////
////            @Override
////            protected void defaultOnEmptyError() {
////                super.defaultOnEmptyError();
////                if (insertOpt.transmitError()) {
////                    ansEmpty();
////                }
////                insertOpt.opt();
////            }
////
////            @Override
////            protected void defaultOnErrorWithNotEmpty(Throwable throwable) {
////                super.defaultOnErrorWithNotEmpty(throwable);
////                if (insertOpt.transmitError()) {
////                    ansError(throwable);
////                }
////                insertOpt.opt();
////            }
////
////            @Override
////            protected void defaultOnComplete() {
////                super.defaultOnComplete();
////            }
////        }, transformer);
//    }
//
////    public Flowable structureFlowable(AutoNetPatternAnontation.NetPattern netPattern) {
////        int netState = analysisTransformationRequestMethod(netPattern);
////        OpenAutoNetUseCase useCase = new OpenAutoNetUseCase(mRepo, netState, transformer);
////        return useCase.getFlowable();
////    }
//
//    private void ansSuccess(Object object) {
//        if (callBack == null) {
//            return;
//        }
//
//        if (callBack instanceof IAutoNetDataSuccessCallBack) {
//            //noinspection unchecked
//            ((IAutoNetDataSuccessCallBack) callBack).onSuccess(object);
//        }
//
//    }
//
//    private void ansFile(Object object) {
//        if (callBack == null) {
//            return;
//        }
//        if (callBack instanceof IAutoNetFileCallBack) {
//            if (object instanceof Float) {
//                ((IAutoNetFileCallBack) callBack).onProgress((Float) object);
//            } else if (object instanceof File) {
//                ((IAutoNetFileCallBack) callBack).onComplete((File) object);
//            }
//        }
//        if (!(object instanceof Float) && !(object instanceof File)) {
//            ansSuccess(object);
//        }
//    }
//
//    private void ansEmpty() {
//        if (callBack == null) {
//            return;
//        }
//        ((IAutoNetDataCallBack) callBack).onEmpty();
//    }
//
//    private void ansError(Throwable throwable) {
//        if (callBack == null) {
//            return;
//        }
//
//        if (throwable == null || throwable instanceof EmptyError) {
//            if (callBack instanceof IAutoNetDataCallBack) {
//                ((IAutoNetDataCallBack) callBack).onEmpty();
//            }
//        } else {
//            if (callBack instanceof IAutoNetDataCallBack) {
//                ((IAutoNetDataCallBack) callBack).onFailed(throwable);
//            }
//        }
//    }
//    /**
//     * Parsing transformation request mode
//     *
//     * @param netPattern
//     * @return
//     */
//    private int analysisTransformationRequestMethod(AutoNetPatternAnontation.NetPattern netPattern) {
//        int netState = BaseUseCase.NET_GET;
//        if (netPattern.equals(AutoNetPatternAnontation.NetPattern.GET)) {
//            netState = BaseUseCase.NET_GET;
//        } else if (netPattern.equals(AutoNetPatternAnontation.NetPattern.POST)) {
//            netState = BaseUseCase.NET_POST;
//        } else if (netPattern.equals(AutoNetPatternAnontation.NetPattern.DELETE)) {
//            netState = BaseUseCase.NET_DELETE;
//        } else if (netPattern.equals(AutoNetPatternAnontation.NetPattern.PUT)) {
//            netState = BaseUseCase.NET_PUT;
//        }
//        return netState;
//    }


    public T synchronizationNet() throws Exception {

        return this.mDistributor.start(null);
    }

    public Flowable structureFlowable(boolean isDefault) {
        return mFlowableDistributor.getFlowable(isDefault);
    }

    public void net() {

    }

//    private interface OnInsertOpt {
//
//        /**
//         * Is it possible to pass the previous error message when inserting operation?
//         *
//         * @return
//         */
//        boolean transmitError();
//
//        /**
//         * Insert operation
//         */
//        void opt();
//
//    }

}
