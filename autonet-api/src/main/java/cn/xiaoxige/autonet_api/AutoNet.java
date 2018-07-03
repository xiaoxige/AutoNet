package cn.xiaoxige.autonet_api;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;

import java.io.File;
import java.util.Map;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.annotation.entity.ProxyInfo;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.constant.AutoNetConstant;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetBodyCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataSuccessCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetHeadCallBack;
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

    public IAutoNetExtraConfig initAutoNet(Context context, AutoNetConfig config) {
        sConfig = config;
        sAutoNetExtraConfig = new AutoNetExtraConfig();
        init(context);
        return sAutoNetExtraConfig;
    }

    public IAutoNetExtraConfig updateOrInsertHead(String key, String value) {
        return sAutoNetExtraConfig.updateOrInsertHead(key, value);
    }

    public IAutoNetExtraConfig removeHead(String key) {
        return sAutoNetExtraConfig.removeHead(key);
    }

    public IAutoNetExtraConfig updateOrInsertDomainNames(String key, String value) {
        return sAutoNetExtraConfig.updateOrInsertDomainNames(key, value);
    }

    public IAutoNetExtraConfig removeDomainName(String key) {
        return sAutoNetExtraConfig.removeDomainName(key);
    }

    public IAutoNetExtraConfig setExtraHeads(Map<String, String> extraHeads) {
        return sAutoNetExtraConfig.setExtraHeads(extraHeads);
    }

    public IAutoNetExtraConfig setExtraDomainNames(Map<String, String> extraDomainNames) {
        return sAutoNetExtraConfig.setExtraDomainNames(extraDomainNames);
    }

    public IAutoNetNonAnontation createNet() {
        return new AutoNetNonAnontation();
    }

    private void init(Context context) {
        AutoNetConstant.sAutoNetContext = context;
        boolean openStetho = sConfig.isOpenStetho();
        if (openStetho) {
            Stetho.initializeWithDefaults(context);
        }
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
    public static final void startNet(IAutoNetRequest requestEntity, Map requestMap, String extraDynamicParam,
                                      String domainNameKey, String suffixUrl,
                                      String mediaType, Long writeOutTime, Long readOutTime,
                                      Long connectOutTime, Long encryptionKey, Boolean isEncryption,
                                      String disposableBaseUrl, String disposableHeads,
                                      AutoNetPatternAnontation.NetPattern netPattern, AutoNetTypeAnontation.Type reqType,
                                      AutoNetTypeAnontation.Type resType, AutoNetStrategyAnontation.NetStrategy netStrategy,
                                      String responseClazzName, IAutoNetCallBack callBack, String pushFileKey, String filePath, String fileName,
                                      FlowableTransformer transformer) {

        if (sConfig == null) {
            throw new IllegalArgumentException("Please initialize first.");
        }

        if (!checkLegitimate(netPattern, reqType, resType, pushFileKey, filePath, fileName)) {
            return;
        }

        Map<String, String> heads = integrationHeads(sConfig.getHeadParam(), sAutoNetExtraConfig.getExtraHeads(), disposableHeads);
        String url = getUrlByRequest(domainNameKey, sConfig.getDomainNames(), sAutoNetExtraConfig.getExtraDomainNames(), disposableBaseUrl, suffixUrl);
        mediaType = autoAdjustmentAdjustmentMediaType(mediaType, reqType);

        AutoNetExecutor executor = new AutoNetExecutor(requestEntity, requestMap, extraDynamicParam, url, mediaType,
                writeOutTime, readOutTime, connectOutTime, encryptionKey, isEncryption, sConfig.getInterceptors(),
                heads, responseClazzName, transformer,
                sAutoNetExtraConfig.getEncryptionCallback(), sAutoNetExtraConfig.getHeadCallBack(), sAutoNetExtraConfig.getBodyCallBack(), callBack);

        if (isPushFileOperation(reqType, pushFileKey, filePath)) {
            executor.pushFile(pushFileKey, filePath);
        } else if (isPullFileOperation(resType, filePath, fileName)) {
            executor.pullFile(filePath, fileName);
        } else {


            if (netStrategy.equals(AutoNetStrategyAnontation.NetStrategy.NET)) {
                executor.netOpt(netPattern);
            } else if (netStrategy.equals(AutoNetStrategyAnontation.NetStrategy.LOCAL_NET)) {
                executor.doLocalNet(netPattern);
            } else if (netStrategy.equals(AutoNetStrategyAnontation.NetStrategy.LOCAL)) {
                executor.doLocal();
            } else if (netStrategy.equals(AutoNetStrategyAnontation.NetStrategy.NET_LOCAL)) {
                executor.doNetLocal(netPattern);
            }

        }
    }

    /**
     * Distribute execution network requests
     *
     * @param executor
     * @param netPattern
     */
    @Deprecated
    private static void doNet(AutoNetExecutor executor, AutoNetPatternAnontation.NetPattern netPattern) {
        if (netPattern.equals(AutoNetPatternAnontation.NetPattern.GET)) {
            executor.doNetGet();
        } else if (netPattern.equals(AutoNetPatternAnontation.NetPattern.POST)) {
            executor.doNetPost();
        } else if (netPattern.equals(AutoNetPatternAnontation.NetPattern.DELETE)) {
            executor.doNetDelete();
        } else if (netPattern.equals(AutoNetPatternAnontation.NetPattern.PUT)) {
            executor.doNetPut();
        }
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
        if (netPattern.equals(AutoNetPatternAnontation.NetPattern.OTHER_PATTERN)) {
            throw new IllegalArgumentException("AutoNet does not support the request method for the time being.");
        }
        if (reqType.equals(AutoNetTypeAnontation.Type.OTHER_TYPE) || resType.equals(AutoNetTypeAnontation.Type.OTHER_TYPE)) {
            throw new IllegalArgumentException("AutoNet temporarily does not support the type of this request.");
        }
        if (isFileOperation(reqType, resType, pushFileKey, filePath, fileName)) {
            if (isPushFileOperation(reqType, pushFileKey, filePath) &&
                    !checkPushFileOperationLegitimate(netPattern, reqType, pushFileKey, filePath)) {
                return false;
            }
            if (isPullFileOperation(resType, filePath, fileName) &&
                    !checkPullFileOperationLegitimate(resType, filePath, fileName)) {
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
     * @param pushFileKey
     * @param filePath
     * @param fileName    @return
     */
    private static boolean isFileOperation(AutoNetTypeAnontation.Type reqType, AutoNetTypeAnontation.Type resType, String pushFileKey, String filePath, String fileName) {
        boolean isStream = reqType.equals(AutoNetTypeAnontation.Type.STREAM) || resType.equals(AutoNetTypeAnontation.Type.STREAM);
        if (isStream) {
            if (reqType.equals(AutoNetTypeAnontation.Type.STREAM) && resType.equals(AutoNetTypeAnontation.Type.STREAM)) {
                if (!TextUtils.isEmpty(pushFileKey) && !TextUtils.isEmpty(filePath) && !TextUtils.isEmpty(fileName)) {
                    throw new IllegalArgumentException("AutoNet can't tell whether to send files or download files.");
                }
            }

            if (TextUtils.isEmpty(filePath)) {
                throw new IllegalArgumentException("AutoNet's file flow operation filePath can't be empty.");
            }
            if (TextUtils.isEmpty(pushFileKey) && TextUtils.isEmpty(fileName)) {
                throw new IllegalArgumentException("AutoNet file flow operation file parameter is incomplete.");
            }
            return true;
        }
        return false;
    }

    /**
     * Determine whether the file is uploaded
     *
     * @param reqType
     * @return
     */
    private static boolean isPushFileOperation(AutoNetTypeAnontation.Type reqType, String pushFileKey, String filePath) {
        return reqType.equals(AutoNetTypeAnontation.Type.STREAM) && !TextUtils.isEmpty(pushFileKey) && !TextUtils.isEmpty(filePath);
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
        return true;
    }


    /**
     * Determine whether it is a download file operation
     *
     * @param resType
     * @return
     */
    private static boolean isPullFileOperation(AutoNetTypeAnontation.Type resType, String filePath, String fileName) {
        return resType.equals(AutoNetTypeAnontation.Type.STREAM) && !TextUtils.isEmpty(filePath) && !TextUtils.isEmpty(fileName);
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


    private static class AutoNetExtraConfig implements IAutoNetExtraConfig {
        /**
         * Extra request header， Variable and influence the overall situation
         */
        private Map<String, String> mExtraHeads;

        /**
         * Extra request DomainNames, Variable and influence the overall situation
         */
        private Map<String, String> mExtraDomainNames;

        private IAutoNetEncryptionCallback mEncryptionCallback;

        private IAutoNetHeadCallBack mHeadCallBack;

        private IAutoNetBodyCallBack mBodyCallBack;

        private AutoNetExtraConfig() {
            mExtraHeads = new ArrayMap<>();
            mExtraDomainNames = new ArrayMap<>();
        }

        @Override
        public IAutoNetExtraConfig setExtraHeads(Map<String, String> extraHeads) {
            this.mExtraHeads = extraHeads;
            return this;
        }

        @Override
        public IAutoNetExtraConfig setExtraDomainNames(Map<String, String> extraDomainNames) {
            this.mExtraDomainNames = extraDomainNames;
            return this;
        }

        @Override
        public IAutoNetExtraConfig setEncryptionCallback(IAutoNetEncryptionCallback encryptionCallback) {
            this.mEncryptionCallback = encryptionCallback;
            return this;
        }

        @Override
        public IAutoNetExtraConfig setHeadsCallback(IAutoNetHeadCallBack headsCallback) {
            this.mHeadCallBack = headsCallback;
            return this;
        }

        @Override
        public IAutoNetExtraConfig setBodyCallback(IAutoNetBodyCallBack bodyCallback) {
            this.mBodyCallBack = bodyCallback;
            return this;
        }


        public Map<String, String> getExtraHeads() {
            return mExtraHeads;
        }

        public Map<String, String> getExtraDomainNames() {
            return mExtraDomainNames;
        }

        public IAutoNetEncryptionCallback getEncryptionCallback() {
            return mEncryptionCallback;
        }

        public IAutoNetHeadCallBack getHeadCallBack() {
            return mHeadCallBack;
        }

        public IAutoNetBodyCallBack getBodyCallBack() {
            return mBodyCallBack;
        }

        @Override
        public IAutoNetExtraConfig updateOrInsertHead(String key, String value) {
            this.mExtraHeads.put(key, value);
            return this;
        }

        @Override
        public IAutoNetExtraConfig removeHead(String key) {
            this.getExtraHeads().remove(key);
            return this;
        }

        @Override
        public IAutoNetExtraConfig updateOrInsertDomainNames(String key, String value) {
            this.mExtraDomainNames.put(key, value);
            return this;
        }

        @Override
        public IAutoNetExtraConfig removeDomainName(String key) {
            this.mExtraDomainNames.remove(key);
            return this;
        }

    }

    private static class AutoNetNonAnontation implements IAutoNetNonAnontation {

        private ProxyInfo info;
        private IAutoNetRequest requestEntity;
        private Map requestMap;
        private String extraDynamicParam;
        String pushFileKey;
        String filePath;
        String fileName;
        FlowableTransformer transformer;
        private StringBuffer disposableHeads;

        private AutoNetNonAnontation() {
            requestMap = new ArrayMap();
            info = new ProxyInfo();
        }

        @Override
        public IAutoNetNonAnontation setRequestEntity(IAutoNetRequest requestEntity) {
            this.requestEntity = requestEntity;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setExtraDynamicParam(String extraDynamicParam) {
            this.extraDynamicParam = extraDynamicParam;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setDomainNameKey(String domainNameKey) {
            this.info.domainNameKey = domainNameKey;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setBaseUrl(String baseUrl) {
            this.info.disposableBaseUrl = baseUrl;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setSuffixUrl(String suffixUrl) {
            this.info.suffixUrl = suffixUrl;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setMediaType(String mediaType) {
            this.info.mediaType = mediaType;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setWriteOutTime(Long outTime) {
            this.info.writeOutTime = outTime;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setReadOutTime(Long outTime) {
            this.info.readOutTime = outTime;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setConnectOutTime(Long outTime) {
            this.info.connectOutTime = outTime;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setEncryptionKey(Long encryptionKey) {
            this.info.encryptionKey = encryptionKey;
            return this;
        }

        @Override
        public IAutoNetNonAnontation isEncryption(boolean isEncryption) {
            this.info.isEncryption = isEncryption;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setHeads(String[] heads) {
            this.info.disposableHeads = heads;
            this.disposableHeads = transformationHeads(heads);
            return this;
        }

        @Override
        public IAutoNetNonAnontation setNetPattern(AutoNetPatternAnontation.NetPattern netPattern) {
            this.info.netPattern = netPattern;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setReqType(AutoNetTypeAnontation.Type reqType) {
            this.info.reqType = reqType;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setResType(AutoNetTypeAnontation.Type resType) {
            this.info.resType = resType;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setNetStrategy(AutoNetStrategyAnontation.NetStrategy netStrategy) {
            this.info.netStrategy = netStrategy;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setPushFileParams(String pushFileKey, String filePath) {
            this.pushFileKey = pushFileKey;
            this.filePath = filePath;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setPullFileParams(String filePath, String fileName) {
            this.filePath = filePath;
            this.fileName = fileName;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setResponseClazz(Class clazz) {
            if (clazz != null) {
                this.info.responseClazzName = clazz.getName();
            }
            return this;
        }

        @Override
        public IAutoNetNonAnontation setTransformer(FlowableTransformer transformer) {
            this.transformer = transformer;
            return this;
        }

        @Override
        public IAutoNetNonAnontation doGet() {
            return doGet(null);
        }

        @Override
        public IAutoNetNonAnontation doPost() {
            return doPost(null);
        }

        @Override
        public IAutoNetNonAnontation doDelete() {
            return doDelete(null);
        }

        @Override
        public IAutoNetNonAnontation doPut() {
            return doPut(null);
        }

        @Override
        public IAutoNetNonAnontation doGet(IAutoNetRequest request) {
            this.info.netPattern = AutoNetPatternAnontation.NetPattern.GET;
            this.requestEntity = request;
            return this;
        }

        @Override
        public IAutoNetNonAnontation doPost(IAutoNetRequest request) {
            this.info.netPattern = AutoNetPatternAnontation.NetPattern.POST;
            this.requestEntity = request;
            return this;
        }

        @Override
        public IAutoNetNonAnontation doDelete(IAutoNetRequest request) {
            this.info.netPattern = AutoNetPatternAnontation.NetPattern.DELETE;
            this.requestEntity = request;
            return this;
        }

        @Override
        public IAutoNetNonAnontation doPut(IAutoNetRequest request) {
            this.info.netPattern = AutoNetPatternAnontation.NetPattern.PUT;
            this.requestEntity = request;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setParam(String key, Object value) {
            requestMap.put(key, value);
            return this;
        }

        @Override
        public IAutoNetNonAnontation setParams(Map params) {
            requestMap.putAll(params);
            return this;
        }

        @Override
        public void start(IAutoNetCallBack callBack) {
            startNet(requestEntity, requestMap, extraDynamicParam, info.domainNameKey, info.suffixUrl, info.mediaType,
                    info.writeOutTime, info.readOutTime, info.connectOutTime, info.encryptionKey, info.isEncryption, info.disposableBaseUrl,
                    disposableHeads == null ? null : disposableHeads.toString(),
                    info.netPattern, info.reqType, info.resType, info.netStrategy, info.responseClazzName, callBack, pushFileKey, filePath, fileName, transformer);
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

        IAutoNetExtraConfig setExtraHeads(Map<String, String> extraHeads);

        IAutoNetExtraConfig setExtraDomainNames(Map<String, String> extraDomainNames);

        IAutoNetExtraConfig updateOrInsertHead(String key, String value);

        IAutoNetExtraConfig removeHead(String key);

        IAutoNetExtraConfig updateOrInsertDomainNames(String key, String value);

        IAutoNetExtraConfig removeDomainName(String key);

        IAutoNetExtraConfig setEncryptionCallback(IAutoNetEncryptionCallback encryptionCallback);

        IAutoNetExtraConfig setHeadsCallback(IAutoNetHeadCallBack headsCallback);

        IAutoNetExtraConfig setBodyCallback(IAutoNetBodyCallBack bodyCallback);

    }

    public interface IAutoNetNonAnontation {

        IAutoNetNonAnontation setRequestEntity(IAutoNetRequest requestEntity);

        IAutoNetNonAnontation setExtraDynamicParam(String extraDynamicParam);

        IAutoNetNonAnontation setDomainNameKey(String domainNameKey);

        IAutoNetNonAnontation setBaseUrl(String baseUrl);

        IAutoNetNonAnontation setSuffixUrl(String suffixUrl);

        IAutoNetNonAnontation setMediaType(String mediaType);

        IAutoNetNonAnontation setWriteOutTime(Long outTime);

        IAutoNetNonAnontation setReadOutTime(Long outTime);

        IAutoNetNonAnontation setConnectOutTime(Long outTime);

        IAutoNetNonAnontation setEncryptionKey(Long encryptionKey);

        IAutoNetNonAnontation isEncryption(boolean isEncryption);

        IAutoNetNonAnontation setHeads(String[] heads);

        IAutoNetNonAnontation setNetPattern(AutoNetPatternAnontation.NetPattern netPattern);

        IAutoNetNonAnontation setReqType(AutoNetTypeAnontation.Type reqType);

        IAutoNetNonAnontation setResType(AutoNetTypeAnontation.Type resType);

        IAutoNetNonAnontation setNetStrategy(AutoNetStrategyAnontation.NetStrategy netStrategy);

        IAutoNetNonAnontation setPushFileParams(String pushFileKey, String filePath);

        IAutoNetNonAnontation setPullFileParams(String filePath, String fileName);

        IAutoNetNonAnontation setResponseClazz(Class clazz);

        IAutoNetNonAnontation setTransformer(FlowableTransformer transformer);

        IAutoNetNonAnontation doGet();

        IAutoNetNonAnontation doPost();

        IAutoNetNonAnontation doDelete();

        IAutoNetNonAnontation doPut();

        IAutoNetNonAnontation doGet(IAutoNetRequest request);

        IAutoNetNonAnontation doPost(IAutoNetRequest request);

        IAutoNetNonAnontation doDelete(IAutoNetRequest request);

        IAutoNetNonAnontation doPut(IAutoNetRequest request);

        IAutoNetNonAnontation setParam(String key, Object value);

        IAutoNetNonAnontation setParams(Map params);

        void start(IAutoNetCallBack callBack);
    }

}
