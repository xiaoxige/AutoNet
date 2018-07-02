package cn.xiaoxige.processor;


import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import cn.xiaoxige.annotation.entity.ProxyInfo;
import io.reactivex.FlowableTransformer;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 *         Write the collected data to the tool class of Java source file.
 */

public class ProxyWriteUtil {

    private static final String AUTO_NET_METHOD_MATRIX = "proxy";
    private static final String AUTO_NET_METHOD_START_NET = "startNet";
    private static final String AUTO_NET_METHOD_PUSH_FILE = "pushFile";
    private static final String AUTO_NET_METHOD_PULL_FILE = "pullFile";

    private static final String AUTO_NET_I_REQUEST_REFERENCE = "cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest";
    private static final String AUTO_NET_API_FACADE = "cn.xiaoxige.autonet_api.AutoNet";
    private static final String AUTO_NET_I_CALLBACK = "cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack";

    private static final String AUTO_NET_PARAM_LEADER_NAME = "leader";
    private static final String AUTO_NET_PARAM_DOMAIN_NAME_KEY_NAME = "domainNameKey";
    private static final String AUTO_NET_PARAM_SUFFIX_URL_NAME = "suffixUrl";
    private static final String AUTO_NET_PARAM_MEDIA_TYPE_NAME = "mediaType";
    private static final String AUTO_NET_PARAM_WRITE_OUT_TIME_NAME = "writeOutTime";
    private static final String AUTO_NET_PARAM_READ_OUT_TIME_NAME = "readOutTime";
    private static final String AUTO_NET_PARAM_CONNECT_OUT_TIME_NAME = "connectOutTime";
    private static final String AUTO_NET_PARAM_ENCRYPTION_KEY_NAME = "encryptionKey";
    private static final String AUTO_NET_PARAM_IS_ENCRYPTION_NAME = "isEncryption";
    private static final String AUTO_NET_PARAM_NET_PATTERN_NAME = "netPattern";
    private static final String AUTO_NET_PARAM_NET_STRATEGY_NAME = "netStrategy";
    private static final String AUTO_NET_PARAM_DISPOSABLE_BASE_URL = "disposableBaseUrl";
    private static final String AUTO_NET_PARAM_DISPOSABLE_HEADS = "disposableHeads";
    private static final String AUTO_NET_PARAM_REQ_TYPE_NAME = "reqType";
    private static final String AUTO_NET_PARAM_RES_TYPE_NAME = "resType";
    private static final String AUTO_NET_PARAM_RESPONSE_CLAZZ_NAME_NAME = "responseClazzName";
    private static final String AUTO_NET_PARAM_PUSH_FILE_KEY_NAME = "pushFileKey";
    private static final String AUTO_NET_PARAM_FILE_PATH_NAME = "filePath";
    private static final String AUTO_NET_PARAM_FILE_NAME_NAME = "fileName";
    private static final String AUTO_NET_PARAM_TRANSFORMER_NAME = "transformer";

    private static final String AUTO_NET_PARAM_REQUEST_ENTITY_NAME = "requestEntity";
    private static final String AUTO_NET_PARAM_REQUEST_MAP_NAME = "requestMap";
    private static final String AUTO_NET_PARAM_EXTRA_DYNAMIC_PARAM_NAME = "extraDynamicParam";

    public static void write(Map<String, ProxyInfo> infoMap, Filer filer) throws Exception {
        if (infoMap == null || infoMap.isEmpty()) {
            return;
        }

        Set<String> keys = infoMap.keySet();
        for (String key : keys) {
            ProxyInfo info = infoMap.get(key);
            write(info, filer);
        }
    }


    public static void write(ProxyInfo info, Filer filer) throws Exception {
        List<MethodSpec> methodSpecs = new ArrayList<>();

        AutoNetTypeAnontation.Type resType = info.resType;
        AutoNetTypeAnontation.Type reqType = info.reqType;
        // Whether there is a file operation
        boolean isExistenceStream = resType.equals(AutoNetTypeAnontation.Type.STREAM) || reqType.equals(AutoNetTypeAnontation.Type.STREAM);

        // test ordinary
        MethodSpec testMethod = createTest(info);
        // net mothods(ordinary)
        List<MethodSpec> ordinaryMethod = createOrdinaryMethods(info);
        // Matrix of User concern
        MethodSpec matrixUserComcern = createMatrixUserComcern(info);
        // Matrix
        MethodSpec matrix = createMatrix(info);

        methodSpecs.add(testMethod);
        //noinspection ConstantConditions
        methodSpecs.addAll(ordinaryMethod);

        if (isExistenceStream) {
            // net nothods(file)
            List<MethodSpec> fileMethodSpecs = createFileMethods(info);
            //noinspection ConstantConditions
            methodSpecs.addAll(fileMethodSpecs);
        }

        methodSpecs.add(matrixUserComcern);
        methodSpecs.add(matrix);

        // Class
        TypeSpec autoClass = createAutoClass(info, methodSpecs);
        // file
        JavaFile javaFile = JavaFile.builder(info.targetPackage, autoClass)
                .addStaticImport(AutoNetPatternAnontation.NetPattern.GET)
                .addStaticImport(AutoNetPatternAnontation.NetPattern.POST)
                .addStaticImport(AutoNetPatternAnontation.NetPattern.DELETE)
                .addStaticImport(AutoNetPatternAnontation.NetPattern.PUT)
                .addStaticImport(AutoNetPatternAnontation.NetPattern.OTHER_PATTERN)
                .addStaticImport(AutoNetStrategyAnontation.NetStrategy.LOCAL)
                .addStaticImport(AutoNetStrategyAnontation.NetStrategy.NET)
                .addStaticImport(AutoNetStrategyAnontation.NetStrategy.LOCAL_NET)
                .addStaticImport(AutoNetStrategyAnontation.NetStrategy.NET_LOCAL)
                .addStaticImport(AutoNetTypeAnontation.Type.FORM)
                .addStaticImport(AutoNetTypeAnontation.Type.JSON)
                .addStaticImport(AutoNetTypeAnontation.Type.STREAM)
                .addStaticImport(AutoNetTypeAnontation.Type.OTHER_TYPE)
                .build();
        javaFile.writeTo(filer);
    }


    /**
     * Method of automatically generating file related request network
     *
     * @param info
     * @return
     */
    private static List<MethodSpec> createFileMethods(ProxyInfo info) throws ClassNotFoundException {
        List<MethodSpec> specs = new ArrayList<>();

        // push
        // pushFile(object, fileKey, path)
        specs.add(createFileMethodTemplate(info, false, true, false, false, false));
        // pushFile(object, fileKey, path, transformer)
        specs.add(createFileMethodTemplate(info, false, true, false, false, true));
        // pushFile(object, entity, fileKey, path)
        specs.add(createFileMethodTemplate(info, false, true, true, false, false));
        // pushFile(object, entity, fileKey, path, transformer)
        specs.add(createFileMethodTemplate(info, false, true, true, false, true));
        // pushFile(object, map, fileKey, path)
        specs.add(createFileMethodTemplate(info, false, true, false, true, false));
        // pushFile(object, map, fileKey, path, transformer)
        specs.add(createFileMethodTemplate(info, false, true, false, true, true));

        // pull
        // pullFile(object, path, fileName)
        specs.add(createFileMethodTemplate(info, false, false, false, false, false));
        // pullFile(object, path, fileName, transformer)
        specs.add(createFileMethodTemplate(info, false, false, false, false, true));
        // pullFile(object, entity, path, fileName)
        specs.add(createFileMethodTemplate(info, false, false, true, false, false));
        // pullFile(object, entity, path, fileName, transformer)
        specs.add(createFileMethodTemplate(info, false, false, true, false, true));
        // pullFile(object, entity, path, fileName)
        specs.add(createFileMethodTemplate(info, false, false, false, true, false));
        // pullFile(object, entity, path, fileName, transformer)
        specs.add(createFileMethodTemplate(info, false, false, false, true, true));

        return specs;
    }

    private static MethodSpec createFileMethodTemplate(ProxyInfo info, boolean isPrivate, boolean isPush, boolean isExistenceTwo, boolean isExistenceThree, boolean isExistenceLast) throws ClassNotFoundException {

        MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(isPush ? AUTO_NET_METHOD_PUSH_FILE : AUTO_NET_METHOD_PULL_FILE)
                .addJavadoc("The method of file request network\n")
                .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                .addParameter(Object.class, AUTO_NET_PARAM_LEADER_NAME);

        if (isExistenceTwo) {
            specBuilder.addJavadoc("@param " + AUTO_NET_PARAM_REQUEST_ENTITY_NAME + ": entity of request\n");
            specBuilder.addParameter(Class.forName(AUTO_NET_I_REQUEST_REFERENCE), AUTO_NET_PARAM_REQUEST_ENTITY_NAME);
        }

        if (isExistenceThree) {
            specBuilder.addJavadoc("@param " + AUTO_NET_PARAM_REQUEST_MAP_NAME + ": map of request\n");
            specBuilder.addParameter(Map.class, AUTO_NET_PARAM_REQUEST_MAP_NAME);
        }

        if (isPush) {
            specBuilder.addJavadoc("@param " + AUTO_NET_PARAM_PUSH_FILE_KEY_NAME + ": Flag bit for sending files\n");
            specBuilder.addParameter(String.class, AUTO_NET_PARAM_PUSH_FILE_KEY_NAME);
            specBuilder.addJavadoc("@param " + AUTO_NET_PARAM_FILE_PATH_NAME + ": The path to the file\n");
            specBuilder.addParameter(String.class, AUTO_NET_PARAM_FILE_PATH_NAME);
        } else {
            specBuilder.addJavadoc("@param " + AUTO_NET_PARAM_FILE_PATH_NAME + ": The path to save the file\n");
            specBuilder.addParameter(String.class, AUTO_NET_PARAM_FILE_PATH_NAME);
            specBuilder.addJavadoc("@param " + AUTO_NET_PARAM_FILE_NAME_NAME + ": Save the name of the file\n");
            specBuilder.addParameter(String.class, AUTO_NET_PARAM_FILE_NAME_NAME);
        }

        if (isExistenceLast) {
            specBuilder.addJavadoc("@param " + AUTO_NET_PARAM_TRANSFORMER_NAME +
                    ": If the life cycle of the dominator is bound to the life cycle of the dominator, then the request will disappear when the ruler's life is over. Memory leak can be solved, such as Activity, Fragment\n");
            specBuilder.addParameter(FlowableTransformer.class, AUTO_NET_PARAM_TRANSFORMER_NAME);
        }

        specBuilder.addStatement(AUTO_NET_METHOD_MATRIX + "($L, $L, $L, null, $L, $L, $L, $L)",
                AUTO_NET_PARAM_LEADER_NAME,
                (isExistenceTwo ? AUTO_NET_PARAM_REQUEST_ENTITY_NAME : null),
                (isExistenceThree ? AUTO_NET_PARAM_REQUEST_MAP_NAME : null),
                (isPush ? AUTO_NET_PARAM_PUSH_FILE_KEY_NAME : null),
                AUTO_NET_PARAM_FILE_PATH_NAME,
                (!isPush ? AUTO_NET_PARAM_FILE_NAME_NAME : null),
                (isExistenceLast ? AUTO_NET_PARAM_TRANSFORMER_NAME : null));

        return specBuilder.build();
    }

    /**
     * A method to automatically generate a common request network.
     *
     * @param info
     * @return
     */
    private static List<MethodSpec> createOrdinaryMethods(ProxyInfo info) throws ClassNotFoundException {
        List<MethodSpec> specs = new ArrayList<>();

        // public startNet(object)
        specs.add(createOrdinaryMatrixTemplate(info, false, false, false, false, false));
        // public startNet(object, transformer)
        specs.add(createOrdinaryMatrixTemplate(info, false, false, false, false, true));

        // public startNet(object, entity)
        specs.add(createOrdinaryMatrixTemplate(info, false, true, false, false, false));
        // public startNet(object, entity, transformer)
        specs.add(createOrdinaryMatrixTemplate(info, false, true, false, false, true));

        // public startNet(object, extraDynamicParam)
        specs.add(createOrdinaryMatrixTemplate(info, false, false, false, true, false));
        // public startNet(object, extraDynamicParam, transformer)
        specs.add(createOrdinaryMatrixTemplate(info, false, false, false, true, true));

        // public startNet(object, entity, extraDynamicParam)
        specs.add(createOrdinaryMatrixTemplate(info, false, true, false, true, false));
        // private startNet(object, entity, extraDynamicParam, transformer)
        specs.add(createOrdinaryMatrixTemplate(info, true, true, false, true, true));


        // public startNet(object, map)
        specs.add(createOrdinaryMatrixTemplate(info, false, false, true, false, false));
        // public startNet(object, map, transformer)
        specs.add(createOrdinaryMatrixTemplate(info, false, false, true, false, true));

        // public startNet(object, map, extraDynamicParam)
        specs.add(createOrdinaryMatrixTemplate(info, false, false, true, true, false));
        // private startNet(object, map, extraDynamicParam, transformer)
        specs.add(createOrdinaryMatrixTemplate(info, true, false, true, true, true));


        return specs;
    }


    /**
     * A template for producing common requests for network methods
     *
     * @param info
     * @param isPrivate
     * @param isExistenceTwo
     * @param isExistenceThree
     * @param isExistenceFore
     * @param isExistenceFive
     * @return
     * @throws ClassNotFoundException
     */
    private static MethodSpec createOrdinaryMatrixTemplate(ProxyInfo info, boolean isPrivate, boolean isExistenceTwo, boolean isExistenceThree, boolean isExistenceFore, boolean isExistenceFive) throws ClassNotFoundException {
        MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(AUTO_NET_METHOD_START_NET)
                .addJavadoc("The method of common request network\n")
                .addModifiers(Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC)
                .addParameter(Object.class, AUTO_NET_PARAM_LEADER_NAME);


        if (isExistenceTwo) {
            specBuilder.addJavadoc("@param " + AUTO_NET_PARAM_REQUEST_ENTITY_NAME + ": entity of request\n");
            specBuilder.addParameter(Class.forName(AUTO_NET_I_REQUEST_REFERENCE), AUTO_NET_PARAM_REQUEST_ENTITY_NAME);
        }

        if (isExistenceThree) {
            specBuilder.addJavadoc("@param " + AUTO_NET_PARAM_REQUEST_MAP_NAME + ": map of request\n");
            specBuilder.addParameter(Map.class, AUTO_NET_PARAM_REQUEST_MAP_NAME);
        }

        if (isExistenceFore) {
            specBuilder.addJavadoc("@param " + AUTO_NET_PARAM_EXTRA_DYNAMIC_PARAM_NAME +
                    ": aynamic param of request, For example, dynamic variable 1230 in https://www.xiaoxige.cn/1230.\n");
            specBuilder.addParameter(String.class, AUTO_NET_PARAM_EXTRA_DYNAMIC_PARAM_NAME);
        }

        if (isExistenceFive) {
            specBuilder.addJavadoc("@param " + AUTO_NET_PARAM_TRANSFORMER_NAME +
                    ": If the life cycle of the dominator is bound to the life cycle of the dominator, then the request will disappear when the ruler's life is over. Memory leak can be solved, such as Activity, Fragment\n");
            specBuilder.addParameter(FlowableTransformer.class, AUTO_NET_PARAM_TRANSFORMER_NAME);
        }


        specBuilder.addStatement(AUTO_NET_METHOD_MATRIX + "($L, $L, $L, $L, null, null, null, $L)",
                AUTO_NET_PARAM_LEADER_NAME,
                (isExistenceTwo ? AUTO_NET_PARAM_REQUEST_ENTITY_NAME : null),
                (isExistenceThree ? AUTO_NET_PARAM_REQUEST_MAP_NAME : null),
                (isExistenceFore ? AUTO_NET_PARAM_EXTRA_DYNAMIC_PARAM_NAME : null),
                (isExistenceFive ? AUTO_NET_PARAM_TRANSFORMER_NAME : null));

        return specBuilder.build();
    }

    /**
     * AutoNet automatic generation of code test method(Testing local connections)
     *
     * @param info
     * @return
     */
    private static MethodSpec createTest(ProxyInfo info) throws ClassNotFoundException {

        MethodSpec.Builder specBuilder = MethodSpec
                .methodBuilder("testLocalLink")
                .addJavadoc("AutoNet automatic generation of code test method(Testing local connections).\n" +
                        "type:\n" +
                        "\t1.onSuccess(null)\n" +
                        "\t2.onFailed(null)\n" +
                        "\t3.onEmpty()\n")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL);

        specBuilder
                .addParameter(Object.class, AUTO_NET_PARAM_LEADER_NAME)
                .addParameter(Integer.class, "type")
        ;

        boolean callBackSelf = isCallBackSelf(info);
        String callBackFormat = callBackSelf ? "(" + AUTO_NET_I_CALLBACK + ") " + AUTO_NET_PARAM_LEADER_NAME :
                "((" + getSuperiorClassPath(info.fullTargetPath, info.targetPackage, info.targetClassSimpleName) + ") " + AUTO_NET_PARAM_LEADER_NAME
                        + ").new " + info.targetClassSimpleName + "()";

        specBuilder
                .addStatement("$T.getInstance().test($N, $L)", Class.forName(AUTO_NET_API_FACADE), callBackFormat, "type");

        return specBuilder.build();
    }

    /**
     * Generate the corresponding classes based on each information
     *
     * @param info    info of class
     * @param methods some methods
     * @return
     */
    private static TypeSpec createAutoClass(ProxyInfo info, List<MethodSpec> methods) {
        String fullTargetPath = info.fullTargetPath;
        String targetPackage = info.targetPackage;
        String targetClassSimpleName = info.targetClassSimpleName;

        // Gets the only prefix to prevent network callbacks from having the same name in the same package.
        String prefixClassName = getExternalClassName(fullTargetPath, targetPackage, targetClassSimpleName);

        String className = prefixClassName + targetClassSimpleName + ProxyInfo.PROXY_CLASS_SUFFIX;

        TypeSpec.Builder specBuilder = TypeSpec.classBuilder(className)
                .addJavadoc("This class is a AutoNet transfer center class.\n which is automatically generated. Please do not make any changes.\n")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        if (methods != null) {
            for (MethodSpec method : methods) {
                specBuilder.addMethod(method);
            }
        }

        return specBuilder.build();
    }

    /**
     * Users are concerned about and use more methods (annotation of collected information automatically assign).
     *
     * @param info
     * @return
     */
    private static MethodSpec createMatrixUserComcern(ProxyInfo info) throws ClassNotFoundException {
        // method name
        MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(AUTO_NET_METHOD_MATRIX)
                .addJavadoc("Users are concerned about and use more methods (annotation of collected information automatically assign).\n")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);

        // param
        specBuilder
                .addParameter(Object.class, AUTO_NET_PARAM_LEADER_NAME)
                .addParameter(Class.forName(AUTO_NET_I_REQUEST_REFERENCE), AUTO_NET_PARAM_REQUEST_ENTITY_NAME)
                .addParameter(Map.class, AUTO_NET_PARAM_REQUEST_MAP_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_EXTRA_DYNAMIC_PARAM_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_PUSH_FILE_KEY_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_FILE_PATH_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_FILE_NAME_NAME)
                .addParameter(FlowableTransformer.class, AUTO_NET_PARAM_TRANSFORMER_NAME);

        StringBuffer heads = transformationHeads(info.disposableHeads);

        specBuilder.addStatement(AUTO_NET_METHOD_MATRIX + "($L, $L, $L, $L, $S, $S, $S, $L, $L, $L, $L, $L, $S, $S, $L, $L, $L, $L, $S, $L, $L, $L, $L)",
                AUTO_NET_PARAM_LEADER_NAME,
                AUTO_NET_PARAM_REQUEST_ENTITY_NAME,
                AUTO_NET_PARAM_REQUEST_MAP_NAME,
                AUTO_NET_PARAM_EXTRA_DYNAMIC_PARAM_NAME,
                info.domainNameKey, info.suffixUrl, info.mediaType,
                info.writeOutTime, info.readOutTime, info.connectOutTime,
                info.encryptionKey, info.isEncryption,
                info.disposableBaseUrl, heads != null ? heads.toString() : null,
                info.netPattern, info.reqType, info.resType,
                info.netStrategy, info.responseClazzName,
                AUTO_NET_PARAM_PUSH_FILE_KEY_NAME, AUTO_NET_PARAM_FILE_PATH_NAME, AUTO_NET_PARAM_FILE_NAME_NAME, AUTO_NET_PARAM_TRANSFORMER_NAME);

        return specBuilder.build();
    }

    /**
     * Head data conversion(String[] --> String)
     *
     * @param disposableHeads
     * @return
     */
    private static StringBuffer transformationHeads(String[] disposableHeads) {
        StringBuffer heads = null;
        if (disposableHeads != null) {
            heads = new StringBuffer();
            for (String head : disposableHeads) {
                heads.append(head + "\n");
            }
        }
        return heads;
    }

    /**
     * According to the information, the final method of generating the main key is generated.
     *
     * @param info info of message
     * @return
     */
    private static MethodSpec createMatrix(ProxyInfo info) throws ClassNotFoundException {

        // method name
        MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(AUTO_NET_METHOD_MATRIX)
                .addJavadoc("matrix of autoNet\n")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);

        // param
        specBuilder
                .addParameter(Object.class, AUTO_NET_PARAM_LEADER_NAME)
                .addParameter(Class.forName(AUTO_NET_I_REQUEST_REFERENCE), AUTO_NET_PARAM_REQUEST_ENTITY_NAME)
                .addParameter(Map.class, AUTO_NET_PARAM_REQUEST_MAP_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_EXTRA_DYNAMIC_PARAM_NAME)

                .addParameter(String.class, AUTO_NET_PARAM_DOMAIN_NAME_KEY_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_SUFFIX_URL_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_MEDIA_TYPE_NAME)
                .addParameter(long.class, AUTO_NET_PARAM_WRITE_OUT_TIME_NAME)
                .addParameter(long.class, AUTO_NET_PARAM_READ_OUT_TIME_NAME)
                .addParameter(long.class, AUTO_NET_PARAM_CONNECT_OUT_TIME_NAME)
                .addParameter(long.class, AUTO_NET_PARAM_ENCRYPTION_KEY_NAME)
                .addParameter(Boolean.class, AUTO_NET_PARAM_IS_ENCRYPTION_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_DISPOSABLE_BASE_URL)
                .addParameter(String.class, AUTO_NET_PARAM_DISPOSABLE_HEADS)
                .addParameter(AutoNetPatternAnontation.NetPattern.class, AUTO_NET_PARAM_NET_PATTERN_NAME)
                .addParameter(AutoNetTypeAnontation.Type.class, AUTO_NET_PARAM_REQ_TYPE_NAME)
                .addParameter(AutoNetTypeAnontation.Type.class, AUTO_NET_PARAM_RES_TYPE_NAME)
                .addParameter(AutoNetStrategyAnontation.NetStrategy.class, AUTO_NET_PARAM_NET_STRATEGY_NAME)

                .addParameter(String.class, AUTO_NET_PARAM_RESPONSE_CLAZZ_NAME_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_PUSH_FILE_KEY_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_FILE_PATH_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_FILE_NAME_NAME)

                .addParameter(FlowableTransformer.class, AUTO_NET_PARAM_TRANSFORMER_NAME);

        boolean callBackSelf = isCallBackSelf(info);
        String callBackFormat = callBackSelf ? "(" + AUTO_NET_I_CALLBACK + ") " + AUTO_NET_PARAM_LEADER_NAME :
                "((" + getSuperiorClassPath(info.fullTargetPath, info.targetPackage, info.targetClassSimpleName) + ") " + AUTO_NET_PARAM_LEADER_NAME
                        + ").new " + info.targetClassSimpleName + "()";

        specBuilder
                .addComment("AutoNet turns to find Api.")
                .addStatement("$T.getInstance().startNet(" +
                                "$L, $L, $L, $L, $L, $L, $L, " +
                                "$L, $L, $L, $L, $L, $L, " +
                                "$L, $L, $L, $L, $L, $N, $L, " +
                                "$L, $L, $L)", Class.forName(AUTO_NET_API_FACADE), AUTO_NET_PARAM_REQUEST_ENTITY_NAME, AUTO_NET_PARAM_REQUEST_MAP_NAME, AUTO_NET_PARAM_EXTRA_DYNAMIC_PARAM_NAME, AUTO_NET_PARAM_DOMAIN_NAME_KEY_NAME,
                        AUTO_NET_PARAM_SUFFIX_URL_NAME, AUTO_NET_PARAM_MEDIA_TYPE_NAME, AUTO_NET_PARAM_WRITE_OUT_TIME_NAME, AUTO_NET_PARAM_READ_OUT_TIME_NAME,
                        AUTO_NET_PARAM_CONNECT_OUT_TIME_NAME, AUTO_NET_PARAM_ENCRYPTION_KEY_NAME, AUTO_NET_PARAM_IS_ENCRYPTION_NAME,
                        AUTO_NET_PARAM_DISPOSABLE_BASE_URL, AUTO_NET_PARAM_DISPOSABLE_HEADS, AUTO_NET_PARAM_NET_PATTERN_NAME,
                        AUTO_NET_PARAM_REQ_TYPE_NAME, AUTO_NET_PARAM_RES_TYPE_NAME, AUTO_NET_PARAM_NET_STRATEGY_NAME, AUTO_NET_PARAM_RESPONSE_CLAZZ_NAME_NAME, callBackFormat,
                        AUTO_NET_PARAM_PUSH_FILE_KEY_NAME, AUTO_NET_PARAM_FILE_PATH_NAME, AUTO_NET_PARAM_FILE_NAME_NAME, AUTO_NET_PARAM_TRANSFORMER_NAME);

        return specBuilder.build();
    }

    /**
     * The corresponding class prefix is obtained according to the policy
     *
     * @param fullTargetPath
     * @param targetPackage
     * @param targetClassSimpleName
     * @return
     */
    private static String getExternalClassName(String fullTargetPath, String targetPackage, String targetClassSimpleName) {
        boolean isSelf = isCallBackSelf(fullTargetPath, targetPackage, targetClassSimpleName);
        if (isSelf) {
            return "";
        }

        return fullTargetPath.substring(targetPackage.length() + 1, fullTargetPath.length() - targetClassSimpleName.length() - 1);
    }

    /**
     * External class full path of introverted callback method
     *
     * @param fullTargetPath
     * @param targetPackage
     * @param targetClassSimpleName
     * @return
     */
    private static String getSuperiorClassPath(String fullTargetPath, String targetPackage, String targetClassSimpleName) {
        if (isCallBackSelf(fullTargetPath, targetPackage, targetClassSimpleName)) {
            return targetPackage;
        }
        return fullTargetPath.substring(0, fullTargetPath.length() - targetClassSimpleName.length() - 1);
    }

    private static boolean isCallBackSelf(ProxyInfo info) {
        return isCallBackSelf(info.fullTargetPath, info.targetPackage, info.targetClassSimpleName);
    }

    /**
     * Determine whether there is an internal class
     *
     * @param fullTargetPath
     * @param targetPackage
     * @param targetClassSimpleName
     * @return
     */
    private static boolean isCallBackSelf(String fullTargetPath, String targetPackage, String targetClassSimpleName) {
        return targetPackage == null || targetPackage.length() <= 0 || fullTargetPath.equals(targetPackage + "." + targetClassSimpleName);
    }

}
