package cn.xiaoxige.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by zhuxiaoan on 2017/12/11 0011.
 *         请求类型和返回类型
 *         reqType： 请求类型
 *         resType： 接受类型
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface AutoNetTypeAnontation {

    /**
     * 请求和接受的类型(主要为了标志请求的参数， 另一方面也在一定程度上对应了MediaType)
     * eg：
     * 1.post->body->json
     * 2.post->body->form
     * 3.post->body->stream
     * 4.get->url->form
     * ......
     * other: 暂没意义
     */
    enum Type {
        JSON,
        FORM,
        STREAM,
        OTHER
    }

    /**
     * 请求类型
     *
     * @return
     */
    Type reqType() default Type.JSON;

    /**
     * 接受类型
     *
     * @return
     */
    Type resType() default Type.JSON;
}
