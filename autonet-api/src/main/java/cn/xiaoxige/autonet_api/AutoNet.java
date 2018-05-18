package cn.xiaoxige.autonet_api;

import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.Map;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.annotation.entity.ProxyInfo;
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

    private static AutoNetConfig sConfig;
    private static AutoNetExtraConfig sAutoNetExtraConfig;

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

    public IAutoNetExtraConfig initAutoNet(AutoNetConfig config) {
        sConfig = config;
        sAutoNetExtraConfig = new AutoNetExtraConfig();
        return sAutoNetExtraConfig;
    }

    public IAutoNetExtraConfig updateOrInsertHead(String key, String value) {
        sAutoNetExtraConfig.updateOrInsertHead(key, value);
        return sAutoNetExtraConfig;
    }

    public IAutoNetExtraConfig updateOrInsertDomainNames(String key, String value) {
        sAutoNetExtraConfig.updateOrInsertDomainNames(key, value);
        return sAutoNetExtraConfig;
    }

    public IAutoNetExtraConfig setExtraHeads(Map<String, String> extraHeads) {
        sAutoNetExtraConfig.setExtraHeads(extraHeads);
        return sAutoNetExtraConfig;
    }

    public IAutoNetExtraConfig setExtraDomainNames(Map<String, String> extraDomainNames) {
        sAutoNetExtraConfig.setExtraDomainNames(extraDomainNames);
        return sAutoNetExtraConfig;
    }

    public AutoNetNonAnontation createNet() {
        return new AutoNetNonAnontation();
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
                                      String disposableBaseUrl, String disposableHeads,
                                      AutoNetPatternAnontation.NetPattern netPattern, AutoNetTypeAnontation.Type reqType,
                                      AutoNetTypeAnontation.Type resType, AutoNetStrategyAnontation.NetStrategy netStrategy,
                                      IAutoNetCallBack callBack, String pushFileKey, String filePath, String fileName,
                                      FlowableTransformer transformer) {

        if (!checkLegitimate(netPattern, reqType, resType, pushFileKey, filePath, fileName)) {
            return;
        }

        Map<String, String> heads = integrationHeads(sConfig.getHeadParam(), sAutoNetExtraConfig.getExtraHeads(), disposableHeads);
        String url = getUrlByRequest(domainNameKey, sConfig.getDomainNames(), sAutoNetExtraConfig.getExtraDomainNames(), disposableBaseUrl, suffixUrl);
        mediaType = autoAdjustmentAdjustmentMediaType(mediaType, reqType);
    }

    /**
     * Auto Adjustment Adjustment MediaType
     *
     * @param mediaType
     * @param reqType
     * @return
     */
    private static String autoAdjustmentAdjustmentMediaType(String mediaType, AutoNetTypeAnontation.Type reqType) {
        if (reqType.equals(AutoNetTypeAnontation.Type.STREAM)) {
            return "application/octet-stream";
        }
        return mediaType;
    }

    /**
     * It is necessary to check if it is legal
     *
     * @param netPattern
     * @param reqType
     * @param resType
     * @param pushFileKey
     * @param filePath
     * @param fileName
     * @return
     */
    private static boolean checkLegitimate(AutoNetPatternAnontation.NetPattern netPattern,
                                           AutoNetTypeAnontation.Type reqType, AutoNetTypeAnontation.Type resType,
                                           String pushFileKey, String filePath, String fileName) {
        if (isFileOperation(reqType, resType)) {
            if (!checkPushFileOperationLegitimate(netPattern, reqType, pushFileKey, filePath)) {
                return false;
            }
            if (!checkPullFileOperationLegitimate(resType, filePath, fileName)) {
                return false;
            }
        }
        return true;
    }


    /**
     * Get the URL for this request
     *
     * @param key
     * @param domainNames
     * @param extraDomainNames
     * @param disposableBaseUrl
     * @return
     */
    private static String getUrlByRequest(String key, Map<String, String> domainNames, Map<String, String> extraDomainNames, String disposableBaseUrl, String suffixUrl) {
        if (!TextUtils.isEmpty(disposableBaseUrl)) {
            return disposableBaseUrl + suffixUrl;
        }
        Map<String, String> domains = integrationBaseUrl(domainNames, extraDomainNames);
        key = TextUtils.isEmpty(key) ? "default" : key;
        String url = domains.get(key);
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("The domain name of this request is not found. Please check whether you have configured the domain name, or whether the domain name corresponding to the domain name key exists.");
        }
        return url + suffixUrl;
    }

    /**
     * Integrating all domain names.
     *
     * @param domainNames
     * @param extraDomainNames
     */
    private static Map<String, String> integrationBaseUrl(Map<String, String> domainNames, Map<String, String> extraDomainNames) {
        Map<String, String> domains = new ArrayMap<>();

        domains.putAll(domainNames);
        domains.putAll(extraDomainNames);

        return domains;
    }

    /**
     * Integrate all the heads
     * priority: sConfig.heads < sAutoNetExtraConfig.heads < annotaion.heads
     * Persistence: sConfig.heads > sAutoNetExtraConfig.heads > annotaion.heads
     *
     * @param headParam
     * @param extraHeads
     * @param disposableHeads
     * @return
     */
    private static Map<String, String> integrationHeads(Map<String, String> headParam, Map<String, String> extraHeads, String disposableHeads) {
        Map<String, String> heads = new ArrayMap<>();
        if (headParam != null) {
            heads.putAll(headParam);
        }
        if (extraHeads != null) {
            heads.putAll(extraHeads);
        }
        Map<String, String> annotationHeads = reductionAnnotationHeads(disposableHeads);
        if (annotationHeads != null) {
            heads.putAll(annotationHeads);
        }
        return heads;
    }

    /**
     * Header data of the restore annotation
     *
     * @param disposableHeads
     * @return
     */
    private static Map<String, String> reductionAnnotationHeads(String disposableHeads) {
        Map<String, String> heads = new ArrayMap<>();
        if (!TextUtils.isEmpty(disposableHeads)) {
            String[] splitHeads = disposableHeads.split("\n");
            if (splitHeads.length > 0) {
                for (String headFormat : splitHeads) {
                    String[] split = headFormat.split(":");
                    if (split.length != 2) {
                        continue;
                    }
                    heads.put(split[0], split[1]);
                }
            }
        }
        return heads;
    }

    /**
     * Determine whether it is a file operation
     *
     * @param reqType
     * @param resType
     * @return
     */
    private static boolean isFileOperation(AutoNetTypeAnontation.Type reqType, AutoNetTypeAnontation.Type resType) {
        return reqType.equals(AutoNetTypeAnontation.Type.STREAM) || resType.equals(AutoNetTypeAnontation.Type.STREAM);
    }

    /**
     * Determine whether the file is uploaded
     *
     * @param reqType
     * @param resType
     * @param pushFileKey
     * @param filePath
     * @param fileName
     * @return
     */
    private static boolean isPushFileOperation(AutoNetTypeAnontation.Type reqType, AutoNetTypeAnontation.Type resType, String pushFileKey, String filePath, String fileName) {

        return true;
    }

    /**
     * Check whether there is a problem of uploading file parameters
     *
     * @param netPattern
     * @param reqType
     * @param pushFileKey
     * @param filePath
     * @return
     */
    private static boolean checkPushFileOperationLegitimate(AutoNetPatternAnontation.NetPattern netPattern, AutoNetTypeAnontation.Type reqType, String pushFileKey, String filePath) {
        if (netPattern.equals(AutoNetPatternAnontation.NetPattern.GET) || netPattern.equals(AutoNetPatternAnontation.NetPattern.DELETE)) {
            throw new IllegalArgumentException("File upload operations cannot be requested for get or delete.");
        }
        if (!reqType.equals(AutoNetTypeAnontation.Type.STREAM)) {
            throw new IllegalArgumentException("File upload operations must be streaming.");
        }
        if (TextUtils.isEmpty(pushFileKey)) {
            throw new IllegalArgumentException("File upload operation file key can not be empty.");
        }
        if (TextUtils.isEmpty(filePath)) {
            throw new IllegalArgumentException("The file path of the file upload operation cannot be empty.");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("File uploading files do not exist.");
        }
        return true;
    }


    /**
     * Determine whether it is a download file operation
     *
     * @param reqType
     * @param resType
     * @param pushFileKey
     * @param filePath
     * @param fileName
     * @return
     */
    private static boolean isPullFileOperation(AutoNetTypeAnontation.Type reqType, AutoNetTypeAnontation.Type resType, String pushFileKey, String filePath, String fileName) {

        return true;
    }

    /**
     * Check whether there is a problem with the download file parameters
     *
     * @param resType
     * @param filePath
     * @param fileName
     * @return
     */
    private static boolean checkPullFileOperationLegitimate(AutoNetTypeAnontation.Type resType, String filePath, String fileName) {
        if (!resType.equals(AutoNetTypeAnontation.Type.STREAM)) {
            throw new IllegalArgumentException("Download File receive type must be flow.");
        }
        if (TextUtils.isEmpty(filePath)) {
            throw new IllegalArgumentException("The path of the download file is empty");
        }
        if (TextUtils.isEmpty(fileName)) {
            throw new IllegalArgumentException("The name of the file saved file can not be empty.");
        }
        return true;
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


    public static class AutoNetExtraConfig implements IAutoNetExtraConfig {
        /**
         * Extra request header， Variable and influence the overall situation
         */
        private Map<String, String> mExtraHeads;

        /**
         * Extra request DomainNames, Variable and influence the overall situation
         */
        private Map<String, String> mExtraDomainNames;

        private AutoNetExtraConfig() {
            mExtraHeads = new ArrayMap<>();
            mExtraDomainNames = new ArrayMap<>();
        }

        @Override
        public void setExtraHeads(Map<String, String> extraHeads) {
            this.mExtraHeads = extraHeads;
        }

        @Override
        public void setExtraDomainNames(Map<String, String> extraDomainNames) {
            this.mExtraDomainNames = extraDomainNames;
        }

        public Map<String, String> getExtraHeads() {
            return mExtraHeads;
        }

        public Map<String, String> getExtraDomainNames() {
            return mExtraDomainNames;
        }

        @Override
        public void updateOrInsertHead(String key, String value) {
            this.mExtraHeads.put(key, value);
        }

        @Override
        public void updateOrInsertDomainNames(String key, String value) {
            this.mExtraDomainNames.put(key, value);
        }

    }

    public static class AutoNetNonAnontation {

        private ProxyInfo info;
        private IAutoNetRequest requestEntity;
        private String extraDynamicParam;
        String pushFileKey;
        String filePath;
        String fileName;
        FlowableTransformer transformer;
        private StringBuffer disposableHeads;

        private AutoNetNonAnontation() {
            info = new ProxyInfo();
        }

        public AutoNetNonAnontation setRequestEntity(IAutoNetRequest requestEntity) {
            this.requestEntity = requestEntity;
            return this;
        }

        public AutoNetNonAnontation setExtraDynamicParam(String extraDynamicParam) {
            this.extraDynamicParam = extraDynamicParam;
            return this;
        }

        public AutoNetNonAnontation setBaseUrl(String baseUrl) {
            this.info.disposableBaseUrl = baseUrl;
            return this;
        }

        public AutoNetNonAnontation setSuffixUrl(String suffixUrl) {
            this.info.suffixUrl = suffixUrl;
            return this;
        }

        public AutoNetNonAnontation setMediaType(String mediaType) {
            this.info.mediaType = mediaType;
            return this;
        }

        public AutoNetNonAnontation setWriteOutTime(Long outTime) {
            this.info.writeOutTime = outTime;
            return this;
        }

        public AutoNetNonAnontation setReadOutTime(Long outTime) {
            this.info.readOutTime = outTime;
            return this;
        }

        public AutoNetNonAnontation setConnectOutTime(Long outTime) {
            this.info.connectOutTime = outTime;
            return this;
        }

        public AutoNetNonAnontation setEncryptionKey(Long encryptionKey) {
            this.info.encryptionKey = encryptionKey;
            return this;
        }

        public AutoNetNonAnontation isEncryption(boolean isEncryption) {
            this.info.isEncryption = isEncryption;
            return this;
        }

        public AutoNetNonAnontation setHeads(String[] heads) {
            this.info.disposableHeads = heads;
            this.disposableHeads = transformationHeads(heads);
            return this;
        }

        public AutoNetNonAnontation setNetPattern(AutoNetPatternAnontation.NetPattern netPattern) {
            this.info.netPattern = netPattern;
            return this;
        }

        public AutoNetNonAnontation setReqType(AutoNetTypeAnontation.Type reqType) {
            this.info.reqType = reqType;
            return this;
        }

        public AutoNetNonAnontation setResType(AutoNetTypeAnontation.Type resType) {
            this.info.resType = resType;
            return this;
        }

        public AutoNetNonAnontation setNetStrategy(AutoNetStrategyAnontation.NetStrategy netStrategy) {
            this.info.netStrategy = netStrategy;
            return this;
        }

        public AutoNetNonAnontation setPushFileParams(String pushFileKey, String filePath) {
            this.pushFileKey = pushFileKey;
            this.filePath = filePath;
            return this;
        }

        public AutoNetNonAnontation setPullFileParams(String filePath, String fileName) {
            this.filePath = filePath;
            this.fileName = fileName;
            return this;
        }

        public AutoNetNonAnontation setTransformer(FlowableTransformer transformer) {
            this.transformer = transformer;
            return this;
        }

        public void start(IAutoNetCallBack callBack) {
            startNet(requestEntity, extraDynamicParam, null, info.suffixUrl, info.mediaType,
                    info.writeOutTime, info.readOutTime, info.connectOutTime, info.encryptionKey, info.isEncryption, info.disposableBaseUrl,
                    disposableHeads == null ? null : disposableHeads.toString(),
                    info.netPattern, info.reqType, info.resType, info.netStrategy, callBack, pushFileKey, filePath, fileName, transformer);
        }

        /**
         * Head data conversion(String[] --> String)
         *
         * @param disposableHeads
         * @return
         */
        private StringBuffer transformationHeads(String[] disposableHeads) {
            StringBuffer heads = null;
            if (disposableHeads != null) {
                heads = new StringBuffer();
                for (String head : disposableHeads) {
                    heads.append(head + "\n");
                }
            }
            return heads;
        }

    }

    public interface IAutoNetExtraConfig {

        void setExtraHeads(Map<String, String> extraHeads);

        void setExtraDomainNames(Map<String, String> extraDomainNames);

        void updateOrInsertHead(String key, String value);

        void updateOrInsertDomainNames(String key, String value);

    }

}
