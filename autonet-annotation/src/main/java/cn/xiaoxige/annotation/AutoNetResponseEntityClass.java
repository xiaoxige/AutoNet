package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2017/11/27 0027.
 *         指定返回结构的实体类类型
 *         value: 指定返回实体类的类型
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetResponseEntityClass {

    Class<?> value() default Void.class;

}
