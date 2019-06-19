package cn.xiaoxige.autonet_api.executor;

import java.io.File;

import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.error.CutOffError;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetComplete;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataSuccessCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.subscriber.DefaultSubscriber;
import cn.xiaoxige.autonet_api.util.AutoNetTypeUtil;
import cn.xiaoxige.autonet_api.util.ClassInstanceofPlusUtil;
import io.reactivex.Flowable;

/**
 * @author xiaoxige
 * @date 2019/6/17 0017 17:15
 * -
 * email: xiaoxigexiaoan@outlook.com
 * desc: AutoNet Distributor, which distributes requests
 */
class NetExecutor<T, Z> {

    private AutoNetStrategyAnontation.NetStrategy mNetStrategy;
    private AutoNetTypeAnontation.Type mReqType;
    private AutoNetTypeAnontation.Type mResType;
    private Class<Z> mTargetClass;
    private IAutoNetCallBack mCallBack;
    private AutoNetFlowableExecutor<Flowable, T> mExecution;

    NetExecutor(AutoNetStrategyAnontation.NetStrategy netStrategy, AutoNetTypeAnontation.Type reqType, AutoNetTypeAnontation.Type resType, Class<Z> targetClass, IAutoNetCallBack callBack, AutoNetFlowableExecutor<Flowable, T> execution) {
        this.mNetStrategy = netStrategy;
        this.mReqType = reqType;
        this.mResType = resType;
        this.mTargetClass = targetClass;
        this.mCallBack = callBack;
        this.mExecution = execution;
    }

    void start() {

        if (AutoNetStrategyAnontation.NetStrategy.LOCAL_NET == this.mNetStrategy) {
            handleLocalNet();
        } else if (AutoNetStrategyAnontation.NetStrategy.NET_LOCAL == this.mNetStrategy) {
            handleNetLocal();
        } else {
            call(false, new FollowUpCallback() {

                @Override
                public boolean transmitComplete() {
                    return true;
                }

                @Override
                public boolean transmitError() {
                    return true;
                }

                @Override
                public void followUpActions() {
                }
            });
        }
    }

    private void handleLocalNet() {
        call(true, new FollowUpCallback() {
            @Override
            public boolean transmitComplete() {
                return false;
            }

            @Override
            public boolean transmitError() {
                return false;
            }

            @Override
            public void followUpActions() {
                call(false, new FollowUpCallback() {
                    @Override
                    public boolean transmitComplete() {
                        return true;
                    }

                    @Override
                    public boolean transmitError() {
                        return true;
                    }

                    @Override
                    public void followUpActions() {
                    }
                });
            }
        });
    }

    private void handleNetLocal() {
        call(false, new FollowUpCallback() {
            @Override
            public boolean transmitComplete() {
                return false;
            }

            @Override
            public boolean transmitError() {
                return false;
            }

            @Override
            public void followUpActions() {
                call(true, new FollowUpCallback() {
                    @Override
                    public boolean transmitComplete() {
                        return true;
                    }

                    @Override
                    public boolean transmitError() {
                        return true;
                    }

                    @Override
                    public void followUpActions() {
                    }
                });
            }
        });
    }

    private void call(boolean isForceLocal, final FollowUpCallback callback) {
        //noinspection unchecked
        this.mExecution.getFlowable(true, isForceLocal)
                .subscribe(new DefaultSubscriber() {
                    @Override
                    protected void defaultOnNext(Object entity) {
                        //noinspection unchecked
                        super.defaultOnNext(entity);
                        asNext(entity);
                    }

                    @Override
                    protected void defaultOnEmptyError() {
                        super.defaultOnEmptyError();
                        if (callback.transmitError()) {
                            asEmpty();
                        }
                        defaultOnComplete();
                    }

                    @Override
                    protected void defaultOnErrorWithNotEmpty(Throwable throwable) {
                        super.defaultOnErrorWithNotEmpty(throwable);

                        if (throwable instanceof CutOffError) {
                            defaultOnComplete();
                            return;
                        }

                        if (callback.transmitError()) {
                            asError(throwable);
                        }
                        defaultOnComplete();
                    }

                    @Override
                    protected void defaultOnComplete() {
                        super.defaultOnComplete();
                        callback.followUpActions();
                        if (callback.transmitComplete()) {
                            asComplete();
                        }
                    }
                });
    }

    private void asNext(Object entity) {
        if (mCallBack == null) {
            return;
        }

        if (AutoNetTypeUtil.isFileOperation(this.mReqType, this.mResType)) {
            asFile(entity);
        } else {
            asTarget(entity);
        }
    }

    private void asFile(Object entity) {
        asProgress(entity);
        asFileComplete(entity);
        asTarget(entity);
    }

    private void asTarget(Object entity) {

        if (ClassInstanceofPlusUtil.instanceOf(entity, this.mTargetClass)) {
            if (mCallBack instanceof IAutoNetDataSuccessCallBack) {
                //noinspection SingleStatementInBlock,unchecked
                ((IAutoNetDataSuccessCallBack) mCallBack).onSuccess(entity);
            }
        }
    }

    private void asProgress(Object entity) {
        if (entity instanceof Float && mCallBack instanceof IAutoNetFileCallBack) {
            ((IAutoNetFileCallBack) mCallBack).onProgress((Float) entity);
        }
    }

    private void asFileComplete(Object entity) {
        if (entity instanceof File && mCallBack instanceof IAutoNetFileCallBack) {
            ((IAutoNetFileCallBack) mCallBack).onComplete((File) entity);
        }
    }

    private void asEmpty() {
        if (mCallBack == null) {
            return;
        }
        if (mCallBack instanceof IAutoNetDataCallBack) {
            ((IAutoNetDataCallBack) mCallBack).onEmpty();
        }
    }

    private void asError(Throwable throwable) {
        if (mCallBack == null) {
            return;
        }
        if (mCallBack instanceof IAutoNetDataCallBack) {
            ((IAutoNetDataCallBack) mCallBack).onFailed(throwable);
        }
    }

    private void asComplete() {
        if (mCallBack == null) {
            return;
        }
        if (mCallBack instanceof IAutoNetComplete) {
            ((IAutoNetComplete) mCallBack).onComplete();
        }
    }

    private interface FollowUpCallback {

        /**
         * Whether the delivery is successful or not
         *
         * @return
         */
        boolean transmitComplete();

        /**
         * Whether to pass errors or not
         *
         * @return
         */
        boolean transmitError();

        /**
         * Follow-up actions
         */
        void followUpActions();
    }

}
