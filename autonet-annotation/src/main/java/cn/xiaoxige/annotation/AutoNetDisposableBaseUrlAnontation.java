package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2018/5/17 0017.
 * Once a domain name, the domain name will only affect this request.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetDisposableBaseUrlAnontation {

    String value() default "";
}
