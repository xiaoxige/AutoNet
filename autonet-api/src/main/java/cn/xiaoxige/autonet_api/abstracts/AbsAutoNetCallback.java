package cn.xiaoxige.autonet_api.abstracts;

import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataBeforeCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;
import io.reactivex.FlowableEmitter;

/**
 * @author by zhuxiaoan on 2018/7/3 0003.
 *         AutoNet抽象的返回类， 用户可更具自己所关心的去重写
 */

public abstract class AbsAutoNetCallback<T, Z> implements IAutoNetDataBeforeCallBack<T>, IAutoNetDataCallBack<Z> {

    @Override
    public boolean handlerBefore(T t, FlowableEmitter emitter) {
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

}
