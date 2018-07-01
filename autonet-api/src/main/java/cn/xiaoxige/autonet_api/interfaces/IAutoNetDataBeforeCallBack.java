package cn.xiaoxige.autonet_api.interfaces;

import io.reactivex.FlowableEmitter;

/**
 * @author by xiaoxige on 2018/7/1.
 *         数据返回前的处理, eg: 空数据处理等
 */

public interface IAutoNetDataBeforeCallBack<T> extends IAutoNetCallBack {

    boolean handlerBefore(T t, FlowableEmitter emitter);

}
