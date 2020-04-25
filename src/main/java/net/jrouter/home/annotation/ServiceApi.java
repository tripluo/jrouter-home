package net.jrouter.home.annotation;

import java.lang.annotation.*;

/**
 * 表征 API 服务类。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceApi {

    /**
     * 命名空间名称。
     *
     * @return 命名空间名称。
     */
    String namespace() default "";

    /**
     * 命名空间的拦截栈名称。
     *
     * @return 命名空间的默认拦截栈名称。
     */
    String interceptorStack() default "";

    /**
     * 是否自动加载定义类所有public/protected方法，默认不自动加载。
     *
     * @return 是否自动加载所定义类的public/protected方法，默认不自动加载。
     */
    boolean autoIncluded() default true;
}
