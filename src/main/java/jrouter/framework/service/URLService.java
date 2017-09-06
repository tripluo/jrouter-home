package jrouter.framework.service;

import javax.servlet.ServletContext;
import org.springframework.web.context.ServletContextAware;

/**
 * 提供统一路径格式的转换功能。
 * 假设freemarker中配置的方法名称为url，上下文路径为 context path，后缀名为 .jsp。则
 * ${url('/index')} -> /contextPath/index.jsp
 * ${url('/home')} -> /contextPath/home.jsp
 */
public class URLService implements ServletContextAware {

    //servlet context path
    private String contextPath;

    //servlet suffix, such as ".do", ".jsp"
    @lombok.Setter
    private String extension;

    public String toURL(String url) {
        return contextPath + ((extension == null || url.endsWith(extension)) ? url : url + extension);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        contextPath = servletContext.getContextPath();
    }
}
