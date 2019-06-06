package cn.xiaoxige.autonet_api.interfaces;

import java.io.File;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 * AutoNet file operation flow progress callback.
 */
public interface IAutoNetFileCallBack extends IAutoNetCallBack {

    /**
     * uploading or downloading the progress of the file.
     *
     * @param progress progress of opt
     */
    void onProgress(float progress);

    /**
     * file callback after file uploading or downloading
     *
     * @param file
     */
    void onComplete(File file);
}
