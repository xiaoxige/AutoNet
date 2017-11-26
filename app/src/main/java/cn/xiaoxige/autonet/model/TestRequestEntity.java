package cn.xiaoxige.autonet.model;

import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;

/**
 * Created by zhuxiaoan on 2017/11/27.
 */

public class TestRequestEntity implements IRequestEntity {

    private String name;
    private int age;

    public TestRequestEntity() {
    }

    public TestRequestEntity(String name, int age) {
        this.name = name;
        this.age = age;
    }

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
