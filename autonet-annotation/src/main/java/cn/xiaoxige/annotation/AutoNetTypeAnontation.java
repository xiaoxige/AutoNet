package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2017/12/11 0011.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetTypeAnontation {
    enum Type {
        JSON,
        STREAM
    }

    /**
     * 请求类型
     *
     * @return
     */
    Type reqType() default Type.JSON;

    /**
     * 接受类型
     *
     * @return
     */
    Type resType() default Type.JSON;
}
