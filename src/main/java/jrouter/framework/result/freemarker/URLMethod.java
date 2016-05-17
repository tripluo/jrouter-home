package jrouter.framework.result.freemarker;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import java.util.List;
import javax.servlet.ServletContext;
import org.springframework.web.context.ServletContextAware;

/**
 * 提供统一路径格式的转换功能。
 * 假设freemarker中配置的方法名称为url，上下文路径为 context path，后缀名为 .jsp。则
 * ${url('/index')} -> /contextPath/index.jsp
 * ${url('/home')} -> /contextPath/home.jsp
 */
public class URLMethod implements TemplateMethodModelEx, ServletContextAware {

    //servlet context path
    private String contextPath;

    //servlet suffix, such as ".do", ".jsp"
    private String extension;

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        String url = ((TemplateScalarModel) arguments.get(0)).getAsString();
        return contextPath + ((extension == null || url.endsWith(extension)) ? url : url + extension);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        contextPath = servletContext.getContextPath();
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
