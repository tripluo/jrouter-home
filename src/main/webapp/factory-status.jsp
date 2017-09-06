<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="jrouter.ActionFactory,jrouter.ActionProxy,jrouter.annotation.Interceptor"%>
<%@page import="jrouter.annotation.Result"%>
<%@page import="jrouter.impl.*"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.concurrent.ConcurrentSkipListMap"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>factory-status</title>
    </head>
    <body>
        <h1>factory-status</h1>
        <a href="#Actions">Actions</a><br/>
        <a href="#InterceptorStacks">InterceptorStacks</a><br/>
        <a href="#ResultTypes">ResultTypes</a><br/>
        <a href="#Results">Results</a><br/>
        <a href="#Interceptors">Interceptors</a><br/>
        <a href="#ActionsCache">ActionsCache</a><br/>
        <hr/>
        <%
            //out.write(ServletActionContext.getContext()+"<br/>");
            String name = "jrouter_factory";

            ActionFactory af = (ActionFactory) application.getAttribute(name);
            if (af == null) {
                out.write("No \"" + name + "\" Attribute in ServletActionContext");
                return;
            }
            out.write(af + "<br/>");
            out.write("DefaultInterceptorStack : " + af.getDefaultInterceptorStack() + "<br/>");
            out.write("DefaultResultType : " + af.getDefaultResultType() + "<br/>");
            out.write("ObjectFactory : " + af.getObjectFactory() + "<br/>");
            out.write("MethodInvokerFactory : " + af.getMethodInvokerFactory() + "<br/>");
            out.write("ConverterFactory : " + af.getConverterFactory() + "<br/>");

            //InterceptorStacks
            out.write("<a name='InterceptorStacks'><h2>InterceptorStacks : </h2></a>");
            Map<String, InterceptorStackProxy> interceptorStacks = new ConcurrentSkipListMap(af.getInterceptorStacks());
            for (InterceptorStackProxy is : interceptorStacks.values()) {
                out.write(is.getName() + " : " + is.getFieldName() + "<br/>");
                List<InterceptorProxy> ips = is.getInterceptors();
                if (ips != null)
                    for (int i = 0; i < ips.size(); i++) {
                        out.write("Interceptor " + (1 + i) + " : " + ips.get(i).getName() + " ---> " + ips.get(i).getMethod() + "<br/>");
                    }
                out.write("<br/>----------------------------------------------------------------------------------------------------------------<br/>");
            }
            out.write("<hr/>");

            //Actions
            out.write("<a name='Actions'><h2>Actions : </h2></a>");
            Map<String, ActionProxy> actions = new ConcurrentSkipListMap(af.getActions());
            for (Map.Entry<String, ActionProxy> ae : actions.entrySet()) {

                String url = ae.getKey();

                ActionProxy ap = ae.getValue();
                out.write("<a href='javascript:'>" + url + "</a> ---> " + ap.getMethodInfo() + "<br/>");

                Map<String, String[]> aps = ap.getActionParameters();
                if (!aps.isEmpty()) {
                    out.write("Action Parameters : ");
                    Iterator<Map.Entry<String, String[]>> it = aps.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry<String, String[]> e = it.next();
                        out.write(e.getKey() + "=" + Arrays.toString(e.getValue()));
                        if (it.hasNext())
                            out.write(", ");
                    }
                    out.write("<br/>");
                    out.write("- - - - - - - - - - - - - - - - - - - - - - - - - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - -<br/> ");
                }
                List<Interceptor> ips = ap.getInterceptors();
                if (ips != null)
                    for (int i = 0; i < ips.size(); i++) {
                        InterceptorProxy ip = (InterceptorProxy) (af.getInterceptors().get(ips.get(i).name()));
                        out.write("Interceptor " + (1 + i) + " : " + ip.getName() + " ---> " + ip.getMethod() + "<br/>");
                    }
                out.write("- - - - - - - - - - - - - - - - - - - - - - - - - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - -<br/> ");
                Map<String, Result> rs = ap.getResults();
                for (Result r : rs.values()) {
                    out.write("Result : " + r.name() + " ---> " + r + "<br/>");
                }

                out.write("<br/>----------------------------------------------------------------------------------------------------------------<br/>");

            }
            out.write("<hr/>");

            //ResultTypes
            out.write("<a name='ResultTypes'><h2>ResultTypes : </h2></a>");
            Map<String, ResultTypeProxy> rts = new ConcurrentSkipListMap(af.getResultTypes());
            for (ResultTypeProxy rt : rts.values()) {
                out.write("ResultType : " + rt.getType() + " ---> " + rt.getMethod() + "<br/>");
                out.write("- - - - - - - - - - - - - - - - - - - - - - - - - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - -<br/> ");
            }
            out.write("<hr/>");

            //Result
            out.write("<a name='Results'><h2>Results : </h2></a>");
            Map<String, ResultProxy> rs = new ConcurrentSkipListMap(af.getResults());
            for (ResultProxy r : rs.values()) {
                out.write("Result : " + r.getResult().name() + " ---> " + r.getMethod() + "<br/>");
                out.write("- - - - - - - - - - - - - - - - - - - - - - - - - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - -<br/> ");
            }
            out.write("<hr/>");

            //Interceptors
            out.write("<a name='Interceptors'><h2>Interceptors : </h2></a>");
            Map<String, InterceptorProxy> ips = new ConcurrentSkipListMap(af.getInterceptors());
            for (InterceptorProxy ip : ips.values()) {
                out.write("Interceptor : " + ip.getName() + " ---> " + ip.getMethod() + "<br/>");
                out.write("- - - - - - - - - - - - - - - - - - - - - - - - - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - -<br/> ");
            }
            out.write("<hr/>");

            out.write("<a name='ActionsCache'><h2>ActionsCache : </h2></a>");
            if (af instanceof PathActionFactory) {
                Map<String, Object> cache = ((PathActionFactory) af).getActionCache();
                for (Map.Entry<String, Object> e : cache.entrySet()) {
                    out.write("ActionCache : " + e.getKey() + " ---> " + e.getValue() + "<br/>");
                    out.write("- - - - - - - - - - - - - - - - - - - - - - - - - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - -<br/> ");
                }
            } else {
                out.write("Not Support <br/>");
            }

            out.write("<hr/>");
            //THE END
        %>
    </body>
</html>