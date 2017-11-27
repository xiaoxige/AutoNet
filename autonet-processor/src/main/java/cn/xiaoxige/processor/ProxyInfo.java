package cn.xiaoxige.processor;

import javax.lang.model.element.TypeElement;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public class ProxyInfo {

    public static final String AUTONETPROXY = "AutoProxy";

    public String packageName;
    // full fullPackageName
    public String fullPackageName;
    public String className;

    public String url;
    public long writeTime;
    public long readTime;
    public long connectOutTime;

    public String baseUrlKey;

    public boolean isEncryption;

    public AutoNetPatternAnontation.NetPattern netPattern;

    public String responseClazzName;

    public TypeElement typeElement;
}