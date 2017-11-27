package cn.xiaoxige.autonet_api.interfaces;

import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public interface IAutoNetDataCallback<T extends AutoResponseEntity> {

    void onSuccess(T entity);

    void onEmpty();

    void onError(Throwable throwable);
}
