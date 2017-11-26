package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhuxiaoan on 2017/11/26.
 * method of net
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
