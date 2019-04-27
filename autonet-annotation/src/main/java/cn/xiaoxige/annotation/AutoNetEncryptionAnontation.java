package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 * <p>
 * desc: encrypted annotation
 * <p>
 * <p>
 * key: identifies which request needs to be encrypted
 * value: whether to start encryption
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetEncryptionAnontation {

    long key() default 0;

    boolean value() default false;

}
