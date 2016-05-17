package jrouter.framework.result.freemarker;

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * FreemarkerScopesHashModel.
 */
public class FreemarkerScopesHashModel extends SimpleHash {

    private static final long serialVersionUID = 1L;

    private final HttpServletRequest request;

    private final ServletContext servletContext;

    private final Map<String, Object> contextMap;

    public FreemarkerScopesHashModel(ObjectWrapper objectWrapper, ServletContext context,
            HttpServletRequest request, Map<String, Object> contextMap) {
        super(objectWrapper);
        this.servletContext = context;
        this.request = request;
        this.contextMap = contextMap;
    }

    @Override
    public TemplateModel get(String key) throws TemplateModelException {
        // Lookup in default scope
        TemplateModel model = super.get(key);

        if (model != null) {
            return model;
        }

        if (contextMap != null) {
            // Lookup in thread local map scope
            Object obj = contextMap.get(key);
            if (obj != null) {
                return wrap(obj);
            }
        }

        if (request != null) {
            // Lookup in request scope
            Object obj = request.getAttribute(key);

            if (obj != null) {
                return wrap(obj);
            }

            // Lookup in session scope
            HttpSession session = request.getSession(false);

            if (session != null) {
                obj = session.getAttribute(key);

                if (obj != null) {
                    return wrap(obj);
                }
            }
        }

        if (servletContext != null) {
            // Lookup in application scope
            Object obj = servletContext.getAttribute(key);

            if (obj != null) {
                return wrap(obj);
            }
        }

        return null;
    }

    @Override
    public void put(String string, boolean b) {
        super.put(string, b);
    }

    @Override
    public void put(String string, Object object) {
        super.put(string, object);
    }
}
