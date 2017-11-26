package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 小稀革 on 2017/11/26.
 * 请求网络的方式
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetPatternAnontation {
    enum NetPattern {
        GET,
        POST,
        DELETE,
        PUT
    }

    NetPattern value() default NetPattern.GET;
}
