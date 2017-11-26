package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by 小稀革 on 2017/11/26.
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNet {

    String url() default "/";

    long writeTime() default 5000;

    long readTime() default 5000;

    long connectOutTime() default 5000;
}
