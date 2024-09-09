$(function () {
    // adicionando drag and drop a diálogos
    $('.modal-parainfo').on('shown.bs.modal', function () {
        $(this).find('.modal-header').drags();
    });

    // datos iniciales para diálogo dlg_search
    $("#ajax_wait").show();
    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/categorias/search",
        dataType: "json",
        type: "GET",
        success: function (result) { //alert(JSON.stringify(result));
            $("#ajax_wait").hide();
            let opt = "";
            for (let i = 0; i < result.findCols.length; ++i) {

                opt += "<option value=\"" + result.findCols[i] + "\">" + result.viewCols[i] + "</option>";
            }

            $("#dlg_search_col").html(opt);
            $("#dlg_search_col_order").html(opt);

            categoriasQry('carga_inicial');
        }
    });

    // evento resize de navegador
    window.addEventListener("resize", resizeWindows);
    resizeWindows();
});

function categoriasQry(carga) {
    $("#ajax").show();
    let pagActual = parseInt($("#pagActual").val());
    let search_col = $("#dlg_search_col").val();
    let search_text = $.trim($("#dlg_search_text").val());
    let col_order = $("#dlg_search_col_order").val();

    $("#ajax_wait").show();
    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/categorias/qry",
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

                    qry += "<tr id=\"" + result.rows[i].idcategoria + "\">" +
                            "<td class=\"col-1\" style=\"width: 16px !important\">" +
                            "<input class=\"form-check-input\" type=\"checkbox\" value=\"" + result.rows[i].idcategoria + "\">" +
                            "</td>" +
                            "<td class=\"col-2 categoria\">" +
                            result.rows[i].categoria +
                            "</td>" +
                            "<td class=\"col-3\">" +
                            "<img src=\"http://173.249.20.156:8080/minimarket/rest/categorias/foto/" + result.rows[i].idcategoria + "\" " + "alt=\"\" style=\"max-height: 50px\">" +
                            "</td>" +
                            "<td class=\"col-6 descripcion\">" +
                            result.rows[i].descripcion +
                            "</td>" +
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
                    
                    $("#pagActual").val(1);
                }
            }
        }
    });
}

function categoriasIns() {
    $("#dlg_categorias_accion").val("INS");
    $("#dlg_categorias_title").text("Nueva categoria");
    $("#dlg_categorias_categoria").val("");
    $("#dlg_categorias_descripcion").val("");
    $("#dlg_categorias_imagen").val("");
    $("#dlg_categorias_imagen_view").text("");

    $("#dlg_categorias_errores").hide();
    $("#dlg_categorias").modal("show");
}

function categoriasGet() {
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
    let idcategoria = ids.split(",")[0];
    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/categorias/get/" + idcategoria,
        dataType: "json",
        type: "GET",
        success: function (result) { //alert(JSON.stringify(result));
            if (result.ok) {

                $("#dlg_categorias_accion").val("UPD");
                $("#dlg_categorias_title").text("Actualizar datos de categoría");
                $("#dlg_categorias_idcategoria").val(result.categorias.idcategoria);
                $("#dlg_categorias_categoria").val(result.categorias.categoria);
                $("#dlg_categorias_descripcion").val(result.categorias.descripcion);
                //$("#dlg_categorias_imagen").val("imagen.png");
                $("#dlg_categorias_imagen_view").html("<img src=\"http://173.249.20.156:8080/minimarket/rest/categorias/foto/" + result.categorias.idcategoria + "\" " + "alt=\"\" style=\"max-height: 50px\">");

                $("#dlg_categorias_errores").hide();
                $("#dlg_categorias").modal("show");

            } else {
                $("#dlg_message_msg").html("No se puede acceder a este producto");
                $('#dlg_message').modal("show");
            }
        }
    });
}

function categoriasInsUpd() {
    let accion = $("#dlg_categorias_accion").val();
    let categoria = $("#dlg_categorias_categoria").val();
    let descripcion = $("#dlg_categorias_descripcion").val();
    let file = $("#dlg_categorias_imagen")[0].files[0];
    var imagen; // obligatorio usar var
    if (file) {
        imagen = file.name;
    } else {
        imagen = "";
    }

    let idcategoria = accion === "INS" ? 0 : $("#dlg_categorias_idcategoria").val();

    // validación
    let msg = "<ul>";
    let regex = /^([a-zA-Z0-9\sÁÉÍÓÚáéíóúÑñÜü.,\%\-\_\/]+)$/;
    if ($.trim(categoria) === "") {
        msg += "<li>Debe ingresar categoría</li>";

    } else if (!regex.test(categoria)) {
        msg += "<li>Categoria debe tener solo letras, espacios y/o dígitos</li>";
    }

    if ($.trim(descripcion) === "") {
        msg += "<li>Debe ingresar descripción</li>";

    } else if (!regex.test(descripcion)) {
        msg += "<li>Descripción debe tener solo letras, espacios y/o dígitos</li>";
    }

    if (accion === "INS" && imagen === "") {
        msg += "<li>Debe seleccionar imagen</li>";
    }

    if (msg !== "<ul>") {
        msg += "</ul>";
        $("#dlg_categorias_errores").html(msg);
        $("#dlg_categorias_errores").show();

        return;
    }

    // consume servicios
    var imagen = document.getElementById("dlg_categorias_imagen").files[0];

    var data = new FormData();
    data.append("accion", accion);
    data.append("idcategoria", idcategoria);
    data.append("categoria", categoria);
    data.append("descripcion", descripcion);
    data.append("imagen", imagen);


    $("#dlg_categorias_ajax_wait").show();
    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/categorias/insUpd",
        dataType: "json",
        type: "POST",
        contentType: false,
        processData: false,
        data: data,
        success: function (result) { //alert(JSON.stringify(result));
            if (!result.ok) {

                let msg = "<ul>";
                for (let i = 0; i < result.msg; ++i) {
                    msg += "<li>" + result.msg[i] + "</li>";
                }
                msg += "</ul>";

                $("#dlg_categorias_errores").html(msg);
                $("#dlg_categorias_errores").show();

            } else {
                window.location = "categorias.jsp";
            }
        }
    });
}

function categoriasDel() {
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
    //

    $("#dlg_confirm_msg").html("¿Seguro de retirar fila?");
    $("#dlg_confirm_dato1").val("DEL");
    $("#dlg_confirm_dato2").val(ids);

    $("#dlg_confirm_error").hide();
    $("#dlg_confirm_title").text("Retirar fila");
    $("#dlg_confirm").modal("show");
}

function categoriasSearch() {
    $('#dlg_search').modal('show');
}


/**************************************************************/
// utilidades
/**************************************************************/

// activado por el botón buscar del diálogo dlg_search
function dlg_search_search() {
    categoriasQry('carga_inicial');
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

    categoriasQry('carga_siguientes');
}

// función complementaria del dialogo dlg_confirm
function dlg_confirm_confirm() {
    var accion = $("#dlg_confirm_dato1").val();
    var ids = $("#dlg_confirm_dato2").val();

    if (accion === "DEL") {
        $.ajax({
            url: "http://173.249.20.156:8080/minimarket/rest/categorias/del/" + ids,
            type: "DELETE",
            success: function (result) {
                if (!result.ok) {

                    $("#dlg_confirm_error").text(result.msg);
                    $("#dlg_confirm_error").show();

                } else {
                    window.location = "categorias.jsp";
                }
            }
        });
    }
}

// detecta ancho de pantalla cuando activa evento resize
function resizeWindows() {
    var resolucion = $(window).width();

    if (resolucion < 576) { // extra-small
        $("table.parainfo .descripcion").addClass("d-none");
        
        $("table.parainfo .categoria").removeClass("col-2");
        $("table.parainfo .categoria").addClass("col-8");

        $("#user_log").addClass("d-none");
        $("#user_ahora").addClass("d-none");
        
    } else {
        $("table.parainfo .descripcion").removeClass("d-none");
        
        $("table.parainfo .categoria").removeClass("col-8");
        $("table.parainfo .categoria").addClass("col-2");

        $("#user_log").removeClass("d-none");
        $("#user_ahora").removeClass("d-none");
    }
}

