$(document).ready(function () {
    // adicionando drag and drop a diálogos
    $('.modal-parainfo').on('shown.bs.modal', function () {
        $(this).find('.modal-header').drags();
    });

    // fecha en el footer
    $("#footer_fecha").text(getFecha());

    // para menú productos
    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/categorias/qry2",
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

    // para formulario
    $("#password").keyup(function (a) {
        if (a.keyCode === 13) {
            ingresar();
        }
    });

    $("#usuario").keyup(function (a) {
        if (a.keyCode === 13) {
            $("#password").focus();
        }
    });

    $("#usuario").focus();
});

function mouseover(id) {
    $("#" + id).attr("type", "text");
}

function mouseout(id) {
    $("#" + id).attr("type", "password");
}

function ingresar() {
    let usuario = $("#usuario").val();
    let password = $("#password").val();

    if ($.trim(usuario) === "" || $.trim(password) === "") {

        $("#dlg_message_msg").html("Debe ingresar Usuario y Password");
        $("#dlg_message").modal("show");
        return;
    }

    let user = btoa(usuario + " - " + password); // user esta en base64

    $("#ajax_wait").show();
    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/autentica",
        headers: {
            user: user
        },
        dataType: "json",
        type: "POST",
        success: function (result) { //alert(JSON.stringify(result));
            $("#ajax_wait").hide();

            if (result.hasOwnProperty("message")) {
                $("#dlg_message_msg").html(result.message);
                $("#dlg_message").modal("show");
                return;
            }
           //si todo ok
            sessionStorage.setItem("nombres", result.nombres);
            sessionStorage.setItem("apellidos", result.apellidos);
            sessionStorage.setItem("idusuario", result.idusuario);
            sessionStorage.setItem("token", result.token);
            window.location = "view/" + result.navega;     //navega vale admin
        }
    });
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

