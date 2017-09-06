package jrouter.framework.result.freemarker;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import java.util.List;
import jrouter.framework.service.URLService;

/**
 * 提供统一路径格式的转换功能。
 */
public class URLMethod extends URLService implements TemplateMethodModelEx {

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        String url = ((TemplateScalarModel) arguments.get(0)).getAsString();
        return toURL(url);
    }
}
