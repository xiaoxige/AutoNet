import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;

/**
 * @author by zhuxiaoan on 2018/5/17 0017.
 */

@AutoNetResponseEntityClass(value = Object.class)
public class ClassInRoot implements IAutoNetDataCallBack {

    @Override
    public void onSuccess(Object entity) {

    }

    @Override
    public void onFailed(Throwable throwable) {

    }

    @Override
    public void onEmpty() {

    }
}
