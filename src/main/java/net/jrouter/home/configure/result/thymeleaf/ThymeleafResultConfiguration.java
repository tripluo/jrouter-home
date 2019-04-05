package net.jrouter.home.configure.result.thymeleaf;

import java.util.HashMap;
import java.util.Map;
import net.jrouter.framework.result.thymeleaf.ThymeleafResult;
import net.jrouter.framework.service.URLService;
import net.jrouter.home.configure.ActionFactoryConfiguration;
import net.jrouter.home.configure.ProjectProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * ThymeleafResultConfiguration.
 */
@Configuration
@AutoConfigureBefore(ActionFactoryConfiguration.class)
public class ThymeleafResultConfiguration {

    @Autowired
    private ProjectProperties projectProperties;

    @Bean
    public ThymeleafResult thymeleafResult(SpringTemplateEngine engine) {
        ThymeleafResult result = new ThymeleafResult();
        result.setTemplateEngine(engine);
        Map<String, Object> staticVariables = new HashMap<>();
        ProjectProperties.Template template = projectProperties.getTemplate();
        staticVariables.put(template.getSharedVariablesName(), projectProperties.getSharedVariables());
        staticVariables.put("url", urlService());
        result.setStaticVariables(staticVariables);
        return result;
    }

    @Bean
    URLService urlService() {
        URLService urlService = new URLService();
        urlService.setExtension(projectProperties.getTemplate().getExtension());
        urlService.setContextPath(projectProperties.getContextPath());
        return urlService;
    }

    @Bean
    SpringResourceTemplateResolver springResourceTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("/WEB-INF/templates");
        resolver.setSuffix(".htm");
        resolver.setForceSuffix(false);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(projectProperties.getTemplate().isCacheable());
        resolver.setCharacterEncoding(projectProperties.getCharacterEncoding());
        return resolver;
    }

    @Bean
    SpringTemplateEngine springTemplateEngine(SpringResourceTemplateResolver resolver) {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(resolver);
        engine.setEnableSpringELCompiler(true);
        return engine;
    }

}
