package net.jrouter.home.configure;

import java.time.Duration;
import java.util.Map;
import static net.jrouter.home.util.Constants.UTF_8;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @see ServerProperties
 */
@Configuration
@ConfigurationProperties(prefix = "project")
@lombok.Getter
@lombok.Setter
public class ProjectProperties {

    @Value("${server.context-path:}")
    private String contextPath = "";

    private String name;

    private String indexURL = "/index.jsp";

    private String[] forwardUrls;

    private String characterEncoding = UTF_8;

    //slow action 5s
    private Duration slowActionTime = Duration.ofSeconds(5);

    private Map<String, String> sharedVariables;

    private Template template = new Template();

    @lombok.Getter
    @lombok.Setter
    public static class Template {

        private String sharedVariablesName = "sharedVariables";

        private String extension = "";

        private String templatePath = "/";

        private boolean cacheable = true;

    }

}
