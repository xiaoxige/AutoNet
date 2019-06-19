package cn.xiaoxige.autonet_api.repository;


import io.reactivex.Flowable;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 * AutoNet's request implementation interface
 */

public interface AutoNetRepo1 {

    /**
     * get request
     *
     * @return
     */
    Flowable doNetGet();

    /**
     * post request
     *
     * @return
     */
    Flowable doNetPost();

    /**
     * put request
     *
     * @return
     */
    Flowable doPut();

    /**
     * delete request
     *
     * @return
     */
    Flowable doDelete();

    /**
     * local request
     *
     * @return
     */
    Flowable doLocal();

    /**
     * upload file
     *
     * @param pushFileKey
     * @param filePath
     * @return
     */
    Flowable pushFile(String pushFileKey, String filePath);

    /**
     * download file
     *
     * @param filePath
     * @param fileName
     * @return
     */
    Flowable pullFile(String filePath, String fileName);

}
