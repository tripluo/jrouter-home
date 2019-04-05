package net.jrouter.framework.util;

import net.jrouter.framework.result.freemarker.FreemarkerResult;
import net.jrouter.framework.result.thymeleaf.ThymeleafResult;
import net.jrouter.http.servlet.result.ServletResult;

/**
 * ResultUtil.
 */
public abstract class ResultUtil {

    public static final char PATH_SEPARATOR = '/';

    private ResultUtil() {
    }

    public static String renderThymeleaf(String templatePath) {
        return render(ThymeleafResult.TYPE, templatePath);
    }

    public static String renderFreemarker(String templatePath) {
        return render(FreemarkerResult.TYPE, templatePath);
    }

    public static String renderActionForward(String path) {
        return render(ServletResult.ACTION_FORWARD, path);
    }

    public static String renderForward(String path) {
        return render(ServletResult.FORWARD, path);
    }

    public static String renderRedirect(String path) {
        return render(ServletResult.REDIRECT, path);
    }

    public static String render(String resultType, String path) {
        if (path.charAt(0) != PATH_SEPARATOR)
            path = PATH_SEPARATOR + path;
        return resultType + ":" + path;
    }
}
