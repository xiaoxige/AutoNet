package cn.xiaoxige.processor;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;

/**
 * Created by zhuxiaoan on 2017/11/26.
 */

public class ProxyWriteUtil {

    public static void write(Map<String, ProxyInfo> infoMap, Filer filer) {

        if (infoMap == null || infoMap.size() <= 0) {
            return;
        }

        Set<String> keys = infoMap.keySet();
        for (String key : keys) {
            ProxyInfo info = infoMap.get(key);
            write(info, filer);
        }

    }

    private static void write(ProxyInfo info, Filer filer) {

    }

}
