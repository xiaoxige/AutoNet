package cn.xiaoxige.processor;

import javax.lang.model.element.TypeElement;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public class ProxyInfo {

    public static final String AUTONETPROXY = "AutoProxy";

    public String packageName;
    // full fullPackageName
    public String fullPackageName;
    public String className;
    public String outClassFullPackageName;

    public String url = "/";
    public long writeTime = 5000;
    public long readTime = 5000;
    public long connectOutTime = 5000;

    public String baseUrlKey = "default";

    public long encryptionKey = 0;
    public boolean isEncryption = false;

    public AutoNetPatternAnontation.NetPattern netPattern = AutoNetPatternAnontation.NetPattern.GET;

    public AutoNetTypeAnontation.Type reqType = AutoNetTypeAnontation.Type.JSON;

    public AutoNetTypeAnontation.Type resType = AutoNetTypeAnontation.Type.JSON;

    public String responseClazzName;

    public TypeElement typeElement;
}