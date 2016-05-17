<!DOCTYPE html>
<html>
    <head>
        <title>JRouter Downloads</title>
        <meta name="keywords" content="JRouter Downloads 下载" />
        <meta name="description" content="JRouter Downloads 下载" />
        [@template.header/]
    </head>
    <body>
        [@template.body navbar="downloads"]
        <div class="row">
            <div class="blog-header">
            </div>
            <div class="col-sm-10 blog-main">
                <div class="blog-post">
                    <h2>Downloads 下载</h2>
                    <hr>
                    <a name="Laest"></a>
                    <h3>最新下载</h3>
                    <ul>
                        <li>
                            <a href="${sharedVariables['jrouterDownloadURL']}/net/jrouter/jrouter/1.7.1/jrouter-1.7.1.jar" target="_blank">
                                jrouter-1.7.1.jar
                            </a>
                        </li>
                        <li>
                            <a href="${sharedVariables['jrouterDownloadURL']}/net/jrouter/jrouter/1.7.1/jrouter-1.7.1-sources.jar" target="_blank">
                                jrouter-1.7.1-sources.jar
                            </a>
                        </li>
                        <li>
                            <a href="https://github.com/innjj/inn4j-repository/tree/master/repo/jrouter/jrouter/" target="_blank">
                                历史版本
                            </a>
                        </li>
                    </ul>

                    <hr />
                    <a name="其它"></a>
                    <h3>其它</h3>
                    <p>
                        一个方便
                        <a href="${url('/showFactoryStatus')}" target="_blank">
                            查看容器各参数的工具
                        </a>
                        ，当然首先必须能够获取得到
                        <a href="${sharedVariables['jrouterSourceURL']}/java/jrouter/ActionFactory.java" target="_blank">ActionFactory</a>对象。
                        <br />
                        <a href="factory-status.jsp" target="_blank">点击查看本站的相关参数</a>。
                    </p>
                </div><!-- /.blog-post -->
            </div><!-- /.blog-main -->
        </div><!-- /.row -->
        [/@template.body]
        [@template.footer/]
    </body>
</html>
