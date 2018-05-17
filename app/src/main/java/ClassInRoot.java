import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;

/**
 * @author by zhuxiaoan on 2018/5/17 0017.
 */

@AutoNetResponseEntityClass(value = AutoResponseEntity.class)
public class ClassInRoot implements IAutoNetDataCallback<AutoResponseEntity> {
    @Override
    public void onSuccess(AutoResponseEntity entity) {

    }

    @Override
    public void onEmpty() {

    }

    @Override
    public void onError(Throwable throwable) {

    }
}
