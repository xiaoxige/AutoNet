package cn.xiaoxige.autonet_api.distributor;

import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetLocalOptCallBack;
import io.reactivex.Flowable;

/**
 * @author xiaoxige
 * @date 2019/6/17 0017 17:15
 * -
 * email: xiaoxigexiaoan@outlook.com
 * desc: AutoNet Distributor, which distributes requests
 */
public class AutoNetStrategyDistributor<T, Z> implements IDistributorExecution<T> {

    private AutoNetStrategyAnontation.NetStrategy mNetStrategy;
    private Class<Z> mTargetClass;
    private IAutoNetCallBack mCallBack;
    private IDistributorExecution<T> mExecution;

    public AutoNetStrategyDistributor(AutoNetStrategyAnontation.NetStrategy netStrategy, Class<Z> targetClass, IAutoNetCallBack callBack, AutoNetFlowableDistributor<Flowable, T> execution) {
        this.mNetStrategy = netStrategy;
        this.mTargetClass = targetClass;
        this.mCallBack = callBack;
        this.mExecution = execution;
    }

    public void start() {

        if (AutoNetStrategyAnontation.NetStrategy.LOCAL_NET == this.mNetStrategy) {

        } else if (AutoNetStrategyAnontation.NetStrategy.NET_LOCAL == this.mNetStrategy) {

        } else {

        }
    }

    @Override
    public T start(IAutoNetFileCallBack callBack) throws Exception {
        return this.mExecution.start(callBack);
    }

    @Override
    public T startNonFileRequest() throws Exception {
        return this.mExecution.startNonFileRequest();
    }

    @Override
    public T startFileRequest(IAutoNetFileCallBack callBack) throws Exception {
        return this.mExecution.startFileRequest(callBack);
    }

}
