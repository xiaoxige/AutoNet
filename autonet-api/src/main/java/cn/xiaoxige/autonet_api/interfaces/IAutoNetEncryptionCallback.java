package cn.xiaoxige.autonet_api.interfaces;

/**
 * @author by zhuxiaoan on 2018/5/21 0021.
 *         AutoNet's request parameter encrypted callback
 */

public interface IAutoNetEncryptionCallback {

    /**
     * @param key               The unique identifier of this encryption
     * @param encryptionContent This encrypted content
     * @return
     */
    String encryption(Long key, String encryptionContent);
}
