package cn.xiaoxige.autonet.entity;

import cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest;

/**
 * @author by zhuxiaoan on 2018/5/22 0022.
 */

public class TestRequest implements IAutoNetRequest {
    private String m;
    private String c;
    private String a;

    public TestRequest() {
    }

    public TestRequest(String m, String c, String a) {
        this.m = m;
        this.c = c;
        this.a = a;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
}
