package cn.xiaoxige.autonet_api.interfaces;

import io.reactivex.FlowableEmitter;

/**
 * @author by zhuxiaoan on 2018/5/22 0022.
 * AutoNet body for all requests
 */

public interface IAutoNetBodyCallBack {

    boolean body(Object flag, String object, FlowableEmitter emitter);

}
