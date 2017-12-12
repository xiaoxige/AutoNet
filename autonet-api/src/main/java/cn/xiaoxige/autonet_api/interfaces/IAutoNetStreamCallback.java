package cn.xiaoxige.autonet_api.interfaces;

import java.io.File;

/**
 * @author by zhuxiaoan on 2017/12/12 0012.
 */

public interface IAutoNetStreamCallback extends IAutoNetDataCallback {
    void onComplete(File file);

    void onPregress(float progress);
}
