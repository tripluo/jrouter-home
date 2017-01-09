package jrouter.framework.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import jrouter.framework.exception.ParameterValidationException;
import jrouter.util.CollectionUtil;
import jrouter.util.StringUtil;

/**
 * RequestParameterUtil.
 */
public abstract class RequestParameterUtil {

    private RequestParameterUtil() {
    }

    public static String getRequiredParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (StringUtil.isBlank(value))
            throw new ParameterValidationException(name, "Request parameter '" + name + "' must not be empty", false);
        return value;
    }

    public static String getParameter(HttpServletRequest request, String name, String def) {
        String value = request.getParameter(name);
        return StringUtil.isEmpty(value) ? def : value;
    }

    public static boolean isNumeric(String str) {
        int len;
        if (str == null || (len = str.length()) == 0) {
            return false;
        }
        char c0 = str.charAt(0);
        if (len == 1) {
            if (false == Character.isDigit(c0))
                return false;
        }
        int start = (c0 == '-' || c0 == '+') ? 1 : 0;
        for (int i = start; i < len; i++) {
            if (false == Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static Integer getParameterInteger(HttpServletRequest request, String name, Integer def) {
        String value = request.getParameter(name);
        return isNumeric(value) ? Integer.valueOf(value) : def;
    }

    public static Long getParameterLong(HttpServletRequest request, String name, Long def) {
        String value = request.getParameter(name);
        return isNumeric(value) ? Long.valueOf(value) : def;
    }

    /**
     * RequestParameters转换成数字Long数组。
     */
    public static Integer[] getParameterIntegerArray(HttpServletRequest request, String name) {
        String[] params = request.getParameterValues(name);
        if (CollectionUtil.isEmpty(params))
            return CollectionUtil.EMPTY_INTEGER_OBJECT_ARRAY;
        List<Integer> nums = new ArrayList<Integer>(params.length);
        for (String s : params) {
            if (isNumeric(s))
                nums.add(Integer.parseInt(s));
        }
        return nums.toArray(CollectionUtil.EMPTY_INTEGER_OBJECT_ARRAY);
    }

    /**
     * RequestParameters转换成数字Long数组。
     */
    public static Long[] getParameterLongArray(HttpServletRequest request, String name) {
        String[] params = request.getParameterValues(name);
        if (CollectionUtil.isEmpty(params))
            return CollectionUtil.EMPTY_LONG_OBJECT_ARRAY;
        List<Long> nums = new ArrayList<Long>(params.length);
        for (String s : params) {
            if (isNumeric(s))
                nums.add(Long.parseLong(s));
        }
        return nums.toArray(CollectionUtil.EMPTY_LONG_OBJECT_ARRAY);
    }

    /**
     * 转换Request Parameters为String。
     */
    public static String parseRequestParameters(Map<String, String[]> requestParameters) {
        Iterator<Map.Entry<String, String[]>> i = requestParameters.entrySet().iterator();
        if (!i.hasNext())
            return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (;;) {
            Map.Entry<String, String[]> e = i.next();
            String key = e.getKey();
            String[] value = e.getValue();
            sb.append(key);
            sb.append('=');
            sb.append(Arrays.toString(value));
            if (!i.hasNext())
                return sb.append('}').toString();
            sb.append(',').append(' ');
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    public static String getParameter(Map<String, String[]> parameters, String name) {
        String[] values = parameters.get(name);
        if (values == null || values.length == 0)
            return null;
        return values[0];
    }

    public static String getRequiredParameter(Map<String, String[]> parameters, String name) {
        String value = getParameter(parameters, name);
        if (StringUtil.isBlank(value))
            throw new ParameterValidationException(name, "Request parameter '" + name + "' must not be empty", false);
        return value;
    }

    public static String getParameter(Map<String, String[]> parameters, String name, String def) {
        String value = getParameter(parameters, name);
        return StringUtil.isEmpty(value) ? def : value;
    }

    public static Integer getParameterInteger(Map<String, String[]> parameters, String name,
            Integer def) {
        String value = getParameter(parameters, name);
        return isNumeric(value) ? Integer.valueOf(value) : def;
    }

    public static Long getParameterLong(Map<String, String[]> parameters, String name, Long def) {
        String value = getParameter(parameters, name);
        return isNumeric(value) ? Long.valueOf(value) : def;
    }

    /**
     * RequestParameters转换成数字Long数组。
     */
    public static Integer[] getParameterIntegerArray(Map<String, String[]> parameters, String name) {
        String[] params = parameters.get(name);
        if (CollectionUtil.isEmpty(params))
            return CollectionUtil.EMPTY_INTEGER_OBJECT_ARRAY;
        List<Integer> nums = new ArrayList<Integer>(params.length);
        for (String s : params) {
            if (isNumeric(s))
                nums.add(Integer.parseInt(s));
        }
        return nums.toArray(CollectionUtil.EMPTY_INTEGER_OBJECT_ARRAY);
    }

    /**
     * RequestParameters转换成数字Long数组。
     */
    public static Long[] getParameterLongArray(Map<String, String[]> parameters, String name) {
        String[] params = parameters.get(name);
        if (CollectionUtil.isEmpty(params))
            return CollectionUtil.EMPTY_LONG_OBJECT_ARRAY;
        List<Long> nums = new ArrayList<Long>(params.length);
        for (String s : params) {
            if (isNumeric(s))
                nums.add(Long.parseLong(s));
        }
        return nums.toArray(CollectionUtil.EMPTY_LONG_OBJECT_ARRAY);
    }

    /**
     * 过滤原有{@code String}数组中的空字符并返回新的数组；如果原有字符串数组均不为空，则返回原有数组。
     */
    public static String[] removeAllBlank(final String[] source) {
        if (source == null || source.length == 0) {
            return source;
        }
        List<String> list = new ArrayList<String>(source.length);
        for (String str : source) {
            if (StringUtil.isNotBlank(str)) {
                list.add(str);
            }
        }
        return list.size() == source.length ? source : list.toArray(new String[0]);
    }

}
