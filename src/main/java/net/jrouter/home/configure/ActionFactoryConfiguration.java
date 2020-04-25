package net.jrouter.home.configure;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.DispatcherType;
import net.jrouter.ActionFactory;
import net.jrouter.ActionFilter;
import net.jrouter.annotation.Action;
import net.jrouter.annotation.Namespace;
import net.jrouter.framework.result.thymeleaf.ThymeleafResult;
import net.jrouter.home.annotation.ServiceApi;
import static net.jrouter.home.util.Constants.UTF_8;
import net.jrouter.http.servlet.ObjectHandlerActionFactory;
import net.jrouter.http.servlet.filter.SpringBeanJRouterFilter;
import net.jrouter.spring.SpringObjectFactory;
import net.jrouter.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

/**
 * ActionFactoryConfiguration.
 */
@Configuration
//@ImportResource("classpath:jrouter-home-spring.xml")
public class ActionFactoryConfiguration {

    @Autowired
    private ThymeleafResult thymeleafResult;

    @Bean
    ActionFactory<String> actionFactory(ApplicationContext applicationContext) {
        ObjectHandlerActionFactory.Properties properties = new ObjectHandlerActionFactory.Properties();
        properties.setActionPathCaseSensitive(false);
        properties.setExtension(".");
        properties.setDefaultResultType("");
        properties.setDefaultInterceptorStack(net.jrouter.home.interceptor.DefaultInterceptorStack.DEFAULT);
        properties.setObjectFactory(new SpringObjectFactory(applicationContext));
        properties.setActionFilter(new ActionFilter() {

            @Override
            public boolean accept(Object obj, Method method) {
                return method.isAnnotationPresent(Action.class);
            }

            @Override
            public Action getAction(Object obj, Method method) {
                return method.getAnnotation(Action.class);
            }

            @Override
            public Namespace getNamespace(Object obj, Method method) {
                Class<?> clazz = method.getDeclaringClass();
                final ServiceApi serviceApi = clazz.getAnnotation(ServiceApi.class);
                if (serviceApi != null) {
                    String namespace = serviceApi.namespace();
                    if (StringUtil.isBlank(namespace)) {
                        namespace = clazz.getCanonicalName();
                    }
                    final String name = namespace;
                    return new Namespace() {

                        @Override
                        public Class<? extends Annotation> annotationType() {
                            return ServiceApi.class;
                        }

                        @Override
                        public String name() {
                            return name;
                        }

                        @Override
                        public String interceptorStack() {
                            return serviceApi.interceptorStack();
                        }

                        @Override
                        public String[] interceptors() {
                            return new String[0];
                        }

                        @Override
                        public boolean autoIncluded() {
                            return serviceApi.autoIncluded();
                        }

                    };
                }
                return clazz.getAnnotation(Namespace.class);
            }
        });
        ObjectHandlerActionFactory actionFactory = new ObjectHandlerActionFactory(properties);

        actionFactory.addInterceptors(net.jrouter.home.interceptor.TimerInterceptor.class);
        actionFactory.addInterceptors(net.jrouter.home.interceptor.ExceptionInterceptor.class);

        actionFactory.addInterceptorStacks(net.jrouter.home.interceptor.DefaultInterceptorStack.class);

        actionFactory.addResultTypes(net.jrouter.http.servlet.result.ServletResult.class);
        actionFactory.addResultTypes(thymeleafResult);

        actionFactory.setObjectResultTypes(Collections.EMPTY_MAP);

        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(ServiceApi.class);
        if (!CollectionUtils.isEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                actionFactory.addActions(serviceBean);
            }
        }
        return actionFactory;
    }

    @Bean
    public FilterRegistrationBean actionFactoryFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        SpringBeanJRouterFilter actionFactoryFilter = new SpringBeanJRouterFilter();
//        actionFactoryFilter.setEncoding(UTF_8);
//        actionFactoryFilter.setFactoryName("jrouter_factory");
//        actionFactoryFilter.setLogNotFoundException(false);
//        actionFactoryFilter.setTrimRequestParameter(true);
//        actionFactoryFilter.setUseThreadLocal(true);

        Map<String, String> initParameters = new HashMap<>();
        //Character encoding (optional)
        initParameters.put("encoding", UTF_8);
        //Trim HttpServletRequest#getParameter(String) (optional) (default:false)
        initParameters.put("trimRequestParameter", "true");
        //ActionFactory's name in ServletContext (optional)
        initParameters.put("factoryName", "jrouter_factory");
        //log NotFoundException (default:true)
        initParameters.put("logNotFoundException", "false");
        registration.setInitParameters(initParameters);

        registration.addUrlPatterns("*.jj");
        registration.setFilter(actionFactoryFilter);
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);
        return registration;
    }

}
