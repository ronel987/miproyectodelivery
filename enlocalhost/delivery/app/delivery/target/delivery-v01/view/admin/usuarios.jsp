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
                        Usuarios del Sistema
                    </h5>
                </div>
            </div>

            <div class="row mt-3">
                <div class="col-12">
                    <table class="table parainfo mb-0">
                        <thead>
                            <tr>
                                <th class="col-3 ps-5">Apellidos</th>
                                <th class="col-3">Nombres</th>
                                <th class="col-3 usuario">Usuario</th>
                                <th class="col-3 autorizacion">Autorización</th>
                            </tr>
                        </thead>
                    </table>

                    <div class="parainfo_overflow" style="overflow: auto;max-height: 400px">
                        <table class="table parainfo">
                            <tbody id="qry"></tbody>
                        </table>
                    </div>

                    <table class="table parainfo">
                        <tfoot>
                            <tr>
                                <th class="col-5">
                                    <span class="btn" onclick="usuariosIns();" title="Nuevo registro"><i class="bi-plus-square"></i></span>
                                    <span class="btn" onclick="usuariosGet();" title="Actualizar datos"><i class="bi-pencil-square"></i></span>
                                    <span class="btn" onclick="usuariosDel();" title="Retirar fila"><i class="bi-trash"></i></span>
                                    <div class="vr"></div>
                                    <span class="btn" onclick="usuariosSearch();" title="Filtrar datos"><i class="bi-search"></i></span>
                                </th>
                                <td class="col-3 text-center">
                                    <span id="ctasFils"></span> fila(s)
                                </td>
                                <th class="col-4 text-end">
                                    <span id="first" class="btn" onclick="botones('first');" title="Ir a página 1"><i class="bi-skip-start-fill"></i></span>
                                    <span id="previus" class="btn" onclick="botones('previus');" title="Anterior página"><i class="bi-rewind-fill"></i></span>
                                    <span id="next" class="btn" onclick="botones('next');" title="Siguiente página"><i class="bi-fast-forward-fill"></i></span>
                                    <span id="last" class="btn" onclick="botones('last');" title="Ir a última página"><i class="bi-skip-end-fill"></i></span>

                                    <input type="hidden" id="ctasPags"/>
                                    <input type="hidden" id="pagActual" value="1"/>
                                </th>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>

            <div class="row mt-5">
                <div class="col-12">
                    <p id="ajax_wait" class="text-center">
                        <img src="../../images/ajax.gif" alt=""/>
                    </p>
                </div>
            </div>
        </div>


        <%-- zona de diálogos - inicio --%>
        <div id="dlg_usuarios" class="modal modal-parainfo" aria-hidden="true"
             data-bs-backdrop="static" data-bs-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 id="dlg_usuarios_title" class="modal-title">Usuarios</h5>
                        <button type="button" class="btn-close"
                                data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <div class="modal-body">

                        <input type="hidden" id="dlg_usuarios_accion"/>
                        <input type="hidden" id="dlg_usuarios_idusuario"/>

                        <div class="row mt-1">
                            <div class="col-12">
                                <label for="dlg_usuarios_apellidos">Apellidos</label>
                                <input id="dlg_usuarios_apellidos" type="text" maxlength="100"
                                       placeholder="apellidos de Cliente"
                                       class="form-control"/>
                            </div>
                        </div>

                        <div class="row mt-3">
                            <div class="col-12">
                                <label for="dlg_usuarios_nombres">Nombres</label>
                                <input id="dlg_usuarios_nombres" type="text" maxlength="100"
                                       placeholder="nombres de Cliente"
                                       class="form-control"/>
                            </div>
                        </div>

                        <div class="row mt-3">
                            <div class="col-12">
                                <label for="dlg_usuarios_usuario">Usuario (nick)</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="bi-person-fill"></i>
                                    </span>
                                    <input id="dlg_usuarios_usuario" type="text" maxlength="50"
                                           placeholder="identificación amistosa (nick)"
                                           class="form-control"/>
                                </div>
                            </div>
                        </div>

                        <div id="dlg_usuarios_div_password" class="row mt-3">
                            <div class="col-12">
                                <label for="dlg_usuarios_password">Password</label>
                                <div class="input-group">
                                    <span class="input-group-text">
                                        <i class="bi-unlock-fill" onmouseover="mouseover('dlg_usuarios_password');" onmouseout="mouseout('dlg_usuarios_password');"></i>
                                    </span>
                                    <input id="dlg_usuarios_password" type="password" class="form-control" maxlength="50">
                                </div>
                            </div>
                        </div>

                        <div id="dlg_usuarios_errores" class="alert alert-danger m-3 p-3" style="display: none"></div>

                        <p id="dlg_usuarios_ajax_wait" class="text-center mt-5" style="display: none">
                            <img src="../../images/ajax.svg" alt=""/>
                        </p>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" onclick="usuariosInsUpd();">Guardar</button>
                        <button class="btn" data-bs-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>
        </div> 

        <%@include file="../../WEB-INF/jspf/menu_admin.jspf" %>
        <%@include file="../../WEB-INF/jspf/message.jspf" %>
        <%@include file="../../WEB-INF/jspf/confirm.jspf" %>
        <%@include file="../../WEB-INF/jspf/search.jspf" %>
        <%-- zona de diálogos - fin --%>

        <script src="../../jq/jquery-3.7.1.min.js"></script>
        <script src="../../jq/bootstrap/bootstrap.bundle.min.js"></script>
        <script src="../../jq/parainfo.js"></script>
        <script src="../../js/tiempo.js"></script>
        
        <script src="js/validaAdmin.js"></script>
        <script src="js/usuarios.js"></script>
    </body>
</html>
