package jrouter.home.interceptor;

import jrouter.annotation.Interceptor;
import jrouter.servlet.ServletActionInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * TimerInterceptor.
 */
public class TimerInterceptor {

    /** 日志 */
    private static final Logger LOG = LoggerFactory.getLogger(TimerInterceptor.class);

    /** slow log */
    private static final Logger SLOW_ACTION_LOG = LoggerFactory.getLogger("slow.action");

    /** 计时拦截器 */
    public static final String TIMER = "timer";

    //default 5s
    @Value("${common.slowTimeMillis:5000}")
    private long slowTimeMillis = Long.MAX_VALUE;

    /**
     * 记录Action调用耗时。
     *
     * @param invocation Action运行时上下文。
     *
     * @return 拦截器处理后的Action调用结果。
     */
    @Interceptor(name = TIMER)
    public Object timer(ServletActionInvocation invocation) {
        long startTime = System.currentTimeMillis();
        Object result = null;
        try {
            //invoke
            result = invocation.invoke();
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            String msg = null;
            if (LOG.isInfoEnabled()) {
                msg = buildMessage(executionTime, invocation);
                LOG.info(msg);
            }
            if (SLOW_ACTION_LOG.isWarnEnabled() && executionTime > slowTimeMillis) {
                if (msg == null)
                    msg = buildMessage(executionTime, invocation);
                SLOW_ACTION_LOG.warn(msg);
            }
            invocation.getContextMap().put("_executionTime", executionTime);
        }
        return result;
    }

    protected String buildMessage(long executionTime, ServletActionInvocation invocation) {
        StringBuilder message = new StringBuilder(64);
        message.append("Executed action [").append(invocation.getActionPath());
        message.append("] took ").append(executionTime).append(" ms.");
        return message.toString();
    }
}
