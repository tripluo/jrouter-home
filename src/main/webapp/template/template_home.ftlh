
<!-- header -->
[#macro header]
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<meta content="${.current_template_name} - ${.version} - ${.now}" name="Template Message" />
<link rel="shortcut icon" href="${base}/favicon.ico" type="image/x-icon" />
<link rel="stylesheet" href="${base}/css/bootstrap.css" />
<link rel="stylesheet" href="${base}/css/blog.css" />
[#nested]
[/#macro]

[#global
navbars = {
"home" : {"link" : "${url('/index')}", "name" :"Home 首页"},
"doc" : {"link" : "${url('/doc')}", "name" :"Documentation 文档"},
"downloads" : {"link" : "${url('/downloads')}", "name" :"Downloads 下载"},
"about" : {"link" : "${url('/about')}", "name" :"About 关于"}
}
]

<!-- body -->
[#macro body navbar="" sidebar=""]
<div class="blog-masthead">
    <div class="container">
        <nav class="blog-nav">
            [#list navbars?keys as k]
            [#local value=navbars[k]]
            <a class="blog-nav-item ${(k==navbar)?string('active','')}" href="${(k==navbar)?string('#',value.link)}" ${(value.target!(false))?string("target='_blank'",'')}>
                ${value.name}
            </a>
            [/#list]
        </nav>
    </div>
</div>
<div class="container">
    [#nested]
</div>
[/#macro]

<!-- footer -->
[#macro footer fixedBottom=false]
<footer class="blog-footer">
    <p>&copy; 2015
        <a href="${url('/index')}">JRouter.NET</a>
        <small>
            [
            <a href="http://www.miitbeian.gov.cn/" target="_blank">
                沪ICP备15053025号
            </a>
            ]
        </small>
    </p>
    <p>
        <a href="#">Back to top</a>
        (${_executionTime!(0)} ms)
    </p>
</footer>
<script type="text/javascript" src="${base}/js/jquery.js"></script>
<script type="text/javascript" src="${base}/js/bootstrap.js"></script>
<script type="text/javascript">
    //<![CDATA[
    jQuery(document).ready(function () {
    });
    //]]>
</script>
[#nested]
[/#macro]
