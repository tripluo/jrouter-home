package net.jrouter.home.api;

import java.io.IOException;
import java.net.MalformedURLException;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.jrouter.annotation.Action;
import net.jrouter.framework.util.ResultUtil;
import net.jrouter.framework.util.ServletUtil;
import net.jrouter.home.annotation.ServiceApi;
import net.jrouter.http.servlet.ServletActionInvocation;
import org.springframework.util.StreamUtils;

/**
 * HomeApi.
 */
@ServiceApi(namespace = "/")
@Slf4j
public class HomeApi {

    @Action(name = {"home", "index"})
    public String index(ServletActionInvocation invocation) {
        return ResultUtil.renderThymeleaf("/home");
    }

    @Action
    public String doc(ServletActionInvocation invocation) {
        return ResultUtil.renderThymeleaf("/doc");
    }

    @Action(name = {"download", "downloads"})
    public String downloads(ServletActionInvocation invocation) {
        return ResultUtil.renderThymeleaf("/downloads");
    }

    @Action
    public String about(ServletActionInvocation invocation) {
        return ResultUtil.renderThymeleaf("/about");
    }

    @Action
    public void showFactoryStatus(ServletActionInvocation invocation) throws IOException {
        HttpServletResponse response = invocation.getResponse();
        ServletUtil.setDownloadableHeader(response, "factory-status.jsp");
        StreamUtils.copy(invocation.getServletContext().getResourceAsStream("/factory-status.jsp"), response.getOutputStream());
    }
}
