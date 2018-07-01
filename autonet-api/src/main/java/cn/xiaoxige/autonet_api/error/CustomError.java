package cn.xiaoxige.autonet_api.error;

/**
 * @author by xiaoxige on 2018/7/1.
 *         自定义错误
 */

public class CustomError extends Exception {

    public CustomError(String error) {
        super(error);
    }
}