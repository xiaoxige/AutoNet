package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 * <p>
 * desc: setting basic information for network access links
 * <p>
 * <p>
 * value: web site is part of the domain name
 * writeTime: writes timeout time
 * readTime: reads the timeout time
 * connectOutTime: link timeout time
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetAnontation {

    String value() default "/";

    int flag() default 0;

    long writeTime() default 5000;

    long readTime() default 5000;

    long connectOutTime() default 5000;
}