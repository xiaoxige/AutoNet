package cn.xiaoxige.autonet.entity;

import cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest;

public class TestARequest implements IAutoNetRequest {

    private Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
