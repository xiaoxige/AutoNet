package cn.xiaoxige.autonet_api.interfaces;

/**
 * @author by zhuxiaoan on 2018/5/17 0017.
 * A callback that cares only about the result of success.
 */
public interface IAutoNetDataSuccessCallBack<T> extends IAutoNetCallBack {

    /**
     * Successful callback
     *
     * @param entity Successful data
     */
    void onSuccess(T entity);

}
