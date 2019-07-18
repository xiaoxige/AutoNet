package cn.xiaoxige.autonet_api.interfaces;


/**
 * @author by zhuxiaoan on 2018/5/22 0022.
 * AutoNet body for all requests
 */

public interface IAutoNetBodyCallBack {

    /**
     * Return volume original content callback
     *
     * @param flag
     * @param httpCode
     * @param body
     * @return
     * @throws Exception
     */
    boolean body(Object flag, int httpCode, String body) throws Exception;

}
