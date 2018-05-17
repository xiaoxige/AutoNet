package cn.xiaoxige.autonet_api.interfaces;

/**
 * @author by zhuxiaoan on 2018/5/17 0017.
 *         Auto's data callback interface.
 */

public interface IAutoNetDataCallback<T extends IAutoNetResponse> extends IAutoNetCallBack<T> {

    void onSuccess(T entity);

    void onFailed(Throwable throwable);

    void onEmpty();

}
