package cn.xiaoxige.autonet_api.distributor;

import java.util.Map;

import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetLocalOptCallBack;

/**
 * @author xiaoxige
 * @date 2019/6/18 0018 14:50
 * -
 * email: xiaoxigexiaoan@outlook.com
 * desc: Distributors that support local policies
 */
public class AutoNetSupportLocalDistributor<T> implements IDistributorExecution<T> {

    private AutoNetStrategyAnontation.NetStrategy mNetStrategy;
    private IDistributorExecution<T> mExecution;
    private Map mRequestParams;
    private IAutoNetLocalOptCallBack<T> mAccompanyLocalOptCallback;
    private IAutoNetCallBack mCallBack;

    public AutoNetSupportLocalDistributor(AutoNetStrategyAnontation.NetStrategy netStrategy, IDistributorExecution<T> execution, Map requestParams, IAutoNetLocalOptCallBack<T> accompanyLocalOptCallback, IAutoNetCallBack callBack) {
        this.mNetStrategy = netStrategy;
        this.mExecution = execution;
        this.mRequestParams = requestParams;
        this.mAccompanyLocalOptCallback = accompanyLocalOptCallback;
        this.mCallBack = callBack;
    }

    @Override
    public T start(IAutoNetFileCallBack callBack) throws Exception {
        T t;
        if (AutoNetStrategyAnontation.NetStrategy.LOCAL.equals(this.mNetStrategy)) {
            t = optLocalDistributor();
        } else {
            t = this.mExecution.start(callBack);
        }
        return t;
    }

    @Override
    public T startNonFileRequest() throws Exception {
        T t;
        if (AutoNetStrategyAnontation.NetStrategy.LOCAL.equals(this.mNetStrategy)) {
            t = optLocalDistributor();
        } else {
            t = this.mExecution.startNonFileRequest();
        }
        return t;
    }

    @Override
    public T startFileRequest(IAutoNetFileCallBack callBack) throws Exception {
        return this.mExecution.startFileRequest(callBack);
    }

    private T optLocalDistributor() {
        if (this.mAccompanyLocalOptCallback != null) {
            return this.mAccompanyLocalOptCallback.optLocalData(this.mRequestParams);
        }
        if (this.mCallBack instanceof IAutoNetLocalOptCallBack) {
            //noinspection unchecked
            return (T) ((IAutoNetLocalOptCallBack) this.mCallBack).optLocalData(this.mRequestParams);
        }
        return null;
    }
}
