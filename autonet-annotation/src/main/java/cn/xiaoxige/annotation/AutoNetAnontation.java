package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 *         设置网络访问链接的基本信息
 *         value: 网址除了域名的部分
 *         writeTime: 写入超时时间
 *         readTime: 读取超时时间
 *         connectOutTime: 链接超时时间
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetAnontation {

    String value() default "/";

    long writeTime() default 5000;

    long readTime() default 5000;

    long connectOutTime() default 5000;
}