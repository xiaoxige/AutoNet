package cn.xiaoxige.autonet_api.data.responsentity;

import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * @author by zhuxiaoan on 2017/12/11 0011.
 */
public class AutoInputStreamEntity implements IResponseEntity {
    private ResponseBody mBody;
    // 输出流
    public InputStream mIs;
}
