package jrouter.framework.util;

import java.util.Map;
import jrouter.servlet.ServletThreadContext;
import jrouter.util.StringUtil;
import org.springframework.util.Assert;

/**
 * ParameterUtil.
 */
public abstract class ParameterUtil {

    private ParameterUtil() {
    }

    public static String getRequiredParameter(String name) {
        String value = getParameter(name);
        Assert.hasLength(value, "Request parameter '" + name + "' must not be empty");
        return value;
    }

    public static String getParameter(String name) {
        return ServletThreadContext.getRequest().getParameter(name);
    }

    public static Map<String, String[]> getParameters() {
        return ServletThreadContext.getRequestParameters();
    }

    protected String getParameter(String name, String def) {
        String value = getParameter(name);
        return StringUtil.isEmpty(value) ? def : value;
    }

    public static boolean isNumeric(String str) {
        int len;
        if (str == null || (len = str.length()) == 0) {
            return false;
        }
        char c0 = str.charAt(0);
        if (len == 1) {
            if (!Character.isDigit(c0))
                return false;
        }
        int start = (c0 == '-' || c0 == '+') ? 1 : 0;
        for (int i = start; i < len; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static Integer getParameterInteger(String name, Integer def) {
        String value = getParameter(name);
        return isNumeric(value) ? Integer.valueOf(value) : def;
    }

    public static Long getParameterLong(String name, Long def) {
        String value = getParameter(name);
        return isNumeric(value) ? Long.valueOf(value) : def;
    }
}
