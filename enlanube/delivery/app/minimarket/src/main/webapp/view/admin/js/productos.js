$(function () {
    // adicionando drag and drop a diálogos
    $('.modal-parainfo').on('shown.bs.modal', function () {
        $(this).find('.modal-header').drags();
    });

    // datos iniciales para diálogo dlg_search
    $("#ajax_wait").show();
    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/categorias/cbo",
        dataType: "json",
        type: "GET",
        success: function (result) { //alert(JSON.stringify(result));

            if (result.ok) {      //llegan las categorias
                let opt = "";
                for (let i = 0; i < result.categorias.length; ++i) {

                    opt += "<option value=\"" + result.categorias[i][0] + "\">" + result.categorias[i][1] + "</option>";
                }

                $("#categoriasCbo").html(opt);
                $("#dlg_productos_idcategoria").html(opt);

                // datos iniciales para diálogo dlg_search
                $.ajax({
                    url: "http://173.249.20.156:8080/minimarket/rest/productos/search",
                    dataType: "json",
                    type: "GET",
                    success: function (result2) { //alert(JSON.stringify(result));
                        $("#ajax_wait").hide();
                        let optSearch = "";
                        let optOrder = "";
                        for (let i = 0; i < result2.findCols.length; ++i) {

                            if (i === 0) { // solo busca producto
                                optSearch += "<option value=\"" + result2.findCols[i] + "\">" + result2.viewCols[i] + "</option>";
                            }

                            optOrder += "<option value=\"" + result2.findCols[i] + "\">" + result2.viewCols[i] + "</option>";
                        }

                        $("#dlg_search_col").html(optSearch);
                        $("#dlg_search_col_order").html(optOrder);

                        productosQry('carga_inicial');
                    }
                });

            } else {
                $("#dlg_message_msg").html("No es posible cargar categorías");
                $('#dlg_message').modal("show");
            }
        }
    });
    
    $("#dlg_productos_precio").number(true, 2, '.');

    // evento resize de navegador, para cuando cambias el tamaño de la ventana
    window.addEventListener("resize", resizeWindows);
    resizeWindows();
});

function productosQry(carga) {
    $("#ajax_wait").show();
    ///////////////////////////////////////////////////////////////////
    if (carga === 'carga_inicial' || carga === 'carga_inicial_search') {

        $("#pagActual").val(1);

        if (carga === 'carga_inicial') {
            dlg_search_clear();

        }
    }
    ///////////////////////////////////////////////////////////////////

    let pagActual = parseInt($("#pagActual").val());
    let search_col = $("#dlg_search_col").val();
    let search_text = $.trim($("#dlg_search_text").val());
    let col_order = $("#dlg_search_col_order").val();
    let idcategoria = $("#categoriasCbo").val();

    $("#ajax_wait").show();
    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/productos/qry/" + idcategoria,
        dataType: "json",
        type: "GET",
        data: {
            numPag: pagActual - 1,
            findCol: search_col,
            findData: search_text,
            orderBy: col_order
        },
        success: function (result) { //alert(JSON.stringify(result));
            $("#ajax_wait").hide();

            if (result.hasOwnProperty("msg")) {
                // porque no hay evento click que lo dispare
                $("#dlg_message").on("shown.bs.modal", function () {
                    $(this).find('.modal-header').drags();
                    $("#dlg_message_msg").text(result.msg);
                }).modal('show');

            } else {

                let qry = "";
                for (let i = 0; i < result.rows.length; ++i) {

                    qry += "<tr id=\"" + result.rows[i][0] + "\">" +
                            "<td class=\"col-1\" style=\"width: 16px !important\">" +
                            "<input class=\"form-check-input\" type=\"checkbox\" value=\"" + result.rows[i][0] + "\">" +
                            "</td>" +
                            "<td class=\"col-7 producto\">" +
                            result.rows[i][1] +
                            "</td>" +
                            "<td class=\"col-1\">" +
                            "<img src=\"http://173.249.20.156:8080/minimarket/rest/productos/foto/" + result.rows[i][0] + "\" " + "alt=\"\" style=\"max-height: 50px\">" +
                            "</td>" +
                            "<td class=\"col-2 precio text-end\">" + result.rows[i][2] + "</td>" +
                            "<td class=\"col-1 estado text-center\">" + result.rows[i][3] + "</td>" +
                            "</tr>";
                }

                $("#qry").html(qry);

                // estableciendo evento click de las filas
                $('#qry > tr').click(function (e) {

                    if ($(this).children("td").hasClass('fil-sel')) {

                        $(this).children("td").removeClass('fil-sel');
                        $(this).find("td:eq(0) input[type='checkbox']").prop("checked", false);

                    } else {

                        $(this).children("td").addClass('fil-sel');
                        $(this).find("td:eq(0) input[type='checkbox']").prop("checked", true);
                    }
                });

                resizeWindows();
                //

                let ctasFils = result.ctasFils; // total de filas de la consulta
                let ctasPags = parseInt(result.ctasPags); // cantidad de páginas de la consulta 

                $("#ctasFils").text(ctasFils);
                $("#ctasPags").val(ctasPags);

                if (carga === 'carga_inicial') {
                    if (ctasPags === 1 || ctasPags === 0) { // viene una página o ninguna
                        $("#first").addClass('disabled');
                        $("#previus").addClass('disabled');
                        $("#next").addClass('disabled');
                        $("#last").addClass('disabled');

                    } else { // vienen varias páginas
                        $("#first").addClass('disabled');
                        $("#previus").addClass('disabled');
                        $("#next").removeClass("disabled");
                        $("#last").removeClass("disabled");
                    }
                }
            }
        }
    });
}

function productosIns() {
    $("#dlg_productos_accion").val("INS");
    $("#dlg_productos_title").text("Nuevo producto");
    $("#dlg_productos_producto").val("");
    $("#dlg_productos_precio").val($.number(0, 2, '.'));
    $("#dlg_productos_estado").prop("checked", true);
    $("#dlg_productos_imagen_view").text("");

    $("#dlg_productos_errores").hide();
    $("#dlg_productos").modal("show");
}

function productosGet() {
    // verifica si hay seleccionado(s)
    let ids = "";
    $("#qry > tr").each(function () {

        if ($(this).children("td").hasClass('fil-sel')) {

            ids += $(this).find("td:eq(0) input[type='checkbox']").val() + ",";
        }
    });

    if (ids === "") {
        $("#dlg_message_msg").html("Debe seleccionar fila para Modificar");
        $('#dlg_message').modal("show");

        return;
    }

    // SI hay fila(s) seleccionadas
    ids = ids.substr(0, ids.length - 1);

    // tomando solo el primer id
    let idproducto = ids.split(",")[0];

    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/productos/get/" + idproducto,
        dataType: "json",
        type: "GET",
        success: function (result) { //alert(JSON.stringify(result));
            if (result.ok) {

                $("#dlg_productos_accion").val("UPD");
                $("#dlg_productos_title").text("Actualizar datos de producto");
                $("#dlg_productos_idproducto").val(result.productos.idproducto);
                $("#dlg_productos_idcategoria").val(result.productos.idcategoria);
                $("#dlg_productos_producto").val(result.productos.producto);
                $("#dlg_productos_precio").val($.number(result.productos.precio, 2, '.'));
                $("#dlg_productos_estado").prop("checked", result.productos.estado === '1');
                $("#dlg_productos_imagen_view").html("<img src=\"http://173.249.20.156:8080/minimarket/rest/productos/foto/" + result.productos.idproducto + "\" " + "alt=\"\" style=\"max-height: 50px\">");

                $("#dlg_productos_errores").hide();
                $("#dlg_productos").modal("show");

            } else {
                $("#dlg_message_msg").html("No se puede acceder a este producto");
                $('#dlg_message').modal("show");
            }
        }
    });
}

function productosInsUpd() {
    let accion = $("#dlg_productos_accion").val();
    let idproducto = accion === "INS" ? 0 : $("#dlg_productos_idproducto").val();
    let idcategoria = $("#dlg_productos_idcategoria").val();
    let producto = $("#dlg_productos_producto").val();
    let precio = $("#dlg_productos_precio").val();
    let estado = $("#dlg_productos_estado").prop("checked") ? '1' : '0';
    let file = $("#dlg_productos_imagen")[0].files[0];
    var imagen; // obligatorio usar var
    if (file) {
        imagen = file.name;
    } else {
        imagen = "";
    }

    // validación
    let msg = "<ul>";
    let regex_user = /^([a-zA-Z0-9\sÁÉÍÓÚáéíóúÑñÜü.\%\-\_\/]+)$/;
    if ($.trim(producto) === "") {
        msg += "<li>Debe ingresar producto</li>";

    } else if (!regex_user.test(producto)) {
        msg += "<li>producto debe tener solo letras, espacios y/o dígitos o [.%-_/]</li>";
    }

    if (!$.isNumeric(precio)) {
        msg += "<li>precio debe ser número</li>";
    } else if (parseFloat(precio) <= 0) {
        msg += "<li>precio debe ser mayor que cero</li>";
    }

    if (accion === "INS" && imagen === "") {
        msg += "<li>Debe seleccionar imagen</li>";
    }

    if (msg !== "<ul>") {
        msg += "</ul>";
        $("#dlg_productos_errores").html(msg);
        $("#dlg_productos_errores").show();

        return;
    }
    
    // consume servicios
    var imagen = document.getElementById("dlg_productos_imagen").files[0];

    var data = new FormData();
    data.append("accion", accion);
    data.append("idproducto", idproducto);
    data.append("idcategoria", idcategoria);
    data.append("producto", producto);
    data.append("precio", precio);
    data.append("estado", estado);
    data.append("imagen", imagen);

    $("#dlg_productos_ajax_wait").show();
    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/productos/insUpd",
        dataType: "json",
        type: "POST",
        contentType: false,
        processData: false,
        data: data,
        success: function (result) {  //alert(JSON.stringify(result));
            if (!result.ok) {

                let msg = "<ul>";
                for (let i = 0; i < result.msg; ++i) {
                    msg += "<li>" + result.msg[i] + "</li>";
                }
                msg += "</ul>";

                $("#dlg_productos_errores").html(msg);
                $("#dlg_productos_errores").show();

            } else {
                window.location = "productos.jsp";
            }
        }
    });
}

function productosDel() {
    // verifica si hay seleccionado(s)
    let ids = "";
    $("#qry > tr").each(function () {

        if ($(this).children("td").hasClass('fil-sel')) {

            ids += $(this).find("td:eq(0) input[type='checkbox']").val() + ",";
        }
    });

    if (ids === "") {
        $("#dlg_message_msg").html("Debe seleccionar fila(s) para Retirar");
        $('#dlg_message').modal("show");

        return;
    }

    // SI hay fila(s) seleccionadas
    ids = ids.substr(0, ids.length - 1);
    //

    $("#dlg_confirm_msg").html("¿Seguro de retirar fila?");
    $("#dlg_confirm_dato1").val("DEL");
    $("#dlg_confirm_dato2").val(ids);

    $("#dlg_confirm_error").hide();
    $("#dlg_confirm_title").text("Retirar fila");
    $("#dlg_confirm").modal("show");
}

function productosSearch() {
    $('#dlg_search').modal('show');
}


/**************************************************************/
// utilidades
/**************************************************************/

// activado por el botón buscar del diálogo dlg_search
function dlg_search_search() {
    ///////////////////////////////////////////////////////////////////
    productosQry('carga_inicial_search');
    ///////////////////////////////////////////////////////////////////
    $('#dlg_search').modal('hide');
}

function dlg_search_clear() {
    $('#dlg_search_text').val("");
    //dlg_search_search(); // cierra el diálogo
}

// activado por botones de desplazamiento de páginas
function botones(boton) {
    let pagActual = parseInt($("#pagActual").val());
    let ctasPags = parseInt($("#ctasPags").val());

    switch (boton) {
        case "first":
            pagActual = 1;
            break;

        case "previus":
            --pagActual;
            pagActual = pagActual < 1 ? 1 : pagActual;
            break;

        case "next":
            ++pagActual;
            pagActual = pagActual > ctasPags ? ctasPags : pagActual;
            break;

        case "last":
            pagActual = ctasPags;
            break;
    }
    //

    $("#pagActual").val(pagActual);
    //

    if (pagActual === 1 && ctasPags > 1) {
        $("#first").addClass('disabled');
        $("#previus").addClass('disabled');

        $("#next").removeClass("disabled");
        $("#last").removeClass("disabled");

    } else if (pagActual === ctasPags && ctasPags > 1) {
        $("#first").removeClass('disabled');
        $("#previus").removeClass('disabled');

        $("#next").addClass('disabled');
        $("#last").addClass('disabled');

    } else if (ctasPags === 1) {
        $("#first").addClass('disabled');
        $("#previus").addClass('disabled');
        $("#next").addClass('disabled');
        $("#last").addClass('disabled');

    } else {
        $("#first").removeClass("disabled");
        $("#previus").removeClass("disabled");
        $("#next").removeClass("disabled");
        $("#last").removeClass("disabled");
    }
    //

    productosQry('carga_siguientes');
}

// función complementaria del dialogo dlg_confirm
function dlg_confirm_confirm() {
    var accion = $("#dlg_confirm_dato1").val();
    var ids = $("#dlg_confirm_dato2").val();

    if (accion === "DEL") {
        $.ajax({
            url: "http://173.249.20.156:8080/minimarket/rest/productos/del/" + ids,
            type: "DELETE",
            success: function (result) {
                if (!result.ok) {

                    $("#dlg_confirm_error").text(result.msg);
                    $("#dlg_confirm_error").show();

                } else {
                    window.location = "productos.jsp?idcategoria=" + $("#categoriasCbo").val();
                }
            }
        });
    }
}

// detecta ancho de pantalla cuando activa evento resize
function resizeWindows() {
    var resolucion = $(window).width();

    if (resolucion < 768) { // small
        $("table.parainfo .estado").addClass("d-none");
    } else {
        $("table.parainfo .estado").removeClass("d-none");
    }

    if (resolucion < 576) { // extra-small
        $("table.parainfo .precio").addClass("d-none");
        
        $("table.parainfo .producto").removeClass("col-7");
        $("table.parainfo .producto").addClass("col-10");

        $("#user_log").addClass("d-none");
        $("#user_ahora").addClass("d-none");
        
    } else {
        $("table.parainfo .precio").removeClass("d-none");
        
        $("table.parainfo .producto").removeClass("col-10");
        $("table.parainfo .producto").addClass("col-7");

        $("#user_log").removeClass("d-none");
        $("#user_ahora").removeClass("d-none");
    }
}

