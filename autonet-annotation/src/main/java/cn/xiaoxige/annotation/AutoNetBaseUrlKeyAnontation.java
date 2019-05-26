package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 * <p>
 * desc: can choose the corresponding domain name according to different key.
 * <p>
 * <p>
 * value: domain name (protocol: //Ip:Port) key
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetBaseUrlKeyAnontation {

    String value() default "default";

}
