package cn.xiaoxige.autonet_api.interfaces;

/**
 * @author by zhuxiaoan on 2018/5/17 0017.
 *         AutoNet data request callback interface.
 */

public interface IAutoNetDataCallBack extends IAutoNetDataSuccessCallBack {

    void onFailed(Throwable throwable);

    void onEmpty();

}
