package cn.xiaoxige.autonet.model;

import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;

/**
 * @author by zhuxiaoan on 2017/11/27 0027.
 */

public class JsonTestRequestEntity implements IRequestEntity {
    private String m;
    private String c;
    private String a;

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
