package net.jrouter.home.interceptor;

import net.jrouter.annotation.Interceptor;
import net.jrouter.framework.util.RequestParameterUtil;
import net.jrouter.http.servlet.ServletActionInvocation;
import net.jrouter.http.servlet.ServletThreadContext;
import net.jrouter.impl.InvocationProxyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExceptionInterceptor.
 */
public class ExceptionInterceptor {

    public static final String EXCEPTION = "exception";

    /** 日志 */
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionInterceptor.class);

    @Interceptor(name = EXCEPTION)
    public Object intercept(ServletActionInvocation invocation) {
        Object result = null;
        try {
            //invoke
            result = invocation.invoke();
        } catch (Exception e) {
            if (e instanceof InvocationProxyException) {
                e = (Exception) ((InvocationProxyException) e).getSource();
            }
            String actionInfo = invocation.getActionPath() + " -> " + RequestParameterUtil.buildRequestParameters(invocation.getRequestParameters());
            LOG.error(actionInfo, e);
            invocation.setInvokeResult(e);
            //store exception
            ServletThreadContext.setException(e);
        }
        return result;
    }
}
