package cn.xiaoxige.autonet_api;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest;
import io.reactivex.FlowableTransformer;

/**
 * @author by zhuxiaoan on 2018/5/17 0017.
 *         The Api facade class of AutoNet, the starting position of all methods.
 */

public final class AutoNet {

    private static AutoNet sAutoNet;

    private AutoNet() {
    }

    public static AutoNet getInstance() {

        synchronized (sAutoNet) {
            if (sAutoNet == null) {
                sAutoNet = new AutoNet();
            }
            return sAutoNet;
        }

    }


    /**
     * Collect and undertake and Distribution and execution
     *
     * @param requestEntity
     * @param domainNameKey
     * @param suffixUrl
     * @param mediaType
     * @param writeOutTime
     * @param readOutTime
     * @param connectOutTime
     * @param encryptionKey
     * @param isEncryption
     * @param netPattern
     * @param reqType
     * @param resType
     * @param netStrategy
     * @param callBack
     * @param pushFileKey
     * @param filePath
     * @param fileName
     * @param transformer
     */
    public static final void startNet(IAutoNetRequest requestEntity,
                                      String domainNameKey, String suffixUrl,
                                      String mediaType, Long writeOutTime, Long readOutTime,
                                      Long connectOutTime, Long encryptionKey, Boolean isEncryption,
                                      AutoNetPatternAnontation.NetPattern netPattern, AutoNetTypeAnontation.Type reqType,
                                      AutoNetTypeAnontation.Type resType, AutoNetStrategyAnontation.NetStrategy netStrategy,
                                      IAutoNetCallBack callBack, String pushFileKey, String filePath, String fileName,
                                      FlowableTransformer transformer) {

    }

    public static final void test(IAutoNetCallBack callback, Integer type) {

        if (callback != null && type != null) {
            switch (type) {
                case 1:
                    //noinspection unchecked
                    callback.onSuccess(null);
                    break;
                case 2:
                    callback.onFailed(null);
                    break;
                case 3:
                    callback.onEmpty();
                    break;
                default:
                    break;
            }
        }
    }

}
