package jrouter.home.interceptor;

import jrouter.annotation.Interceptor;
import jrouter.framework.util.ParameterUtil;
import jrouter.impl.InvocationProxyException;
import jrouter.servlet.ServletActionInvocation;
import jrouter.servlet.ServletThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExceptionInterceptor.
 */
public class ExceptionInterceptor {

    /** 日志 */
    private static final Logger LOG = LoggerFactory.getLogger(ExceptionInterceptor.class);

    public static final String EXCEPTION = "exception";

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
            String actionInfo = invocation.getActionPath() + " -> " + ParameterUtil.parseRequestParameters(invocation.getRequestParameters());
            LOG.error(actionInfo, e);
            //store exception
            ServletThreadContext.setException(e);
        }
        return result;
    }
}
