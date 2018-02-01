<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8" session="false"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>System getenv/getProperties</title>
    </head>
    <body>
        <%= Thread.currentThread()%> <hr />
        <%
            for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
                out.write(entry.getKey() + "--->" + entry.getValue());
                out.write("<br />");
            }
            out.write("<hr />");
            out.write("<hr />");
            out.write("<hr />");
            for (Map.Entry<Object, Object> entry : System.getProperties().entrySet()) {
                out.write(entry.getKey() + "--->" + entry.getValue());
                out.write("<br />");
            }
            out.write("<hr />");
            out.write("<hr />");
            out.write("<hr />");
        %>
    </body>
</html>