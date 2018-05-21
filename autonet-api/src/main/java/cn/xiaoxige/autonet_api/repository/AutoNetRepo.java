package cn.xiaoxige.autonet_api.repository;


import io.reactivex.Flowable;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 *         AutoNet's request implementation interface
 */

public interface AutoNetRepo {

    Flowable doNetGet();

    Flowable doNetPost();

    Flowable doPut();

    Flowable doDelete();

    Flowable doLocal();

    Flowable pushFile();

    Flowable pullFile();
}
