package net.jrouter.home.configure;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * WebServerFactoryCustomizerBean.
 */
@Component
public class WebServerFactoryCustomizerBean implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {

    @Override
    public void customize(ConfigurableServletWebServerFactory server) {
        ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/40x.html");
        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/40x.html");
        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/50x.html");
        server.addErrorPages(error401Page, error404Page, error500Page);
    }

}
