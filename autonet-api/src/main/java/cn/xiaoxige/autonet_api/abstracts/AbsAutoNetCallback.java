package cn.xiaoxige.autonet_api.abstracts;

import cn.xiaoxige.autonet_api.interfaces.IAutoNetComplete;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataBeforeCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;
import io.reactivex.FlowableEmitter;

/**
 * @author by zhuxiaoan on 2018/7/3 0003.
 * AutoNet Abstract return class, users have more concerns to rewrite
 */
public abstract class AbsAutoNetCallback<T, Z> implements IAutoNetDataBeforeCallBack<T, Z>, IAutoNetDataCallBack<Z>, IAutoNetComplete {

    @Override
    public boolean handlerBefore(T t, FlowableEmitter<Z> emitter) {
        return false;
    }

    @Override
    public void onSuccess(Z entity) {
    }

    @Override
    public void onFailed(Throwable throwable) {
    }

    @Override
    public void onEmpty() {
    }

    @Override
    public void onComplete() {
    }
}
