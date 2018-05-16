package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 *         加密注解
 *         key: 标识哪一个请求需要加密
 *         value: 是否启动加密
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetEncryptionAnontation {

    long key() default 0;

    boolean value() default false;

}
