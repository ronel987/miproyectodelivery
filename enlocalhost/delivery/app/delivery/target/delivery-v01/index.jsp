
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Mini Market Serfer</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" href="images/pi-favicon.gif" type="image/gif" />

        <link href="jq/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="jq/bootstrap/bootstrap-icons.min.css" rel="stylesheet" type="text/css"/>
        <link href="jq/whatsapp/floating-wpp.min.css" rel="stylesheet" type="text/css"/>

        <link href="jq/parainfo.css" rel="stylesheet" type="text/css"/>
        <link href="css/index.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <%@include file="WEB-INF/jspf/menu.jspf" %>
            </div>

            <div class="row">
                <div class="col-12">
                    <%@include file="WEB-INF/jspf/banner.jspf" %>
                </div>
            </div>

            <div id="index_categorias" class="row m-3">
                <p style="text-align: center;margin-top: 50px;margin-bottom: 200px">
                    <img src="images/ajax2.svg" alt=""/>
                </p>
            </div>

            <%@include file="WEB-INF/jspf/footer.jspf" %>

            <div id="WAButton" style="z-index: 9000"></div>
        </div>

        <%-- zona de diálogos - inicio --%>
        <%@include file="WEB-INF/jspf/message.jspf" %>
        <%-- zona de diálogos - fin --%>

        <script src="jq/jquery-3.7.1.min.js"></script>
        <script src="jq/bootstrap/bootstrap.bundle.min.js"></script>
        <script src="jq/whatsapp/floating-wpp.min.js"></script>
        <script src="js/fecha.js"></script>

        <script src="js/index.js"></script>
    </body>
</html>
