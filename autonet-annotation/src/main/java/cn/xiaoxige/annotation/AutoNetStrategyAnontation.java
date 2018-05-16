package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 *         网络请求的策略
 *         value: 策略
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetStrategyAnontation {

    /**
     * 网络策略
     * LOCAL: 本地请求
     * NET: 网络请求
     * LOCAL_NET： 先本地后网络
     * NET_LOCAL: 先网络后本地
     */
    enum NetStrategy {
        LOCAL,
        NET,
        LOCAL_NET,
        NET_LOCAL
    }

    NetStrategy value() default NetStrategy.NET;

}
