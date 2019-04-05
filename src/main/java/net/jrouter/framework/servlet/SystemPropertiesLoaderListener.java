package net.jrouter.framework.servlet;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

/**
 * SystemPropertiesLoaderListener.
 *
 * @deprecated
 */
public class SystemPropertiesLoaderListener implements ServletContextListener {

    /**
     * web.xml中配置参数。
     * <p>
     * <context-param>
     * <param-name>systemPropertiesConfigLocation</param-name>
     * <param-value>classpath:system.properties</param-value>
     * </context-param>
     */
    public static final String CONFIG_LOCATION_PARAM = "systemPropertiesConfigLocation";

    /** Default config location from the class path */
    public static final String DEFAULT_CONFIG_LOCATION = "classpath:system.properties";

    /** log */
    private static final Logger LOG = LoggerFactory.getLogger(SystemPropertiesLoaderListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();
        String location = sc.getInitParameter(CONFIG_LOCATION_PARAM);
        if (location == null) {
            location = DEFAULT_CONFIG_LOCATION;
        }
        try {
            URL url = ResourceUtils.getURL(location);
            Properties props = new Properties();
            props.load(url.openStream());
            LOG.info("Load SystemProperties from [{}]", url);
            Properties sysProps = System.getProperties();
            for (Map.Entry e : props.entrySet()) {
                LOG.info("Set System Property [{}:{}]", e.getKey(), e.getValue());
            }
            sysProps.putAll(props);
        } catch (IOException ex) {
            LOG.warn("Could not load SystemProperties from [" + location + "]", ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sc) {
        // Does nothing.
    }

}
