package cn.xiaoxige.processor;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public class ProxyInfo {

    public static final String AUTONETPROXY = "autoproxy";
    public String packageName;
    // full pullPackageName
    public String pullPackageName;
    public String className;

    public String url;
    public long writeTime;
    public long readTime;
    public long connectOutTime;

    public String baseUrlKey;

    public boolean isEncryption;

    public AutoNetPatternAnontation.NetPattern netPattern;
}