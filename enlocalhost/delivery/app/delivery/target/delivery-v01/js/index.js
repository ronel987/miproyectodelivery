$(document).ready(function () {
    // fecha en el footer
    $("#footer_fecha").text(getFecha());
    
    // whatsapp
    $('#WAButton').floatingWhatsApp({ 
        phone: '51997268252',
        headerTitle: 'Conversa por Whatsapp',
        popupMessage: 'Buen día, ¿En que te ayudo?',
        showPopup: true,
        buttonImage: '<img src="images/whatsapp.svg" />',
        headerColor: '#b35e36',
        backgroundColor: '#b35e36',
        position: "right"
    });

    
    // carga categorías en los card
    $.ajax({
        url: "http://localhost:8080/delivery/rest/categorias/qry2",
        dataType: "json",
        type: "GET",
        success: function (result) { //alert(JSON.stringify(result));

            if (result.ok) {

                let qry = "";
                let menu_productos_main = "";
                for (let i = 0; i < result.categorias.length; ++i) {

                    qry += "<div class=\"col-md-12 col-lg-6 col-xl-6 col-xxl-4\">" +
                            "<div class=\"card card-parainfo mt-3 mb-4\">" +
                            
                            "<div class=\"card-header\">" +
                            "<h5>" + result.categorias[i][1] + "</h5>" + 
                            "</div>" +

                            "<div class=\"card-body\">" +
                            "<div class=\"row mt-2\">" +
                            "<div class=\"col-12\">" +
                            "<div style=\"height: 200px;overflow: auto\">" +
                            "<a href=\"#\" onclick=\"productosQry('" + result.categorias[i][0] + "');\">" + 
                            "<img src=\"http://localhost:8080/delivery/rest/categorias/foto/" + result.categorias[i][0] + "\" " + 
                            "class=\"img-fluid mx-auto d-block\" alt=\"\" " + 
                            "style=\"width: 512px;height:128px\" alt=\"\"/>" + 
                            "</a>" + 
                            result.categorias[i][2] + 
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            "</div>" +
                            
                            "<div class=\"card-footer border text-center\">" + 
                            "<button class=\"btn parainfo\" onclick=\"productosQry('" + result.categorias[i][0] + "');\" type=\"button\" style=\"width:300px\">" +
                            "Ir a " + result.categorias[i][1] + 
                            "</button>" +
                            "</div>" +
                            
                            "</div>" +
                            "</div>";
                    
                    menu_productos_main += "<a class=\"dropdown-item\" href=\"productos.jsp?idcate=" + result.categorias[i][0] + "\">" +
                            result.categorias[i][1] +
                            "</a>";
                }

                $("#index_categorias").html(qry);
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

function productosQry(idcate) {
    window.location = "productos.jsp?idcate=" + idcate;
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




