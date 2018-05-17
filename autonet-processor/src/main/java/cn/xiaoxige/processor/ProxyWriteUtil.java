package cn.xiaoxige.processor;


import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

import cn.xiaoxige.annotation.AutoNetPatternAnontation;
import cn.xiaoxige.annotation.AutoNetStrategyAnontation;
import cn.xiaoxige.annotation.AutoNetTypeAnontation;
import io.reactivex.FlowableTransformer;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 *         Write the collected data to the tool class of Java source file.
 */

public class ProxyWriteUtil {

    private static final String AUTO_NET_METHOD_MATRIX = "proxy";

    private static final String AUTO_NET_I_REQUEST_REFERENCE = "cn.xiaoxige.autonet_api.interfaces.IAutoNetRequest";
    private static final String AUTO_NET_API_FACADE = "cn.xiaoxige.autonet_api.AutoNet";

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
    private static final String AUTO_NET_PARAM_REQ_TYPE_NAME = "reqType";
    private static final String AUTO_NET_PARAM_RES_TYPE_NAME = "resType";
    private static final String AUTO_NET_PARAM_RESPONSE_CLAZZ_NAME_NAME = "responseClazzName";
    private static final String AUTO_NET_PARAM_PUSH_FILE_KEY_NAME = "pushFileKey";
    private static final String AUTO_NET_PARAM_FILE_PATH_NAME = "filePath";
    private static final String AUTO_NET_PARAM_FILE_NAME_NAME = "fileName";
    private static final String AUTO_NET_PARAM_TRANSFORMER_NAME = "transformer";

    private static final String AUTO_NET_PARAM_REQUEST_ENTITY_NAME = "requestEntity";

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
        // Matrix
        MethodSpec matrix = createMatrix(info);
        // Class
        TypeSpec autoClass = createAutoClass(info, matrix);
        // file
        JavaFile javaFile = JavaFile.builder(info.targetPackage, autoClass).build();
        javaFile.writeTo(filer);
    }

    /**
     * Generate the corresponding classes based on each information
     *
     * @param info    info of class
     * @param methods some methods
     * @return
     */
    private static TypeSpec createAutoClass(ProxyInfo info, MethodSpec... methods) {
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
     * According to the information, the final method of generating the main key is generated.
     *
     * @param info info of message
     * @return
     */
    private static MethodSpec createMatrix(ProxyInfo info) throws ClassNotFoundException {

        // method name
        MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(AUTO_NET_METHOD_MATRIX)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);

        // param
        specBuilder
                .addJavadoc("matrix of autoNet\n")
                .addParameter(Object.class, AUTO_NET_PARAM_LEADER_NAME)
                .addParameter(Class.forName(AUTO_NET_I_REQUEST_REFERENCE), AUTO_NET_PARAM_REQUEST_ENTITY_NAME)

                .addParameter(String.class, AUTO_NET_PARAM_DOMAIN_NAME_KEY_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_SUFFIX_URL_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_MEDIA_TYPE_NAME)
                .addParameter(Long.class, AUTO_NET_PARAM_WRITE_OUT_TIME_NAME)
                .addParameter(Long.class, AUTO_NET_PARAM_READ_OUT_TIME_NAME)
                .addParameter(Long.class, AUTO_NET_PARAM_CONNECT_OUT_TIME_NAME)
                .addParameter(Long.class, AUTO_NET_PARAM_ENCRYPTION_KEY_NAME)
                .addParameter(Boolean.class, AUTO_NET_PARAM_IS_ENCRYPTION_NAME)
                .addParameter(AutoNetPatternAnontation.NetPattern.class, AUTO_NET_PARAM_NET_PATTERN_NAME)
                .addParameter(AutoNetTypeAnontation.Type.class, AUTO_NET_PARAM_REQ_TYPE_NAME)
                .addParameter(AutoNetTypeAnontation.Type.class, AUTO_NET_PARAM_RES_TYPE_NAME)
                .addParameter(AutoNetStrategyAnontation.NetStrategy.class, AUTO_NET_PARAM_NET_STRATEGY_NAME)

                .addParameter(String.class, AUTO_NET_PARAM_RESPONSE_CLAZZ_NAME_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_PUSH_FILE_KEY_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_FILE_PATH_NAME)
                .addParameter(String.class, AUTO_NET_PARAM_FILE_NAME_NAME)

                .addParameter(FlowableTransformer.class, AUTO_NET_PARAM_TRANSFORMER_NAME);

        specBuilder
                .addComment("AutoNet turns to find Api.")
                .addStatement("$T.getInstance().startNet(" +
                                "$L, $L, $L, $L, $L, " +
                                "$L, $L, $L, $L, $L, " +
                                "$L, $L, $L, $L, $L, " +
                                "$L, $L, $L)", Class.forName(AUTO_NET_API_FACADE), AUTO_NET_PARAM_REQUEST_ENTITY_NAME, AUTO_NET_PARAM_DOMAIN_NAME_KEY_NAME,
                        AUTO_NET_PARAM_SUFFIX_URL_NAME, AUTO_NET_PARAM_MEDIA_TYPE_NAME, AUTO_NET_PARAM_WRITE_OUT_TIME_NAME, AUTO_NET_PARAM_READ_OUT_TIME_NAME,
                        AUTO_NET_PARAM_CONNECT_OUT_TIME_NAME, AUTO_NET_PARAM_ENCRYPTION_KEY_NAME, AUTO_NET_PARAM_IS_ENCRYPTION_NAME, AUTO_NET_PARAM_NET_PATTERN_NAME,
                        AUTO_NET_PARAM_REQ_TYPE_NAME, AUTO_NET_PARAM_RES_TYPE_NAME, AUTO_NET_PARAM_NET_STRATEGY_NAME, AUTO_NET_PARAM_RESPONSE_CLAZZ_NAME_NAME,
                        AUTO_NET_PARAM_PUSH_FILE_KEY_NAME, AUTO_NET_PARAM_FILE_PATH_NAME, AUTO_NET_PARAM_FILE_NAME_NAME, AUTO_NET_PARAM_TRANSFORMER_NAME);

        boolean callBackSelf = isCallBackSelf(info);
        String callBackFormat = callBackSelf ? "(cn.xiaoxige.autonet_api.interfaces.IAutoNetCallBack)" + AUTO_NET_PARAM_LEADER_NAME :
                "((" + getSuperiorClassPath(info.fullTargetPath, info.targetPackage, info.targetClassSimpleName) + ")" + AUTO_NET_PARAM_LEADER_NAME
                        + ").new " + info.targetClassSimpleName + "()";

        specBuilder.addStatement("$T.getInstance().test($N)", Class.forName(AUTO_NET_API_FACADE), callBackFormat);

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
