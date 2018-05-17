import cn.xiaoxige.annotation.AutoNetResponseEntityClass;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;

/**
 * @author by zhuxiaoan on 2018/5/17 0017.
 */

@AutoNetResponseEntityClass(value = Object.class)
public class ClassInRoot implements IAutoNetCallBack {
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
