package cn.xiaoxige.autonet_api.interfaces;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public interface IAutoNetEncryptionCallback {

    String encryption(long encryptionKey, String beforeValue);

}
