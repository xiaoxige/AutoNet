package cn.xiaoxige.autonet_api;

import android.support.v4.util.ArrayMap;

import java.util.Map;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataSuccessCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest;
import io.reactivex.FlowableTransformer;

/**
 * @author by zhuxiaoan on 2018/5/17 0017.
 *         The Api facade class of AutoNet, the starting position of all methods.
 */

public final class AutoNet {

    private static volatile AutoNet sAutoNet = new AutoNet();
    /**
     * Extra request header， Variable and influence the overall situation
     */
    private Map<String, String> mExtraHeads;

    /**
     * Extra request DomainNames, Variable and influence the overall situation
     */
    private Map<String, String> mExtraDomainNames;

    private AutoNetConfig mConfig;

    private AutoNet() {
    }

    public static AutoNet getInstance() {
        if (sAutoNet == null) {
            synchronized (AutoNet.class) {
                if (sAutoNet == null) {
                    sAutoNet = new AutoNet();
                }
            }
        }
        return sAutoNet;
    }

    public Builder initAutoNet(AutoNetConfig config) {
        mConfig = config;
        mExtraHeads = new ArrayMap<>();
        mExtraDomainNames = new ArrayMap<>();
        return new Builder();
    }

    /**
     * Collect and undertake and Distribution and execution
     *
     * @param requestEntity
     * @param extraDynamicParam
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
    public static final void startNet(IAutoNetRequest requestEntity, String extraDynamicParam,
                                      String domainNameKey, String suffixUrl,
                                      String mediaType, Long writeOutTime, Long readOutTime,
                                      Long connectOutTime, Long encryptionKey, Boolean isEncryption,
                                      AutoNetPatternAnontation.NetPattern netPattern, AutoNetTypeAnontation.Type reqType,
                                      AutoNetTypeAnontation.Type resType, AutoNetStrategyAnontation.NetStrategy netStrategy,
                                      IAutoNetCallBack callBack, String pushFileKey, String filePath, String fileName,
                                      FlowableTransformer transformer) {

    }

    /**
     * 测试连通性
     *
     * @param callback
     * @param type
     */
    public static final void test(IAutoNetCallBack callback, Integer type) {

        if (callback != null && type != null) {
            switch (type) {
                case 1:
                    if (callback instanceof IAutoNetDataSuccessCallBack) {
                        IAutoNetDataSuccessCallBack successCallBack = (IAutoNetDataSuccessCallBack) callback;
                        //noinspection unchecked
                        successCallBack.onSuccess(null);
                    }
                    break;
                case 2:
                    if (callback instanceof IAutoNetDataCallBack) {
                        IAutoNetDataCallBack dataCallBack = (IAutoNetDataCallBack) callback;
                        dataCallBack.onFailed(null);
                    }
                    break;
                case 3:
                    if (callback instanceof IAutoNetDataCallBack) {
                        IAutoNetDataCallBack dataCallBack = (IAutoNetDataCallBack) callback;
                        dataCallBack.onEmpty();
                    }
                    break;
                default:
                    break;
            }
        }
    }


    public static class Builder {
        private Builder() {
        }

    }

}
