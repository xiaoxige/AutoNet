package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiaoxige
 * @date 2019/5/26 10:48 AM
 * -
 * email: xiaoxigexiaoan@outlook.com
 * desc: AutoNet target entity type
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetTargetEntityClass {

    Class<?> value() default Void.class;

}
