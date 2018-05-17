package cn.xiaoxige.autonet_api.interfaces;

/**
 * @author by zhuxiaoan on 2018/5/17 0017.
 *         Auto's  callback interface
 */

public interface IAutoNetCallBack<T> {

    void onSuccess(T entity);

    void onFailed(Throwable throwable);

    void onEmpty();
}