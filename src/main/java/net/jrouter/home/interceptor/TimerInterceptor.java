package net.jrouter.home.interceptor;

import lombok.extern.slf4j.Slf4j;
import net.jrouter.annotation.Interceptor;
import net.jrouter.home.configure.ProjectProperties;
import net.jrouter.http.servlet.ServletActionInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TimerInterceptor.
 */
@Slf4j
public class TimerInterceptor {

    /** 计时拦截器 */
    public static final String TIMER = "timer";

    /** slow log */
    private static final Logger SLOW_ACTION_LOG = LoggerFactory.getLogger("slow.action");

    @Autowired
    private ProjectProperties projectProperties;

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
            if (log.isInfoEnabled()) {
                msg = buildMessage(executionTime, invocation);
                log.info(msg);
            }
            if (SLOW_ACTION_LOG.isWarnEnabled() && executionTime > projectProperties.getSlowActionTime().toMillis()) {
                if (msg == null) {
                    msg = buildMessage(executionTime, invocation);
                }
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
