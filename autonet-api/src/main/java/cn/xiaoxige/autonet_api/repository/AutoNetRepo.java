package cn.xiaoxige.autonet_api.repository;

import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;
import io.reactivex.Flowable;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public interface AutoNetRepo {

    Flowable doGet(IRequestEntity entity);

    Flowable doPost(IRequestEntity entity);

    Flowable doDelete(IRequestEntity entity);

    Flowable doPut(IRequestEntity entity);

}
