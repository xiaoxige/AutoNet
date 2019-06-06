package cn.xiaoxige.autonet_api.interfaces;

import okhttp3.Headers;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 * Header information callback of the AutoNet request to return the data.
 */

public interface IAutoNetHeadCallBack {

    /**
     * AutoNet request to return the header information of the data
     *
     * @param flag    Tracking Mark
     * @param headers headers of response
     */
    void head(Object flag, Headers headers);
}
