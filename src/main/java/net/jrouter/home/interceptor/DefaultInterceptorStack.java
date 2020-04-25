package net.jrouter.home.interceptor;

import net.jrouter.annotation.InterceptorStack;

/**
 * DefaultInterceptorStack.
 */
public class DefaultInterceptorStack {

    /** 时间记录 -> 异常处理 */
    @InterceptorStack(interceptors = {
            @InterceptorStack.Interceptor(TimerInterceptor.TIMER),
            @InterceptorStack.Interceptor(ExceptionInterceptor.EXCEPTION)
    }, include = "/**/**")
    public static final String DEFAULT = "default";

    /** 空拦截栈 */
    @InterceptorStack(interceptors = {})
    public static final String EMPTY = "empty";

}
