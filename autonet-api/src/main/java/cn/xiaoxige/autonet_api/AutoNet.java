package cn.xiaoxige.autonet_api;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.annotation.entity.ProxyInfo;
import cn.xiaoxige.autonet_api.abstracts.AbsAutoNetCallback;
import cn.xiaoxige.autonet_api.config.AutoNetConfig;
import cn.xiaoxige.autonet_api.constant.AutoNetConstant;
import cn.xiaoxige.autonet_api.executor.AutoNetExecutor;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetBodyCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataBeforeCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataSuccessCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetEncryptionCallback;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetFileCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetHeadCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetLocalOptCallBack;
import cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest;
import cn.xiaoxige.autonet_api.util.AutoNetTypeUtil;
import cn.xiaoxige.autonet_api.util.DataConvertorUtils;
import cn.xiaoxige.autonet_api.util.GenericParadigmUtil;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;

/**
 * @author by zhuxiaoan on 2018/5/17 0017.
 * The Api facade class of AutoNet, the starting position of all methods.
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

    public IAutoNetExtraConfig setExtraHeads(Map<String, Object> extraHeads) {
        return sAutoNetExtraConfig.setExtraHeads(extraHeads);
    }

    public IAutoNetExtraConfig setExtraDomainNames(Map<String, String> extraDomainNames) {
        return sAutoNetExtraConfig.setExtraDomainNames(extraDomainNames);
    }

    private void init(Context context) {
        AutoNetConstant.sAutoNetContext = context;
        boolean openStetho = sConfig.isOpenStetho();
        if (openStetho) {
            Stetho.initializeWithDefaults(context);
        }
    }

    public IAutoNetNonAnontation createNet() {
        return new AutoNetNonAnontation();
    }

    /**
     * AutoNet initiates synchronization requests
     *
     * @param requestEntity
     * @param requestMap
     * @param extraDynamicParam
     * @param suffixUrl
     * @param flag
     * @param writeOutTime
     * @param readOutTime
     * @param connectOutTime
     * @param domainNameKey
     * @param disposableBaseUrl
     * @param disposableHeads
     * @param encryptionKey
     * @param isEncryption
     * @param mediaType
     * @param netPattern
     * @param responseClazz
     * @param netStrategy
     * @param reqType
     * @param resType
     * @param pushFileKey
     * @param filePath
     * @param fileName
     * @param accompanyFileCallback
     * @param accompanyLocalOptCallback
     * @param transformer
     * @param <T>
     * @return
     * @throws Exception
     */
    private <T> T startSynchronizationNet(IAutoNetRequest requestEntity, Map requestMap,
                                          String extraDynamicParam, String suffixUrl,
                                          Object flag, long writeOutTime, long readOutTime,
                                          long connectOutTime, String domainNameKey,
                                          String disposableBaseUrl, String disposableHeads,
                                          long encryptionKey, Boolean isEncryption, String mediaType,
                                          AutoNetPatternAnontation.NetPattern netPattern, Class<T> responseClazz,
                                          AutoNetStrategyAnontation.NetStrategy netStrategy,
                                          AutoNetTypeAnontation.Type reqType,
                                          AutoNetTypeAnontation.Type resType, String pushFileKey,
                                          String filePath, String fileName, IAutoNetFileCallBack accompanyFileCallback, IAutoNetLocalOptCallBack accompanyLocalOptCallback, FlowableTransformer transformer) throws Exception {

        assertCommon(netPattern, netStrategy, reqType, resType, responseClazz, pushFileKey, filePath, fileName, null);
        assertSynchronization(netStrategy);

        Class<?> responseClass = assertResponseClass(responseClazz);
        //noinspection unchecked
        AutoNetExecutor<T, ?> autoNetExecutor = (AutoNetExecutor<T, ?>) structuralExecutor(requestEntity, requestMap,
                extraDynamicParam, suffixUrl,
                flag, writeOutTime, readOutTime,
                connectOutTime, domainNameKey, disposableBaseUrl, disposableHeads, encryptionKey, isEncryption, mediaType,
                netPattern, responseClass, netStrategy, responseClass, reqType,
                resType, pushFileKey, filePath, fileName, accompanyFileCallback, accompanyLocalOptCallback, null, transformer);

        return autoNetExecutor.synchronizationNet();
    }

    /**
     * AutoNet获取上游发射器， 提供给用户自定义下游的
     *
     * @param requestEntity
     * @param requestMap
     * @param extraDynamicParam
     * @param suffixUrl
     * @param flag
     * @param writeOutTime
     * @param readOutTime
     * @param connectOutTime
     * @param domainNameKey
     * @param disposableBaseUrl
     * @param disposableHeads
     * @param encryptionKey
     * @param isEncryption
     * @param mediaType
     * @param netPattern
     * @param responseClazz
     * @param netStrategy
     * @param reqType
     * @param resType
     * @param pushFileKey
     * @param filePath
     * @param fileName
     * @param accompanyFileCallback
     * @param accompanyLocalOptCallback
     * @param transformer
     * @param <T>
     * @return
     */
    private <T> Flowable createFlowable(IAutoNetRequest requestEntity, Map requestMap,
                                        String extraDynamicParam, String suffixUrl,
                                        Object flag, long writeOutTime, long readOutTime,
                                        long connectOutTime, String domainNameKey,
                                        String disposableBaseUrl, String disposableHeads,
                                        long encryptionKey, Boolean isEncryption, String mediaType,
                                        AutoNetPatternAnontation.NetPattern netPattern, Class<T> responseClazz,
                                        AutoNetStrategyAnontation.NetStrategy netStrategy,
                                        AutoNetTypeAnontation.Type reqType,
                                        AutoNetTypeAnontation.Type resType, String pushFileKey,
                                        String filePath, String fileName, IAutoNetFileCallBack accompanyFileCallback, IAutoNetLocalOptCallBack accompanyLocalOptCallback, FlowableTransformer transformer) {
        assertCommon(netPattern, netStrategy, reqType, resType, responseClazz, pushFileKey, filePath, fileName, null);
        assertFlowable(netStrategy);

        Class<?> responseClass = assertResponseClass(responseClazz);
        //noinspection unchecked
        AutoNetExecutor<T, ?> autoNetExecutor = (AutoNetExecutor<T, ?>) structuralExecutor(requestEntity, requestMap,
                extraDynamicParam, suffixUrl,
                flag, writeOutTime, readOutTime,
                connectOutTime, domainNameKey, disposableBaseUrl, disposableHeads, encryptionKey, isEncryption, mediaType,
                netPattern, responseClass, netStrategy, responseClass, reqType,
                resType, pushFileKey, filePath, fileName, accompanyFileCallback, accompanyLocalOptCallback, null, transformer);

        return autoNetExecutor.structureFlowable(false);
    }

    /**
     * AutoNet initiates asynchronous requests
     *
     * @param requestEntity
     * @param requestMap
     * @param extraDynamicParam
     * @param suffixUrl
     * @param flag
     * @param writeOutTime
     * @param readOutTime
     * @param connectOutTime
     * @param domainNameKey
     * @param disposableBaseUrl
     * @param disposableHeads
     * @param encryptionKey
     * @param isEncryption
     * @param mediaType
     * @param netPattern
     * @param netStrategy
     * @param reqType
     * @param resType
     * @param pushFileKey
     * @param filePath
     * @param fileName
     * @param accompanyFileCallback
     * @param accompanyLocalOptCallback
     * @param callBack
     * @param transformer
     */
    public void startNet(IAutoNetRequest requestEntity, Map requestMap,
                         String extraDynamicParam, String suffixUrl,
                         Object flag, long writeOutTime, long readOutTime,
                         long connectOutTime, String domainNameKey,
                         String disposableBaseUrl, String disposableHeads,
                         long encryptionKey, Boolean isEncryption, String mediaType,
                         AutoNetPatternAnontation.NetPattern netPattern,
                         AutoNetStrategyAnontation.NetStrategy netStrategy,
                         AutoNetTypeAnontation.Type reqType,
                         AutoNetTypeAnontation.Type resType, String pushFileKey,
                         String filePath, String fileName,
                         IAutoNetFileCallBack accompanyFileCallback, IAutoNetLocalOptCallBack accompanyLocalOptCallback, IAutoNetCallBack callBack, FlowableTransformer transformer) {

        Class responseClass = integrationResponseClass(callBack);
        assertCommon(netPattern, netStrategy, reqType, resType, responseClass, pushFileKey, filePath, fileName, callBack);
        assertStartNet(accompanyLocalOptCallback, callBack);

        AutoNetExecutor<?, ?> autoNetExecutor = structuralExecutor(requestEntity, requestMap,
                extraDynamicParam, suffixUrl,
                flag, writeOutTime, readOutTime,
                connectOutTime, domainNameKey, disposableBaseUrl, disposableHeads, encryptionKey, isEncryption, mediaType,
                netPattern, responseClass, netStrategy, integrationTargetClass(callBack, responseClass), reqType,
                resType, pushFileKey, filePath, fileName, accompanyFileCallback, accompanyLocalOptCallback, callBack, transformer);

        autoNetExecutor.net();
    }

    /**
     * Constructing AutuNet Real Executors
     *
     * @param requestEntity
     * @param requestMap
     * @param extraDynamicParam
     * @param suffixUrl
     * @param flag
     * @param writeOutTime
     * @param readOutTime
     * @param connectOutTime
     * @param domainNameKey
     * @param disposableBaseUrl
     * @param disposableHeads
     * @param encryptionKey
     * @param isEncryption
     * @param mediaType
     * @param netPattern
     * @param responseClazz
     * @param netStrategy
     * @param targetClazz
     * @param reqType
     * @param resType
     * @param pushFileKey
     * @param filePath
     * @param fileName
     * @param accompanyFileCallback
     * @param accompanyLocalOptCallback
     * @param callBack
     * @param transformer
     * @param <T>
     * @param <Z>
     * @return
     */
    private <T, Z> AutoNetExecutor<T, Z> structuralExecutor(IAutoNetRequest requestEntity, Map requestMap,
                                                            String extraDynamicParam, String suffixUrl,
                                                            Object flag, long writeOutTime, long readOutTime,
                                                            long connectOutTime, String domainNameKey,
                                                            String disposableBaseUrl, String disposableHeads,
                                                            long encryptionKey, Boolean isEncryption, String mediaType,
                                                            AutoNetPatternAnontation.NetPattern netPattern, Class<T> responseClazz,
                                                            AutoNetStrategyAnontation.NetStrategy netStrategy, Class<Z> targetClazz,
                                                            AutoNetTypeAnontation.Type reqType,
                                                            AutoNetTypeAnontation.Type resType, String pushFileKey,
                                                            String filePath, String fileName,
                                                            IAutoNetFileCallBack accompanyFileCallback, IAutoNetLocalOptCallBack accompanyLocalOptCallback, IAutoNetCallBack callBack, FlowableTransformer transformer) {

        // Merge header data
        Map<String, Object> heads = integrationHeads(sConfig.getHeadParam(), sAutoNetExtraConfig.getExtraHeads(), disposableHeads);
        // Merge collation request addresses
        String url = getUrlByRequest(domainNameKey, sConfig.getDomainNames(), sAutoNetExtraConfig.getExtraDomainNames(), disposableBaseUrl, suffixUrl, extraDynamicParam);
        // Automatically adjust the type if not specified manually
        mediaType = TextUtils.isEmpty(mediaType) ? autoAdjustmentAdjustmentMediaType(mediaType, reqType) : mediaType;
        // Merge request params
        Map params = integrationParams(requestEntity, requestMap);

        //noinspection unchecked
        return new AutoNetExecutor(url, heads, params, flag, writeOutTime, readOutTime, connectOutTime,
                encryptionKey, isEncryption, sConfig.getInterceptors(),
                mediaType,
                netPattern,
                responseClazz,
                netStrategy,
                targetClazz,
                reqType, resType,
                pushFileKey, filePath, fileName,
                accompanyFileCallback, accompanyLocalOptCallback,
                sAutoNetExtraConfig.getEncryptionCallback(), sAutoNetExtraConfig.getHeadCallBack(), sAutoNetExtraConfig.getBodyCallBack(),
                callBack, transformer);
    }

    /**
     * Check whether AutoNet public parts are legal
     *
     * @param netPattern
     * @param reqType
     * @param resType
     * @param pushFileKey
     * @param filePath
     * @param fileName
     */
    private void assertCommon(AutoNetPatternAnontation.NetPattern netPattern, AutoNetStrategyAnontation.NetStrategy netStrategy,
                              AutoNetTypeAnontation.Type reqType, AutoNetTypeAnontation.Type resType, Class<?> responseClazz,
                              String pushFileKey, String filePath, String fileName, IAutoNetCallBack callBack) {
        // 1. check is initialize
        if (sConfig == null) {
            throw new IllegalArgumentException("Please initialize first.");
        }

        // 2. Autonet checks if this version is supported
        if (netPattern.equals(AutoNetPatternAnontation.NetPattern.OTHER_PATTERN)) {
            throw new IllegalArgumentException("Current AutoNet version does not support this request mode for the time being.");
        }
        if (reqType.equals(AutoNetTypeAnontation.Type.OTHER_TYPE) || resType.equals(AutoNetTypeAnontation.Type.OTHER_TYPE)) {
            throw new IllegalArgumentException("Current AutoNet version does not support this request type for the time being.");
        }

        // 3. check some request param
        // 3.1. => Can't send and receive files at the same time
        if (AutoNetTypeUtil.isPushFileOperation(reqType) && AutoNetTypeUtil.isPullFileOperation(resType)) {
            throw new IllegalArgumentException("Autonet does not accept sending and receiving files at the same time.");
        }
        // 3.2. => If it is file operation, it does not support network local, local network, local policy.
        if (AutoNetTypeUtil.isFileOperation(reqType, resType)) {
            if (netStrategy.equals(AutoNetStrategyAnontation.NetStrategy.NET_LOCAL)
                    || netStrategy.equals(AutoNetStrategyAnontation.NetStrategy.LOCAL_NET)
                    || netStrategy.equals(AutoNetStrategyAnontation.NetStrategy.LOCAL)) {
                throw new IllegalArgumentException("If it is file operation, it does not support network local, local network, local policy.");
            }
        }

        // 3.3. => check file operation
        if (AutoNetTypeUtil.isPushFileOperation(reqType)) {
            // 3.3.1. => Uploading files does not support request for get or delete
            if (netPattern.equals(AutoNetPatternAnontation.NetPattern.GET)
                    || netPattern.equals(AutoNetPatternAnontation.NetPattern.DELETE)) {
                throw new IllegalArgumentException("Uploading files does not support request for get or delete");
            }
            // 3.3.2. => Necessary parameters for uploading files cannot be empty
            if (TextUtils.isEmpty(pushFileKey) || TextUtils.isEmpty(filePath)) {
                throw new IllegalArgumentException("Push file operation, pushFileKey and filePath cannot be empty.");
            }

            // 3.3.3. => Uploading documents must guarantee the existence of documents
            if (!new File(filePath).exists()) {
                throw new IllegalArgumentException("The push file does not exist.");
            }
        } else if (AutoNetTypeUtil.isPullFileOperation(resType)) {
            // 3.3.4. => Necessary parameters for downloading files cannot be empty
            if (TextUtils.isEmpty(filePath) || TextUtils.isEmpty(fileName)) {
                throw new IllegalArgumentException("Please specify the location and name of the download file.");
            }
            // 3.3.5. => The download file must be File if the return value is specified
            if (callBack == null && responseClazz != null && !File.class.equals(responseClazz) && !Object.class.equals(responseClazz)) {
                throw new IllegalArgumentException("The download file must be File if the return value is specified");
            }
        }
    }

    /**
     * Check the validity of synchronization requests
     *
     * @param netStrategy
     */
    private void assertSynchronization(AutoNetStrategyAnontation.NetStrategy netStrategy) {
        // Check whether the policy is legitimate
        if (netStrategy.equals(AutoNetStrategyAnontation.NetStrategy.LOCAL_NET) || netStrategy.equals(AutoNetStrategyAnontation.NetStrategy.NET_LOCAL)) {
            throw new IllegalArgumentException("Synchronization does not support a local-first network, network-first-local policy.");
        }
    }

    /**
     * Check the validity of flowable requests
     *
     * @param netStrategy
     */
    private void assertFlowable(AutoNetStrategyAnontation.NetStrategy netStrategy) {
        // Check whether the policy is legitimate
        if (netStrategy.equals(AutoNetStrategyAnontation.NetStrategy.LOCAL_NET) || netStrategy.equals(AutoNetStrategyAnontation.NetStrategy.NET_LOCAL)) {
            throw new IllegalArgumentException("Flowable does not support a local-first network, network-first-local policy.");
        }
    }

    private void assertStartNet(IAutoNetLocalOptCallBack accompanyLocalOptCallback, IAutoNetCallBack callBack) {
        if (callBack != null && accompanyLocalOptCallback != null) {
            if (callBack instanceof IAutoNetLocalOptCallBack) {
                throw new IllegalArgumentException("There is an intelligent callback setup for local policy.");
            }
        }
    }

    /**
     * Check and return valid class object types
     *
     * @param responseClass
     * @param <T>
     * @return
     */
    private <T> Class<?> assertResponseClass(Class<T> responseClass) {
        return responseClass == null ? String.class : responseClass;
    }

    /**
     * Auto Adjustment Adjustment MediaType
     *
     * @param mediaType
     * @param reqType
     * @return
     */
    private String autoAdjustmentAdjustmentMediaType(String mediaType, AutoNetTypeAnontation.Type reqType) {
        if (reqType.equals(AutoNetTypeAnontation.Type.JSON)) {
            return "application/json;charset=utf-8";
        } else if (reqType.equals(AutoNetTypeAnontation.Type.STREAM)) {
            return "application/octet-stream;charset=UTF-8";
        } else if (reqType.equals(AutoNetTypeAnontation.Type.FORM)) {
            return "application/x-www-form-urlencoded;charset=UTF-8";
        }
        return mediaType;
    }

    /**
     * AutoNet Automatically Analyses and Finds the Right Return Body
     *
     * @param callBack
     * @return
     */
    private Class integrationResponseClass(IAutoNetCallBack callBack) {
        Class responseClass = null;
        // pathfinder
        List<GenericParadigmUtil.Pathfinder> pathfinders = new ArrayList<>(1);
        pathfinders.add(new GenericParadigmUtil.Pathfinder(0, 0));

        // 1. First Abstract callback
        if (callBack instanceof AbsAutoNetCallback) {
            responseClass = GenericParadigmUtil.parseGenericParadigm(callBack, pathfinders);
        }

        // 2. Second Interface callback
        // 2.1 IAutoNetDataSuccessCallBack
        if (responseClass == null) {
            responseClass = GenericParadigmUtil.parseInterfaceGenericParadigm(callBack,
                    GenericParadigmUtil.getInterfacePosition(callBack, IAutoNetDataSuccessCallBack.class),
                    pathfinders);
        }
        // 2.2 IAutoNetDataCallBack
        if (responseClass == null || responseClass.equals(IAutoNetDataSuccessCallBack.class)) {
            responseClass = GenericParadigmUtil.parseInterfaceGenericParadigm(callBack,
                    GenericParadigmUtil.getInterfacePosition(callBack, IAutoNetDataCallBack.class),
                    pathfinders);
        }
        // 2.3 IAutoNetDataBeforeCallBack
        if (responseClass == null || responseClass.equals(IAutoNetDataCallBack.class)) {
            responseClass = GenericParadigmUtil.parseInterfaceGenericParadigm(callBack,
                    GenericParadigmUtil.getInterfacePosition(callBack, IAutoNetDataBeforeCallBack.class),
                    pathfinders);
        }
        // 2.4 IAutoNetLocalOptCallBack
        if (responseClass == null || responseClass.equals(IAutoNetDataBeforeCallBack.class)) {
            responseClass = GenericParadigmUtil.parseInterfaceGenericParadigm(callBack,
                    GenericParadigmUtil.getInterfacePosition(callBack, IAutoNetLocalOptCallBack.class),
                    pathfinders);
        }

        return responseClass == null || responseClass.equals(IAutoNetLocalOptCallBack.class) ? String.class : responseClass;
    }

    /**
     * AutoNet Automatically Analyses and Finds the Right Target Body
     *
     * @param callBack
     * @return
     */
    private Class integrationTargetClass(IAutoNetCallBack callBack, Class responseClass) {
        Class targetClass = null;
        // pathfinder
        List<GenericParadigmUtil.Pathfinder> pathfinders = new ArrayList<>(1);
        pathfinders.add(new GenericParadigmUtil.Pathfinder(0, 1));

        if (callBack instanceof AbsAutoNetCallback) {
            targetClass = GenericParadigmUtil.parseGenericParadigm(callBack, pathfinders);
        }

        // 2. Second Interface callback
        // 2.1 IAutoNetDataBeforeCallBack
        if (targetClass == null) {
            targetClass = GenericParadigmUtil.parseInterfaceGenericParadigm(callBack,
                    GenericParadigmUtil.getInterfacePosition(callBack, IAutoNetDataBeforeCallBack.class),
                    pathfinders);
        }
        // 2.2 If the target type is not specified in the above two steps, the target type is the return body type.
        if (targetClass == null || targetClass.equals(IAutoNetDataBeforeCallBack.class)) {
            targetClass = responseClass;
        }

        return targetClass == null ? String.class : targetClass;
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
    @SuppressWarnings("ConstantConditions")
    private String getUrlByRequest(String key, Map<String, String> domainNames, Map<String, String> extraDomainNames,
                                   String disposableBaseUrl, String suffixUrl, String extraDynamicParam) {

        // Ensure that the parameters are legitimate and illegally automatically processed
        disposableBaseUrl = TextUtils.isEmpty(disposableBaseUrl) ? "" : disposableBaseUrl;
        suffixUrl = TextUtils.isEmpty(suffixUrl) ? "" : suffixUrl;
        extraDynamicParam = TextUtils.isEmpty(extraDynamicParam) ? "" : extraDynamicParam;

        if (!TextUtils.isEmpty(disposableBaseUrl)) {
            return intelligenceSplicingUrl(intelligenceSplicingUrl(new StringBuffer(disposableBaseUrl), suffixUrl), extraDynamicParam).toString();
        }
        Map<String, String> domains = integrationBaseUrl(domainNames, extraDomainNames);
        key = TextUtils.isEmpty(key) ? "default" : key;
        String url = domains.get(key);
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("The domain name of this request is not found. Please check whether you have configured the domain name, or whether the domain name corresponding to the domain name key exists.");
        }
        return intelligenceSplicingUrl(intelligenceSplicingUrl(new StringBuffer(url), suffixUrl), extraDynamicParam).toString();
    }

    /**
     * Integrating all domain names.
     *
     * @param domainNames
     * @param extraDomainNames
     */
    private Map<String, String> integrationBaseUrl(Map<String, String> domainNames, Map<String, String> extraDomainNames) {
        Map<String, String> domains = new ArrayMap<>();

        domains.putAll(domainNames);
        domains.putAll(extraDomainNames);

        return domains;
    }

    /**
     * AutoNet automatically and legally splicing URL addresses according to URL rules
     *
     * @param first
     * @param second
     * @return
     */
    private StringBuffer intelligenceSplicingUrl(StringBuffer first, String second) {

        // first is need opt if with / in end
        if (first.toString().endsWith(AutoNetConstant.SLASH)) {
            first = first.deleteCharAt(first.length() - 1);
        }

        if (!TextUtils.isEmpty(second)) {
            if (!second.startsWith(AutoNetConstant.SLASH)) {
                second = AutoNetConstant.SLASH + second;
            }
        }

        return first.append(second);
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
    private Map<String, Object> integrationHeads(Map<String, Object> headParam, Map<String, Object> extraHeads, String disposableHeads) {
        Map<String, Object> heads = new ArrayMap<>();
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

    @SuppressWarnings("unchecked")
    private Map integrationParams(IAutoNetRequest requestEntity, Map requestMap) {
        Map params = new ArrayMap();
        if (requestEntity != null) {
            params.putAll(DataConvertorUtils.convertEntityToMapPlus(requestEntity, true));
        }
        if (requestMap != null) {
            params.putAll(requestMap);
        }
        return params;
    }

    /**
     * Header data of the restore annotation
     *
     * @param disposableHeads
     * @return
     */
    private Map<String, String> reductionAnnotationHeads(String disposableHeads) {
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
     * Test connectivity
     *
     * @param callback
     * @param type
     */
    public final void test(IAutoNetCallBack callback, Integer type) {

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
        private Map<String, Object> mExtraHeads;

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
        public IAutoNetExtraConfig setExtraHeads(Map<String, Object> extraHeads) {
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


        public Map<String, Object> getExtraHeads() {
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
        public IAutoNetExtraConfig updateOrInsertHead(String key, Object value) {
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
        private Object mFlag;
        private IAutoNetRequest requestEntity;
        private Map requestMap;
        private String extraDynamicParam;
        private String pushFileKey;
        private String filePath;
        private String fileName;
        private StringBuffer disposableHeads;
        private IAutoNetFileCallBack accompanyFileCallback;
        private IAutoNetLocalOptCallBack accompanyLocalOptCallback;
        private FlowableTransformer transformer;

        private AutoNetNonAnontation() {
            info = new ProxyInfo();
            requestMap = new ArrayMap();
        }

        @Override
        public IAutoNetNonAnontation setFlag(Object flag) {
            this.mFlag = flag;
            return this;
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
            //noinspection unchecked
            requestMap.put(key, value);
            return this;
        }

        @Override
        public IAutoNetNonAnontation setParams(Map params) {
            if (params != null) {
                //noinspection unchecked
                requestMap.putAll(params);
            }
            return this;
        }

        @Override
        public IAutoNetNonAnontation setAutoNetFileCallback(IAutoNetFileCallBack callBack) {
            this.accompanyFileCallback = callBack;
            return this;
        }

        @Override
        public IAutoNetNonAnontation setAutoNetLocalOptCallback(IAutoNetLocalOptCallBack callBack) {
            this.accompanyLocalOptCallback = callBack;
            return this;
        }

        @Override
        public <T> T synchronizationNet(Class<T> responseClass) throws Exception {
            return AutoNet.getInstance().startSynchronizationNet(requestEntity, requestMap, extraDynamicParam,
                    info.suffixUrl, mFlag, info.writeOutTime, info.readOutTime, info.connectOutTime,
                    info.domainNameKey,
                    info.disposableBaseUrl,
                    disposableHeads == null ? null : disposableHeads.toString(),
                    info.encryptionKey, info.isEncryption,
                    info.mediaType,
                    info.netPattern,
                    responseClass,
                    info.netStrategy,
                    info.reqType, info.resType, pushFileKey, filePath, fileName, accompanyFileCallback, accompanyLocalOptCallback, transformer);
        }

        @Override
        public <T> Flowable getFlowable(Class<T> responseClass) {
            return AutoNet.getInstance().createFlowable(requestEntity, requestMap, extraDynamicParam,
                    info.suffixUrl, mFlag, info.writeOutTime, info.readOutTime, info.connectOutTime,
                    info.domainNameKey,
                    info.disposableBaseUrl,
                    disposableHeads == null ? null : disposableHeads.toString(),
                    info.encryptionKey, info.isEncryption,
                    info.mediaType,
                    info.netPattern,
                    responseClass,
                    info.netStrategy,
                    info.reqType, info.resType, pushFileKey, filePath, fileName, accompanyFileCallback, accompanyLocalOptCallback, transformer);
        }

        @Override
        public void start(IAutoNetCallBack callBack) {
            AutoNet.getInstance().startNet(requestEntity, requestMap, extraDynamicParam,
                    info.suffixUrl, mFlag, info.writeOutTime, info.readOutTime, info.connectOutTime,
                    info.domainNameKey,
                    info.disposableBaseUrl,
                    disposableHeads == null ? null : disposableHeads.toString(),
                    info.encryptionKey, info.isEncryption,
                    info.mediaType,
                    info.netPattern,
                    info.netStrategy,
                    info.reqType, info.resType, pushFileKey, filePath, fileName, accompanyFileCallback, accompanyLocalOptCallback, callBack, transformer);
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

        IAutoNetExtraConfig setExtraHeads(Map<String, Object> extraHeads);

        IAutoNetExtraConfig setExtraDomainNames(Map<String, String> extraDomainNames);

        IAutoNetExtraConfig updateOrInsertHead(String key, Object value);

        IAutoNetExtraConfig removeHead(String key);

        IAutoNetExtraConfig updateOrInsertDomainNames(String key, String value);

        IAutoNetExtraConfig removeDomainName(String key);

        IAutoNetExtraConfig setEncryptionCallback(IAutoNetEncryptionCallback encryptionCallback);

        IAutoNetExtraConfig setHeadsCallback(IAutoNetHeadCallBack headsCallback);

        IAutoNetExtraConfig setBodyCallback(IAutoNetBodyCallBack bodyCallback);

    }

    public interface IAutoNetNonAnontation {

        IAutoNetNonAnontation setFlag(Object flag);

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

        IAutoNetNonAnontation setAutoNetFileCallback(IAutoNetFileCallBack callBack);

        <T> IAutoNetNonAnontation setAutoNetLocalOptCallback(IAutoNetLocalOptCallBack<T> callBack);

        <T> Flowable getFlowable(Class<T> responseClass);

        void start(IAutoNetCallBack callBack);

        <T> T synchronizationNet(Class<T> responseClass) throws Exception;
    }

}
