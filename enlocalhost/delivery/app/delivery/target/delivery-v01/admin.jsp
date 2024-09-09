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

            <div class="row" style="margin-top: 120px"></div>

            <div class="row mt-5">
                <div class="col-sm-1 col-md-1 col-lg-2 col-xl-3 col-xxl-4"></div>
                <div class="col-sm-10 col-md-10 col-lg-8 col-xl-6 col-xxl-4">
                    <form class="marco">
                        <div class="row">
                            <div class="col-12">
                                <div class="row mt-3">
                                    <div class="col-1"></div>
                                    <div class="col-10">
                                        <label for="usuario">Usuario</label>
                                        <div class="input-group">
                                            <span class="input-group-text">
                                                <i class="bi-person-fill"></i>
                                            </span>
                                            <!--<input id="usuario" type="text" class="form-control" maxlength="200" placeholder="digite su usuario">-->
                                            <input id="usuario" type="text" class="form-control" maxlength="200" value="vitucho">
                                        </div>
                                    </div>
                                    <div class="col-1"></div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-1"></div>            
                                <div class="col-10 mt-3">
                                    <label for="password">Password</label>
                                    <div class="input-group">
                                        <span class="input-group-text">
                                            <i class="bi-unlock-fill" onmouseover="mouseover('password');" onmouseout="mouseout('password');"></i>
                                        </span>
                                        <!--<input id="password" type="password" class="form-control" maxlength="200" placeholder="digite su contraseña">-->
                                        <input id="password" type="password" class="form-control" maxlength="200" value="999999999">
                                    </div>
                                </div>
                                <div class="col-1"></div>
                            </div> 

                            <p id="ajax_wait" style="text-align: center;display: none;margin-top: 12px">
                                <img src="images/ajax.svg" alt=""/>
                            </p>

                            <div class="row mt-4">
                                <div class="col-1"></div>  
                                <div class="col-10">
                                    <button type="button" onclick="ingresar();" class="btn parainfo" style="width: 100%" title="ingresar al sistema">
                                        Iniciar Sesión <i class="bi-send-fill"></i>
                                    </button>
                                </div>
                                <div class="col-1"></div>     
                            </div>

                            <div class="row mt-4 mb-4">
                                <div class="col-12 text-center">
                                    <i class="bi-c-circle"></i> Copyright a <a href="#" target="_blank" title="mini market" class="parainfo">Mini Market Serfer</a> <a href="#" title="Mini Market"><img src="images/store.png" alt="" style="vertical-align: bottom; width: 400px"></a>
                                    <br><i class="bi-bookmark-star"></i> Auspiciado por <a href="http://www.chiclayo-hotelcentral.com" target="_blank" title="Hotel Central" class="parainfo">chiclayo-hotelcentral.com</a> <a href="#" title="HCentral"><img src="images/ti-viable.png" alt="" style="vertical-align: bottom"></a>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-sm-1 col-md-1 col-lg-2 col-xl-3 col-xxl-4"></div>
            </div>
        </div>


        <%@include file="WEB-INF/jspf/message.jspf" %>

        <script src="jq/jquery-3.7.1.min.js"></script>
        <script src="jq/bootstrap/bootstrap.bundle.min.js"></script>
        <script src="jq/parainfo.js"></script>
        <script src="js/fecha.js"></script>

        <script src="js/admin.js"></script>
    </body>
</html>
