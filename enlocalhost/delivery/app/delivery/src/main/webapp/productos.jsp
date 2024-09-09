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
                <input type="hidden" id="idcategoria" value="${param.idcate}"/>
            </div>

            <div class="row" style="margin-top: 100px"></div>

            <div id="index_productos" class="row m-3">
                <p style="text-align: center;margin-top: 50px;margin-bottom: 500px">
                    <img src="images/ajax2.svg" alt=""/>
                </p>
            </div>

            <%@include file="WEB-INF/jspf/footer.jspf" %>

            <div id="WAButton" style="z-index: 9000"></div>
        </div>


        <%-- zona de diálogos - inicio --%>
        <div id="dlg_carrito" class="modal modal-parainfo" aria-hidden="true"
             data-bs-backdrop="static" data-bs-keyboard="false">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 id="dlg_carrito_title" class="modal-title">Carrito de Compras</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <div class="modal-body">

                        <div class="row mt-1">
                            <div class="col-12">
                                <table class="table parainfo mb-0">
                                    <thead>
                                        <tr>
                                            <td class="col-1" style="width: 16px"></td>
                                            <td class="col-6 ps-5">Producto</td>
                                            <td class="col-1 text-end">Cantidad</td>
                                            <td class="col-2 text-end">Precio</td>
                                            <td class="col-2 text-center">Sub total</td>
                                        </tr>
                                    </thead>
                                </table>

                                <div class="parainfo_overflow" style="overflow: auto;max-height: 200px">
                                    <table class="table parainfo">
                                        <tbody id="dlg_carrito_qry"></tbody>
                                    </table>
                                </div>

                                <table class="table parainfo">
                                    <tfoot>
                                        <tr>
                                            <td id="dlg_carrito_totalpago" class="col-12 text-end">
                                                Total a pagar S/ 0.00
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </div>
                        </div>

                        <div class="row mt-2">
                            <div class="col-8">
                                <div class="row">
                                    <div class="col-12">
                                        <label for="dlg_carrito_cliente">Cliente</label>
                                        <input id="dlg_carrito_cliente" type="text" class="form-control" placeholder="a quién se entrega delivery" maxlength="100">
                                    </div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-12">
                                        <label for="dlg_carrito_telefono">Teléfono</label>
                                        <input id="dlg_carrito_telefono" type="text" class="form-control" placeholder="para comunicación con Cliente" maxlength="20">
                                    </div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-12">
                                        <label for="dlg_carrito_direccion">Dirección</label>
                                        <textarea id="dlg_carrito_direccion" class="form-control" placeholder="a dónde se entrega delivery" 
                                                  maxlength="200" style="resize: none;height: 60px"></textarea>
                                    </div>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="row mt-4">
                                    <div class="col-12">
                                        Puede hacer su pago por Yape o Plin al teléfono: 997-268252
                                    </div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-6 text-end">
                                        <img src="images/yape.png" alt="" style="height: 60px"/>
                                    </div>
                                    <div class="col-6">
                                        <img src="images/plin.png" alt="" style="height: 60px"/>
                                    </div>
                                </div>
                                <div class="row mt-3">
                                    <div class="col-12">
                                        Al Generar Delivery se creará un texto para ser enviado por Whatsapp.
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div id="dlg_carrito_errores" class="alert alert-danger m-3 p-3" style="display: none"></div>

                        <p id="dlg_carrito_ajax_wait" class="text-center mt-3" style="display: none">
                            <img src="images/ajax.svg" alt=""/>
                        </p>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" onclick="carritoAcepta();">Generar Delivery</button>
                        <button class="btn" data-bs-dismiss="modal">Cancelar</button>
                    </div>
                </div>
            </div>
        </div>

        <div id="dlg_resumen" class="modal modal-parainfo" aria-hidden="true"
             data-bs-backdrop="static" data-bs-keyboard="false">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">

                    <div class="modal-header">
                        <h5 id="dlg_resumen_title" class="modal-title">Resumen para enviar por Whatsapp</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>

                    <div class="modal-body">
                        <div class="row mt-3">
                            <div class="col-12">
                                <label for="dlg_resumen_msg">Dirección</label>
                                <textarea id="dlg_resumen_msg" class="form-control" style="resize: none;height: 200px" readonly="readonly"></textarea>
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="col-1 text-end">
                                <img src="images/yape.png" alt="" style="height: 60px"/>
                            </div>
                            <div class="col-1">
                                <img src="images/plin.png" alt="" style="height: 60px"/>
                            </div>
                            <div class="col-10">
                                Seleccione el texto de sus pedidos y cópielo, luego cierre este diálogo, y el texto copiado péguelo en el Whatsapp de la aplicación o en su propio Whatsapp. 
                                Recuerde que puede hacer su pago por Yape o Plin al teléfono: 997-268252. 
                                Para mayor información comuníquese con nosotros. 
                            </div>
                        </div>

                        <div id="dlg_resumen_errores" class="alert alert-warning m-3 p-3" style="display: none"></div>

                        <p id="dlg_resumen_ajax_wait" class="text-center mt-3" style="display: none">
                            <img src="images/ajax.svg" alt=""/>
                        </p>
                    </div>

                    <div class="modal-footer">
                        <button class="btn" onclick="resumenCopy();">Seleccionar y Copiar</button>                        
                        <button class="btn" data-bs-dismiss="modal">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>

        <%@include file="WEB-INF/jspf/message.jspf" %>
        <%@include file="WEB-INF/jspf/confirm.jspf" %>
        <%@include file="WEB-INF/jspf/zoom.jspf" %>
        <%-- zona de diálogos - fin --%>

        <script src="jq/jquery-3.7.1.min.js"></script>
        <script src="jq/number/jquery.number.min.js"></script>
        <script src="jq/bootstrap/bootstrap.bundle.min.js"></script>
        <script src="jq/whatsapp/floating-wpp.min.js"></script>
        <script src="jq/parainfo.js"></script>
        <script src="js/fecha.js"></script>

        <script src="js/productos.js"></script>
    </body>
</html>
