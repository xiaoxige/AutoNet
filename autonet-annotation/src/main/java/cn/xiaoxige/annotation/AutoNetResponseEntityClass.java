package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2017/11/27 0027.
 * <p>
 * desc: the entity class type that specifies the return structure
 * <p>
 * <p>
 * value: specifies the type of return entity class
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetResponseEntityClass {

    Class<?> value() default Void.class;

}
