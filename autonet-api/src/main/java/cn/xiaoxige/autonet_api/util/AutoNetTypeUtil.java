package cn.xiaoxige.autonet_api.util;

import cn.xiaoxige.annotation.AutoNetTypeAnontation;

/**
 * @author xiaoxige
 * @date 2019/6/17 0017 11:39
 * -
 * email: xiaoxigexiaoan@outlook.com
 * desc: Tool class of AutoNet type
 * <p>
 * 1. Common request mode (non-file operation)
 * <p>
 * 2. Upload files
 * <p>
 * 3. Download files
 */
public class AutoNetTypeUtil {

    /**
     * json ?
     *
     * @param reqType
     * @return
     */
    public static boolean isJsonOperation(AutoNetTypeAnontation.Type reqType) {
        return reqType.equals(AutoNetTypeAnontation.Type.JSON);
    }

    /**
     * form ?
     *
     * @param reqType
     * @return
     */
    public static boolean isFormOperation(AutoNetTypeAnontation.Type reqType) {
        return reqType.equals(AutoNetTypeAnontation.Type.FORM);
    }

    /**
     * upload or download file ?
     *
     * @param reqType
     * @param resType
     * @return
     */
    public static boolean isFileOperation(AutoNetTypeAnontation.Type reqType, AutoNetTypeAnontation.Type resType) {
        return isPullFileOperation(resType) || isPushFileOperation(reqType);
    }

    /**
     * upload file ?
     *
     * @param reqType
     * @return
     */
    public static boolean isPushFileOperation(AutoNetTypeAnontation.Type reqType) {
        return reqType.equals(AutoNetTypeAnontation.Type.STREAM);
    }

    /**
     * download file ?
     *
     * @param resType
     * @return
     */
    public static boolean isPullFileOperation(AutoNetTypeAnontation.Type resType) {
        return resType.equals(AutoNetTypeAnontation.Type.STREAM);
    }

}
