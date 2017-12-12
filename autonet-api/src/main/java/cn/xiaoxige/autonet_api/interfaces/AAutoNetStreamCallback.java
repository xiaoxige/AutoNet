package cn.xiaoxige.autonet_api.interfaces;

import java.io.File;

import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;

/**
 * @author by zhuxiaoan on 2017/12/12 0012.
 */

public abstract class AAutoNetStreamCallback implements IAutoNetDataCallback {

    public abstract void onComplete(File file);

    public abstract void onPregress(float progress);

    @Override
    public void onSuccess(AutoResponseEntity entity) {
    }
}
