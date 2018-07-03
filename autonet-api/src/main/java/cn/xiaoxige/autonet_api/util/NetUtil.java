package cn.xiaoxige.autonet_api.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author by zhuxiaoan on 2018/7/3 0003.
 *         网络相关的工具类
 */

public class NetUtil {
    /**
     * 判定网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            return getNetworkInfo(context).isAvailable();
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * 获取可用网络信息
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        return context == null ? null : ((ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }
}