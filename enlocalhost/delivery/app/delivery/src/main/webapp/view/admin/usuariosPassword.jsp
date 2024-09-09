<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@include file="../../WEB-INF/jspf/browser.jspf" %>
<html>
    <head>
        <title>Mini Market Serfer</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" href="../../images/pi-favicon.gif" type="image/gif" />

        <link href="../../jq/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="../../jq/bootstrap/bootstrap-icons.min.css" rel="stylesheet" type="text/css"/>

        <link href="../../jq/parainfo.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="container-fluid">
            <%@include file="../../WEB-INF/jspf/head_admin.jspf" %>

            <div class="row mt-4">
                <div class="col-12">
                    <h5 class="text-center">
                        Cambiar Password del Usuario
                    </h5>
                </div>
            </div>

            <div class="row mt-4">
                <div class="col-sm-1 col-md-1 col-lg-2 col-xl-3 col-xxl-4"></div>
                <div class="col-sm-10 col-md-10 col-lg-8 col-xl-6 col-xxl-4">
                    <form class="marco">
                        <div class="row mt-2">
                            <div class="col-12">
                                <label for="password" class="parainfo">Password actual</label>
                                <div class="input-group">
                                    <span class="input-group-text parainfo">
                                        <i class="bi-eye-fill" onmouseover="mouseover('password');" onmouseout="mouseout('password');"></i>
                                    </span>
                                    <input id="password" type="password" class="form-control" style="z-index: 1001" maxlength="50" placeholder="digite su password actual">
                                </div>
                            </div>

                            <div class="col-12 mt-4">
                                <label for="password1" class="parainfo">Password nuevo</label>
                                <div class="input-group">
                                    <span class="input-group-text parainfo">
                                        <i class="bi-eye-fill" onmouseover="mouseover('password1');" onmouseout="mouseout('password1');"></i>
                                    </span>
                                    <input id="password1" type="password" class="form-control" maxlength="50" placeholder="digite nuevo password">
                                </div>
                            </div>

                            <div class="col-12 mt-4">
                                <label for="password2" class="parainfo">Password nuevo (confirmar)</label>
                                <div class="input-group">
                                    <span class="input-group-text parainfo">
                                        <i class="bi-eye-fill" onmouseover="mouseover('password2');" onmouseout="mouseout('password2');"></i>
                                    </span>
                                    <input id="password2" type="password" class="form-control" maxlength="50" placeholder="confirme nuevo password">
                                </div>
                            </div>

                            <div class="row mt-3">
                                <div class="col-12">
                                    <p id="password_ajax_wait" style="text-align: center;display: none">
                                        <img src="../../images/ajax.svg" alt=""/>
                                    </p>
                                    <div id="password_error" class="alert alert-danger m-3 p-3 text-center" style="display: none"></div>
                                </div>
                            </div>

                            <div class="col-12 mt-5 mb-3 text-end">
                                <button type="button" onclick="passwordChange();" class="btn parainfo">Cambiar Password</button>
                                <a href="index.jsp" class="btn btn-success">Ir a Inicio</a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <%@include file="../../WEB-INF/jspf/menu_admin.jspf" %>
        <%@include file="../../WEB-INF/jspf/message.jspf" %>

        <script src="../../jq/jquery-3.7.1.min.js"></script>
        <script src="../../jq/bootstrap/bootstrap.bundle.min.js"></script>
        <script src="../../jq/parainfo.js"></script>
        <script src="../../js/tiempo.js"></script>
        
        <script src="js/validaAdmin.js"></script>
        <script src="js/usuariosPassword.js"></script>
    </body>
</html>
