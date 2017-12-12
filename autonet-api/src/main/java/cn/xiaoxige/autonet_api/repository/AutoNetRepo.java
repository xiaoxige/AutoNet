package cn.xiaoxige.autonet_api.repository;

import java.io.File;

import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;
import io.reactivex.Flowable;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public interface AutoNetRepo {

    Flowable doGet(IRequestEntity entity, Class responseEntityClass);

    Flowable doPost(IRequestEntity entity, Class responseEntityClass);

    Flowable doDelete(IRequestEntity entity, Class responseEntityClass);

    Flowable doPut(IRequestEntity entity, Class responseEntityClass);

    Flowable doPullStreamGet(IRequestEntity requestEntity, File file);

    Flowable doPullStreamPost(IRequestEntity requestEntity, File file);

    Flowable doPushStreamGet(IRequestEntity requestEntity, File file);

    Flowable doPushStreamPost(IRequestEntity requestEntity, File file);

}
