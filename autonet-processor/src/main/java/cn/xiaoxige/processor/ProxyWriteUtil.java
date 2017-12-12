package cn.xiaoxige.processor;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public class ProxyWriteUtil {

    public static void write(Map<String, ProxyInfo> infoMap, Filer filer) throws IOException {

        if (infoMap == null || infoMap.size() <= 0) {
            return;
        }

        Set<String> keys = infoMap.keySet();
        for (String key : keys) {
            ProxyInfo info = infoMap.get(key);
            write(info, filer);
        }

    }

    private static void write(ProxyInfo info, Filer filer) throws IOException {

        String newClassName = info.className + ProxyInfo.AUTONETPROXY;
        String fullPackageName = info.fullPackageName;
        String[] split = fullPackageName.split("\\.");
        if (split != null && split.length > 2) {
            int length = split.length;
            newClassName = split[length - 2] + split[length - 1] + ProxyInfo.AUTONETPROXY;
        }

        if (newClassName.length() > 0) {
            char charAtHead = newClassName.charAt(0);
            char[] ch = new char[1];
            ch[0] = charAtHead;
            String atHead = new String(ch);
            String upperCase = atHead.toUpperCase();
            newClassName = newClassName.replaceFirst(atHead, upperCase);
        }

        JavaFileObject classFile
                = filer.createSourceFile(newClassName, info.typeElement);
        StringBuffer buffer = new StringBuffer();
        if (info.packageName != null && info.packageName.length() > 0) {
            buffer.append("package ").append(info.packageName).append(";\n\n");
        }
        buffer.append("import cn.xiaoxige.autonet_api.interfaces.IAutoNetDataCallback;\n")
                .append("import cn.xiaoxige.autonet_api.AutoNet;\n")
                .append("import static cn.xiaoxige.annotation.AutoNetPatternAnontation.NetPattern.GET;\n")
                .append("import static cn.xiaoxige.annotation.AutoNetPatternAnontation.NetPattern.POST;\n")
                .append("import static cn.xiaoxige.annotation.AutoNetPatternAnontation.NetPattern.DELETE;\n")
                .append("import static cn.xiaoxige.annotation.AutoNetPatternAnontation.NetPattern.PUT;\n")
                .append("import static cn.xiaoxige.annotation.AutoNetTypeAnontation.Type.JSON;\n")
                .append("import static cn.xiaoxige.annotation.AutoNetTypeAnontation.Type.STREAM;\n")
                .append("import cn.xiaoxige.autonet_api.data.requestentity.IRequestEntity;\n")
                .append("import io.reactivex.*;\n")
                .append("import cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity;")
                .append("import io.reactivex.FlowableTransformer;\n");

        if (info.packageName != null && info.packageName.length() > 0) {
            buffer.append("import " + info.packageName + ".*;\n");
        }
        buffer.append("import cn.xiaoxige.annotation.AutoNetPatternAnontation;\n\n")
        ;

        // class start
        buffer.append("public class " + (newClassName) + " { \n");

        // 过时
        buffer.append("@Deprecated\n");
        buffer.append("public static void startUnSoftNet(IRequestEntity entity, IAutoNetDataCallback callback) {\n");

        buffer.append("AutoNet.getInstance().startNet("
                + "entity" + ", "
                + (info.responseClazzName == null
                ? "cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity.class, "
                : info.responseClazzName + ".class, ")

                + "\"" + info.baseUrlKey + "\"" + ", "
                + "\"" + info.url + "\"" + ", "
                + info.writeTime + ", "
                + info.readTime + ", "
                + info.connectOutTime + ","
                + info.isEncryption + ","
                + info.encryptionKey + ", "
                + info.netPattern + ", "
                + "callback);\n");

        buffer.append("\n}\n\n");

        // 过时
        buffer.append("@Deprecated\n");
        buffer.append("public static void startSoftNet(IRequestEntity entity, FlowableTransformer transformer, IAutoNetDataCallback callback) {\n");
        buffer.append("AutoNet.getInstance().startNet("
                + "entity" + ", "
                + (info.responseClazzName == null
                ? "cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity.class, "
                : info.responseClazzName + ".class, ")

                + "\"" + info.baseUrlKey + "\"" + ", "
                + "\"" + info.url + "\"" + ", "
                + "null" + ", "
                + info.writeTime + ", "
                + info.readTime + ", "
                + info.connectOutTime + ","
                + info.isEncryption + ","
                + info.encryptionKey + ", "
                + info.netPattern + ", "
                + info.reqType + ", "
                + info.resType + ", "
                + "transformer, callback);\n");

        buffer.append("\n}\n\n");


        buffer.append("public static void startUnSoftNet(Object object, IRequestEntity entity) {\n");
        buffer.append("startSoftNet(object, entity, null);\n");
        buffer.append("\n}\n\n");

        buffer.append("public static void startUnSoftNet(Object object) {\n");
        buffer.append("startSoftNet(object, null, null, null);\n");
        buffer.append("\n}\n\n");


        buffer.append("public static void startSoftNet(Object object, FlowableTransformer transformer) {\n");
        buffer.append("startSoftNet(object, null, null, transformer);\n");
        buffer.append("\n}\n\n");

        buffer.append("public static void startSoftNet(Object object, IRequestEntity entity, FlowableTransformer transformer) {\n");
        buffer.append("startSoftNet(object, entity, null, transformer);\n");
        buffer.append("\n}\n\n");

        buffer.append("public static void startUnSoftNet(Object object, IRequestEntity entity, String extraParam) {\n");
        buffer.append("startSoftNet(object, entity, extraParam, null);\n");
        buffer.append("\n}\n\n");

        buffer.append("public static void startUnSoftNet(Object object, String extraParam) {\n");
        buffer.append("startSoftNet(object, null, extraParam, null);\n");
        buffer.append("\n}\n\n");

        buffer.append("public static void startSoftNet(Object object, String extraParam, FlowableTransformer transformer) {\n");
        buffer.append("startSoftNet(object, null, extraParam, transformer);\n");
        buffer.append("\n}\n\n");

        buffer.append("public static void startSoftNet(Object object, IRequestEntity entity, String extraParam, FlowableTransformer transformer) {\n");
        buffer.append("json(object, entity, extraParam, transformer);\n");
        buffer.append("\n}\n\n");

        /**
         * ----------------------------------
         * File opration
         * ----------------------------------
         */
        /**
         * SendFile
         */
        buffer.append("public static void pushFile(Object object, String fileKey, String path) {\n");
        buffer.append("pushFile(object, fileKey, path, null);");
        buffer.append("\n}\n\n");
        /**
         * RecFile
         */
        buffer.append("public static void pullFile(Object object, String path, String fileName) {\n");
        buffer.append("pullFile(object, path, fileName, null);");
        buffer.append("\n}\n\n");

        /**
         * SendFile
         */
        buffer.append("public static void pushFile(Object object, IRequestEntity entity, String fileKey, String path) {\n");
        buffer.append("stream(object, entity, fileKey, path, null, null);\n");
        buffer.append("\n}\n\n");
        /**
         * RecFile
         */
        buffer.append("public static void pullFile(Object object, IRequestEntity entity, String path, String fileName) {\n");
        buffer.append("stream(object, null, null, path, fileName, null);\n");
        buffer.append("\n}\n\n");


        /**
         * SendFile
         */
        buffer.append("public static void pushFile(Object object, String fileKey, String path, FlowableTransformer transformer) {\n");
        buffer.append("stream(object, null, fileKey, path, null, transformer);\n");
        buffer.append("\n}\n\n");
        /**
         * RecFIle
         */
        buffer.append("public static void pullFile(Object object, String path, String fileName, FlowableTransformer transformer) {\n");
        buffer.append("stream(object, null, null, path, fileName, transformer);\n");
        buffer.append("\n}\n\n");


        /**
         * SendFile
         */
        buffer.append("public static void pushFile(Object object, IRequestEntity entity, String fileKey, String path, FlowableTransformer transformer) {\n");
        buffer.append("stream(object, entity, fileKey, path, null, transformer);\n");
        buffer.append("\n}\n\n");
        /**
         * RecFIle
         */
        buffer.append("public static void pullFile(Object object, IRequestEntity entity, String path, String fileName, FlowableTransformer transformer) {\n");
        buffer.append("stream(object, entity, null, path, fileName, transformer);\n");
        buffer.append("\n}\n\n");


        /**
         * --------------------------------
         * Mather
         * --------------------------------
         */
        /**
         * Json Mather
         */
        buffer.append("private static void json(Object object, IRequestEntity entity, String extraParam, FlowableTransformer transformer) {\n");
        buffer.append("AutoNet.getInstance().startNet("
                + "entity" + ", "
                + (info.responseClazzName == null
                ? "cn.xiaoxige.autonet_api.data.responsentity.AutoResponseEntity.class, "
                : info.responseClazzName + ".class, ")
                + "\"" + info.baseUrlKey + "\"" + ", "
                + "\"" + info.url + "\"" + ", "
                + "extraParam" + ", "
                + info.writeTime + ", "
                + info.readTime + ", "
                + info.connectOutTime + ","
                + info.isEncryption + ","
                + info.encryptionKey + ", "
                + info.netPattern + ", "
                + info.reqType + ", "
                + info.resType + ", "
                + "transformer, "
                + (info.outClassFullPackageName == null || info.outClassFullPackageName.equals(info.fullPackageName) ? "(IAutoNetDataCallback)object"
                : "((" + info.outClassFullPackageName + ")object).new " + info.className + "()")
                + ");\n");
        buffer.append("\n}\n\n");

        /**
         * stream Mather
         */
        buffer.append("private static void stream(Object object, IRequestEntity entity, String fileKey, String path, String fileName, FlowableTransformer transformer) {\n");
        buffer.append("AutoNet.getInstance().startStream("
                + "entity" + ", "
                + "\"" + info.mediaType + "\"" + ", "
                + "fileKey, "
                + "path" + ", "
                + "fileName" + ", "
                + "\"" + info.baseUrlKey + "\"" + ", "
                + "\"" + info.url + "\"" + ", "
                + info.writeTime + ", "
                + info.readTime + ", "
                + info.connectOutTime + ","
                + info.netPattern + ", "
                + info.reqType + ", "
                + info.resType + ", "
                + "transformer, "
                + (info.outClassFullPackageName == null || info.outClassFullPackageName.equals(info.fullPackageName) ? "(IAutoNetDataCallback)object"
                : "((" + info.outClassFullPackageName + ")object).new " + info.className + "()")
                + ");\n");
        buffer.append("\n}\n\n");

        // class end
        buffer.append("\n}");


        Writer writer;

        writer = classFile.openWriter();
        String code = buffer.toString();
        writer.write(code);
        writer.flush();
        writer.close();
    }

}
