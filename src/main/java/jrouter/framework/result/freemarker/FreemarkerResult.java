package jrouter.framework.result.freemarker;

import freemarker.template.*;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jrouter.annotation.ResultType;
import jrouter.servlet.ServletActionInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.context.ServletContextAware;

/**
 * FreemarkerResult. Require springframework container.
 */
public class FreemarkerResult implements ServletContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(FreemarkerResult.class);

    public static final String TYPE = "freemarker";

    private Configuration configuration;

    private String templatePath = "/";

    @Override
    public void setServletContext(ServletContext servletContext) {
        Assert.notNull(configuration);
        //after ServletThreadContext setted
        String templateFile = servletContext.getRealPath(templatePath);
        LOG.info("Freemarker version [{}]", Configuration.getVersion());
        configuration.setServletContextForTemplateLoading(servletContext, templatePath);
        LOG.info("Freemarker setDirectoryForTemplateLoading [{}]", templateFile);

        LOG.info("Freemarker getObjectWrapper [{}]", configuration.getObjectWrapper());
    }

    @ResultType(type = TYPE)
    public void result(ServletActionInvocation invocation) throws IOException, TemplateException {
        if (invocation.getResult() != null) {
            String location = invocation.getResult().location();
            if (location.charAt(0) != '/') {
                location = "/" + location;
            }
            TemplateModel model = createModel(invocation);
            result(location, model, invocation);
        }
    }

    /**
     * Render template.
     *
     * @param templateLocation 模板路径。
     * @param dataModel 数据模型。
     * @param invocation the ServletActionInvocation.
     *
     * @throws IOException 如果发生I/O错误。
     * @throws TemplateException 如果发生模板解析错误。
     *
     * @see Template#process(Object, Writer)
     */
    public void result(String templateLocation, Object dataModel, ServletActionInvocation invocation)
            throws IOException, TemplateException {
        Template template = configuration.getTemplate(templateLocation);
        Writer writer = invocation.getResponse().getWriter();
        try {
            // Process the template
            if (configuration.getTemplateExceptionHandler() == TemplateExceptionHandler.RETHROW_HANDLER) {
                CharArrayWriter charArrayWriter = new CharArrayWriter(4096);
                template.process(dataModel, charArrayWriter);
                charArrayWriter.writeTo(writer);
                writer.flush();
            } else {
                template.process(dataModel, writer);
            }
        } finally {
            // Give subclasses a chance to hook into postprocessing
            postTemplateProcess(invocation.getRequest(), invocation.getResponse(), template, (TemplateModel) dataModel);
        }
    }

    /**
     * Create Model.
     *
     * @param invocation the ServletActionInvocation.
     *
     * @return TemplateModel.
     */
    protected TemplateModel createModel(ServletActionInvocation invocation) {
        ServletContext servletContext = invocation.getServletContext();
        HttpServletRequest request = invocation.getRequest();
        FreemarkerScopesHashModel model = new FreemarkerScopesHashModel(configuration.getObjectWrapper(), servletContext, request, invocation.getContextMap());
        //put the parameters
        model.put("actionPath", invocation.getActionPath());
        model.put("request", request);
        model.put("parameters", invocation.getRequestParameters());
        model.put("base", request.getContextPath());
        model.put("session", request.getSession(false));
        return model;
    }

    protected void postTemplateProcess(HttpServletRequest request, HttpServletResponse response,
            Template template, TemplateModel data) {
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
