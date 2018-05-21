package cn.xiaoxige.autonet;

import cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 */

public class TestEntity implements IAutoNetRequest{
    private String name;
    private int age;

    public TestEntity() {
    }

    public TestEntity(String name, int age) {
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
