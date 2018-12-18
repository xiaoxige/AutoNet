package cn.xiaoxige.recommendeddemo.util;

import android.text.TextUtils;

import cn.xiaoxige.autonet_api.error.CustomError;
import cn.xiaoxige.autonet_api.error.EmptyError;
import cn.xiaoxige.autonet_api.error.NoNetError;

/**
 * @author by zhuxiaoan on 2018/7/4 0004.
 * 统一错误处理
 */

public class HandlerError {

    public static boolean handlerError(Throwable throwable) {
        if (throwable instanceof CustomError) {
            String message = throwable.getMessage();
            if (TextUtils.isEmpty(message)) {
                message = "数据异常， 请稍后再试";
            }
//            SingletonToastUtil.showLongToast(message);

            return true;
        } else if (throwable instanceof NoNetError) {

//            SingletonToastUtil.showLongToast("网络异常， 请检查网络连接");

            return true;
        } else if (throwable instanceof EmptyError) {
            handlerEmpty();
            return true;
        }

        return false;
    }

    public static void handlerEmpty() {

//        SingletonToastUtil.showLongToast("暂无数据");

    }
}
