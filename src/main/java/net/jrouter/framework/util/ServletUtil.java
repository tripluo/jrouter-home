package net.jrouter.framework.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * ServletUtil.
 */
@Slf4j
public abstract class ServletUtil {

    //-- content-type 常量定义 --//
    public static final String HTML_TYPE = "text/html";

    public static final String JS_TYPE = "text/javascript";

    public static final String JSON_TYPE = "application/json";

    public static final String XML_TYPE = "text/xml";

    public static final String TEXT_TYPE = "text/plain";

    ////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
    public static final long ONE_DAY_SECONDS = 60 * 60 * 24;

    //-- header 常量定义 --//
    private static final String HEADER_ENCODING = "encoding";

    private static final String DEFAULT_ENCODING = "UTF-8";
    //-- 绕过jsp/freemaker直接输出文本的函数 --//

    /**
     * 分析并设置contentType与headers.
     */
    private static void initResponse(HttpServletResponse response, final String contentType, final String... headers) {

        //分析headers参数
        String encoding = DEFAULT_ENCODING;

        for (String header : headers) {
            String headerName = null;
            String headerValue = null;
            int sep = header.indexOf(':');
            if (-1 == sep) {
                headerName = header.substring(0, sep);
                headerValue = header.substring(1 + sep);
            }
            if (HEADER_ENCODING.equalsIgnoreCase(headerName)) {
                encoding = headerValue;
            } else {
                throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
            }
        }
        //设置headers参数
        String fullContentType = contentType + ";charset=" + encoding;
        response.setContentType(fullContentType);

    }

    /**
     * 直接输出内容的简便函数.
     * <p>
     * eg.
     * render("text/plain", "hello", "encoding:GBK");
     * render("text/plain", "hello", "no-cache:false");
     * render("text/plain", "hello", "encoding:GBK", "no-cache:false");
     *
     * @param headers 可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.
     */
    public static void render(HttpServletResponse response, final String contentType, final String content,
                              final String... headers) {
        //设置headers参数
        initResponse(response, contentType, headers);
        try {
            response.getWriter().write(content);
            response.getWriter().flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 直接输出文本.
     *
     * @see #render(HttpServletResponse, String, String, String...)
     */
    public static void renderText(HttpServletResponse response, final String text, final String... headers) {
        render(response, TEXT_TYPE, text, headers);
    }

    /**
     * 直接输出HTML.
     *
     * @see #render(HttpServletResponse, String, String, String...)
     */
    public static void renderHtml(HttpServletResponse response, final String html, final String... headers) {
        render(response, HTML_TYPE, html, headers);
    }

    /**
     * 直接输出XML.
     *
     * @see #render(HttpServletResponse, String, String, String...)
     */
    public static void renderXml(HttpServletResponse response, final String xml, final String... headers) {
        render(response, XML_TYPE, xml, headers);
    }

    /**
     * 直接输出JSON格式的字符串.
     */
    public static void renderJson(HttpServletResponse response, final String jsonString, final String... headers) {
        render(response, JSON_TYPE, jsonString, headers);
    }

    /**
     * 直接输出支持跨域Mashup的JSONP.
     */
    public static void renderJsonp(HttpServletResponse response, final String callbackName, final String jsonString,
                                   final String... headers) {
        StringBuilder result = new StringBuilder().append(callbackName).append('(').append(jsonString).append(')');
        //渲染Content-Type为javascript的返回内容,输出结果为javascript语句, 如callback197("{content:'Hello World!!!'}")
        render(response, JS_TYPE, result.toString(), headers);
    }

    /**
     * 设置让浏览器弹出下载对话框的Header.
     *
     * @param fileName 下载后的文件名.
     */
    public static void setDownloadableHeader(HttpServletResponse response, String fileName) {
        try {
            // 中文文件名支持
            String encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + '"');
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置客户端缓存过期时间的Header.
     */
    public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
        //Http 1.0 header, set a fix expires date.
        response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000);
        //Http 1.1 header, set a time after now.
        response.setHeader("Cache-Control", "private, max-age=" + expiresSeconds);
    }

    /**
     * 设置禁止客户端缓存的Header.
     */
    public static void setNoCacheHeader(HttpServletResponse response) {
        //Http 1.0 header
        response.setDateHeader("Expires", 1L);
        response.addHeader("Pragma", "no-cache");
        //Http 1.1 header
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
    }

    /**
     * 设置LastModified Header.
     */
    public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
        response.setDateHeader("Last-Modified", lastModifiedDate);
    }

    /**
     * 设置Etag Header.
     */
    public static void setEtag(HttpServletResponse response, String etag) {
        response.setHeader("ETag", etag);
    }

    /**
     * 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
     * <p>
     * 如果无修改, checkIfModify返回false ,设置304 not modify status.
     *
     * @param lastModified 内容的最后修改时间.
     */
    public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
                                               long lastModified) {
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return false;
        }
        return true;
    }

    /**
     * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
     * <p>
     * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
     *
     * @param etag 内容的ETag.
     */
    public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
        String headerValue = request.getHeader("If-None-Match");
        if (headerValue != null) {
            boolean conditionSatisfied = false;
            if (!"*".equals(headerValue)) {
                StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");
                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(etag)) {
                        conditionSatisfied = true;
                    }
                }
            } else {
                conditionSatisfied = true;
            }
            if (conditionSatisfied) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                response.setHeader("ETag", etag);
                return false;
            }
        }
        return true;
    }
}
