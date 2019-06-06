package cn.xiaoxige.autonet_api.interfaces;

import io.reactivex.FlowableEmitter;

/**
 * @author by zhuxiaoan on 2018/5/22 0022.
 * AutoNet body for all requests
 */

public interface IAutoNetBodyCallBack {

    /**
     * Return volume original content callback
     *
     * @param flag    Tracking Mark
     * @param body    Original Content
     * @param emitter Upstream Launcher
     * @return
     */
    boolean body(Object flag, String body, FlowableEmitter emitter);

}
