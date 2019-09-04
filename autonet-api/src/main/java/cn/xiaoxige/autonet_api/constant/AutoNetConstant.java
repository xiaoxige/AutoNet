package cn.xiaoxige.autonet_api.constant;

import android.content.Context;

/**
 * @author by zhuxiaoan on 2018/5/22 0022.
 * static final value of autonet
 */

public class AutoNetConstant {

    public static Context sAutoNetContext;

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    public static final String PUT = "PUT";

    public static final String SLASH = "/";

    public static final String PARAMETER_START_FLAG = "?";
    public static final String PARAMETRIC_LINK_MARKER = "&";

    public static final Float NUM_100 = 100.0f;

    public static final Float MAX_PROGRESS = NUM_100;

    public static final int DEFAULT_BYBE_SIZE = 2048;

    /**
     * Node types that return data
     */
    public static final int TYPE_RESULT_NODE_PROGRESS = 0x01;
    public static final int TYPE_RESULT_NODE_FILE = 0x02;
    public static final int TYPE_RESULT_NODE_RESPONSE = 0x03;

}
