package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2017/12/11 0011.
 * <p>
 * desc: request type and return type
 * <p>
 * <p>
 * reqType: request type
 * resType: type of acceptance
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetTypeAnontation {

    /**
     * <p>
     * the type of request and acceptance (mainly for marking the parameters of the request, and on the other hand, to a certain extent, it corresponds to MediaType).
     * <p>
     * eg:
     * 1.post->body->json
     * 2.post->body->form
     * 3.post->body->stream
     * 4.get->url->form
     * ...
     * other: is temporarily meaningless
     */
    enum Type {
        /**
         * Data transfer protocol way of json
         */
        JSON,
        /**
         * Data transfer protocol way of form
         */
        FORM,
        /**
         * Data transfer protocol way of stream
         * if request type is stream, so is push file
         * else response type is stream, so is pull file
         */
        STREAM,
        /**
         * Data transfer protocol way of other (this way temporarily not supported)
         */
        OTHER_TYPE
    }

    /**
     * request Type
     *
     * @return
     */
    Type reqType() default Type.JSON;

    /**
     * response Type
     *
     * @return
     */
    Type resType() default Type.JSON;
}
