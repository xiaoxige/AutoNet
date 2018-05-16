package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 *         value: 域名(协议://Ip:Port)的选择key
 *         根据不同的key可以选择对应的域名
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetBaseUrlKeyAnontation {

    String value() default "default";
}
