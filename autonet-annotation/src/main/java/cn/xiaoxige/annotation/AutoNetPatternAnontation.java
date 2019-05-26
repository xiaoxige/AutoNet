package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2018/5/16 0016.
 * <p>
 * desc: network request type
 * <p>
 * <p>
 * NetPattern: network
 * value: request type
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetPatternAnontation {

    /**
     * <p>
     * type of network request
     * <p>
     * <p>
     * get: get request
     * post: post request
     * delete: delete request
     * put:put request
     * other: has no significance for the time being
     */
    enum NetPattern {
        /**
         * net request the way of get
         */
        GET,
        /**
         * net request the way of post
         */
        POST,
        /**
         * net request the way of delete
         */
        DELETE,
        /**
         * net request the way of put
         */
        PUT,
        /**
         * net request the way of other (this way temporarily not supported)
         */
        OTHER_PATTERN
    }

    NetPattern value() default NetPattern.GET;
}
