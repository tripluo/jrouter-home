package net.jrouter.framework.result.thymeleaf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.jrouter.annotation.ResultType;
import net.jrouter.http.servlet.ServletActionInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.WebExpressionContext;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.expression.ThymeleafEvaluationContext;
import org.thymeleaf.spring5.view.ThymeleafView;

/**
 * ThymeleafResult.
 *
 * @see ThymeleafView
 */
@Slf4j
public class ThymeleafResult implements ApplicationContextAware, InitializingBean {

    public static final String TYPE = "thymeleaf";

    @lombok.Setter
    private ITemplateEngine templateEngine;

    @lombok.Setter
    private Map<String, Object> staticVariables = null;

    @lombok.Setter
    private boolean enableSpringEL = false;

    /** ApplicationContext this object runs in */
    private ApplicationContext applicationContext;

    @ResultType(type = TYPE)
    public void result(ServletActionInvocation invocation) throws IOException {
        HttpServletResponse response = invocation.getResponse();
        if (response.isCommitted()) {
            log.info("HttpServletResponse is already committed at : {}", invocation.getActionPath());
            return;
        }
        if (invocation.getResult() != null) {
            String location = invocation.getResult().location();
            if (location.charAt(0) != '/') {
                location = "/" + location;
            }
            IContext context = createContext(invocation);
            templateEngine.process(location, context, response.getWriter());
        }
    }

    /**
     * @see ThymeleafView#renderFragment
     */
    protected IContext createContext(ServletActionInvocation invocation) {
        ServletContext servletContext = invocation.getServletContext();
        HttpServletRequest request = invocation.getRequest();
        final Map<String, Object> mergedModel = new HashMap<>(invocation.getContextMap());
        if (staticVariables != null) {
            mergedModel.putAll(staticVariables);
        }
        mergedModel.put("actionPath", invocation.getActionPath());
        mergedModel.put("request", request);
        mergedModel.put("parameters", invocation.getRequestParameters());
        mergedModel.put("base", request.getContextPath());
        //expose all spring configurations/beans or not
        if (enableSpringEL) {
            // Expose Thymeleaf's own evaluation context as a model variable
            //
            // Note Spring's EvaluationContexts are NOT THREAD-SAFE (in exchange for SpelExpressions being thread-safe).
            // That's why we need to create a new EvaluationContext for each request / template execution, even if it is
            // quite expensive to create because of requiring the initialization of several ConcurrentHashMaps.
            final ConversionService conversionService
                    = (ConversionService) request.getAttribute(ConversionService.class.getName()); // might be null!
            final ThymeleafEvaluationContext evaluationContext
                    = new ThymeleafEvaluationContext(applicationContext, conversionService);
            mergedModel.put(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME, evaluationContext);
        }
        return new WebExpressionContext(templateEngine.getConfiguration(), request, invocation.getResponse(), servletContext, Locale.getDefault(), mergedModel);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * @see SpringTemplateEngine#getConfiguration()
     */
    @Override
    public void afterPropertiesSet() {
        Assert.notNull(templateEngine, "ITemplateEngine can't be null.");
        Assert.notNull(templateEngine.getConfiguration(), "ITemplateEngine.Configuration can't be null.");
    }
}
