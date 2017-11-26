package cn.xiaoxige.autonet_api.interfaces;

import cn.xiaoxige.autonet_api.data.responsentity.IResponseEntity;

/**
 * Created by 小稀革 on 2017/11/26.
 */

public interface IAutoNetDataCallback {

    void onSuccess(IResponseEntity entity);

    void onEmpty();

    void onError(Throwable throwable);
}
