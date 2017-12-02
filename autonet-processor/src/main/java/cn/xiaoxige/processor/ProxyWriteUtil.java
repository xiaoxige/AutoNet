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
                + info.writeTime + ", "
                + info.readTime + ", "
                + info.connectOutTime + ","
                + info.isEncryption + ","
                + info.encryptionKey + ", "
                + info.netPattern + ", "
                + "transformer, callback);\n");

        buffer.append("\n}\n\n");

        buffer.append("public static void startUnSoftNet(Object object, IRequestEntity entity) {\n");

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
                + (info.outClassFullPackageName == null || info.outClassFullPackageName.equals(info.fullPackageName) ? "new " + info.fullPackageName + "()"
                : "((" + info.outClassFullPackageName + ")object).new " + info.className + "()")
                + ");\n");

        buffer.append("\n}\n\n");


        buffer.append("public static void startSoftNet(Object object, IRequestEntity entity, FlowableTransformer transformer) {\n");
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
                + "transformer, "
                + (info.outClassFullPackageName == null || info.outClassFullPackageName.equals(info.fullPackageName) ? "new " + info.fullPackageName + "()"
                : "((" + info.outClassFullPackageName + ")object).new " + info.className + "()")
                + ");\n");

        buffer.append("\n}\n\n");


        buffer.append("public static void startUnSoftNet(Object object) {\n");

        buffer.append("AutoNet.getInstance().startNet("
                + "null" + ", "

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
                + (info.outClassFullPackageName == null || info.outClassFullPackageName.equals(info.fullPackageName) ? "new " + info.fullPackageName + "()"
                : "((" + info.outClassFullPackageName + ")object).new " + info.className + "()")
                + ");\n");

        buffer.append("\n}\n\n");

        buffer.append("public static void startSoftNet(Object object, FlowableTransformer transformer) {\n");
        buffer.append("AutoNet.getInstance().startNet("
                + "null" + ", "

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
                + "transformer, "
                + (info.outClassFullPackageName == null || info.outClassFullPackageName.equals(info.fullPackageName) ? "new " + info.fullPackageName + "()"
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
