package cn.xiaoxige.autonet.model;

/**
 * @author by zhuxiaoan on 2017/11/27 0027.
 */

public class TestEntity implements ITestEntity {
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
