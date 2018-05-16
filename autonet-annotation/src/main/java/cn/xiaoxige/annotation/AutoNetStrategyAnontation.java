package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 *         <p>
 *         desc: network request strategy
 *         <p>
 *         <p>
 *         value: strategy
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetStrategyAnontation {

    /**
     * <p>
     * network strategy
     * <p>
     * LOCAL: local request
     * NET: network request
     * LOCAL_NET: first local back network
     * NET_LOCAL: first network after the local
     */
    enum NetStrategy {
        LOCAL,
        NET,
        LOCAL_NET,
        NET_LOCAL
    }

    NetStrategy value() default NetStrategy.NET;

}
