package cn.xiaoxige.autonet.model;

import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;

/**
 * Created by 小稀革 on 2017/11/26.
 */

public class TestResponseEntity extends AutoResponseEntity {
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
