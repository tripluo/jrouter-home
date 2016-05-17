<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>404 Not Found</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
        <style type="text/css">
            <!--
            body {
                margin: 20px;
                font-family: Tahoma, Verdana;
                font-size: 14px;
                color: #333333;
                background-color: #FFFFFF;
            }
            a {
                color: #1F4881;
                text-decoration: none;
            }
            -->
        </style>
    </head>
    <body>
        <div style="border: #cccccc solid 1px; padding: 20px; width: 500px; margin: auto" align="center">对不起，找不到该访问地址！ <br />
            <a href='javascript:history.back();'>
                <img src="<%= application.getContextPath()%>/img/wall-e.png" title="有一个机器人" alt="返回上页"/>
            </a>
            <br />
            <a href='javascript:history.back();'>【返回上页】</a>
            <a href='<%= application.getContextPath()%>'>【返回首页】</a>
        </div>
        <br />
        <br />
    </body>
</html>