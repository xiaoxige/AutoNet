package cn.xiaoxige.autonet_api.interfaces;

import io.reactivex.FlowableEmitter;

/**
 * @author by xiaoxige on 2018/7/1.
 * Data processing before returning, eg: empty data processing, etc.
 */

public interface IAutoNetDataBeforeCallBack<T, Z> extends IAutoNetCallBack {

    /**
     * Processing before Data Return
     *
     * @param t       Processing content
     * @param emitter Upstream Launcher
     * @return
     */
    boolean handlerBefore(T t, FlowableEmitter<Z> emitter);

}
