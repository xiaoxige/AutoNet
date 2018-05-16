package cn.xiaoxige.processor;


import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
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

    public static void write(Map<String, ProxyInfo> infoMap, Filer filer) throws IOException {
        if (infoMap == null || infoMap.isEmpty()) {
            return;
        }

        Set<String> keys = infoMap.keySet();
        for (String key : keys) {
            ProxyInfo info = infoMap.get(key);
            write(info, filer);
        }
    }


    public static void write(ProxyInfo info, Filer filer) throws IOException {
        // Matrix
        MethodSpec matrix = createMatrix(info);
        // Class
        TypeSpec autoClass = createAutoClass(info, matrix);
        // file
        JavaFile javaFile = JavaFile.builder(info.targetPackage, autoClass).build();
        javaFile.writeTo(filer);
    }

    private static TypeSpec createAutoClass(ProxyInfo info, MethodSpec... methods) {
        String fullTargetPath = info.fullTargetPath;
        String targetPackage = info.targetPackage;
        String targetClassSimpleName = info.targetClassSimpleName;

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

    private static String getExternalClassName(String fullTargetPath, String targetPackage, String targetClassSimpleName) {
        boolean isSelf = isCallBackSelf(fullTargetPath, targetPackage, targetClassSimpleName);
        if (isSelf) {
            return "";
        }


        return "";
    }

    private static boolean isCallBackSelf(String fullTargetPath, String targetPackage, String targetClassSimpleName) {
        return targetPackage == null || targetPackage.length() <= 0 || fullTargetPath.equals(targetPackage + "." + targetClassSimpleName);
    }

    /**
     * @param info
     * @return
     */
    private static MethodSpec createMatrix(ProxyInfo info) {

        // method name
        MethodSpec.Builder specBuilder = MethodSpec.methodBuilder(AUTO_NET_METHOD_MATRIX)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);

        // param
        specBuilder
                .addJavadoc("matrix of autoNet\n")
                .addParameter(Object.class, "object")
                .addParameter(String.class, "domainNameKey")
                .addParameter(String.class, "suffixUrl")
                .addParameter(String.class, "mediaType")
                .addParameter(Long.class, "writeOutTime")
                .addParameter(Long.class, "readOutTime")
                .addParameter(Long.class, "connectOutTime")
                .addParameter(Long.class, "encryptionKey")
                .addParameter(Boolean.class, "isEncryption")
                .addParameter(AutoNetPatternAnontation.NetPattern.class, "netPattern")
                .addParameter(AutoNetTypeAnontation.Type.class, "reqType")
                .addParameter(AutoNetTypeAnontation.Type.class, "resType")
                .addParameter(AutoNetStrategyAnontation.NetStrategy.class, "netStrategy")

                .addParameter(String.class, "responseClazzName")
                .addParameter(String.class, "pushFileKey")
                .addParameter(String.class, "filePath")
                .addParameter(String.class, "fileName")

                .addParameter(FlowableTransformer.class, "transformer")
        ;

        specBuilder.addStatement("");

        return specBuilder.build();
    }

//    public static void write(Map<String, ProxyInfo> infoMap, Filer filer) throws IOException {
//
//        if (infoMap == null || infoMap.size() <= 0) {
//            return;
//        }
//
//        Set<String> keys = infoMap.keySet();
//        for (String key : keys) {
//            ProxyInfo info = infoMap.get(key);
//            write(info, filer);
//        }
//
//    }
//
//    private static void write(ProxyInfo info, Filer filer) throws IOException {
//
//        String newClassName = info.className + ProxyInfo.AUTONETPROXY;
//        String fullPackageName = info.fullPackageName;
//        String[] split = fullPackageName.split("\\.");
//        if (split != null && split.length > 2) {
//            int length = split.length;
//            newClassName = split[length - 2] + split[length - 1] + ProxyInfo.AUTONETPROXY;
//        }
//
//        if (newClassName.length() > 0) {
//            char charAtHead = newClassName.charAt(0);
//            char[] ch = new char[1];
//            ch[0] = charAtHead;
//            String atHead = new String(ch);
//            String upperCase = atHead.toUpperCase();
//            newClassName = newClassName.replaceFirst(atHead, upperCase);
//        }
//
//        JavaFileObject classFile
//                = filer.createSourceFile(newClassName, info.typeElement);
//        StringBuffer buffer = new StringBuffer();
//        if (info.packageName != null && info.packageName.length() > 0) {
//            buffer.append("package ").append(info.packageName).append(";\n\n");
//        }
//        buffer.append("import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;\n")
//                .append("import cn.xiaoxige.autonet_api.AutoNet;\n")
//                .append("import static cn.xiaoxige.annotation.AutoNetPatternAnontation.NetPattern.GET;\n")
//                .append("import static cn.xiaoxige.annotation.AutoNetPatternAnontation.NetPattern.POST;\n")
//                .append("import static cn.xiaoxige.annotation.AutoNetPatternAnontation.NetPattern.DELETE;\n")
//                .append("import static cn.xiaoxige.annotation.AutoNetPatternAnontation.NetPattern.PUT;\n")
//                .append("import static cn.xiaoxige.annotation.AutoNetTypeAnontation.Type.JSON;\n")
//                .append("import static cn.xiaoxige.annotation.AutoNetTypeAnontation.Type.STREAM;\n")
//                .append("import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;\n")
//                .append("import io.reactivex.*;\n")
//                .append("import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;")
//                .append("import io.reactivex.FlowableTransformer;\n");
//
//        if (info.packageName != null && info.packageName.length() > 0) {
//            buffer.append("import " + info.packageName + ".*;\n");
//        }
//        buffer.append("import cn.xiaoxige.annotation.AutoNetPatternAnontation;\n\n")
//        ;
//
//        // class start
//        buffer.append("public class " + (newClassName) + " { \n");
//
//        // 过时
//        buffer.append("@Deprecated\n");
//        buffer.append("public static void startUnSoftNet(IRequestEntity entity, IAutoNetDataCallback callback) {\n");
//
//        buffer.append("AutoNet.getInstance().startNet("
//                + "entity" + ", "
//                + (info.responseClazzName == null
//                ? "cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity.class, "
//                : info.responseClazzName + ".class, ")
//
//                + "\"" + info.baseUrlKey + "\"" + ", "
//                + "\"" + info.url + "\"" + ", "
//                + info.writeTime + ", "
//                + info.readTime + ", "
//                + info.connectOutTime + ","
//                + info.isEncryption + ","
//                + info.encryptionKey + ", "
//                + info.netPattern + ", "
//                + "callback);\n");
//
//        buffer.append("\n}\n\n");
//
//        // 过时
//        buffer.append("@Deprecated\n");
//        buffer.append("public static void startSoftNet(IRequestEntity entity, FlowableTransformer transformer, IAutoNetDataCallback callback) {\n");
//        buffer.append("AutoNet.getInstance().startNet("
//                + "entity" + ", "
//                + (info.responseClazzName == null
//                ? "cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity.class, "
//                : info.responseClazzName + ".class, ")
//
//                + "\"" + info.baseUrlKey + "\"" + ", "
//                + "\"" + info.url + "\"" + ", "
//                + "null" + ", "
//                + info.writeTime + ", "
//                + info.readTime + ", "
//                + info.connectOutTime + ","
//                + info.isEncryption + ","
//                + info.encryptionKey + ", "
//                + info.netPattern + ", "
//                + info.reqType + ", "
//                + info.resType + ", "
//                + "transformer, callback);\n");
//
//        buffer.append("\n}\n\n");
//
//
//        buffer.append("public static void startUnSoftNet(Object object, IRequestEntity entity) {\n");
//        buffer.append("startSoftNet(object, entity, null);\n");
//        buffer.append("\n}\n\n");
//
//        buffer.append("public static void startUnSoftNet(Object object) {\n");
//        buffer.append("startSoftNet(object, null, null, null);\n");
//        buffer.append("\n}\n\n");
//
//
//        buffer.append("public static void startSoftNet(Object object, FlowableTransformer transformer) {\n");
//        buffer.append("startSoftNet(object, null, null, transformer);\n");
//        buffer.append("\n}\n\n");
//
//        buffer.append("public static void startSoftNet(Object object, IRequestEntity entity, FlowableTransformer transformer) {\n");
//        buffer.append("startSoftNet(object, entity, null, transformer);\n");
//        buffer.append("\n}\n\n");
//
//        buffer.append("public static void startUnSoftNet(Object object, IRequestEntity entity, String extraParam) {\n");
//        buffer.append("startSoftNet(object, entity, extraParam, null);\n");
//        buffer.append("\n}\n\n");
//
//        buffer.append("public static void startUnSoftNet(Object object, String extraParam) {\n");
//        buffer.append("startSoftNet(object, null, extraParam, null);\n");
//        buffer.append("\n}\n\n");
//
//        buffer.append("public static void startSoftNet(Object object, String extraParam, FlowableTransformer transformer) {\n");
//        buffer.append("startSoftNet(object, null, extraParam, transformer);\n");
//        buffer.append("\n}\n\n");
//
//        buffer.append("public static void startSoftNet(Object object, IRequestEntity entity, String extraParam, FlowableTransformer transformer) {\n");
//        buffer.append("json(object, entity, extraParam, transformer);\n");
//        buffer.append("\n}\n\n");
//
//        /**
//         * ----------------------------------
//         * File opration
//         * ----------------------------------
//         */
//        /**
//         * SendFile
//         */
//        buffer.append("public static void pushFile(Object object, String fileKey, String path) {\n");
//        buffer.append("pushFile(object, fileKey, path, null);");
//        buffer.append("\n}\n\n");
//        /**
//         * RecFile
//         */
//        buffer.append("public static void pullFile(Object object, String path, String fileName) {\n");
//        buffer.append("pullFile(object, path, fileName, null);");
//        buffer.append("\n}\n\n");
//
//        /**
//         * SendFile
//         */
//        buffer.append("public static void pushFile(Object object, IRequestEntity entity, String fileKey, String path) {\n");
//        buffer.append("stream(object, entity, fileKey, path, null, null);\n");
//        buffer.append("\n}\n\n");
//        /**
//         * RecFile
//         */
//        buffer.append("public static void pullFile(Object object, IRequestEntity entity, String path, String fileName) {\n");
//        buffer.append("stream(object, null, null, path, fileName, null);\n");
//        buffer.append("\n}\n\n");
//
//
//        /**
//         * SendFile
//         */
//        buffer.append("public static void pushFile(Object object, String fileKey, String path, FlowableTransformer transformer) {\n");
//        buffer.append("stream(object, null, fileKey, path, null, transformer);\n");
//        buffer.append("\n}\n\n");
//        /**
//         * RecFIle
//         */
//        buffer.append("public static void pullFile(Object object, String path, String fileName, FlowableTransformer transformer) {\n");
//        buffer.append("stream(object, null, null, path, fileName, transformer);\n");
//        buffer.append("\n}\n\n");
//
//
//        /**
//         * SendFile
//         */
//        buffer.append("public static void pushFile(Object object, IRequestEntity entity, String fileKey, String path, FlowableTransformer transformer) {\n");
//        buffer.append("stream(object, entity, fileKey, path, null, transformer);\n");
//        buffer.append("\n}\n\n");
//        /**
//         * RecFIle
//         */
//        buffer.append("public static void pullFile(Object object, IRequestEntity entity, String path, String fileName, FlowableTransformer transformer) {\n");
//        buffer.append("stream(object, entity, null, path, fileName, transformer);\n");
//        buffer.append("\n}\n\n");
//
//
//        /**
//         * --------------------------------
//         * Mather
//         * --------------------------------
//         */
//        /**
//         * Json Mather
//         */
//        buffer.append("private static void json(Object object, IRequestEntity entity, String extraParam, FlowableTransformer transformer) {\n");
//        buffer.append("AutoNet.getInstance().startNet("
//                + "entity" + ", "
//                + (info.responseClazzName == null
//                ? "cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity.class, "
//                : info.responseClazzName + ".class, ")
//                + "\"" + info.baseUrlKey + "\"" + ", "
//                + "\"" + info.url + "\"" + ", "
//                + "extraParam" + ", "
//                + info.writeTime + ", "
//                + info.readTime + ", "
//                + info.connectOutTime + ","
//                + info.isEncryption + ","
//                + info.encryptionKey + ", "
//                + info.netPattern + ", "
//                + info.reqType + ", "
//                + info.resType + ", "
//                + "transformer, "
//                + (info.outClassFullPackageName == null || info.outClassFullPackageName.equals(info.fullPackageName) ? "(IAutoNetDataCallback)object"
//                : "((" + info.outClassFullPackageName + ")object).new " + info.className + "()")
//                + ");\n");
//        buffer.append("\n}\n\n");
//
//        /**
//         * stream Mather
//         */
//        buffer.append("private static void stream(Object object, IRequestEntity entity, String fileKey, String path, String fileName, FlowableTransformer transformer) {\n");
//        buffer.append("AutoNet.getInstance().startStream("
//                + "entity" + ", "
//                + (info.responseClazzName == null
//                ? "cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity.class, "
//                : info.responseClazzName + ".class, ")
//                + "\"" + info.mediaType + "\"" + ", "
//                + "fileKey, "
//                + "path" + ", "
//                + "fileName" + ", "
//                + "\"" + info.baseUrlKey + "\"" + ", "
//                + "\"" + info.url + "\"" + ", "
//                + info.writeTime + ", "
//                + info.readTime + ", "
//                + info.connectOutTime + ","
//                + info.netPattern + ", "
//                + info.reqType + ", "
//                + info.resType + ", "
//                + "transformer, "
//                + (info.outClassFullPackageName == null || info.outClassFullPackageName.equals(info.fullPackageName) ? "(IAutoNetDataCallback)object"
//                : "((" + info.outClassFullPackageName + ")object).new " + info.className + "()")
//                + ");\n");
//        buffer.append("\n}\n\n");
//
//        // class end
//        buffer.append("\n}");
//
//
//        Writer writer;
//
//        writer = classFile.openWriter();
//        String code = buffer.toString();
//        writer.write(code);
//        writer.flush();
//        writer.close();
//    }

}
