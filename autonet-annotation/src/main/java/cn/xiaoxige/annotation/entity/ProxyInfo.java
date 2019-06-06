package cn.xiaoxige.annotation.entity;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 * All the information entity classes of a request
 */

public final class ProxyInfo {

    /**
     * Automatically generating the suffix of the class
     */
    public static final String PROXY_CLASS_SUFFIX = "AutoProxy";

    /**
     * Unique identity of a class
     * full path
     */
    public String fullTargetPath = "";

    /**
     * package' name of target
     */
    public String targetPackage = "";

    /**
     * class' name of terget
     */
    public String targetClassSimpleName = "";

    /**
     * domain name key of request
     * eg:
     * url is https://www.xiaoxige.cn:8080/index.jsp
     * this param is https://www.xiaoxige.com:8080
     */
    public String domainNameKey = "default";

    /**
     * suffix of url
     * eg:
     * url is https://www.xiaoxige.cn:8080/index.jsp
     * this param is /index.jsp
     */
    public String suffixUrl = "/";

    /**
     * Request tracking flag
     */
    public int flag = 0;

    /**
     * out time of write with net
     */
    public long writeOutTime = 5000;
    /**
     * out time of read with net
     */
    public long readOutTime = 5000;
    /**
     * out time of connect with net
     */
    public long connectOutTime = 5000;

    /**
     * Unique identity of data with net encryption
     */
    public long encryptionKey = 0;
    /**
     * is open encryption
     */
    public boolean isEncryption = false;

    /**
     * mediaType of request
     */
    public String mediaType = "application/json; charset=utf-8";

    /**
     * A temporary domain name
     */
    public String disposableBaseUrl = "";

    /**
     * Temporary header information
     */
    public String[] disposableHeads = null;

    /**
     * type of network request
     */
    public AutoNetPatternAnontation.NetPattern netPattern = AutoNetPatternAnontation.NetPattern.GET;

    /**
     * strategy of net
     */
    public AutoNetStrategyAnontation.NetStrategy netStrategy = AutoNetStrategyAnontation.NetStrategy.NET;

    /**
     * request Type
     */
    public AutoNetTypeAnontation.Type reqType = AutoNetTypeAnontation.Type.JSON;

    /**
     * response Type
     */
    public AutoNetTypeAnontation.Type resType = AutoNetTypeAnontation.Type.JSON;
}