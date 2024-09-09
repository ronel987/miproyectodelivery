$(function () {
    // adicionando drag and drop a diálogos
    $('.modal-parainfo').on('shown.bs.modal', function () {
        $(this).find('.modal-header').drags();
    });

    // datos iniciales para diálogo dlg_search
    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/usuarios/search",
        dataType: "json",
        type: "GET",
        success: function (result) { //alert(JSON.stringify(result));
            let opt = "";
            for (let i = 0; i < result.findCols.length; ++i) {

                opt += "<option value=\"" + result.findCols[i] + "\">" + result.viewCols[i] + "</option>";
            }

            $("#dlg_search_col").html(opt);
            $("#dlg_search_col_order").html(opt);

            usuariosQry('carga_inicial');
        }
    });

    // evento resize de navegador
    window.addEventListener("resize", resizeWindows);
});

function usuariosQry(carga) {
    $("#ajax_wait").show();
    let pagActual = parseInt($("#pagActual").val());
    let search_col = $("#dlg_search_col").val();
    let search_text = $.trim($("#dlg_search_text").val());
    let col_order = $("#dlg_search_col_order").val();

    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/usuarios/qry",
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

                    qry += "<tr id=\"" + result.rows[i].idusuario + "\">" +
                            "<td class=\"col-3\">" +
                            "<input class=\"form-check-input\" type=\"checkbox\" value=\"" + result.rows[i].idusuario + "\">" +
                            "<span class=\"ps-3\">" + result.rows[i].apellidos + "</span>" +
                            "</td>" +
                            "<td class=\"col-3\">" + result.rows[i].nombres + "</td>" +
                            "<td class=\"col-3 usuario\">" + result.rows[i].usuario + "</td>" +
                            "<td class=\"col-3 autorizacion\">" + result.rows[i].autorizacion + "</td>" +
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

function usuariosIns() {
    $("#dlg_usuarios_accion").val("INS");
    $("#dlg_usuarios_title").text("Nuevo Usuario");
    $("#dlg_usuarios_apellidos").val("");
    $("#dlg_usuarios_nombres").val("");
    $("#dlg_usuarios_usuario").val("");
    $("#dlg_usuarios_password").val("999999999");
    $("#dlg_usuarios_div_password").show();
    $("#dlg_usuarios_vende").prop('checked', true);
    $("#dlg_usuarios_admin").prop('checked', false);

    $("#dlg_usuarios_errores").hide();
    $("#dlg_usuarios").modal("show");
}

function usuariosGet() {
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
    let idusuario = ids.split(",")[0];

    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/usuarios/get/" + idusuario,
        dataType: "json",
        type: "GET",
        success: function (result) { // alert(JSON.stringify(result));

            if (!result.hasOwnProperty("ok")) {
                $("#dlg_usuarios_accion").val("UPD");
                $("#dlg_usuarios_title").text("Actualizar datos de Usuario");
                $("#dlg_usuarios_idusuario").val(result.idusuario);
                $("#dlg_usuarios_apellidos").val(result.apellidos);
                $("#dlg_usuarios_nombres").val(result.nombres);
                $("#dlg_usuarios_usuario").val(result.usuario);

                let autorizaciones = result.autorizacion;
                let autorizacion = autorizaciones.split(", ");
                //
                $("#dlg_usuarios_div_password").hide();
                $("#dlg_usuarios_vende").prop('checked', false);
                $("#dlg_usuarios_admin").prop('checked', false);

                for (let i = 0; i < autorizacion.length; ++i) {
                    switch (autorizacion[i]) {
                        case "VENDE":
                            $("#dlg_usuarios_vende").prop('checked', true);
                            break;

                        case "ADMIN":
                            $("#dlg_usuarios_admin").prop('checked', true);
                            break;
                    }
                }

                $("#dlg_usuarios_errores").hide();
                $("#dlg_usuarios").modal("show");

            } else {
                $("#dlg_message_msg").html(result.msg);
                $('#dlg_message').modal("show");
            }
        }
    });
}

function usuariosInsUpd() {
    let accion = $("#dlg_usuarios_accion").val();
    let apellidos = $("#dlg_usuarios_apellidos").val();
    let nombres = $("#dlg_usuarios_nombres").val();
    let usuario = $("#dlg_usuarios_usuario").val();
    let password = $("#dlg_usuarios_password").val();

    let idusuario = accion === "INS" ? 0 : $("#dlg_usuarios_idusuario").val();
    let autorizacion = "";

    // validación
    let msg = "<ul>";

    let regex_ape_nom = /^([a-zA-Z\sÁÉÍÓÚáéíóúÑñÜü]+)$/;
    if ($.trim(apellidos) === "") {
        msg += "<li>Debe ingresar apellidos de Usuario</li>";

    } else if (!regex_ape_nom.test(apellidos)) {
        msg += "<li>Apellidos debe tener solo letras y espacio</li>";
    }

    if ($.trim(nombres) === "") {
        msg += "<li>Debe ingresar nombres de Usuario</li>";

    } else if (!regex_ape_nom.test(nombres)) {
        msg += "<li>Nombres debe tener solo letras y espacio</li>";
    }

    let regex_user = /^([a-zA-Z0-9ÁÉÍÓÚáéíóúÑñÜü]+)$/;
    if ($.trim(usuario) === "") {
        msg += "<li>Debe ingresar Usuario (nick)</li>";

    } else if (!regex_user.test(usuario)) {
        msg += "<li>Usuario debe tener solo letras y dígitos</li>";
    }

    if (accion === "INS") {
        if ($.trim(password) === "") {
            msg += "<li>Debe ingresar password</li>";

        } else {
            password = password
                    .replaceAll(">", "&gt;")
                    .replaceAll("<", "&lt;");
        }
    }

    if (msg !== "<ul>") {
        msg += "</ul>";
        $("#dlg_usuarios_errores").html(msg);
        $("#dlg_usuarios_errores").show();

        return;

    } else {
        $("#dlg_usuarios_errores").hide();
    }

    // consume servicios
    let url = accion === "INS"
            ? "http://173.249.20.156:8080/minimarket/rest/usuarios/ins"
            : "http://173.249.20.156:8080/minimarket/rest/usuarios/upd";

    let method = accion === "INS" ? "POST" : "PUT";

    let data = {
        idusuario: idusuario,
        apellidos: apellidos,
        nombres: nombres,
        usuario: usuario,
        password: password,
        autorizacion: 'admin'
    };

    $("#dlg_usuarios_ajax_wait").show();
    $.ajax({
        url: url,
        contentType: "application/json", // necesario para indicar que va JSON
        type: method,
        data: JSON.stringify(data),
        success: function (result) {
            $("#dlg_usuarios_ajax_wait").hide();

            if (!result.ok) {

                $("#dlg_usuarios_errores").text(result.msg);
                $("#dlg_usuarios_errores").show();

            } else {
                // actualizar nombre de usuario en el head
                let id = sessionStorage.getItem('idusuario');
                if (accion === "UPD" && id === idusuario) {
                    $.ajax({
                        url: "http://173.249.20.156:8080/minimarket/rest/usuarios/get/" + idusuario,
                        dataType: "json",
                        type: "GET",
                        success: function (result) { //alert(JSON.stringify(result));

                            sessionStorage.setItem("nombres", result.nombres);
                            sessionStorage.setItem("apellidos", result.apellidos);

                            let nombres = sessionStorage.getItem('nombres');
                            let apellidos = sessionStorage.getItem('apellidos');

                            let nom = nombres.split(" ");
                            let ape = apellidos.split(" ");

                            $("#user_log").text(nom[0] + " " + ape[0]);
                        }
                    });
                }

                $("#dlg_usuarios").modal("hide");
                usuariosQry('carga_inicial');
            }
        }
    });
}

function usuariosDel() {
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

    $("#dlg_confirm_msg").html("¿Seguro de retirar Usuario?");
    $("#dlg_confirm_dato1").val("DEL");
    $("#dlg_confirm_dato2").val(ids);

    $("#dlg_confirm_error").hide();
    $("#dlg_confirm_title").text("Retirar Usuario");
    $("#dlg_confirm").modal("show");
}

function usuariosSearch() {
    $('#dlg_search').modal('show');
}


/**************************************************************/
// utilidades
/**************************************************************/

// activado por el botón buscar del diálogo dlg_search
function dlg_search_search() {
    usuariosQry('carga_inicial');
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

    usuariosQry('carga_siguientes');
}

// función complementaria del dialogo dlg_confirm
function dlg_confirm_confirm() {
    var accion = $("#dlg_confirm_dato1").val();
    var ids = $("#dlg_confirm_dato2").val();

    if (accion === "DEL") {
        $.ajax({
            url: "http://173.249.20.156:8080/minimarket/rest/usuarios/del/" + ids,
            type: "DELETE",
            success: function (result) {
                if (!result.ok) {

                    $("#dlg_confirm_error").text(result.msg);
                    $("#dlg_confirm_error").show();

                } else {
                    usuariosQry('carga_inicial');
                    $("#dlg_confirm").modal("hide");
                }
            }
        });
    }
}

// detecta ancho de pantalla cuando activa evento resize
function resizeWindows() {
    var resolucion = $(window).width();

    if (resolucion < 768) { // small
        $("table.parainfo .autorizacion").addClass("d-none");
    } else {
        $("table.parainfo .autorizacion").removeClass("d-none");
    }

    if (resolucion < 576) { // extra-small
        $("table.parainfo .usuario").addClass("d-none");
    } else {
        $("table.parainfo .usuario").removeClass("d-none");
    }
}

function mouseover(id) {
    $("#" + id).attr("type", "text");
}

function mouseout(id) {
    $("#" + id).attr("type", "password");
}

