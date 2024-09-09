let whatsapp;

$(document).ready(function () {
    // adicionando drag and drop a diálogos
    $('.modal-parainfo').on('shown.bs.modal', function () {
        $(this).find('.modal-header').drags();
    });
    //

    // para poblar dlg_carrito_qry con productos pedidos en carrito
    carritoGrilla();
    //

    // fecha en el footer
    $("#footer_fecha").text(getFecha());

    // whatsapp
    whatsapp = $('#WAButton').floatingWhatsApp({
        phone: '51997268252',
        headerTitle: 'Conversa por Whatsapp',
        popupMessage: 'Buen día, ¿En que te ayudo?',
        showPopup: true,
        buttonImage: '<img src="images/whatsapp.svg" />',
        headerColor: '#b35e36',
        backgroundColor: '#b35e36',
        position: "right"
    });

    let idcategoria = $("#idcategoria").val();
    if (idcategoria === undefined || idcategoria === '') {
        idcategoria = 1;
    }
    // carga categorías en los card
    $.ajax({
        url: "http://localhost:8080/delivery/rest/productos/qry2/" + idcategoria,
        dataType: "json",
        type: "GET",
        success: function (result) { //alert(JSON.stringify(result));

            if (result.ok) {
                let qry = "";
                for (let i = 0; i < result.productos.length; ++i) {

                    let select = "<select id=\"cantidad_" + result.productos[i][0] + "\" class=\"m-0 text-end\" style=\"width:80px\">";
                    for (let i = 1; i <= 12; ++i) {

                        select += "<option value=\"" + i + "\">" + i + "</option>";
                    }
                    select += "</select>";

                    qry += "<div style=\"position:fixed;top:200px;left:0px;margin-left:-11px\">" +
                            "<button class=\"btn parainfo fst-italic\" onclick=\"carritoQry();\" style=\"opacity:0.4\">Ver Carrito <img src=\"images/shoppingcart.png\" alt=\"\"></button>" +
                            "</div>" +
                            "<div class=\"col-md-12 col-lg-6 col-xl-6 col-xxl-4\">" +
                            "<div class=\"card card-parainfo mt-3 mb-4\">" +
                            "<div class=\"card-body\" style=\"height: 300px;overflow: auto;font-size:18px\">" +
                            "<div class=\"row\">" +
                            "<div class=\"col-12\">" +
                            "<h5 id=\"producto_" + result.productos[i][0] + "\" class=\"text-center\">" + result.productos[i][2] + "</h5>" +
                            "</div>" +
                            "</div>" +
                            "<div class=\"row mt-3\">" +
                            "<div class=\"col-6\">" +
                            "<div class=\"position-relative\">" +
                            "<div class=\"position-absolute bottom-0 end-0\">" +
                            "<a href=\"#\" onclick=\"imageZoom('" + result.productos[i][0] + "');\" class=\"btn btn-warning\"><img src=\"images/view.png\" alt=\"\" title=\"ampliar foto\"></a>" +
                            "</div>" +
                            "<img src=\"http://localhost:8080/delivery/rest/productos/foto/" + result.productos[i][0] + "\" " +
                            "class=\"img-fluid mx-auto d-block\" alt=\"\" style=\"max-height: 200px\">" +
                            "</div>" +
                            "</div>" +
                            "<div class=\"col-6\">" +
                            "<p class=\"text-center\">" +
                            "<span class=\"badge rounded-pill bg-success\" style=\"display: inline-block;width: 240px;padding: 14px 12px\">" +
                            "Precio unitario: S/ " + "<span id=\"precio_" + result.productos[i][0] + "\">" + result.productos[i][3] + "</span>" +
                            "</span>" +
                            "<span class=\"badge rounded-pill bg-info mt-1\" style=\"display: inline-block;width: 240px;padding: 10px 12px\">" +
                            "Cantidad a llevar: " + select +
                            "</span>" +
                            "</p>" +
                            "<p class=\"text-center\">" +
                            "<button class=\"btn parainfo\" onclick=\"carritoAddItem('" + result.productos[i][0] + "');\" type=\"button\" style=\"width:240px\">" +
                            "<span class=\"fst-italic me-2\">Producto al Carrito</span> " + "<img src=\"images/shoppingcart.png\" alt=\"\">" +
                            "</button>" +
                            "</p>" +
                            "<p style=\"font-size:14px\">" +
                            "Si desea llevar este producto solo elija la cantidad y pulse el botón <span class=\"fst-italic\">Producto al Carrito</span>." +
                            "</p>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>";
                }

                $("#index_productos").html(qry);

            } else {
                $("#dlg_message_msg").html("No es posible cargar Productos");
                $('#dlg_message').modal("show");
            }
        }
    });

    // para menú productos
    $.ajax({
        url: "http://localhost:8080/delivery/rest/categorias/qry2",
        dataType: "json",
        type: "GET",
        success: function (result) { //alert(JSON.stringify(result));

            if (result.ok) {

                let menu_productos_main = "";
                for (let i = 0; i < result.categorias.length; ++i) {

                    menu_productos_main += "<a class=\"dropdown-item\" href=\"productos.jsp?idcate=" + result.categorias[i][0] + "\">" +
                            result.categorias[i][1] +
                            "</a>";
                }

                $("#menu_productos_main").html(menu_productos_main);

            } else {
                $("#dlg_message_msg").html("No es posible cargar Categorías");
                $('#dlg_message').modal("show");
            }
        }
    });

    // evento resize de navegador
    window.addEventListener("resize", changeBackcolorNavbar);
    changeBackcolorNavbar();
});

function imageZoom(idproducto) {
    let obj = "<img src=\"http://localhost:8080/delivery/rest/productos/foto/" + idproducto + "\" " +
            "class=\"img-fluid mx-auto d-block\" alt=\"\" style=\"max-height: 500px\">";

    $("#dlg_zoom_obj").html(obj);
    $('#dlg_zoom').modal("show");
}

function carritoAddItem(idproducto) {
    if (sessionStorage.getItem("carrito") === null) {
        let carrito = []; // inicializando carrito
        sessionStorage.setItem("carrito", JSON.stringify(carrito));

    } else {
        let carrito = JSON.parse(sessionStorage.getItem("carrito"));
        // verificando que producto NO esté en carrito
        let producto = "";
        let estaEnCart = false;
        for (let i = 0; i < carrito.length; ++i) {
            if (carrito[i].idproducto === idproducto) {
                estaEnCart = true;
                producto = carrito[i].producto;
                break;
            }
        }

        if (estaEnCart) {
            $("#dlg_message_msg").html("Producto <span class=\"fw-bold\">" + producto + "</span> ya se encuentra en carrito.");
            $('#dlg_message').modal("show");
            return;
        }
    }

    // producto seleccionado no está en carrito
    let producto = $("#producto_" + idproducto).text();
    let cantidad = $("#cantidad_" + idproducto).val();
    let precio = $("#precio_" + idproducto).text();

    // guardando en sessionStorage
    let carritoItem = {
        idproducto: idproducto,
        producto: producto,
        cantidad: cantidad,
        precio: precio
    };

    let carrito = JSON.parse(sessionStorage.getItem("carrito"));
    carrito.push(carritoItem);
    sessionStorage.setItem("carrito", JSON.stringify(carrito));
    //

    carritoGrilla(); // para poblar dlg_carrito_qry con productos pedidos

    let msg = "<div class=\"row\">" +
            "<div class=\"col-8\">" + cantidad + (cantidad === 1 ? " producto" : " productos") + " <span class=\"fw-bold\">" + producto + "</span> adicionado al carrito.</div>" +
            "<div class=\"col-4\"><img src=\"http://localhost:8080/delivery/rest/productos/foto/" + idproducto + "\" style=\"height: 100px\"></div>" +
            "</div>";
    //$("#dlg_message_msg").html("Producto <span class=\"fw-bold\">" + producto + "</span> adicionado al carrito.");
    $("#dlg_message_msg").html(msg);
    $('#dlg_message').modal("show");
}

function carritoGrilla() { // puebla dlg_carrito_qry
    if (sessionStorage.getItem("carrito") !== null) {
        let carrito = JSON.parse(sessionStorage.getItem("carrito"));

        let trs = "";
        for (let i = 0; i < carrito.length; ++i) {
            let item = carrito[i];
            let subtotal = parseInt(item.cantidad) * parseFloat(item.precio);

            // añadiendo a la grilla dlg_carrito_qry
            trs += "<tr id=\"dlg_carrito_tr_" + item.idproducto + "\">" +
                    "<td class=\"col-1 text-center\" style=\"width: 16px !important\">" +
                    "<button type=\"button\" class=\"btn btn-warning\" onclick=\"carritoDel('" + item.idproducto + "', '" + item.producto + "')\"><i class=\"bi-trash3-fill\"></i></button>" +
                    "</td>" +
                    "<td class=\"col-6\">" +
                    item.producto +
                    "</td>" +
                    "<td class=\"col-1 text-end\">" + item.cantidad + "</td>" +
                    "<td class=\"col-2 text-end\">" + item.precio + "</td>" +
                    "<td class=\"col-2 text-end\">" + $.number(subtotal, 2, '.') + "</td>";
        }
        $("#dlg_carrito_qry").html(trs);

        // total a pagar
        let total = 0;
        $("#dlg_carrito_qry > tr").each(function () {

            total += parseFloat($(this).find("td:eq(4)").text());
        });
        $("#dlg_carrito_totalpago").text("Total a pagar S/ " + $.number(total, 2, '.'));
    }
}

function carritoQry() {
    $("#dlg_carrito_cliente").val("");
    $("#dlg_carrito_telefono").val("");
    $("#dlg_carrito_direccion").val("");
    $("#dlg_carrito").modal("show");
}

function carritoDel(idproducto, producto) {
    let msg = "<div class=\"row\">" +
            "<div class=\"col-8\">¿Seguro de retirar producto " + producto + "?</div>" +
            "<div class=\"col-4\"><img src=\"http://localhost:8080/delivery/rest/productos/foto/" + idproducto + "\" style=\"height: 100px\"></div>" +
            "</div>";
    $("#dlg_confirm_msg").html(msg);
    $("#dlg_confirm_dato1").val("DEL_ITEM");
    $("#dlg_confirm_dato2").val(idproducto);

    $("#dlg_confirm_error").hide();
    $("#dlg_confirm_title").text("Retirar producto");
    $("#dlg_confirm").modal("show");
}

function carritoAcepta() {
    let cliente = $.trim($("#dlg_carrito_cliente").val());
    if (cliente === "") {
        $("#dlg_message_msg").html("Ingrese nombre de Cliente.");
        $('#dlg_message').modal("show");
        return;
    }

    let telefono = $.trim($("#dlg_carrito_telefono").val());
    if (telefono === "") {
        $("#dlg_message_msg").html("Ingrese telefono de Cliente.");
        $('#dlg_message').modal("show");
        return;
    }

    let direccion = $.trim($("#dlg_carrito_direccion").val());
    if (direccion === "") {
        $("#dlg_message_msg").html("Ingrese dirección de Cliente.");
        $('#dlg_message').modal("show");
        return;
    }

    // generación de mensaje a enviar por Whatsapp
    let msg = "cliente: " + cliente + "\n" +
            "telefono: " + telefono + "\n" +
            "direccion: " + direccion + "\n" +
            "-----------------------------------------------\n" +
            getFecha() + " - " + getHora() + "\n" +
            "-----------------------------------------------\n";

    let totalPago = 0;
    $("#dlg_carrito_qry > tr").each(function () {

        msg += "- " + $(this).find('td').eq(2).text() + " " +
                $(this).find('td').eq(1).text() +
                " a S/" + $(this).find('td').eq(3).text() +
                " son S/" + $(this).find('td').eq(4).text() + "\n";
        
        totalPago += parseFloat($(this).find('td').eq(4).text());
    });
    msg += "-----------------------------------------------\n" +
            "total a pagar ---> S/" + $.number(totalPago, 2, ".") + "\n";
    //

    $("#dlg_carrito").modal("hide");

    $("#dlg_resumen_errores").val("");
    $("#dlg_resumen_errores").hide();
    $("#dlg_resumen_msg").html(msg);
    $("#dlg_resumen").modal("show");
}

function resumenCopy() {
    let msg = $("#dlg_resumen_msg").val();

    $("#dlg_resumen_msg").val(msg).select();
    document.execCommand('copy');

    $("#dlg_resumen_errores").text("Texto de pedidos copiado!");
    $("#dlg_resumen_errores").show();
}

// función complementaria del dialogo dlg_confirm
function dlg_confirm_confirm() {
    var accion = $("#dlg_confirm_dato1").val();

    if (accion === "DEL_ITEM") {
        var idproducto = $("#dlg_confirm_dato2").val();

        // retirar item de carrito
        let carrito = JSON.parse(sessionStorage.getItem("carrito"));
        let carrito2 = [];

        for (let i = 0; i < carrito.length; ++i) {
            if (carrito[i].idproducto === idproducto) {
                continue;
            }

            carrito2.push(carrito[i]);
        }

        sessionStorage.setItem("carrito", JSON.stringify(carrito2));
        carritoGrilla(); // actualiza grilla
        $("#dlg_confirm").modal("hide");
    }
}

// detecta ancho de pantalla cuando activa evento resize
function changeBackcolorNavbar() {
    // cambiando de backgroup al navbar cuando este es un botón
    var navbar_visible = $("#barraDeMenu").is(":visible");

    if (navbar_visible) {
        //$('.navbar-brand').text("true");
        $('.navbar-nav').css("background-color", "transparent");
    } else {
        //$('.navbar-brand').text("false");
        $('.navbar-nav').css("background-color", "#fff4e5");
    }
}




