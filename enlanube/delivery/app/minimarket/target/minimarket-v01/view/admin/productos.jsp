<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%@include file="../../WEB-INF/jspf/browser.jspf" %>
<html>
    <head>
        <title>Mini Market Serfer</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" href="../../images/pi-favicon.png" type="image/gif" />

        <link href="../../jq/bootstrap/bootstrap.min.css" rel="stylesheet" type="text/css"/>
        <link href="../../jq/bootstrap/bootstrap-icons.min.css" rel="stylesheet" type="text/css"/>

        <link href="../../jq/parainfo.css" rel="stylesheet" type="text/css"/>

    </head>
    <body>
        <div class="container-fluid">
            <%@include file="../../WEB-INF/jspf/head_admin.jspf" %>

            <div class="row mt-3">
                <div class="col-12">
                    <h5 class="text-center">
                        Productos del Mini Market
                    </h5>
                </div>
            </div>

            <div class="row mt-2">
                <div class="col-12 col-sm-12 col-md-6 col-lg-5 col-xl-4 col-xxl-3">
                    <label for="categoriasCbo">Categoría</label>
                    <select id="categoriasCbo" class="form-select" onchange="productosQry('carga_inicial');"></select>
                </div>
                <div class="col-md-6 col-lg-7 col-xl-8 col-xxl-9"></div>
            </div>

            <div class="row mt-1">
                <div class="col-12">
                    <table class="table parainfo mb-0">
                        <thead>
                            <tr>
                                <td class="col-1" style="width: 16px !important"></td>
                                <th class="col-7 producto">Producto</th>
                                <td class="col-1">Imagen</td>
                                <th class="col-2 precio text-end">Precio (S/)</th>
                                <th class="col-1 estado">Activo</th>
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
                                    <span class="btn" onclick="productosIns();" title="Nuevo registro"><i class="bi-plus-square"></i></span>
                                    <span class="btn" onclick="productosGet();" title="Actualizar datos"><i class="bi-pencil-square"></i></span>
                                    <span class="btn" onclick="productosDel();" title="Retirar fila"><i class="bi-trash"></i></span>
                                    <div class="vr"></div>
                                    <span class="btn" onclick="productosSearch();" title="Filtrar datos"><i class="bi-search"></i></span>
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
                    <p id="ajax_wait" class="text-center" style="display: none">
                        <img src="../../images/ajax2.svg" alt=""/>
                    </p>
                </div>
            </div>
        </div>


        <%-- zona de diálogos - inicio --%>
        <div id="dlg_productos" class="modal modal-parainfo" aria-hidden="true"
             data-bs-backdrop="static" data-bs-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 id="dlg_productos_title" class="modal-title">Productos</h5>
                        <button type="button" class="btn-close"
                                data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <div class="modal-body">

                        <input type="hidden" id="dlg_productos_accion"/>
                        <input type="hidden" id="dlg_productos_idproducto"/>

                        <div class="row mt-1">
                            <div class="col-12">
                                <label for="dlg_productos_idcategoria">Categoría</label>
                                <select id="dlg_productos_idcategoria" class="form-select"></select>
                            </div>
                        </div>

                        <div class="row mt-3">
                            <div class="col-12">
                                <label for="dlg_productos_producto">Producto</label>
                                <input id="dlg_productos_producto" type="text" maxlength="100"
                                       placeholder="texto de Producto"
                                       class="form-control"/>
                            </div>
                        </div>
                        
                        <div class="row mt-3">
                            <div class="col-6">
                                <label for="dlg_productos_precio">Precio S/</label>
                                <input id="dlg_productos_precio" type="text" maxlength="10"
                                       class="form-control text-end"/>
                            </div>
                            <div class="col-6">
                                <label>Estado</label>
                                <div class="form-check mt-1">
                                    <input class="form-check-input" type="checkbox" id="dlg_productos_estado">
                                    <label class="form-check-label" for="dlg_productos_estado">Activo</label>
                                </div>
                            </div>
                        </div>

                        <div class="row mt-3">
                            <div id="dlg_productos_imagen_view" class="col-12 text-center"></div>
                            <div class="col-12">
                                <label for="dlg_productos_imagen">Imagen</label>
                                <input type="file" class="form-control" id="dlg_productos_imagen" accept="image/*">
                            </div>
                        </div>

                        <div id="dlg_productos_errores" class="alert alert-warning m-3 ps-3 pe-3 pt-2 pb-2" style="display: none"></div>

                        <p id="dlg_productos_ajax_wait" class="text-center mt-5" style="display: none">
                            <img src="../../images/ajax.svg" alt=""/>
                        </p>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" onclick="productosInsUpd();">Guardar</button>
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
        <script src="../../jq/number/jquery.number.min.js"></script>
        <script src="../../jq/parainfo.js"></script>
        <script src="../../js/tiempo.js"></script>
        
        <script src="js/validaAdmin.js"></script>
        <script src="js/productos.js"></script>
    </body>
</html>
