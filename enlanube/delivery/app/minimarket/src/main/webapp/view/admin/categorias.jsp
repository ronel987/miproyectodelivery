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
                        Categorías de Productos
                    </h5>
                </div>
            </div>

            <div class="row mt-3">
                <div class="col-xxl-1"></div>
                <div class="col-12 col-sm-12 col-md-12 col-lg-12 col-xl-12 col-xxl-10">
                    <table class="table parainfo mb-0">
                        <thead>
                            <tr>
                                <td class="col-1" style="width: 16px !important"></td>
                                <td class="col-2 categoria">Categoría</td>
                                <td class="col-3">Imagen</td>
                                <td class="col-6 descripcion">Descripción</td>
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
                                    <span class="btn" onclick="categoriasIns();" title="Nuevo registro"><i class="bi-plus-square"></i></span>
                                    <span class="btn" onclick="categoriasGet();" title="Actualizar datos"><i class="bi-pencil-square"></i></span>
                                    <span class="btn" onclick="categoriasDel();" title="Retirar fila"><i class="bi-trash"></i></span>
                                    <div class="vr"></div>
                                    <span class="btn" onclick="categoriasSearch();" title="Filtrar datos"><i class="bi-search"></i></span>
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
                <div class="col-xxl-1"></div>
            </div>

            <div class="row mt-5">
                <div class="col-12">
                    <p id="ajax_wait" class="text-center">
                        <img src="../../images/ajax2.svg" alt=""/>
                    </p>
                </div>
            </div>
        </div>


        <%-- zona de diálogos - inicio --%>
        <div id="dlg_categorias" class="modal modal-parainfo" aria-hidden="true"
             data-bs-backdrop="static" data-bs-keyboard="false">
            <div class="modal-dialog">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 id="dlg_categorias_title" class="modal-title">Categorías</h5>
                        <button type="button" class="btn-close"
                                data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <div class="modal-body">

                        <input type="hidden" id="dlg_categorias_accion"/>
                        <input type="hidden" id="dlg_categorias_idcategoria"/>

                        <div class="row mt-1">
                            <div class="col-12">
                                <label for="dlg_categorias_categoria">Categoría</label>
                                <input id="dlg_categorias_categoria" type="text" maxlength="100" class="form-control"/>
                            </div>
                        </div>

                        <div class="row mt-3">
                            <div class="col-12">
                                <label for="dlg_categorias_descripcion">Descripción</label>
                                <textarea id="dlg_categorias_descripcion" class="form-control"
                                          maxlength="800" style="resize: none;height: 60px"></textarea>
                            </div>
                        </div>

                        <div class="row mt-3">
                            <div id="dlg_categorias_imagen_view" class="col-12 text-center"></div>
                            <div class="col-12">
                                <label for="dlg_categorias_imagen">Imagen</label>
                                <input type="file" class="form-control" id="dlg_categorias_imagen" accept="image/*">
                            </div>
                        </div>

                        <div id="dlg_categorias_errores" class="alert alert-warning m-3 ps-3 pe-3 pt-2 pb-2" style="display: none"></div>

                        <p id="dlg_categorias_ajax_wait" class="text-center mt-5" style="display: none">
                            <img src="../../images/ajax.svg" alt=""/>
                        </p>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" onclick="categoriasInsUpd();">Guardar</button>
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
        <script src="js/categorias.js"></script>
    </body>
</html>
