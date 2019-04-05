package net.jrouter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.jrouter.home.annotation.ServiceApi;
import net.jrouter.home.configure.ProjectProperties;
import net.jrouter.home.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.Ordered;

/**
 * AppStart.
 */
@Configuration
@EnableAutoConfiguration(exclude = {
        ErrorMvcAutoConfiguration.class,
        FreeMarkerAutoConfiguration.class,
        ThymeleafAutoConfiguration.class,
        WebMvcAutoConfiguration.class
})
@ComponentScan(basePackages = Constants.BASE_PACKAGE,
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ANNOTATION, value = ServiceApi.class)
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "test.*")
        }
)
@Slf4j
public class AppStart {

    @Autowired
    private ProjectProperties projectProperties;

    //main
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AppStart.class);
        ConfigurableApplicationContext context = app.run(args);
//        for (String bn : context.getBeanDefinitionNames()) {
//            System.out.println(bn + "--->" + context.getBean(bn));
//        }
        ServerProperties serverProp = context.getBean(ServerProperties.class);
        log.info(String.format("Project is running at http://localhost:%d", serverProp.getPort()));
        System.out.println("-----Started-----");
    }

    @Bean
    public FilterRegistrationBean indexFactoryFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter indexFilter = new Filter() {

            private String indexURL;

            private Set<String> forwardUrls;

            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
                indexURL = projectProperties.getIndexURL();
                forwardUrls = new HashSet<>(Arrays.asList(projectProperties.getForwardUrls()));
            }

            @Override
            public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
                    throws IOException, ServletException {
                String path = ((HttpServletRequest) req).getServletPath();
                if ("/".equals(path)) {
                    req.getRequestDispatcher(indexURL).forward(req, res);
                } else if (forwardUrls.contains(path)) {
                    req.getRequestDispatcher(path + projectProperties.getTemplate().getExtension()).forward(req, res);
                } else {
                    chain.doFilter(req, res);
                }
            }

            @Override
            public void destroy() {
            }
        };

        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.addUrlPatterns("/*");
        registration.setFilter(indexFilter);
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        return registration;
    }
}
