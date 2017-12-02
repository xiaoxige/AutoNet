package cn.xiaoxige.autonet;

import android.util.Log;

import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;

/**
 * Created by zhuxiaoan on 2017/12/2.
 */


@AutoNetResponseEntityClass(value = AutoResponseEntity.class)
public class ImmediateNet implements IAutoNetDataCallback<AutoResponseEntity> {

    @Override
    public void onSuccess(AutoResponseEntity entity) {
        Log.e("TAG", "" + entity.toString());
    }

    @Override
    public void onEmpty() {
        Log.e("TAG", "空");
    }

    @Override
    public void onError(Throwable throwable) {
        Log.e("TAG", "" + throwable.toString());
    }
}
