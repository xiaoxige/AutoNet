package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 *         NetPattern: 网络的请求类型
 *         value: 请求类型
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetPatternAnontation {

    /**
     * 网络请求的类型
     * get: get请求
     * post: post请求
     * delete: delete请求
     * put： put请求
     * other: 暂无意义
     */
    enum NetPattern {
        GET,
        POST,
        DELETE,
        PUT,
        OTHER
    }

    NetPattern value() default NetPattern.GET;
}
